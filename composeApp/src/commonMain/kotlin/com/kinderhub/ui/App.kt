package com.kinderhub.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kinderhub.ui.navigation.KinderHubNavHost
import com.kinderhub.ui.theme.KhTheme
import com.kinderhub.ui.theme.KinderHubTheme
import com.kinderhub.ui.theme.Palette
import com.kinderhub.ui.util.AppLanguage
import com.kinderhub.ui.util.LocalizationProvider
import org.koin.compose.KoinContext

@Composable
fun App(
    palette: Palette = Palette.DuskyRose,
    initialLanguage: AppLanguage = AppLanguage.English
) {
    KoinContext {
        LocalizationProvider(initialLanguage = initialLanguage) {
            KinderHubTheme(palette = palette) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = KhTheme.colors.bg
                ) {
                    KinderHubNavHost()
                }
            }
        }
    }
}
