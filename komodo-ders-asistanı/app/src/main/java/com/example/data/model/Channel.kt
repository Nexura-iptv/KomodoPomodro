package com.example.data.model

import androidx.compose.ui.graphics.Color

enum class ChannelCategory(
    val id: String,
    val title: String,
    val subtitle: String,
    val tagColor: Color,
    val iconName: String
) {
    ORTAOKUL_LGS(
        "ortaokul_lgs",
        "5, 6, 7 ve 8. Sınıf",
        "Ortaokul & LGS Hazırlık",
        Color(0xFF0D9488),
        "🎒"
    ),
    MATEMATIK_GEO(
        "matematik_geo",
        "Matematik & Geometri",
        "Tüm Sınıflar & YKS (TYT/AYT)",
        Color(0xFF0284C7),
        "📐"
    ),
    TURKCE_EDEBIYAT(
        "turkce_edebiyat",
        "Türkçe & Edebiyat",
        "Tüm Sınıflar & YKS / LGS",
        Color(0xFFE11D48),
        "📖"
    ),
    FEN_BILIMLERI(
        "fen_bilimleri",
        "Fizik - Kimya - Biyoloji",
        "Fen Bilimleri & YKS (TYT/AYT)",
        Color(0xFF16A34A),
        "🔬"
    ),
    SOSYAL_TARIH_COG(
        "sosyal_tarih_cog",
        "Tarih & Coğrafya",
        "Sosyal Bilgiler & YKS",
        Color(0xFFD97706),
        "🌍"
    ),
    LISE_YKS_GENEL(
        "lise_yks_genel",
        "9, 10, 11 ve 12. Sınıf Genel",
        "Lise & YKS Tüm Dersler",
        Color(0xFF7C3AED),
        "🎓"
    )
}

data class StudyChannel(
    val id: String,
    val name: String,
    val teacherOrDetail: String,
    val handle: String,
    val youtubeUrl: String,
    val category: ChannelCategory,
    val grades: List<String>,
    val subjects: List<Subject>,
    val highlights: String,
    val avatarBgColor: Color
)

object ChannelDataSource {
    val allChannels: List<StudyChannel> = listOf(
        // 1. Ortaokul & LGS (5, 6, 7, 8)
        StudyChannel(
            id = "tonguc_akademi",
            name = "Tonguç Akademi",
            teacherOrDetail = "LGS & Ortaokul Konu Anlatımları",
            handle = "@tongucakademi",
            youtubeUrl = "https://www.youtube.com/@tongucakademi",
            category = ChannelCategory.ORTAOKUL_LGS,
            grades = listOf("5. Sınıf", "6. Sınıf", "7. Sınıf", "8. Sınıf / LGS"),
            subjects = listOf(Subject.MATEMATIK, Subject.FEN_BILIMLERI, Subject.TURKCE, Subject.SOSYAL_BILGILER),
            highlights = "Eğlenceli ve animasyonlu LGS konu anlatımları, soru kampları",
            avatarBgColor = Color(0xFFFF6B6B)
        ),
        StudyChannel(
            id = "hocalara_geldik_ortaokul",
            name = "Hocalara Geldik Ortaokul",
            teacherOrDetail = "Ortaokul ve LGS Uzman Kadrosu",
            handle = "@HocalaraGeldikOrtaokul",
            youtubeUrl = "https://www.youtube.com/@HocalaraGeldikOrtaokul",
            category = ChannelCategory.ORTAOKUL_LGS,
            grades = listOf("5. Sınıf", "6. Sınıf", "7. Sınıf", "8. Sınıf / LGS"),
            subjects = listOf(Subject.MATEMATIK, Subject.FEN_BILIMLERI, Subject.TURKCE, Subject.SOSYAL_BILGILER),
            highlights = "Detaylı müfredat anlatımları, yeni nesil soru çözümleri",
            avatarBgColor = Color(0xFF4ECDC4)
        ),
        StudyChannel(
            id = "ders_market",
            name = "Ders-Market",
            teacherOrDetail = "Ortaokul & LGS Video Dersler",
            handle = "@DersMarket",
            youtubeUrl = "https://www.youtube.com/@DersMarket",
            category = ChannelCategory.ORTAOKUL_LGS,
            grades = listOf("5. Sınıf", "6. Sınıf", "7. Sınıf", "8. Sınıf / LGS"),
            subjects = listOf(Subject.MATEMATIK, Subject.FEN_BILIMLERI, Subject.TURKCE),
            highlights = "Kısa ve öz konu tekrarları, soru çözüm teknikleri",
            avatarBgColor = Color(0xFFFFBE0B)
        ),

        // 2. Matematik & Geometri
        StudyChannel(
            id = "rehber_matematik",
            name = "Rehber Matematik",
            teacherOrDetail = "Mehmet Hoca • 'Gülümse Çekiyorum!'",
            handle = "@RehberMatematik",
            youtubeUrl = "https://www.youtube.com/@RehberMatematik",
            category = ChannelCategory.MATEMATIK_GEO,
            grades = listOf("9. Sınıf", "10. Sınıf", "11. Sınıf", "12. Sınıf", "TYT / AYT"),
            subjects = listOf(Subject.MATEMATIK, Subject.GEOMETRI),
            highlights = "Sıfırdan TYT-AYT kampları, 49 Günde TYT Matematik, Geometri",
            avatarBgColor = Color(0xFF3A86FF)
        ),
        StudyChannel(
            id = "mert_hoca",
            name = "Mert Hoca",
            teacherOrDetail = "Mert Hoca • 'Matematiğin Efendisi'",
            handle = "@MertHoca",
            youtubeUrl = "https://www.youtube.com/@MertHoca",
            category = ChannelCategory.MATEMATIK_GEO,
            grades = listOf("9. Sınıf", "10. Sınıf", "11. Sınıf", "12. Sınıf", "TYT / AYT"),
            subjects = listOf(Subject.MATEMATIK, Subject.GEOMETRI),
            highlights = "70 Günde TYT Matematik Kampı, AYT Matematik, soru avcısı",
            avatarBgColor = Color(0xFF8338EC)
        ),
        StudyChannel(
            id = "biyikli_matematik",
            name = "Bıyıklı Matematik",
            teacherOrDetail = "Selim Yüksel • 'Gör Beni Gör!'",
            handle = "@BiyikliMatematik",
            youtubeUrl = "https://www.youtube.com/@BiyikliMatematik",
            category = ChannelCategory.MATEMATIK_GEO,
            grades = listOf("9. Sınıf", "10. Sınıf", "11. Sınıf", "12. Sınıf", "TYT / AYT"),
            subjects = listOf(Subject.MATEMATIK, Subject.GEOMETRI),
            highlights = "55 Günde TYT Matematik, 10 Günde Temel Atma, geometri kampları",
            avatarBgColor = Color(0xFFFB5607)
        ),
        StudyChannel(
            id = "tunc_kurt",
            name = "Tunç Kurt",
            teacherOrDetail = "Tunç Kurt Matematik",
            handle = "@TuncKurt",
            youtubeUrl = "https://www.youtube.com/@TuncKurt",
            category = ChannelCategory.MATEMATIK_GEO,
            grades = listOf("11. Sınıf", "12. Sınıf", "AYT / Derece"),
            subjects = listOf(Subject.MATEMATIK, Subject.GEOMETRI),
            highlights = "Derinlemesine ispatlar, olimpiyat ve ileri seviye AYT soruları",
            avatarBgColor = Color(0xFF00B4D8)
        ),

        // 3. Türkçe & Edebiyat
        StudyChannel(
            id = "rustu_hoca",
            name = "Rüştü Hoca ile Türkçe",
            teacherOrDetail = "Rüştü Bayındır • Taktiklerle Türkçe",
            handle = "@RustuHocailaTurkce",
            youtubeUrl = "https://www.youtube.com/@RustuHocailaTurkce",
            category = ChannelCategory.TURKCE_EDEBIYAT,
            grades = listOf("Tüm Sınıflar", "TYT / AYT", "LGS"),
            subjects = listOf(Subject.TURKCE, Subject.EDEBIYAT),
            highlights = "Taktiklerle Paragraf, Dil Bilgisi kampları, fulleten notlar",
            avatarBgColor = Color(0xFFE63946)
        ),
        StudyChannel(
            id = "kadir_gumus",
            name = "Kadir Gümüş (Benim Hocam)",
            teacherOrDetail = "Kadir Gümüş • Türk Dili ve Edebiyatı",
            handle = "@BenimHocam",
            youtubeUrl = "https://www.youtube.com/@BenimHocam",
            category = ChannelCategory.TURKCE_EDEBIYAT,
            grades = listOf("9-12. Sınıf", "TYT / AYT"),
            subjects = listOf(Subject.TURKCE, Subject.EDEBIYAT),
            highlights = "Kapsamlı AYT Edebiyat ve TYT Türkçe video ders serileri",
            avatarBgColor = Color(0xFF9D0208)
        ),

        // 4. Fizik - Kimya - Biyoloji (Fen Bilimleri & YKS)
        StudyChannel(
            id = "vip_fizik",
            name = "VIP Fizik",
            teacherOrDetail = "Kemal Hoca • VIP Fizik Kampları",
            handle = "@VIPFizik",
            youtubeUrl = "https://www.youtube.com/@VIPFizik",
            category = ChannelCategory.FEN_BILIMLERI,
            grades = listOf("9. Sınıf", "10. Sınıf", "11. Sınıf", "12. Sınıf", "TYT / AYT"),
            subjects = listOf(Subject.FIZIK),
            highlights = "Görsel simülasyonlu Fizik anlatımı, TYT & AYT check-up kampları",
            avatarBgColor = Color(0xFF4361EE)
        ),
        StudyChannel(
            id = "fizikfiniti",
            name = "Fizikfiniti",
            teacherOrDetail = "Fizikfiniti • Temelden Zirveye",
            handle = "@Fizikfiniti",
            youtubeUrl = "https://www.youtube.com/@Fizikfiniti",
            category = ChannelCategory.FEN_BILIMLERI,
            grades = listOf("9-12. Sınıf", "TYT / AYT"),
            subjects = listOf(Subject.FIZIK),
            highlights = "Kavram haritalı fizik dersleri ve soru analizleri",
            avatarBgColor = Color(0xFF3F37C9)
        ),
        StudyChannel(
            id = "gorkem_sahin",
            name = "Görkem Şahin (Kimya)",
            teacherOrDetail = "Görkem Şahin • Benim Hocam Kimya",
            handle = "@BenimHocam",
            youtubeUrl = "https://www.youtube.com/@BenimHocam",
            category = ChannelCategory.FEN_BILIMLERI,
            grades = listOf("9-12. Sınıf", "TYT / AYT Kimya"),
            subjects = listOf(Subject.KIMYA),
            highlights = "Sempatik ve akılda kalıcı TYT/AYT Kimya tam video ders serisi",
            avatarBgColor = Color(0xFF2A9D8F)
        ),
        StudyChannel(
            id = "kimyadasiniz",
            name = "Kimyadasınız",
            teacherOrDetail = "Murat Hoca • Kimyadasınız",
            handle = "@Kimyadasiniz",
            youtubeUrl = "https://www.youtube.com/@Kimyadasiniz",
            category = ChannelCategory.FEN_BILIMLERI,
            grades = listOf("9. Sınıf", "10. Sınıf", "11. Sınıf", "12. Sınıf", "TYT / AYT"),
            subjects = listOf(Subject.KIMYA),
            highlights = "Animasyonlu kimya deneyleri, soru bankası çözümleri",
            avatarBgColor = Color(0xFF264653)
        ),
        StudyChannel(
            id = "dr_biyoloji",
            name = "Dr. Biyoloji",
            teacherOrDetail = "Barış Hoca • Dr. Biyoloji",
            handle = "@DrBiyoloji",
            youtubeUrl = "https://www.youtube.com/@DrBiyoloji",
            category = ChannelCategory.FEN_BILIMLERI,
            grades = listOf("9. Sınıf", "10. Sınıf", "11. Sınıf", "12. Sınıf", "TYT / AYT"),
            subjects = listOf(Subject.BIYOLOJI),
            highlights = "Tıp fakültesi bakış açısıyla MEB odaklı derinlemesine Biyoloji",
            avatarBgColor = Color(0xFF2D6A4F)
        ),
        StudyChannel(
            id = "selin_hoca",
            name = "Selin Hoca (Biyoloji)",
            teacherOrDetail = "Selin Hoca Biyoloji",
            handle = "@SelinHoca",
            youtubeUrl = "https://www.youtube.com/@SelinHoca",
            category = ChannelCategory.FEN_BILIMLERI,
            grades = listOf("9-12. Sınıf", "TYT / AYT"),
            subjects = listOf(Subject.BIYOLOJI),
            highlights = "Renkli el yazısı özetleri, nokta atışı biyoloji kodlamaları",
            avatarBgColor = Color(0xFF52B788)
        ),

        // 5. Tarih & Coğrafya
        StudyChannel(
            id = "cografyanin_kodlari",
            name = "Coğrafyanın Kodları",
            teacherOrDetail = "Yunus Hoca • Kodlamalarla Coğrafya",
            handle = "@CografyaninKodlari",
            youtubeUrl = "https://www.youtube.com/@CografyaninKodlari",
            category = ChannelCategory.SOSYAL_TARIH_COG,
            grades = listOf("9. Sınıf", "10. Sınıf", "TYT / AYT", "KPSS"),
            subjects = listOf(Subject.COGRAFYA),
            highlights = "Harita teknikleri, akılda kalıcı hafıza teknikleri ve animasyonlar",
            avatarBgColor = Color(0xFFD4A373)
        ),
        StudyChannel(
            id = "ramazan_yetgin",
            name = "Ramazan Yetgin (Tarih)",
            teacherOrDetail = "Ramazan Yetgin • Benim Hocam Tarih",
            handle = "@BenimHocam",
            youtubeUrl = "https://www.youtube.com/@BenimHocam",
            category = ChannelCategory.SOSYAL_TARIH_COG,
            grades = listOf("9-12. Sınıf", "TYT / AYT", "Tarih"),
            subjects = listOf(Subject.TARIH),
            highlights = "Hikayeleştirerek Tarih anlatımı, kronolojik detaylar ve soru taktikleri",
            avatarBgColor = Color(0xFFBC6C25)
        ),

        // 6. 9, 10, 11 ve 12. Sınıf Genel (Lise & YKS)
        StudyChannel(
            id = "hocalara_geldik_lise",
            name = "Hocalara Geldik",
            teacherOrDetail = "Lise ve YKS Tüm Dersler",
            handle = "@HocalaraGeldik",
            youtubeUrl = "https://www.youtube.com/@HocalaraGeldik",
            category = ChannelCategory.LISE_YKS_GENEL,
            grades = listOf("9. Sınıf", "10. Sınıf", "11. Sınıf", "12. Sınıf", "YKS"),
            subjects = listOf(Subject.MATEMATIK, Subject.FIZIK, Subject.KIMYA, Subject.BIYOLOJI, Subject.EDEBIYAT, Subject.TARIH, Subject.COGRAFYA),
            highlights = "Tüm lise dersleri için konu anlatımları, rehberlik ve motivasyon yayınları",
            avatarBgColor = Color(0xFF7209B7)
        ),
        StudyChannel(
            id = "benim_hocam_genel",
            name = "Benim Hocam",
            teacherOrDetail = "Türkiye'nin En Geniş YKS & Lise Kadrosu",
            handle = "@BenimHocam",
            youtubeUrl = "https://www.youtube.com/@BenimHocam",
            category = ChannelCategory.LISE_YKS_GENEL,
            grades = listOf("9. Sınıf", "10. Sınıf", "11. Sınıf", "12. Sınıf", "TYT / AYT"),
            subjects = listOf(Subject.MATEMATIK, Subject.FIZIK, Subject.KIMYA, Subject.BIYOLOJI, Subject.EDEBIYAT, Subject.TARIH, Subject.COGRAFYA),
            highlights = "YKS video ders defterleri ile birebir uyumlu tam kapsamlı dersler",
            avatarBgColor = Color(0xFF560BAD)
        )
    )
}
