package com.example.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

object IntentHelper {

    fun openYouTubeChannel(context: Context, youtubeUrl: String, channelName: String) {
        try {
            // First try direct YouTube intent or standard browser intent
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            try {
                // Fallback to Google Search / Browser
                val searchUrl = "https://www.google.com/search?q=" + Uri.encode("$channelName YouTube")
                val fallbackIntent = Intent(Intent.ACTION_VIEW, Uri.parse(searchUrl)).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(fallbackIntent)
            } catch (fallbackEx: Exception) {
                Toast.makeText(
                    context,
                    "Kanal açılamadı: $youtubeUrl",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun shareApp(context: Context) {
        try {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(
                    Intent.EXTRA_TEXT,
                    "Komodo Ders Odaklanma Takipçisi ile ders çalışıyor, YKS/LGS hedeflerime adım adım ilerliyorum! 🐲📚"
                )
                type = "text/plain"
            }
            context.startActivity(Intent.createChooser(sendIntent, "Komodo Odak'ı Paylaş"))
        } catch (_: Exception) {}
    }
}
