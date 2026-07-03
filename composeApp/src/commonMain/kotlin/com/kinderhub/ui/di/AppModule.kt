package com.kinderhub.ui.di

import com.kinderhub.ui.data.auth.Auth0Service
import com.kinderhub.ui.data.auth.MockAuth0Service
import com.kinderhub.ui.data.repository.ActivityRepository
import com.kinderhub.ui.data.repository.Auth0AuthRepository
import com.kinderhub.ui.data.repository.AuthRepository
import com.kinderhub.ui.data.repository.BookingRepository
import com.kinderhub.ui.data.repository.MessageRepository
import com.kinderhub.ui.data.repository.MockActivityRepository
import com.kinderhub.ui.data.repository.MockBookingRepository
import com.kinderhub.ui.data.repository.MockMessageRepository
import com.kinderhub.ui.data.repository.MockUserRepository
import com.kinderhub.ui.data.repository.UserRepository
import com.kinderhub.ui.screens.account.AccountViewModel
import com.kinderhub.ui.screens.activity.ActivityDetailViewModel
import com.kinderhub.ui.screens.auth.AuthViewModel
import com.kinderhub.ui.screens.booking.BookingViewModel
import com.kinderhub.ui.screens.bookings.BookingDetailViewModel
import com.kinderhub.ui.screens.bookings.MyBookingsViewModel
import com.kinderhub.ui.screens.checkout.CheckoutViewModel
import com.kinderhub.ui.screens.child.AddChildViewModel
import com.kinderhub.ui.screens.confirmation.ConfirmationViewModel
import com.kinderhub.ui.screens.discover.DiscoverViewModel
import com.kinderhub.ui.screens.messages.MessagesViewModel
import com.kinderhub.ui.screens.messages.ThreadViewModel
import com.kinderhub.ui.screens.search.SearchViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

/**
 * Auth0 service module - swap MockAuth0Service for platform-specific implementations
 */
val authModule = module {
    single<Auth0Service> { MockAuth0Service() }
    single<AuthRepository> { Auth0AuthRepository(get()) }
}

/**
 * Data repositories module - swap Mock implementations for real API when ready
 */
val dataModule = module {
    single<ActivityRepository> { MockActivityRepository() }
    single<BookingRepository> { MockBookingRepository(get()) }
    single<MessageRepository> { MockMessageRepository() }
    single<UserRepository> { MockUserRepository() }
}

/**
 * ViewModel module
 */
val viewModelModule = module {
    // Auth & Onboarding
    viewModel { AuthViewModel(get()) }
    viewModel { AddChildViewModel() }

    // Discovery
    viewModel { DiscoverViewModel(get()) }
    viewModel { SearchViewModel(get()) }
    viewModel { ActivityDetailViewModel(get()) }

    // Booking flow
    viewModel { BookingViewModel(get()) }
    viewModel { CheckoutViewModel(get()) }
    viewModel { ConfirmationViewModel(get()) }

    // My Bookings
    viewModel { MyBookingsViewModel(get()) }
    viewModel { BookingDetailViewModel(get()) }

    // Messages
    viewModel { MessagesViewModel(get()) }
    viewModel { ThreadViewModel(get()) }

    // Account
    viewModel { AccountViewModel(get()) }
}

/**
 * Combined app module
 */
val appModule = module {
    includes(authModule, dataModule, viewModelModule)
}

fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(appModule)
}
