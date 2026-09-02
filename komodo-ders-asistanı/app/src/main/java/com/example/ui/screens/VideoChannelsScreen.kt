package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.ChannelCategory
import com.example.data.model.StudyChannel
import com.example.data.model.Subject
import com.example.ui.theme.KomodoAmber
import com.example.ui.theme.KomodoEmerald
import com.example.ui.theme.KomodoMint
import com.example.ui.theme.KomodoTeal
import com.example.ui.viewmodel.FocusViewModel
import com.example.util.IntentHelper

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VideoChannelsScreen(
    viewModel: FocusViewModel,
    onStartFocusWithChannel: (StudyChannel) -> Unit,
    initialSubjectFilter: Subject? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val favoriteEntities by viewModel.favoriteChannels.collectAsState()

    val allChannels = remember { viewModel.getAllChannels() }
    val favoriteIds = remember(favoriteEntities) { favoriteEntities.map { it.channelId }.toSet() }

    var onlyFavorites by remember { mutableStateOf(false) }

    // Filter channels
    val filteredChannels = remember(searchQuery, selectedCategory, onlyFavorites, favoriteIds, initialSubjectFilter) {
        allChannels.filter { channel ->
            val matchesSearch = if (searchQuery.isBlank()) true else {
                channel.name.contains(searchQuery, ignoreCase = true) ||
                        channel.teacherOrDetail.contains(searchQuery, ignoreCase = true) ||
                        channel.handle.contains(searchQuery, ignoreCase = true) ||
                        channel.highlights.contains(searchQuery, ignoreCase = true) ||
                        channel.grades.any { it.contains(searchQuery, ignoreCase = true) } ||
                        channel.subjects.any { it.displayName.contains(searchQuery, ignoreCase = true) }
            }

            val matchesCategory = if (selectedCategory == null) true else {
                channel.category == selectedCategory
            }

            val matchesFav = if (!onlyFavorites) true else {
                favoriteIds.contains(channel.id)
            }

            val matchesInitialSubject = if (initialSubjectFilter == null || selectedCategory != null) true else {
                channel.subjects.contains(initialSubjectFilter)
            }

            matchesSearch && matchesCategory && matchesFav && matchesInitialSubject
        }
    }

    // Group by category if "All" is selected and no search
    val isCategoryGrouped = selectedCategory == null && searchQuery.isBlank() && !onlyFavorites

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Header
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(top = 12.dp, start = 16.dp, end = 16.dp, bottom = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Videolu Ders Kanalları",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Sınıfına ve dersine göre uzman YouTube öğretmenleri",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Favorites filter toggle button
                    FilterChip(
                        selected = onlyFavorites,
                        onClick = { onlyFavorites = !onlyFavorites },
                        label = { Text("Favoriler") },
                        leadingIcon = {
                            Icon(
                                imageVector = if (onlyFavorites) Icons.Default.Star else Icons.Default.BookmarkBorder,
                                contentDescription = null,
                                tint = if (onlyFavorites) KomodoAmber else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = KomodoAmber.copy(alpha = 0.2f),
                            selectedLabelColor = KomodoAmber
                        )
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Kanal, öğretmen, ders veya sınıf ara (Örn: LGS, Rehber Matematik...)") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Ara",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotBlank()) {
                            IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                Icon(imageVector = Icons.Default.Clear, contentDescription = "Temizle")
                            }
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f)
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        // Category Filter Chips
        ScrollableTabRow(
            selectedTabIndex = if (selectedCategory == null) 0 else ChannelCategory.entries.indexOf(selectedCategory) + 1,
            edgePadding = 16.dp,
            divider = {},
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Tab(
                selected = selectedCategory == null,
                onClick = { viewModel.setSelectedCategory(null) },
                text = {
                    Text(
                        text = "🌟 Tümü (${allChannels.size})",
                        fontWeight = if (selectedCategory == null) FontWeight.Bold else FontWeight.Medium
                    )
                }
            )

            ChannelCategory.entries.forEach { category ->
                val isSelected = selectedCategory == category
                Tab(
                    selected = isSelected,
                    onClick = { viewModel.setSelectedCategory(category) },
                    text = {
                        Text(
                            text = "${category.iconName} ${category.title}",
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                )
            }
        }

        // Main Channel List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header Info Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFFF0000),
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Doğrudan YouTube & Google Entegrasyonu",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Kanala tıkladığında resmi kanal sayfası doğrudan YouTube ve tarayıcında açılır.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            if (filteredChannels.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "🔍", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Aradığın kriterlere uygun kanal bulunamadı",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Arama terimini değiştir veya filtreleri sıfırla.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else if (isCategoryGrouped) {
                // Render category by category with clear section headers
                ChannelCategory.entries.forEach { category ->
                    val categoryChannels = filteredChannels.filter { it.category == category }
                    if (categoryChannels.isNotEmpty()) {
                        item(key = "header_${category.id}") {
                            CategorySectionHeader(category = category, count = categoryChannels.size)
                        }

                        items(categoryChannels, key = { it.id }) { channel ->
                            StudyChannelCard(
                                channel = channel,
                                isFavorite = favoriteIds.contains(channel.id),
                                onOpenClick = {
                                    viewModel.recordChannelVisit(channel)
                                    IntentHelper.openYouTubeChannel(context, channel.youtubeUrl, channel.name)
                                },
                                onFavoriteToggle = {
                                    val currentFav = favoriteIds.contains(channel.id)
                                    viewModel.toggleChannelFavorite(channel, currentFav)
                                    Toast.makeText(
                                        context,
                                        if (currentFav) "Favorilerden çıkarıldı" else "Favorilere eklendi ⭐",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                onStartFocus = {
                                    viewModel.startFocusForChannel(channel)
                                    onStartFocusWithChannel(channel)
                                }
                            )
                        }
                    }
                }
            } else {
                items(filteredChannels, key = { it.id }) { channel ->
                    StudyChannelCard(
                        channel = channel,
                        isFavorite = favoriteIds.contains(channel.id),
                        onOpenClick = {
                            viewModel.recordChannelVisit(channel)
                            IntentHelper.openYouTubeChannel(context, channel.youtubeUrl, channel.name)
                        },
                        onFavoriteToggle = {
                            val currentFav = favoriteIds.contains(channel.id)
                            viewModel.toggleChannelFavorite(channel, currentFav)
                            Toast.makeText(
                                context,
                                if (currentFav) "Favorilerden çıkarıldı" else "Favorilere eklendi ⭐",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        onStartFocus = {
                            viewModel.startFocusForChannel(channel)
                            onStartFocusWithChannel(channel)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CategorySectionHeader(category: ChannelCategory, count: Int) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = category.tagColor.copy(alpha = 0.15f),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = category.iconName, fontSize = 20.sp)
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = category.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        color = category.tagColor
                    )
                    Text(
                        text = category.subtitle,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Surface(
                shape = CircleShape,
                color = category.tagColor.copy(alpha = 0.25f)
            ) {
                Text(
                    text = "$count Kanal",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = category.tagColor,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StudyChannelCard(
    channel: StudyChannel,
    isFavorite: Boolean,
    onOpenClick: () -> Unit,
    onFavoriteToggle: () -> Unit,
    onStartFocus: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onOpenClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header Row: Avatar, Title, Handle, Favorite Star
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar badge
                Surface(
                    shape = CircleShape,
                    color = channel.avatarBgColor,
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = channel.name.take(2).uppercase(),
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = channel.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = channel.teacherOrDetail,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = channel.handle,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                IconButton(onClick = onFavoriteToggle) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Star else Icons.Default.BookmarkBorder,
                        contentDescription = "Favori",
                        tint = if (isFavorite) KomodoAmber else MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Highlights
            Text(
                text = "✨ ${channel.highlights}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Grade & Subject Tags
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                channel.grades.forEach { grade ->
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Text(
                            text = grade,
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                channel.subjects.forEach { subject ->
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = subject.color.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = subject.displayName,
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = subject.color,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons: "YouTube'da Aç" + "Bu Kanalla Odaklan"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Open in YouTube Button
                Button(
                    onClick = onOpenClick,
                    modifier = Modifier.weight(1.3f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF0000)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "YouTube'da Aç",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 13.sp
                    )
                }

                // Focus with this channel
                OutlinedButton(
                    onClick = onStartFocus,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Odaklan",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}
