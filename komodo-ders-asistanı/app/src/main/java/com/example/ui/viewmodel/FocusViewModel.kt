package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.db.FavoriteChannelEntity
import com.example.data.db.FocusSessionEntity
import com.example.data.db.StudyTaskEntity
import com.example.data.model.ChannelCategory
import com.example.data.model.MascotState
import com.example.data.model.StudyChannel
import com.example.data.model.Subject
import com.example.data.repository.StudyRepository
import com.example.util.AmbientSound
import com.example.util.SoundPlayer
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

enum class TimerPreset(
    val title: String,
    val focusMinutes: Int,
    val breakMinutes: Int,
    val description: String
) {
    POMODORO("Klasik Pomodoro", 25, 5, "25 dk Odak + 5 dk Mola"),
    DEEP_FOCUS("Derin Odaklanma", 50, 10, "50 dk Yoğun Ders + 10 dk Mola"),
    LGS_PROVA("LGS / Branş Deneme", 40, 10, "40 dk Soru Provası"),
    TYT_PROVA("YKS / TYT Simülasyon", 165, 20, "165 dk Tam Sınav Simülasyonu"),
    QUICK_SESSION("Hızlı Tekrar", 15, 3, "15 dk Hızlı Konu Tekrarı"),
    CUSTOM("Özel Süre", 30, 5, "İstediğin dakikayı ayarla")
}

enum class SessionPhase {
    FOCUS,
    BREAK
}

data class TimerUiState(
    val preset: TimerPreset = TimerPreset.POMODORO,
    val phase: SessionPhase = SessionPhase.FOCUS,
    val isRunning: Boolean = false,
    val timeRemainingSeconds: Int = 25 * 60,
    val totalPhaseSeconds: Int = 25 * 60,
    val selectedSubject: Subject = Subject.MATEMATIK,
    val solvedQuestionsInSession: Int = 0,
    val targetQuestionsInSession: Int = 20,
    val sessionNotes: String = "",
    val ambientSound: AmbientSound = AmbientSound.NONE,
    val completedSessionsCount: Int = 0,
    val showCompletionCelebration: Boolean = false
)

class FocusViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: StudyRepository
    private val soundPlayer = SoundPlayer()
    private var timerJob: Job? = null

    init {
        val db = AppDatabase.getInstance(application)
        repository = StudyRepository(db)
    }

    // Repository flows
    val allSessions: StateFlow<List<FocusSessionEntity>> = repository.allSessions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalFocusMinutes: StateFlow<Int> = repository.totalFocusMinutes
        .combine(MutableStateFlow(0)) { total, _ -> total ?: 0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalQuestionsSolved: StateFlow<Int> = repository.totalQuestionsSolved
        .combine(MutableStateFlow(0)) { total, _ -> total ?: 0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val allTasks: StateFlow<List<StudyTaskEntity>> = repository.allTasks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favoriteChannels: StateFlow<List<FavoriteChannelEntity>> = repository.favoriteChannels
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Timer State
    private val _timerState = MutableStateFlow(TimerUiState())
    val timerState: StateFlow<TimerUiState> = _timerState.asStateFlow()

    // Video Channels State
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow<ChannelCategory?>(null)
    val selectedCategory: StateFlow<ChannelCategory?> = _selectedCategory.asStateFlow()

    private val _selectedGradeFilter = MutableStateFlow<String?>(null)
    val selectedGradeFilter: StateFlow<String?> = _selectedGradeFilter.asStateFlow()

    // Mascot State derived
    val mascotState: StateFlow<MascotState> = _timerState.combine(MutableStateFlow(Unit)) { state, _ ->
        when {
            state.showCompletionCelebration -> MascotState.CELEBRATING
            state.isRunning && state.phase == SessionPhase.FOCUS -> MascotState.FOCUSING
            state.isRunning && state.phase == SessionPhase.BREAK -> MascotState.BREAK_TIME
            else -> MascotState.IDLE
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MascotState.IDLE)

    fun setTimerPreset(preset: TimerPreset, customFocusMins: Int = 30, customBreakMins: Int = 5) {
        if (_timerState.value.isRunning) {
            pauseTimer()
        }
        val focusMins = if (preset == TimerPreset.CUSTOM) customFocusMins else preset.focusMinutes
        val totalSecs = focusMins * 60
        _timerState.value = _timerState.value.copy(
            preset = preset,
            phase = SessionPhase.FOCUS,
            timeRemainingSeconds = totalSecs,
            totalPhaseSeconds = totalSecs,
            isRunning = false,
            showCompletionCelebration = false
        )
    }

    fun setSelectedSubject(subject: Subject) {
        _timerState.value = _timerState.value.copy(selectedSubject = subject)
    }

    fun startTimer() {
        if (_timerState.value.isRunning) return
        _timerState.value = _timerState.value.copy(
            isRunning = true,
            showCompletionCelebration = false
        )

        // If ambient sound is selected, play it
        if (_timerState.value.ambientSound != AmbientSound.NONE) {
            soundPlayer.play(_timerState.value.ambientSound)
        }

        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (isActive && _timerState.value.isRunning && _timerState.value.timeRemainingSeconds > 0) {
                delay(1000)
                val newRemaining = _timerState.value.timeRemainingSeconds - 1
                _timerState.value = _timerState.value.copy(timeRemainingSeconds = newRemaining)
            }

            if (_timerState.value.timeRemainingSeconds <= 0) {
                handlePhaseCompletion()
            }
        }
    }

    fun pauseTimer() {
        _timerState.value = _timerState.value.copy(isRunning = false)
        timerJob?.cancel()
        timerJob = null
        soundPlayer.stop()
    }

    fun resetTimer() {
        pauseTimer()
        val currentPreset = _timerState.value.preset
        val focusMins = currentPreset.focusMinutes
        _timerState.value = _timerState.value.copy(
            phase = SessionPhase.FOCUS,
            timeRemainingSeconds = focusMins * 60,
            totalPhaseSeconds = focusMins * 60,
            showCompletionCelebration = false
        )
    }

    private fun handlePhaseCompletion() {
        val currentState = _timerState.value
        if (currentState.phase == SessionPhase.FOCUS) {
            // Save completed focus session to Room
            val focusMins = currentState.totalPhaseSeconds / 60
            viewModelScope.launch {
                repository.saveSession(
                    FocusSessionEntity(
                        subjectName = currentState.selectedSubject.displayName,
                        durationMinutes = focusMins.coerceAtLeast(1),
                        modeType = currentState.preset.name,
                        questionsSolved = currentState.solvedQuestionsInSession,
                        notes = currentState.sessionNotes
                    )
                )
            }

            // Switch to Break phase
            val breakMins = currentState.preset.breakMinutes
            val breakSecs = breakMins * 60
            _timerState.value = currentState.copy(
                phase = SessionPhase.BREAK,
                timeRemainingSeconds = breakSecs,
                totalPhaseSeconds = breakSecs,
                isRunning = false,
                completedSessionsCount = currentState.completedSessionsCount + 1,
                showCompletionCelebration = true
            )
            soundPlayer.stop()
        } else {
            // Break completed, switch back to Focus
            val focusMins = currentState.preset.focusMinutes
            val focusSecs = focusMins * 60
            _timerState.value = currentState.copy(
                phase = SessionPhase.FOCUS,
                timeRemainingSeconds = focusSecs,
                totalPhaseSeconds = focusSecs,
                isRunning = false,
                showCompletionCelebration = false
            )
            soundPlayer.stop()
        }
    }

    fun addSolvedQuestions(count: Int) {
        val current = _timerState.value.solvedQuestionsInSession
        _timerState.value = _timerState.value.copy(
            solvedQuestionsInSession = (current + count).coerceAtLeast(0)
        )
    }

    fun setTargetQuestions(target: Int) {
        _timerState.value = _timerState.value.copy(targetQuestionsInSession = target.coerceAtLeast(1))
    }

    fun setSessionNotes(notes: String) {
        _timerState.value = _timerState.value.copy(sessionNotes = notes)
    }

    fun setAmbientSound(sound: AmbientSound) {
        _timerState.value = _timerState.value.copy(ambientSound = sound)
        if (_timerState.value.isRunning) {
            if (sound == AmbientSound.NONE) {
                soundPlayer.stop()
            } else {
                soundPlayer.play(sound)
            }
        }
    }

    fun dismissCelebration() {
        _timerState.value = _timerState.value.copy(showCompletionCelebration = false)
    }

    // Channels operations
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedCategory(category: ChannelCategory?) {
        _selectedCategory.value = category
    }

    fun setSelectedGradeFilter(grade: String?) {
        _selectedGradeFilter.value = grade
    }

    fun getAllChannels(): List<StudyChannel> = repository.getChannels()

    fun toggleChannelFavorite(channel: StudyChannel, isFav: Boolean) {
        viewModelScope.launch {
            repository.toggleChannelFavorite(channel, isFav)
        }
    }

    fun recordChannelVisit(channel: StudyChannel) {
        viewModelScope.launch {
            repository.recordChannelVisit(channel.id, channel.name, channel.handle)
        }
    }

    // Task Operations
    fun addNewTask(
        title: String,
        subject: Subject,
        targetQuestions: Int,
        estimatedMins: Int,
        relatedChannel: String? = null
    ) {
        viewModelScope.launch {
            repository.addTask(
                StudyTaskEntity(
                    title = title,
                    subjectName = subject.displayName,
                    targetQuestions = targetQuestions,
                    estimatedMinutes = estimatedMins,
                    relatedChannelName = relatedChannel
                )
            )
        }
    }

    fun toggleTask(id: Long, completed: Boolean) {
        viewModelScope.launch {
            repository.toggleTaskCompleted(id, completed)
        }
    }

    fun updateTaskSolved(id: Long, solved: Int) {
        viewModelScope.launch {
            repository.updateTaskSolvedQuestions(id, solved)
        }
    }

    fun deleteTask(id: Long) {
        viewModelScope.launch {
            repository.deleteTask(id)
        }
    }

    fun deleteSession(id: Long) {
        viewModelScope.launch {
            repository.deleteSession(id)
        }
    }

    fun startFocusForChannel(channel: StudyChannel) {
        val matchingSubject = channel.subjects.firstOrNull() ?: Subject.MATEMATIK
        setSelectedSubject(matchingSubject)
        _timerState.value = _timerState.value.copy(
            sessionNotes = "${channel.name} video dersi ile odaklanma"
        )
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        soundPlayer.stop()
    }
}
