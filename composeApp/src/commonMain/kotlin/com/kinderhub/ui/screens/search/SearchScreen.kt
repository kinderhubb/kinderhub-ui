package com.kinderhub.ui.screens.search

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kinderhub.ui.components.ActivityCard
import com.kinderhub.ui.components.IconChevronDown
import com.kinderhub.ui.components.IconMapPin
import com.kinderhub.ui.components.IconSliders
import com.kinderhub.ui.components.IconSparkles
import com.kinderhub.ui.components.IconX
import com.kinderhub.ui.components.KhButton
import com.kinderhub.ui.components.KhButtonVariant
import com.kinderhub.ui.components.LoadingSkeletons
import com.kinderhub.ui.theme.KhTheme
import com.kinderhub.ui.theme.Shapes
import com.kinderhub.ui.theme.Space
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SearchScreen(
    initialQuery: String = "",
    onActivityClick: (String) -> Unit,
    onBack: () -> Unit,
    viewModel: SearchViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val c = KhTheme.colors
    val typography = KhTheme.typography

    LaunchedEffect(initialQuery) {
        if (initialQuery.isNotBlank()) {
            viewModel.updateQuery(initialQuery)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(c.bg)
    ) {
        // Search header
        SearchHeader(
            query = uiState.query,
            locationText = uiState.locationText,
            onQueryChange = viewModel::updateQuery,
            onBack = onBack
        )

        // Filter bar
        FilterBar(
            categories = uiState.categories,
            selectedCategoryId = uiState.selectedCategoryId,
            activeFilters = uiState.activeFilters,
            onCategorySelect = viewModel::selectCategory,
            onRemoveFilter = viewModel::removeFilter,
            onShowFilters = viewModel::showFilters
        )

        // Results
        when (uiState.screenState) {
            SearchScreenState.Loading -> {
                LoadingSkeletons()
            }
            SearchScreenState.Results -> {
                SearchResults(
                    results = uiState.results,
                    resultCount = uiState.resultCount,
                    sortBy = uiState.sortBy,
                    onSortChange = viewModel::updateSortBy,
                    onActivityClick = onActivityClick,
                    onFavoriteClick = { /* TODO */ }
                )
            }
            SearchScreenState.Empty -> {
                EmptySearchState(
                    query = uiState.query,
                    onLetAiFind = viewModel::letAiFindAlternatives,
                    onClearFilters = viewModel::clearAllFilters
                )
            }
            SearchScreenState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Something went wrong", color = c.tx2)
                }
            }
            SearchScreenState.Idle -> {
                // Show suggestions or recent searches
                IdleSearchState()
            }
        }
    }
}

@Composable
private fun SearchHeader(
    query: String,
    locationText: String,
    onQueryChange: (String) -> Unit,
    onBack: () -> Unit
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(c.surface)
            .padding(horizontal = Space.screenPadding)
            .padding(top = Space.s48, bottom = Space.s12)
    ) {
        // Search input with gradient border
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(Shapes.lg)
                .background(KhTheme.aiGradient())
                .padding(2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(c.surface)
                    .padding(horizontal = Space.s14, vertical = Space.s12),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconSparkles(color = c.p6, size = 20.dp)

                Spacer(modifier = Modifier.width(Space.s12))

                Box(modifier = Modifier.weight(1f)) {
                    if (query.isEmpty()) {
                        Text(
                            text = "Search activities...",
                            style = typography.body,
                            color = c.tx3
                        )
                    }
                    BasicTextField(
                        value = query,
                        onValueChange = onQueryChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        textStyle = typography.body.copy(color = c.tx),
                        cursorBrush = SolidColor(c.p6),
                        singleLine = true
                    )
                }

                if (query.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onQueryChange("") },
                        contentAlignment = Alignment.Center
                    ) {
                        IconX(color = c.tx3, size = 18.dp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(Space.s10))

        // Location row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconMapPin(color = c.p6, size = 14.dp)
            Spacer(modifier = Modifier.width(Space.s8))
            Text(
                text = locationText,
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
private fun FilterBar(
    categories: List<com.kinderhub.ui.data.model.Category>,
    selectedCategoryId: String?,
    activeFilters: List<FilterChip>,
    onCategorySelect: (String?) -> Unit,
    onRemoveFilter: (FilterChip) -> Unit,
    onShowFilters: () -> Unit
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(c.surface)
            .padding(bottom = Space.s12)
    ) {
        // Category chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = Space.screenPadding),
            horizontalArrangement = Arrangement.spacedBy(Space.s8)
        ) {
            // All category
            FilterChipView(
                label = "All",
                isSelected = selectedCategoryId == null,
                onClick = { onCategorySelect(null) }
            )

            categories.forEach { category ->
                FilterChipView(
                    label = category.name,
                    isSelected = selectedCategoryId == category.id,
                    onClick = { onCategorySelect(category.id) }
                )
            }

            // Filters button
            Box(
                modifier = Modifier
                    .clip(Shapes.pill)
                    .background(c.p50)
                    .clickable { onShowFilters() }
                    .padding(horizontal = Space.s12, vertical = Space.s8)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconSliders(color = c.p7, size = 14.dp)
                    Spacer(modifier = Modifier.width(Space.s8))
                    Text(
                        text = "Filters",
                        style = typography.small,
                        color = c.p7,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // Active filter chips
        if (activeFilters.isNotEmpty()) {
            Spacer(modifier = Modifier.height(Space.s10))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = Space.screenPadding),
                horizontalArrangement = Arrangement.spacedBy(Space.s8)
            ) {
                activeFilters.forEach { chip ->
                    RemovableChip(
                        label = chip.label,
                        onRemove = { onRemoveFilter(chip) }
                    )
                }
            }
        }
    }
}

@Composable
private fun FilterChipView(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    Box(
        modifier = Modifier
            .clip(Shapes.pill)
            .background(if (isSelected) c.p50 else c.bg)
            .border(1.dp, if (isSelected) c.pb else c.bd, Shapes.pill)
            .clickable { onClick() }
            .padding(horizontal = Space.s12, vertical = Space.s8)
    ) {
        Text(
            text = label,
            style = typography.small,
            color = if (isSelected) c.p7 else c.tx2,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
        )
    }
}

@Composable
private fun RemovableChip(
    label: String,
    onRemove: () -> Unit
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    Box(
        modifier = Modifier
            .clip(Shapes.pill)
            .background(c.p50)
            .border(1.dp, c.pb, Shapes.pill)
            .padding(start = Space.s10, end = Space.s8, top = 6.dp, bottom = 6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = label,
                style = typography.small,
                color = c.p7,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.width(Space.s8))
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clickable { onRemove() },
                contentAlignment = Alignment.Center
            ) {
                IconX(color = c.p7, size = 12.dp)
            }
        }
    }
}

@Composable
private fun SearchResults(
    results: List<com.kinderhub.ui.data.model.Activity>,
    resultCount: Int,
    sortBy: SortOption,
    onSortChange: (SortOption) -> Unit,
    onActivityClick: (String) -> Unit,
    onFavoriteClick: (String) -> Unit
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    LazyColumn(
        contentPadding = PaddingValues(bottom = Space.s24)
    ) {
        // Results header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Space.screenPadding, vertical = Space.s12),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$resultCount results",
                    style = typography.body,
                    color = c.tx2,
                    fontWeight = FontWeight.Medium
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { /* Show sort options */ }
                ) {
                    Text(
                        text = sortBy.label,
                        style = typography.small,
                        color = c.p6,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    IconChevronDown(color = c.p6, size = 14.dp)
                }
            }
        }

        // Activity cards
        items(results) { activity ->
            ActivityCard(
                activity = activity,
                onClick = { onActivityClick(activity.id) },
                onFavoriteClick = { onFavoriteClick(activity.id) },
                modifier = Modifier
                    .padding(horizontal = Space.screenPadding)
                    .padding(bottom = Space.s14)
            )
        }
    }
}

@Composable
private fun EmptySearchState(
    query: String,
    onLetAiFind: () -> Unit,
    onClearFilters: () -> Unit
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Space.screenPadding)
            .padding(top = Space.s40),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Search X icon
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(c.p50),
            contentAlignment = Alignment.Center
        ) {
            // SearchX icon
            IconX(color = c.p6, size = 44.dp)
        }

        Spacer(modifier = Modifier.height(Space.s20))

        Text(
            text = "No matches — yet",
            style = typography.h2,
            color = c.tx,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(Space.s8))

        Text(
            text = "Nothing fits your current filters. Try widening your search criteria.",
            style = typography.body,
            color = c.tx2,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Space.s24))

        KhButton(
            text = "Let AI widen the search",
            onClick = onLetAiFind,
            variant = KhButtonVariant.Ai,
            icon = { IconSparkles(color = Color.White, size = 18.dp) }
        )

        Spacer(modifier = Modifier.height(Space.s12))

        KhButton(
            text = "Clear all filters",
            onClick = onClearFilters,
            variant = KhButtonVariant.Outline
        )
    }
}

@Composable
private fun IdleSearchState() {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Space.screenPadding)
            .padding(top = Space.s24)
    ) {
        Text(
            text = "Try searching for",
            style = typography.h3,
            color = c.tx,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(Space.s16))

        val suggestions = listOf(
            "Swimming lessons near me",
            "Saturday morning football",
            "Coding classes for kids",
            "Music lessons ages 5-8"
        )

        suggestions.forEach { suggestion ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(Shapes.md)
                    .clickable { /* Search with suggestion */ }
                    .padding(vertical = Space.s12),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconSparkles(color = c.p6, size = 16.dp)
                Spacer(modifier = Modifier.width(Space.s12))
                Text(
                    text = suggestion,
                    style = typography.body,
                    color = c.tx2
                )
            }
        }
    }
}
