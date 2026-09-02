package com.example.util

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Random
import kotlin.math.sin

enum class AmbientSound(val displayName: String, val icon: String) {
    NONE("Sessiz", "🔇"),
    RAIN("Yağmur Sesi", "🌧️"),
    WHITE_NOISE("Beyaz Gürültü", "📻"),
    FOCUS_WAVE("40Hz Odak Dalgaları", "🧠"),
    CAMPFIRE("Şömine Çıtırtısı", "🔥"),
    LIBRARY("Kütüphane Ambiyansı", "📚")
}

class SoundPlayer {
    private var audioTrack: AudioTrack? = null
    private var job: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)
    private var currentSound: AmbientSound = AmbientSound.NONE
    private var isPlaying: Boolean = false

    fun play(sound: AmbientSound) {
        if (sound == AmbientSound.NONE) {
            stop()
            return
        }
        if (isPlaying && currentSound == sound) return

        stop()
        currentSound = sound
        isPlaying = true

        job = scope.launch {
            try {
                val sampleRate = 22050
                val bufferSize = AudioTrack.getMinBufferSize(
                    sampleRate,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT
                ).coerceAtLeast(4096)

                val track = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(bufferSize)
                    .setTransferMode(AudioTrack.MODE_STREAM)
                    .build()

                audioTrack = track
                track.play()

                val buffer = ShortArray(bufferSize / 2)
                val random = Random()
                var phase = 0.0
                var filterState = 0.0

                while (isActive && isPlaying) {
                    when (sound) {
                        AmbientSound.RAIN -> {
                            for (i in buffer.indices) {
                                val white = (random.nextDouble() * 2 - 1)
                                // Low-pass filter for rain-like sound
                                filterState += 0.08 * (white - filterState)
                                // Add random rain drops
                                val drop = if (random.nextDouble() < 0.002) (random.nextDouble() * 0.8) else 0.0
                                val sample = (filterState * 0.6 + drop * 0.4) * 0.3
                                buffer[i] = (sample.coerceIn(-1.0, 1.0) * Short.MAX_VALUE).toInt().toShort()
                            }
                        }
                        AmbientSound.WHITE_NOISE -> {
                            for (i in buffer.indices) {
                                val white = (random.nextDouble() * 2 - 1)
                                filterState += 0.25 * (white - filterState)
                                buffer[i] = (filterState * 0.2 * Short.MAX_VALUE).toInt().toShort()
                            }
                        }
                        AmbientSound.FOCUS_WAVE -> {
                            // Binaural / Isochronic tone at 40Hz (Gamma waves for deep study focus) combined with soft harmonic base
                            val freq = 216.0
                            val pulseFreq = 40.0
                            for (i in buffer.indices) {
                                val t = phase / sampleRate
                                val carrier = sin(2.0 * Math.PI * freq * t)
                                val modulator = 0.5 + 0.5 * sin(2.0 * Math.PI * pulseFreq * t)
                                val sample = carrier * modulator * 0.18
                                buffer[i] = (sample * Short.MAX_VALUE).toInt().toShort()
                                phase += 1.0
                            }
                        }
                        AmbientSound.CAMPFIRE -> {
                            for (i in buffer.indices) {
                                val white = (random.nextDouble() * 2 - 1)
                                filterState += 0.04 * (white - filterState)
                                val crackle = if (random.nextDouble() < 0.004) (random.nextDouble() * 1.5 - 0.75) else 0.0
                                val sample = (filterState * 0.4 + crackle) * 0.25
                                buffer[i] = (sample.coerceIn(-1.0, 1.0) * Short.MAX_VALUE).toInt().toShort()
                            }
                        }
                        AmbientSound.LIBRARY -> {
                            for (i in buffer.indices) {
                                val white = (random.nextDouble() * 2 - 1)
                                filterState += 0.02 * (white - filterState) // very soft warm noise
                                buffer[i] = (filterState * 0.15 * Short.MAX_VALUE).toInt().toShort()
                            }
                        }
                        AmbientSound.NONE -> break
                    }
                    track.write(buffer, 0, buffer.size)
                }
            } catch (_: Exception) {}
        }
    }

    fun stop() {
        isPlaying = false
        currentSound = AmbientSound.NONE
        job?.cancel()
        job = null
        try {
            audioTrack?.pause()
            audioTrack?.flush()
            audioTrack?.stop()
            audioTrack?.release()
        } catch (_: Exception) {}
        audioTrack = null
    }

    fun getCurrentSound(): AmbientSound = currentSound
}
