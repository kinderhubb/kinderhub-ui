package com.kinderhub.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kinderhub.ui.screens.account.AccountScreen
import com.kinderhub.ui.screens.activity.ActivityDetailScreen
import com.kinderhub.ui.screens.auth.AuthScreen
import com.kinderhub.ui.screens.booking.BookingScreen
import com.kinderhub.ui.screens.bookings.BookingDetailScreen
import com.kinderhub.ui.screens.bookings.MyBookingsScreen
import com.kinderhub.ui.screens.checkout.CheckoutScreen
import com.kinderhub.ui.screens.child.AddChildScreen
import com.kinderhub.ui.screens.child.EditChildScreen
import com.kinderhub.ui.screens.payment.EditPaymentScreen
import com.kinderhub.ui.data.repository.UserRepository
import org.koin.compose.koinInject
import com.kinderhub.ui.screens.confirmation.ConfirmationScreen
import com.kinderhub.ui.screens.discover.DiscoverScreen
import com.kinderhub.ui.screens.messages.MessagesScreen
import com.kinderhub.ui.screens.messages.ThreadScreen
import com.kinderhub.ui.screens.search.SearchScreen
import com.kinderhub.ui.screens.settings.LanguageSettingsScreen
import com.kinderhub.ui.screens.settings.ThemeSettingsScreen

/**
 * App navigation routes
 */
object Routes {
    const val AUTH = "auth"
    const val ADD_CHILD = "add_child"
    const val DISCOVER = "discover"
    const val SEARCH = "search?query={query}"
    const val ACTIVITY_DETAIL = "activity/{activityId}"
    const val BOOKING = "booking/{activityId}?sessionId={sessionId}&childId={childId}"
    const val CHECKOUT = "checkout/{activityId}/{sessionId}/{childId}"
    const val CONFIRMATION = "confirmation/{bookingId}"
    const val BOOKINGS = "bookings"
    const val BOOKING_DETAIL = "booking_detail/{bookingId}"
    const val MESSAGES = "messages"
    const val THREAD = "thread/{threadId}"
    const val ACCOUNT = "account"
    const val EDIT_CHILD = "edit_child/{childId}"
    const val ADD_PAYMENT = "add_payment"
    const val EDIT_PAYMENT = "edit_payment/{paymentId}"
    const val NOTIFICATIONS = "notifications"
    const val PRIVACY = "privacy"
    const val SETTINGS = "settings"
    const val HELP = "help"
    const val TERMS = "terms"
    const val LANGUAGE = "language"
    const val THEME = "theme"

    fun activityDetail(activityId: String) = "activity/$activityId"
    fun editChild(childId: String) = "edit_child/$childId"
    fun editPayment(paymentId: String) = "edit_payment/$paymentId"
    fun booking(activityId: String, sessionId: String? = null, childId: String? = null): String {
        var route = "booking/$activityId"
        if (sessionId != null) route += "?sessionId=$sessionId"
        if (childId != null) route += if (sessionId != null) "&childId=$childId" else "?childId=$childId"
        return route
    }
    fun checkout(activityId: String, sessionId: String, childId: String) =
        "checkout/$activityId/$sessionId/$childId"
    fun confirmation(bookingId: String) = "confirmation/$bookingId"
    fun bookingDetail(bookingId: String) = "booking_detail/$bookingId"
    fun thread(threadId: String) = "thread/$threadId"
    fun search(query: String = "") = "search?query=$query"
}

@Composable
fun KinderHubNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Routes.AUTH
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // P1: Auth
        composable(Routes.AUTH) {
            AuthScreen(
                onAuthSuccess = {
                    navController.navigate(Routes.ADD_CHILD) {
                        popUpTo(Routes.AUTH) { inclusive = true }
                    }
                }
            )
        }

        // P2: Add Child
        composable(Routes.ADD_CHILD) {
            AddChildScreen(
                onComplete = {
                    navController.navigate(Routes.DISCOVER) {
                        popUpTo(Routes.ADD_CHILD) { inclusive = true }
                    }
                },
                onSkip = {
                    navController.navigate(Routes.DISCOVER) {
                        popUpTo(Routes.ADD_CHILD) { inclusive = true }
                    }
                }
            )
        }

        // P3: Discover
        composable(Routes.DISCOVER) {
            DiscoverScreen(
                onActivityClick = { activityId ->
                    navController.navigate(Routes.activityDetail(activityId))
                },
                onSearchClick = {
                    navController.navigate(Routes.search())
                },
                onNavigateToBookings = {
                    navController.navigate(Routes.BOOKINGS)
                },
                onNavigateToMessages = {
                    navController.navigate(Routes.MESSAGES)
                },
                onNavigateToAccount = {
                    navController.navigate(Routes.ACCOUNT)
                },
                onAddChildClick = {
                    navController.navigate(Routes.ADD_CHILD)
                }
            )
        }

        // P4: Search
        composable(
            route = Routes.SEARCH,
            arguments = listOf(
                navArgument("query") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) { backStackEntry ->
            val query = backStackEntry.arguments?.getString("query") ?: ""
            SearchScreen(
                initialQuery = query,
                onActivityClick = { activityId ->
                    navController.navigate(Routes.activityDetail(activityId))
                },
                onBack = { navController.popBackStack() }
            )
        }

        // P5: Activity Detail
        composable(
            route = Routes.ACTIVITY_DETAIL,
            arguments = listOf(
                navArgument("activityId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val activityId = backStackEntry.arguments?.getString("activityId") ?: ""
            ActivityDetailScreen(
                activityId = activityId,
                onBook = { actId, sessionId, childId ->
                    navController.navigate(Routes.checkout(actId, sessionId, childId))
                },
                onExpressBook = { actId, sessionId, childId ->
                    // Express book goes straight to checkout
                    navController.navigate(Routes.checkout(actId, sessionId, childId))
                },
                onAskQuestion = { actId ->
                    // Navigate to messages with activity context
                    navController.navigate(Routes.MESSAGES)
                },
                onBack = { navController.popBackStack() }
            )
        }

        // P6: Booking Selection
        composable(
            route = Routes.BOOKING,
            arguments = listOf(
                navArgument("activityId") { type = NavType.StringType },
                navArgument("sessionId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
                navArgument("childId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val activityId = backStackEntry.arguments?.getString("activityId") ?: ""
            val sessionId = backStackEntry.arguments?.getString("sessionId")
            val childId = backStackEntry.arguments?.getString("childId")

            BookingScreen(
                activityId = activityId,
                sessionId = sessionId,
                childId = childId,
                onProceedToCheckout = { actId, sessId, chId ->
                    navController.navigate(Routes.checkout(actId, sessId, chId))
                },
                onBack = { navController.popBackStack() }
            )
        }

        // P7: Checkout & Payment
        composable(
            route = Routes.CHECKOUT,
            arguments = listOf(
                navArgument("activityId") { type = NavType.StringType },
                navArgument("sessionId") { type = NavType.StringType },
                navArgument("childId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val activityId = backStackEntry.arguments?.getString("activityId") ?: ""
            val sessionId = backStackEntry.arguments?.getString("sessionId") ?: ""
            val childId = backStackEntry.arguments?.getString("childId") ?: ""

            CheckoutScreen(
                activityId = activityId,
                sessionId = sessionId,
                childId = childId,
                onPaymentSuccess = { bookingId ->
                    navController.navigate(Routes.confirmation(bookingId)) {
                        popUpTo(Routes.DISCOVER)
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        // P8: Booking Confirmation
        composable(
            route = Routes.CONFIRMATION,
            arguments = listOf(
                navArgument("bookingId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val bookingId = backStackEntry.arguments?.getString("bookingId") ?: ""

            ConfirmationScreen(
                bookingId = bookingId,
                onViewBookings = {
                    navController.navigate(Routes.BOOKINGS) {
                        popUpTo(Routes.DISCOVER)
                    }
                },
                onBackToDiscover = {
                    navController.navigate(Routes.DISCOVER) {
                        popUpTo(Routes.DISCOVER) { inclusive = true }
                    }
                }
            )
        }

        // P9: My Bookings
        composable(Routes.BOOKINGS) {
            MyBookingsScreen(
                onBookingClick = { bookingId ->
                    navController.navigate(Routes.bookingDetail(bookingId))
                },
                onExplore = {
                    navController.navigate(Routes.DISCOVER) {
                        popUpTo(Routes.DISCOVER) { inclusive = true }
                    }
                },
                onNavigateToDiscover = {
                    navController.navigate(Routes.DISCOVER) {
                        popUpTo(Routes.DISCOVER) { inclusive = true }
                    }
                },
                onNavigateToMessages = {
                    navController.navigate(Routes.MESSAGES)
                },
                onNavigateToAccount = {
                    navController.navigate(Routes.ACCOUNT)
                }
            )
        }

        // P10: Booking Detail
        composable(
            route = Routes.BOOKING_DETAIL,
            arguments = listOf(
                navArgument("bookingId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val bookingId = backStackEntry.arguments?.getString("bookingId") ?: ""

            BookingDetailScreen(
                bookingId = bookingId,
                onMessageProvider = { providerName ->
                    navController.navigate(Routes.MESSAGES)
                },
                onCancel = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }

        // P11: Messages
        composable(Routes.MESSAGES) {
            MessagesScreen(
                onThreadClick = { threadId ->
                    navController.navigate(Routes.thread(threadId))
                },
                onNavigateToDiscover = {
                    navController.navigate(Routes.DISCOVER) {
                        popUpTo(Routes.DISCOVER) { inclusive = true }
                    }
                },
                onNavigateToBookings = {
                    navController.navigate(Routes.BOOKINGS)
                },
                onNavigateToAccount = {
                    navController.navigate(Routes.ACCOUNT)
                }
            )
        }

        // Thread detail
        composable(
            route = Routes.THREAD,
            arguments = listOf(
                navArgument("threadId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val threadId = backStackEntry.arguments?.getString("threadId") ?: ""

            ThreadScreen(
                threadId = threadId,
                onBack = { navController.popBackStack() }
            )
        }

        // P12: Account
        composable(Routes.ACCOUNT) {
            AccountScreen(
                onLoggedOut = {
                    navController.navigate(Routes.AUTH) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigateToDiscover = {
                    navController.navigate(Routes.DISCOVER) {
                        popUpTo(Routes.DISCOVER) { inclusive = true }
                    }
                },
                onNavigateToBookings = {
                    navController.navigate(Routes.BOOKINGS)
                },
                onNavigateToMessages = {
                    navController.navigate(Routes.MESSAGES)
                },
                onMenuItemClick = { itemId ->
                    // Handle menu navigation based on item ID
                    when {
                        itemId.startsWith("edit_child:") -> {
                            val childId = itemId.removePrefix("edit_child:")
                            navController.navigate(Routes.editChild(childId))
                        }
                        itemId.startsWith("edit_payment:") -> {
                            val paymentId = itemId.removePrefix("edit_payment:")
                            navController.navigate(Routes.editPayment(paymentId))
                        }
                        itemId == "add_child" -> navController.navigate(Routes.ADD_CHILD)
                        itemId == "add_payment" -> navController.navigate(Routes.ADD_PAYMENT)
                        itemId == "notifications" -> navController.navigate(Routes.NOTIFICATIONS)
                        itemId == "privacy" -> navController.navigate(Routes.PRIVACY)
                        itemId == "settings" -> navController.navigate(Routes.SETTINGS)
                        itemId == "help" -> navController.navigate(Routes.HELP)
                        itemId == "terms" -> navController.navigate(Routes.TERMS)
                        itemId == "language" -> navController.navigate(Routes.LANGUAGE)
                        itemId == "theme" -> navController.navigate(Routes.THEME)
                        itemId == "edit_profile" -> navController.navigate(Routes.SETTINGS) // TODO: Add profile edit
                    }
                }
            )
        }

        // Edit Child screen
        composable(
            route = Routes.EDIT_CHILD,
            arguments = listOf(
                navArgument("childId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val childId = backStackEntry.arguments?.getString("childId") ?: ""
            val userRepository: UserRepository = koinInject()
            val child = userRepository.getChildById(childId)

            EditChildScreen(
                child = child,
                onSave = { updatedChild ->
                    userRepository.updateChild(updatedChild)
                    navController.popBackStack()
                },
                onDelete = { id ->
                    userRepository.deleteChild(id)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }

        // Add Payment placeholder
        composable(Routes.ADD_PAYMENT) {
            PlaceholderScreen(title = "Add Payment", navController = navController)
        }

        // Edit Payment screen
        composable(
            route = Routes.EDIT_PAYMENT,
            arguments = listOf(
                navArgument("paymentId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val paymentId = backStackEntry.arguments?.getString("paymentId") ?: ""
            val userRepository: UserRepository = koinInject()
            val paymentMethod = userRepository.getPaymentMethodById(paymentId)

            EditPaymentScreen(
                paymentMethod = paymentMethod,
                onSetDefault = { id ->
                    userRepository.setDefaultPaymentMethod(id)
                },
                onDelete = { id ->
                    userRepository.deletePaymentMethod(id)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.NOTIFICATIONS) {
            PlaceholderScreen(title = "Notifications", navController = navController)
        }
        composable(Routes.PRIVACY) {
            PlaceholderScreen(title = "Privacy & Security", navController = navController)
        }
        composable(Routes.SETTINGS) {
            PlaceholderScreen(title = "App Settings", navController = navController)
        }
        composable(Routes.HELP) {
            PlaceholderScreen(title = "Help Centre", navController = navController)
        }
        composable(Routes.TERMS) {
            PlaceholderScreen(title = "Terms & Privacy Policy", navController = navController)
        }

        // Language Settings
        composable(Routes.LANGUAGE) {
            LanguageSettingsScreen(
                onBack = { navController.popBackStack() }
            )
        }

        // Theme Settings
        composable(Routes.THEME) {
            ThemeSettingsScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@Composable
private fun PlaceholderScreen(
    title: String,
    navController: NavHostController
) {
    val c = com.kinderhub.ui.theme.KhTheme.colors
    val typography = com.kinderhub.ui.theme.KhTheme.typography

    androidx.compose.foundation.layout.Box(
        modifier = Modifier
            .fillMaxSize()
            .background(c.bg),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        androidx.compose.foundation.layout.Column(
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
            modifier = Modifier.padding(com.kinderhub.ui.theme.Space.screenPadding)
        ) {
            androidx.compose.material3.Text(
                text = title,
                style = typography.h2,
                color = c.tx
            )
            androidx.compose.foundation.layout.Spacer(
                modifier = Modifier.height(16.dp)
            )
            com.kinderhub.ui.components.KhButton(
                text = "Go Back",
                onClick = { navController.popBackStack() },
                variant = com.kinderhub.ui.components.KhButtonVariant.Secondary
            )
        }
    }
}
