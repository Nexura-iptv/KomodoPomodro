package com.example.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Biotech
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

enum class Subject(
    val displayName: String,
    val shortCode: String,
    val color: Color,
    val containerColor: Color
) {
    MATEMATIK("Matematik", "MAT", Color(0xFF0284C7), Color(0xFFE0F2FE)),
    GEOMETRI("Geometri", "GEO", Color(0xFF0EA5E9), Color(0xFFE0F2FE)),
    TURKCE("Türkçe", "TRK", Color(0xFFE11D48), Color(0xFFFFE4E6)),
    EDEBIYAT("Türk Dili & Edebiyatı", "EDB", Color(0xFFBE185D), Color(0xFFFCE7F3)),
    FIZIK("Fizik", "FZK", Color(0xFF7C3AED), Color(0xFFEDE9FE)),
    KIMYA("Kimya", "KMY", Color(0xFF0D9488), Color(0xFFCCFBF1)),
    BIYOLOJI("Biyoloji", "BYL", Color(0xFF16A34A), Color(0xFFDCFCE7)),
    FEN_BILIMLERI("Fen Bilimleri", "FEN", Color(0xFF059669), Color(0xFFD1FAE5)),
    TARIH("Tarih", "TAR", Color(0xFFD97706), Color(0xFFFEF3C7)),
    COGRAFYA("Coğrafya", "CGR", Color(0xFFB45309), Color(0xFFFEF3C7)),
    SOSYAL_BILGILER("Sosyal Bilgiler", "SOS", Color(0xFFC2410C), Color(0xFFFFEDD5)),
    FELSEFE("Felsefe & Din", "FLS", Color(0xFF6366F1), Color(0xFFEEF2FF)),
    GENEL_DENEME("Genel Deneme & Soru", "DNM", Color(0xFF10B981), Color(0xFFD1FAE5));

    companion object {
        fun fromName(name: String): Subject {
            return entries.find { it.displayName.equals(name, ignoreCase = true) } ?: MATEMATIK
        }
    }
}
