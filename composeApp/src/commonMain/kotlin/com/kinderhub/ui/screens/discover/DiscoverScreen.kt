package com.kinderhub.ui.screens.discover

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kinderhub.ui.components.ActivityCard
import com.kinderhub.ui.components.AiSearchHero
import com.kinderhub.ui.components.AppShell
import com.kinderhub.ui.components.CategoryTile
import com.kinderhub.ui.components.LayoutMode
import com.kinderhub.ui.components.ChildSelector
import com.kinderhub.ui.components.IconMapPin
import com.kinderhub.ui.components.IconChevronDown
import com.kinderhub.ui.components.IconSliders
import com.kinderhub.ui.components.IconSparkles
import com.kinderhub.ui.components.LoadingSkeletons
import com.kinderhub.ui.data.model.Activity
import com.kinderhub.ui.data.model.AiSuggestion
import com.kinderhub.ui.data.model.Category
import com.kinderhub.ui.data.model.Child
import com.kinderhub.ui.theme.KhTheme
import com.kinderhub.ui.theme.Shapes
import com.kinderhub.ui.theme.Space
import com.kinderhub.ui.util.AppStrings
import com.kinderhub.ui.util.Strings
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DiscoverScreen(
    onActivityClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    onNavigateToBookings: () -> Unit,
    onNavigateToMessages: () -> Unit,
    onNavigateToAccount: () -> Unit,
    onAddChildClick: () -> Unit = {},
    viewModel: DiscoverViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val c = KhTheme.colors
    val strings = Strings.current

    AppShell(
        selectedTab = 0,
        unreadCount = uiState.unreadMessagesCount,
        children = uiState.children,
        onDiscoverClick = { /* Already here */ },
        onBookingsClick = onNavigateToBookings,
        onMessagesClick = onNavigateToMessages,
        onAccountClick = onNavigateToAccount,
        onAddChildClick = onAddChildClick
    ) { layoutMode ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(c.bg)
        ) {
            when (uiState.screenState) {
                DiscoverScreenState.Loading -> {
                    LoadingSkeletons()
                }
                DiscoverScreenState.Loaded, DiscoverScreenState.Empty -> {
                    DiscoverContent(
                        uiState = uiState,
                        strings = strings,
                        layoutMode = layoutMode,
                        onActivityClick = onActivityClick,
                        onSearchClick = onSearchClick,
                        onChildSelect = viewModel::selectChild,
                        onAiSuggestionClick = viewModel::searchWithAiSuggestion,
                        onFavoriteClick = viewModel::toggleFavorite,
                    )
                }
                DiscoverScreenState.LocationRequest -> {
                    DiscoverContent(
                        uiState = uiState,
                        strings = strings,
                        layoutMode = layoutMode,
                        onActivityClick = onActivityClick,
                        onSearchClick = onSearchClick,
                        onChildSelect = viewModel::selectChild,
                        onAiSuggestionClick = viewModel::searchWithAiSuggestion,
                        onFavoriteClick = viewModel::toggleFavorite,
                    )
                }
                DiscoverScreenState.LocationDenied -> {
                    DiscoverContent(
                        uiState = uiState,
                        strings = strings,
                        layoutMode = layoutMode,
                        onActivityClick = onActivityClick,
                        onSearchClick = onSearchClick,
                        onChildSelect = viewModel::selectChild,
                        onAiSuggestionClick = viewModel::searchWithAiSuggestion,
                        onFavoriteClick = viewModel::toggleFavorite,
                    )
                }
                DiscoverScreenState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(strings.commonError, color = c.tx2)
                    }
                }
            }
        }
    }
}

@Composable
private fun DiscoverContent(
    uiState: DiscoverUiState,
    strings: AppStrings,
    layoutMode: LayoutMode,
    onActivityClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    onChildSelect: (String?) -> Unit,
    onAiSuggestionClick: (AiSuggestion) -> Unit,
    onFavoriteClick: (String) -> Unit,
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    // Desktop doesn't need bottom padding (uses sidebar), mobile needs space for bottom nav
    val bottomPadding = if (layoutMode == LayoutMode.Desktop) Space.s24 else 0.dp

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = bottomPadding)
    ) {
        // Header with greeting
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Space.screenPadding)
                    .padding(top = Space.s48)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Text(
                            text = uiState.greeting,
                            style = typography.h2,
                            color = c.tx,
                            fontWeight = FontWeight.Bold
                        )
                        if (uiState.subGreeting.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(Space.s4))
                            Text(
                                text = uiState.subGreeting,
                                style = typography.small,
                                color = c.tx3b
                            )
                        } else {
                            Spacer(modifier = Modifier.height(Space.s8))
                            // Location pill
                            LocationPill(
                                location = uiState.locationText,
                                onClick = { /* Open location picker */ }
                            )
                        }
                    }

                    // User avatar
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(Shapes.pill)
                            .background(c.p100),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = uiState.userInitials,
                            style = typography.button,
                            color = c.p7
                        )
                    }
                }
            }
        }

        // Child selector
        if (uiState.children.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(Space.s14))
                ChildSelectorRow(
                    children = uiState.children,
                    selectedChildId = uiState.selectedChildId,
                    showAllChildren = uiState.showAllChildren,
                    allChildrenLabel = strings.discoverAllChildren,
                    onChildSelect = onChildSelect
                )
            }
        }

        // AI Search Hero
        item {
            Spacer(modifier = Modifier.height(Space.s16))
            Column(modifier = Modifier.padding(horizontal = Space.screenPadding)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = Space.s8)
                ) {
                    IconSparkles(color = c.p6, size = 15.dp)
                    Spacer(modifier = Modifier.width(Space.s8))
                    Text(
                        text = strings.discoverAskAi,
                        style = typography.label,
                        color = c.p7,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                AiSearchHero(
                    placeholder = uiState.searchPlaceholder,
                    onClick = onSearchClick
                )
            }
        }

        // AI Suggestions
        if (uiState.aiSuggestions.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(Space.s10))
                AiSuggestionsRow(
                    suggestions = uiState.aiSuggestions,
                    onSuggestionClick = onAiSuggestionClick
                )
            }
        }

        // Categories
        if (uiState.categories.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(Space.s18))
                CategoriesRow(categories = uiState.categories)
            }
        }

        // Feed header
        item {
            Spacer(modifier = Modifier.height(Space.s20))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Space.screenPadding),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconSparkles(color = c.p6, size = 16.dp)
                    Spacer(modifier = Modifier.width(Space.s8))
                    Text(
                        text = if (uiState.selectedChildId != null) {
                            "For ${uiState.children.find { it.id == uiState.selectedChildId }?.firstName ?: "your child"}"
                        } else {
                            strings.discoverForYou
                        },
                        style = typography.h3,
                        color = c.tx,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Filters button
                Box(
                    modifier = Modifier
                        .clip(Shapes.md)
                        .background(c.p50)
                        .clickable { /* Open filters */ }
                        .padding(horizontal = Space.s12, vertical = Space.s10)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconSliders(color = c.p7, size = 15.dp)
                        Spacer(modifier = Modifier.width(Space.s8))
                        Text(
                            text = strings.discoverFilters,
                            style = typography.small,
                            color = c.p7,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        // Activity cards
        items(uiState.activities) { activity ->
            Spacer(modifier = Modifier.height(Space.s14))
            ActivityCard(
                activity = activity,
                onClick = { onActivityClick(activity.id) },
                onFavoriteClick = { onFavoriteClick(activity.id) },
                modifier = Modifier.padding(horizontal = Space.screenPadding)
            )
        }

        // Bottom spacing
        item {
            Spacer(modifier = Modifier.height(Space.s24))
        }
    }
}

@Composable
private fun LocationPill(
    location: String,
    onClick: () -> Unit
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    Box(
        modifier = Modifier
            .clip(Shapes.pill)
            .background(c.surface)
            .border(1.dp, c.bd, Shapes.pill)
            .clickable { onClick() }
            .padding(horizontal = Space.s12, vertical = Space.s8)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconMapPin(color = c.p6, size = 15.dp)
            Spacer(modifier = Modifier.width(Space.s8))
            Text(
                text = location,
                style = typography.small,
                color = c.tx2,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.width(Space.s4))
            IconChevronDown(color = c.tx3, size = 14.dp)
        }
    }
}

@Composable
private fun ChildSelectorRow(
    children: List<Child>,
    selectedChildId: String?,
    showAllChildren: Boolean,
    allChildrenLabel: String,
    onChildSelect: (String?) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = Space.screenPadding),
        horizontalArrangement = Arrangement.spacedBy(Space.s8)
    ) {
        // All children option
        ChildSelector(
            label = allChildrenLabel,
            isSelected = showAllChildren,
            onClick = { onChildSelect(null) }
        )

        // Individual children
        children.forEach { child ->
            ChildSelector(
                child = child,
                isSelected = selectedChildId == child.id,
                onClick = { onChildSelect(child.id) }
            )
        }
    }
}

@Composable
private fun AiSuggestionsRow(
    suggestions: List<AiSuggestion>,
    onSuggestionClick: (AiSuggestion) -> Unit
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = Space.screenPadding),
        horizontalArrangement = Arrangement.spacedBy(Space.s8)
    ) {
        suggestions.forEach { suggestion ->
            Box(
                modifier = Modifier
                    .clip(Shapes.pill)
                    .background(c.surface)
                    .border(1.dp, c.p100, Shapes.pill)
                    .clickable { onSuggestionClick(suggestion) }
                    .padding(horizontal = Space.s12, vertical = Space.s8)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconSparkles(color = c.p6, size = 13.dp)
                    Spacer(modifier = Modifier.width(Space.s8))
                    Text(
                        text = suggestion.label,
                        style = typography.small,
                        color = c.p7,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoriesRow(
    categories: List<Category>
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = Space.screenPadding),
        horizontalArrangement = Arrangement.spacedBy(Space.s16)
    ) {
        items(categories) { category ->
            CategoryTile(category = category, onClick = { /* Navigate to category */ })
        }
    }
}
