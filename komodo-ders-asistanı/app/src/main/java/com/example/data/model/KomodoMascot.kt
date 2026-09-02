package com.example.data.model

enum class MascotState {
    IDLE,
    FOCUSING,
    BREAK_TIME,
    CELEBRATING
}

data class KomodoRank(
    val level: Int,
    val title: String,
    val minMinutes: Int,
    val description: String,
    val emoji: String,
    val badgeName: String
)

object KomodoEvolutions {
    val ranks: List<KomodoRank> = listOf(
        KomodoRank(1, "Yavru Komodo", 0, "Ders yolculuğuna yeni başlayan meraklı ejderha!", "🐣", "Başlangıç"),
        KomodoRank(2, "Çalışkan Komodo", 60, "İlk saatlerini tamamladı, gözlüklerini taktı!", "🤓", "Odak Çırağı"),
        KomodoRank(3, "Kitap Kurdu Komodo", 180, "Konuları sindirmeye ve notlar almaya başladı!", "📚", "Ders Kurdu"),
        KomodoRank(4, "Soru Avcısı Komodo", 400, "Soruları tek tek çözüyor, formülleri ezberledi!", "🏹", "Soru Avcısı"),
        KomodoRank(5, "Bilge Ejder Komodo", 800, "Zamanı ve dikkati mükemmel yönetiyor!", "🧙‍♂️", "Bilge Ejder"),
        KomodoRank(6, "Komodo Şampiyonu", 1500, "Tüm sınavların fatihi, odaklanma efsanesi!", "👑", "Efsane")
    )

    fun getRankForMinutes(totalMinutes: Int): KomodoRank {
        return ranks.lastOrNull { totalMinutes >= it.minMinutes } ?: ranks.first()
    }

    fun getNextRank(totalMinutes: Int): KomodoRank? {
        return ranks.firstOrNull { it.minMinutes > totalMinutes }
    }

    val studyQuotes = listOf(
        "\"Zorluklar seni yıldırmasın, her soru seni hedefine bir adım yaklaştırır!\"",
        "\"Şimdi odaklanma zamanı. Telefonu bırak, geleceğini inşa et!\"",
        "\"Küçük adımlar, büyük başarıları doğurur. Hadi başlayalım!\"",
        "\"Derin bir nefes al ve formülleri zihnine kodla!\"",
        "\"Yorulduğunda bırakma, mola ver ve daha güçlü dön!\"",
        "\"Bugün çözdüğün her test, sınav günü senin en büyük gücün olacak!\""
    )

    val breakQuotes = listOf(
        "\"Harika bir odaklanma seansıydı! Biraz su iç ve gözlerini dinlendir.\"",
        "\"Kaslarını esnet, kısa bir yürüyüş yap. Komodo seninle gurur duyuyor!\"",
        "\"Zihnini taze tut. Mola bitince gücümüze güç katacağız!\""
    )
}
