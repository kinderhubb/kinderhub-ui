package com.kinderhub.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.automirrored.outlined.Message
import androidx.compose.material.icons.automirrored.outlined.NavigateNext
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.Navigation
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material.icons.outlined.Verified
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Icon composables using Material Icons

@Composable
fun IconApple(
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    size: Dp = 24.dp
) {
    // Apple icon not in Material Icons - using a placeholder (star)
    // In production, you'd use a custom vector or brand icon
    Icon(
        imageVector = Icons.Filled.Star,
        contentDescription = "Apple",
        modifier = modifier.size(size),
        tint = color
    )
}

@Composable
fun IconMail(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    size: Dp = 24.dp
) {
    Icon(
        imageVector = Icons.Outlined.Email,
        contentDescription = "Email",
        modifier = modifier.size(size),
        tint = color
    )
}

@Composable
fun IconLock(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    size: Dp = 24.dp
) {
    Icon(
        imageVector = Icons.Outlined.Lock,
        contentDescription = "Lock",
        modifier = modifier.size(size),
        tint = color
    )
}

@Composable
fun IconEye(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    size: Dp = 24.dp
) {
    Icon(
        imageVector = Icons.Outlined.Visibility,
        contentDescription = "Show",
        modifier = modifier.size(size),
        tint = color
    )
}

@Composable
fun IconEyeOff(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    size: Dp = 24.dp
) {
    Icon(
        imageVector = Icons.Outlined.VisibilityOff,
        contentDescription = "Hide",
        modifier = modifier.size(size),
        tint = color
    )
}

@Composable
fun IconGoogle(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp
) {
    // Google icon not in Material Icons - using Language as placeholder
    Icon(
        imageVector = Icons.Outlined.Language,
        contentDescription = "Google",
        modifier = modifier.size(size),
        tint = Color(0xFF4285F4)
    )
}

@Composable
fun IconCheck(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    size: Dp = 24.dp
) {
    Icon(
        imageVector = Icons.Outlined.Check,
        contentDescription = "Check",
        modifier = modifier.size(size),
        tint = color
    )
}

@Composable
fun IconX(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    size: Dp = 24.dp
) {
    Icon(
        imageVector = Icons.Outlined.Close,
        contentDescription = "Close",
        modifier = modifier.size(size),
        tint = color
    )
}

@Composable
fun IconMail2(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    size: Dp = 24.dp
) {
    Icon(
        imageVector = Icons.Outlined.Email,
        contentDescription = "Email",
        modifier = modifier.size(size),
        tint = color
    )
}

@Composable
fun IconRefresh(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    size: Dp = 24.dp
) {
    Icon(
        imageVector = Icons.Outlined.Refresh,
        contentDescription = "Refresh",
        modifier = modifier.size(size),
        tint = color
    )
}

@Composable
fun IconPlus(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    size: Dp = 24.dp
) {
    Icon(
        imageVector = Icons.Outlined.Add,
        contentDescription = "Add",
        modifier = modifier.size(size),
        tint = color
    )
}

@Composable
fun IconSparkles(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    size: Dp = 24.dp
) {
    // Using Star as placeholder for sparkles/AI
    Icon(
        imageVector = Icons.Outlined.Star,
        contentDescription = "AI",
        modifier = modifier.size(size),
        tint = color
    )
}

@Composable
fun IconMapPin(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    size: Dp = 24.dp
) {
    Icon(
        imageVector = Icons.Outlined.LocationOn,
        contentDescription = "Location",
        modifier = modifier.size(size),
        tint = color
    )
}

@Composable
fun IconMic(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    size: Dp = 24.dp
) {
    Icon(
        imageVector = Icons.Outlined.Mic,
        contentDescription = "Microphone",
        modifier = modifier.size(size),
        tint = color
    )
}

@Composable
fun IconCompass(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    size: Dp = 24.dp
) {
    Icon(
        imageVector = Icons.Outlined.Explore,
        contentDescription = "Discover",
        modifier = modifier.size(size),
        tint = color
    )
}

@Composable
fun IconCalendarCheck(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    size: Dp = 24.dp
) {
    Icon(
        imageVector = Icons.Outlined.CalendarMonth,
        contentDescription = "Bookings",
        modifier = modifier.size(size),
        tint = color
    )
}

@Composable
fun IconMessageCircle(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    size: Dp = 24.dp
) {
    Icon(
        imageVector = Icons.AutoMirrored.Outlined.Message,
        contentDescription = "Messages",
        modifier = modifier.size(size),
        tint = color
    )
}

@Composable
fun IconUser(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    size: Dp = 24.dp
) {
    Icon(
        imageVector = Icons.Outlined.Person,
        contentDescription = "Account",
        modifier = modifier.size(size),
        tint = color
    )
}

@Composable
fun IconChevronDown(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    size: Dp = 24.dp
) {
    Icon(
        imageVector = Icons.Outlined.KeyboardArrowDown,
        contentDescription = "Expand",
        modifier = modifier.size(size),
        tint = color
    )
}

@Composable
fun IconStar(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    filled: Boolean = false,
    size: Dp = 24.dp
) {
    Icon(
        imageVector = if (filled) Icons.Filled.Star else Icons.Outlined.Star,
        contentDescription = "Rating",
        modifier = modifier.size(size),
        tint = color
    )
}

@Composable
fun IconHeart(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    filled: Boolean = false,
    size: Dp = 24.dp
) {
    Icon(
        imageVector = if (filled) Icons.Filled.Favorite else Icons.Outlined.Favorite,
        contentDescription = "Favorite",
        modifier = modifier.size(size),
        tint = color
    )
}

@Composable
fun IconBadgeCheck(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    size: Dp = 24.dp
) {
    Icon(
        imageVector = Icons.Outlined.Verified,
        contentDescription = "Verified",
        modifier = modifier.size(size),
        tint = color
    )
}

@Composable
fun IconSliders(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    size: Dp = 24.dp
) {
    Icon(
        imageVector = Icons.Outlined.Tune,
        contentDescription = "Filters",
        modifier = modifier.size(size),
        tint = color
    )
}

@Composable
fun IconArrowLeft(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    size: Dp = 24.dp
) {
    Icon(
        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
        contentDescription = "Back",
        modifier = modifier.size(size),
        tint = color
    )
}

@Composable
fun IconCalendar(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    size: Dp = 24.dp
) {
    Icon(
        imageVector = Icons.Outlined.CalendarToday,
        contentDescription = "Calendar",
        modifier = modifier.size(size),
        tint = color
    )
}

@Composable
fun IconClock(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    size: Dp = 24.dp
) {
    Icon(
        imageVector = Icons.Outlined.AccessTime,
        contentDescription = "Time",
        modifier = modifier.size(size),
        tint = color
    )
}

@Composable
fun IconUsers(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    size: Dp = 24.dp
) {
    Icon(
        imageVector = Icons.Outlined.Groups,
        contentDescription = "Users",
        modifier = modifier.size(size),
        tint = color
    )
}

@Composable
fun IconCreditCard(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    size: Dp = 24.dp
) {
    Icon(
        imageVector = Icons.Outlined.CreditCard,
        contentDescription = "Payment",
        modifier = modifier.size(size),
        tint = color
    )
}

@Composable
fun IconShield(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    size: Dp = 24.dp
) {
    Icon(
        imageVector = Icons.Outlined.Shield,
        contentDescription = "Security",
        modifier = modifier.size(size),
        tint = color
    )
}

@Composable
fun IconTag(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    size: Dp = 24.dp
) {
    Icon(
        imageVector = Icons.Outlined.LocalOffer,
        contentDescription = "Tag",
        modifier = modifier.size(size),
        tint = color
    )
}

@Composable
fun IconNavigation(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    size: Dp = 24.dp
) {
    Icon(
        imageVector = Icons.Outlined.Navigation,
        contentDescription = "Navigate",
        modifier = modifier.size(size),
        tint = color
    )
}

@Composable
fun IconShare(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    size: Dp = 24.dp
) {
    Icon(
        imageVector = Icons.Outlined.Share,
        contentDescription = "Share",
        modifier = modifier.size(size),
        tint = color
    )
}

@Composable
fun IconChevronRight(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    size: Dp = 24.dp
) {
    Icon(
        imageVector = Icons.AutoMirrored.Outlined.NavigateNext,
        contentDescription = "Next",
        modifier = modifier.size(size),
        tint = color
    )
}

@Composable
fun IconAlertCircle(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    size: Dp = 24.dp
) {
    Icon(
        imageVector = Icons.Outlined.Warning,
        contentDescription = "Alert",
        modifier = modifier.size(size),
        tint = color
    )
}

@Composable
fun IconSend(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    size: Dp = 24.dp
) {
    Icon(
        imageVector = Icons.AutoMirrored.Outlined.Send,
        contentDescription = "Send",
        modifier = modifier.size(size),
        tint = color
    )
}

@Composable
fun IconSettings(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    size: Dp = 24.dp
) {
    Icon(
        imageVector = Icons.Outlined.Settings,
        contentDescription = "Settings",
        modifier = modifier.size(size),
        tint = color
    )
}

@Composable
fun IconLogOut(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    size: Dp = 24.dp
) {
    Icon(
        imageVector = Icons.AutoMirrored.Outlined.ExitToApp,
        contentDescription = "Log out",
        modifier = modifier.size(size),
        tint = color
    )
}

@Composable
fun IconBell(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    size: Dp = 24.dp
) {
    Icon(
        imageVector = Icons.Outlined.Notifications,
        contentDescription = "Notifications",
        modifier = modifier.size(size),
        tint = color
    )
}

@Composable
fun IconHelpCircle(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    size: Dp = 24.dp
) {
    Icon(
        imageVector = Icons.AutoMirrored.Outlined.HelpOutline,
        contentDescription = "Help",
        modifier = modifier.size(size),
        tint = color
    )
}

@Composable
fun IconPanelLeftClose(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    size: Dp = 24.dp
) {
    // Using a simple chevron for panel close
    Icon(
        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
        contentDescription = "Collapse",
        modifier = modifier.size(size),
        tint = color
    )
}

@Composable
fun IconPanelLeftOpen(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    size: Dp = 24.dp
) {
    // Using a simple chevron for panel open
    Icon(
        imageVector = Icons.AutoMirrored.Outlined.NavigateNext,
        contentDescription = "Expand",
        modifier = modifier.size(size),
        tint = color
    )
}

@Composable
fun IconSearch(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    size: Dp = 24.dp
) {
    Icon(
        imageVector = Icons.Outlined.Search,
        contentDescription = "Search",
        modifier = modifier.size(size),
        tint = color
    )
}

@Composable
fun IconGlobe(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    size: Dp = 24.dp
) {
    Icon(
        imageVector = Icons.Outlined.Language,
        contentDescription = "Language",
        modifier = modifier.size(size),
        tint = color
    )
}
