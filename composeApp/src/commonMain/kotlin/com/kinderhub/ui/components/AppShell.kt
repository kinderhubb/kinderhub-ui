package com.kinderhub.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kinderhub.ui.data.model.Child
import com.kinderhub.ui.theme.Size

/**
 * Layout mode based on screen width
 */
enum class LayoutMode {
    Mobile,  // <768dp: Bottom nav, single column
    Desktop  // ≥1024dp: Sidebar, multi-column layouts
}

/**
 * Responsive app shell that switches between:
 * - Desktop (≥1024dp): Collapsible sidebar + content
 * - Mobile (<768dp): Content + bottom navigation bar
 *
 * The tablet range (768-1023dp) uses mobile layout by default.
 */
@Composable
fun AppShell(
    selectedTab: Int,
    unreadCount: Int = 0,
    children: List<Child> = emptyList(),
    onDiscoverClick: () -> Unit,
    onBookingsClick: () -> Unit,
    onMessagesClick: () -> Unit,
    onAccountClick: () -> Unit,
    onAddChildClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    content: @Composable (LayoutMode) -> Unit
) {
    var isSidebarCollapsed by rememberSaveable { mutableStateOf(false) }

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val layoutMode = when {
            maxWidth >= Size.breakpointTablet -> LayoutMode.Desktop
            else -> LayoutMode.Mobile
        }

        when (layoutMode) {
            LayoutMode.Desktop -> {
                DesktopLayout(
                    selectedTab = selectedTab,
                    unreadCount = unreadCount,
                    children = children,
                    isSidebarCollapsed = isSidebarCollapsed,
                    onToggleSidebar = { isSidebarCollapsed = !isSidebarCollapsed },
                    onDiscoverClick = onDiscoverClick,
                    onBookingsClick = onBookingsClick,
                    onMessagesClick = onMessagesClick,
                    onAccountClick = onAccountClick,
                    onAddChildClick = onAddChildClick,
                    content = { content(LayoutMode.Desktop) }
                )
            }
            LayoutMode.Mobile -> {
                MobileLayout(
                    selectedTab = selectedTab,
                    unreadCount = unreadCount,
                    onDiscoverClick = onDiscoverClick,
                    onBookingsClick = onBookingsClick,
                    onMessagesClick = onMessagesClick,
                    onAccountClick = onAccountClick,
                    content = { content(LayoutMode.Mobile) }
                )
            }
        }
    }
}

@Composable
private fun DesktopLayout(
    selectedTab: Int,
    unreadCount: Int,
    children: List<Child>,
    isSidebarCollapsed: Boolean,
    onToggleSidebar: () -> Unit,
    onDiscoverClick: () -> Unit,
    onBookingsClick: () -> Unit,
    onMessagesClick: () -> Unit,
    onAccountClick: () -> Unit,
    onAddChildClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Row(modifier = Modifier.fillMaxSize()) {
        Sidebar(
            selectedTab = selectedTab,
            isCollapsed = isSidebarCollapsed,
            unreadCount = unreadCount,
            children = children,
            onToggleCollapse = onToggleSidebar,
            onDiscoverClick = onDiscoverClick,
            onBookingsClick = onBookingsClick,
            onMessagesClick = onMessagesClick,
            onAccountClick = onAccountClick,
            onAddChildClick = onAddChildClick
        )

        Box(modifier = Modifier.weight(1f).fillMaxSize()) {
            content()
        }
    }
}

@Composable
private fun MobileLayout(
    selectedTab: Int,
    unreadCount: Int,
    onDiscoverClick: () -> Unit,
    onBookingsClick: () -> Unit,
    onMessagesClick: () -> Unit,
    onAccountClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Main content with bottom padding for nav bar
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = Size.bottomNavHeight)
        ) {
            content()
        }

        // Bottom navigation
        BottomNavBar(
            selectedTab = selectedTab,
            unreadCount = unreadCount,
            onDiscoverClick = onDiscoverClick,
            onBookingsClick = onBookingsClick,
            onMessagesClick = onMessagesClick,
            onAccountClick = onAccountClick,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

/**
 * Simplified shell for screens that don't need navigation
 * (e.g., auth flow, onboarding)
 */
@Composable
fun SimpleShell(
    modifier: Modifier = Modifier,
    content: @Composable (LayoutMode) -> Unit
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val layoutMode = when {
            maxWidth >= Size.breakpointTablet -> LayoutMode.Desktop
            else -> LayoutMode.Mobile
        }
        content(layoutMode)
    }
}
