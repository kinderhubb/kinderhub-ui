package com.kinderhub.ui

import androidx.compose.ui.window.ComposeUIViewController
import com.kinderhub.ui.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App()
}
