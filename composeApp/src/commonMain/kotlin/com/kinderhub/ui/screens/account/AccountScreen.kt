package com.kinderhub.ui.screens.account

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kinderhub.ui.components.AppShell
import com.kinderhub.ui.components.IconApple
import com.kinderhub.ui.components.IconBell
import com.kinderhub.ui.components.IconChevronRight
import com.kinderhub.ui.components.IconCreditCard
import com.kinderhub.ui.components.IconGlobe
import com.kinderhub.ui.components.IconHelpCircle
import com.kinderhub.ui.components.IconLogOut
import com.kinderhub.ui.components.IconSettings
import com.kinderhub.ui.components.IconShield
import com.kinderhub.ui.components.IconUsers
import com.kinderhub.ui.components.IconPalette
import com.kinderhub.ui.util.AppStrings
import com.kinderhub.ui.util.Strings
import com.kinderhub.ui.components.LayoutMode
import com.kinderhub.ui.components.MenuActionRow
import com.kinderhub.ui.components.MenuItem
import com.kinderhub.ui.components.MenuItemRow
import com.kinderhub.ui.components.MenuItemsList
import com.kinderhub.ui.components.MenuSection
import com.kinderhub.ui.components.MenuSectionHeader
import com.kinderhub.ui.data.model.AvatarColor
import com.kinderhub.ui.data.model.Child
import com.kinderhub.ui.data.model.PaymentMethod
import com.kinderhub.ui.data.model.PaymentMethodType
import com.kinderhub.ui.theme.KhTheme
import com.kinderhub.ui.theme.Shapes
import com.kinderhub.ui.theme.Space
import org.koin.compose.viewmodel.koinViewModel

/**
 * Menu item IDs for navigation
 */
object AccountMenuIds {
    const val EDIT_PROFILE = "edit_profile"
    const val ADD_CHILD = "add_child"
    const val ADD_PAYMENT = "add_payment"
    const val NOTIFICATIONS = "notifications"
    const val PRIVACY = "privacy"
    const val SETTINGS = "settings"
    const val LANGUAGE = "language"
    const val THEME = "theme"
    const val HELP = "help"
    const val TERMS = "terms"
    const val LOGOUT = "logout"

    fun editChild(id: String) = "edit_child:$id"
    fun editPayment(id: String) = "edit_payment:$id"
}

@Composable
fun AccountScreen(
    onLoggedOut: () -> Unit,
    onNavigateToDiscover: () -> Unit = {},
    onNavigateToBookings: () -> Unit = {},
    onNavigateToMessages: () -> Unit = {},
    onMenuItemClick: (String) -> Unit = {},
    viewModel: AccountViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val c = KhTheme.colors
    val strings = Strings.current

    // Build settings menu items
    val settingsItems = listOf(
        MenuItem(
            id = AccountMenuIds.NOTIFICATIONS,
            title = strings.accountNotifications,
            icon = { color, size -> IconBell(color = color, size = size) }
        ),
        MenuItem(
            id = AccountMenuIds.PRIVACY,
            title = strings.accountPrivacy,
            icon = { color, size -> IconShield(color = color, size = size) }
        ),
        MenuItem(
            id = AccountMenuIds.LANGUAGE,
            title = "Language",
            icon = { color, size -> IconGlobe(color = color, size = size) }
        ),
        MenuItem(
            id = AccountMenuIds.THEME,
            title = "Theme",
            subtitle = "Change app appearance",
            icon = { color, size -> IconPalette(color = color, size = size) }
        ),
        MenuItem(
            id = AccountMenuIds.SETTINGS,
            title = strings.accountAppSettings,
            icon = { color, size -> IconSettings(color = color, size = size) }
        )
    )

    // Build support menu items
    val supportItems = listOf(
        MenuItem(
            id = AccountMenuIds.HELP,
            title = strings.accountHelp,
            icon = { color, size -> IconHelpCircle(color = color, size = size) }
        ),
        MenuItem(
            id = AccountMenuIds.TERMS,
            title = strings.accountTerms,
            icon = { color, size -> IconHelpCircle(color = color, size = size) }
        )
    )

    // Handle menu item clicks
    val handleMenuClick: (String) -> Unit = { itemId ->
        when (itemId) {
            AccountMenuIds.LOGOUT -> viewModel.logout(onLoggedOut)
            else -> onMenuItemClick(itemId)
        }
    }

    AppShell(
        selectedTab = 3,
        onDiscoverClick = onNavigateToDiscover,
        onBookingsClick = onNavigateToBookings,
        onMessagesClick = onNavigateToMessages,
        onAccountClick = { /* Already here */ }
    ) { layoutMode ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(c.bg)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = if (layoutMode == LayoutMode.Desktop) Space.s24 else 0.dp)
            ) {
                // Header
                AccountHeader(title = strings.accountTitle)

                // Profile card
                uiState.user?.let { user ->
                    ProfileCard(
                        name = "${user.firstName} ${user.lastName}",
                        email = user.email,
                        initials = user.avatarInitials,
                        onClick = { handleMenuClick(AccountMenuIds.EDIT_PROFILE) }
                    )
                }

                Spacer(modifier = Modifier.height(Space.s20))

                // Children section
                if (uiState.children.isNotEmpty()) {
                    MenuSectionHeader(title = strings.accountChildren)
                    ChildrenMenuSection(
                        children = uiState.children,
                        strings = strings,
                        onEditChild = { childId -> handleMenuClick(AccountMenuIds.editChild(childId)) },
                        onAddChild = { handleMenuClick(AccountMenuIds.ADD_CHILD) }
                    )
                    Spacer(modifier = Modifier.height(Space.s20))
                }

                // Payment methods
                if (uiState.paymentMethods.isNotEmpty()) {
                    MenuSectionHeader(title = strings.accountPaymentMethods)
                    PaymentMethodsMenuSection(
                        paymentMethods = uiState.paymentMethods,
                        strings = strings,
                        onEditPayment = { paymentId -> handleMenuClick(AccountMenuIds.editPayment(paymentId)) },
                        onAddPayment = { handleMenuClick(AccountMenuIds.ADD_PAYMENT) }
                    )
                    Spacer(modifier = Modifier.height(Space.s20))
                }

                // Settings section
                MenuSectionHeader(title = strings.accountSettings)
                MenuItemsList(
                    items = settingsItems,
                    onItemClick = handleMenuClick
                )

                Spacer(modifier = Modifier.height(Space.s20))

                // Support section
                MenuSectionHeader(title = strings.accountSupport)
                MenuItemsList(
                    items = supportItems,
                    onItemClick = handleMenuClick
                )

                Spacer(modifier = Modifier.height(Space.s24))

                // Logout
                LogoutButton(
                    isLoading = uiState.isLoggingOut,
                    logoutText = strings.accountLogOut,
                    loggingOutText = strings.accountLoggingOut,
                    onClick = { handleMenuClick(AccountMenuIds.LOGOUT) }
                )

                Spacer(modifier = Modifier.height(Space.s40))
            }
        }
    }
}

@Composable
private fun AccountHeader(title: String) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(c.surface)
            .padding(horizontal = Space.screenPadding)
            .padding(top = Space.s48, bottom = Space.s16)
    ) {
        Text(
            text = title,
            style = typography.h1,
            color = c.tx,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ProfileCard(
    name: String,
    email: String,
    initials: String,
    onClick: () -> Unit
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(c.surface)
            .clickable { onClick() }
            .padding(Space.screenPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(c.p50),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initials,
                style = typography.h2,
                color = c.p7,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.width(Space.s16))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                style = typography.h3,
                color = c.tx,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = email,
                style = typography.small,
                color = c.tx2
            )
        }

        IconChevronRight(color = c.tx3, size = 20.dp)
    }
}

@Composable
private fun ChildrenMenuSection(
    children: List<Child>,
    strings: AppStrings,
    onEditChild: (String) -> Unit,
    onAddChild: () -> Unit
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    // Convert children to menu items
    val childItems = children.map { child ->
        val avatarColor = when (child.avatarColor) {
            AvatarColor.Accent -> c.ac
            AvatarColor.Sunshine -> c.sun
            AvatarColor.Primary -> c.p6
            AvatarColor.Success -> c.succ
        }

        MenuItem(
            id = child.id,
            title = child.firstName,
            subtitle = strings.accountYearsOld(child.age),
            icon = { _, size ->
                Box(
                    modifier = Modifier
                        .size(size * 2)
                        .clip(CircleShape)
                        .background(avatarColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = child.firstName.first().toString(),
                        style = typography.body,
                        color = avatarColor,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },
            iconBackgroundColor = Color.Transparent
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(c.surface)
    ) {
        childItems.forEachIndexed { index, item ->
            MenuItemRow(
                item = item,
                onClick = { onEditChild(item.id) }
            )
            if (index < childItems.lastIndex) {
                HorizontalDivider(
                    color = c.bd,
                    thickness = 1.dp,
                    modifier = Modifier.padding(start = 70.dp)
                )
            }
        }

        HorizontalDivider(color = c.bd, thickness = 1.dp)

        MenuActionRow(
            title = strings.accountAddChild,
            icon = { color, size -> IconUsers(color = color, size = size) },
            onClick = onAddChild
        )
    }
}

@Composable
private fun PaymentMethodsMenuSection(
    paymentMethods: List<PaymentMethod>,
    strings: AppStrings,
    onEditPayment: (String) -> Unit,
    onAddPayment: () -> Unit
) {
    val c = KhTheme.colors

    // Convert payment methods to menu items
    val paymentItems = paymentMethods.map { method ->
        MenuItem(
            id = method.id,
            title = when (method.type) {
                PaymentMethodType.ApplePay -> "Apple Pay"
                PaymentMethodType.GooglePay -> "Google Pay"
                PaymentMethodType.Card -> "${method.brand} •••• ${method.last4}"
            },
            subtitle = if (method.isDefault) strings.accountDefault else null,
            icon = { color, size ->
                when (method.type) {
                    PaymentMethodType.ApplePay -> IconApple(color = color, size = size)
                    else -> IconCreditCard(color = color, size = size)
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(c.surface)
    ) {
        paymentItems.forEachIndexed { index, item ->
            MenuItemRow(
                item = item,
                onClick = { onEditPayment(item.id) }
            )
            if (index < paymentItems.lastIndex) {
                HorizontalDivider(
                    color = c.bd,
                    thickness = 1.dp,
                    modifier = Modifier.padding(start = 70.dp)
                )
            }
        }

        HorizontalDivider(color = c.bd, thickness = 1.dp)

        MenuActionRow(
            title = strings.accountAddPayment,
            icon = { color, size -> IconCreditCard(color = color, size = size) },
            onClick = onAddPayment
        )
    }
}

@Composable
private fun LogoutButton(
    isLoading: Boolean,
    logoutText: String,
    loggingOutText: String,
    onClick: () -> Unit
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Space.screenPadding)
            .clip(Shapes.lg)
            .background(c.surface)
            .clickable(enabled = !isLoading) { onClick() }
            .padding(Space.s16),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconLogOut(color = c.error, size = 20.dp)
        Spacer(modifier = Modifier.width(Space.s10))
        Text(
            text = if (isLoading) loggingOutText else logoutText,
            style = typography.body,
            color = c.error,
            fontWeight = FontWeight.SemiBold
        )
    }
}
