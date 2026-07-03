package com.kinderhub.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

object Space {
    val s4 = 4.dp
    val s6 = 6.dp
    val s8 = 8.dp
    val s10 = 10.dp
    val s12 = 12.dp
    val s14 = 14.dp
    val s16 = 16.dp
    val s18 = 18.dp
    val s20 = 20.dp
    val s22 = 22.dp
    val s24 = 24.dp
    val s32 = 32.dp
    val s40 = 40.dp
    val s48 = 48.dp
    val s64 = 64.dp

    // Screen padding
    val screenPadding = 16.dp
}

object Radius {
    val sm = 8.dp
    val md = 12.dp
    val lg = 16.dp
    val xl = 24.dp
    val pill = 999.dp
}

object Shapes {
    val sm = RoundedCornerShape(Radius.sm)
    val md = RoundedCornerShape(Radius.md)
    val lg = RoundedCornerShape(Radius.lg)
    val xl = RoundedCornerShape(Radius.xl)
    val pill = RoundedCornerShape(50)
    val topSheet = RoundedCornerShape(topStart = Radius.xl, topEnd = Radius.xl)
}

object Elevation {
    val card = 4.dp
    val floating = 10.dp
    val device = 40.dp
}

object Size {
    val buttonHeight = 48.dp
    val inputHeight = 48.dp
    val iconSm = 16.dp
    val iconMd = 20.dp
    val iconLg = 24.dp
    val touchTarget = 44.dp
    val bottomNavHeight = 78.dp
    val sidebarExpanded = 240.dp
    val sidebarCollapsed = 76.dp

    // Responsive breakpoints
    val breakpointMobile = 768.dp    // Below: mobile patterns (bottom nav)
    val breakpointTablet = 1024.dp   // At/above: desktop patterns (sidebar)
}
