package com.kinderhub.ui.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.kinderhub.ui.components.IconApple
import com.kinderhub.ui.components.IconEye
import com.kinderhub.ui.components.IconEyeOff
import com.kinderhub.ui.components.IconGoogle
import com.kinderhub.ui.components.IconMail2
import com.kinderhub.ui.components.IconRefresh
import com.kinderhub.ui.components.IconX
import com.kinderhub.ui.components.KhButton
import com.kinderhub.ui.components.KhButtonVariant
import com.kinderhub.ui.components.KhDividerWithText
import com.kinderhub.ui.components.KhTextField
import com.kinderhub.ui.theme.KhTheme
import com.kinderhub.ui.theme.Shapes
import com.kinderhub.ui.theme.Space
import com.kinderhub.ui.util.AppStrings
import com.kinderhub.ui.util.Strings
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AuthScreen(
    onAuthSuccess: () -> Unit,
    viewModel: AuthViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val c = KhTheme.colors
    val strings = Strings.current

    // Navigate on success
    LaunchedEffect(uiState.screenState) {
        if (uiState.screenState == AuthScreenState.Success) {
            onAuthSuccess()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(c.bg)
    ) {
        when (uiState.screenState) {
            AuthScreenState.VerificationPending -> {
                VerificationPendingContent(
                    email = uiState.email,
                    onResendEmail = { viewModel.resendVerificationEmail() },
                    onOpenEmail = { /* Platform specific */ },
                    isLoading = false
                )
            }
            else -> {
                AuthFormContent(
                    uiState = uiState,
                    strings = strings,
                    onEmailChange = viewModel::updateEmail,
                    onPasswordChange = viewModel::updatePassword,
                    onFirstNameChange = viewModel::updateFirstName,
                    onLastNameChange = viewModel::updateLastName,
                    onTogglePassword = viewModel::togglePasswordVisibility,
                    onToggleMode = viewModel::toggleMode,
                    onLogin = viewModel::login,
                    onSignUp = viewModel::signUp,
                    onLoginWithApple = viewModel::loginWithApple,
                    onLoginWithGoogle = viewModel::loginWithGoogle,
                    onClearError = viewModel::clearError,
                    onForgotPassword = { /* TODO */ }
                )
            }
        }
    }
}

@Composable
private fun AuthFormContent(
    uiState: AuthUiState,
    strings: AppStrings,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onTogglePassword: () -> Unit,
    onToggleMode: () -> Unit,
    onLogin: () -> Unit,
    onSignUp: () -> Unit,
    onLoginWithApple: () -> Unit,
    onLoginWithGoogle: () -> Unit,
    onClearError: () -> Unit,
    onForgotPassword: () -> Unit,
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography
    val isLogin = uiState.mode == AuthMode.Login
    val isLoading = uiState.screenState == AuthScreenState.Loading

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 420.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Space.screenPadding)
                .padding(top = Space.s64, bottom = Space.s32),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
        // Logo / Title
        Text(
            text = strings.appName,
            style = typography.display,
            color = c.p6,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(Space.s8))

        Text(
            text = if (isLogin) strings.authWelcomeBack else "Create your account",
            style = typography.h2,
            color = c.tx
        )

        Spacer(modifier = Modifier.height(Space.s8))

        Text(
            text = if (isLogin) strings.authSignInToContinue else "Join thousands of parents",
            style = typography.body,
            color = c.tx2
        )

        Spacer(modifier = Modifier.height(Space.s32))

        // Error banner
        AnimatedVisibility(
            visible = uiState.errorMessage != null,
            enter = slideInVertically() + fadeIn(),
            exit = slideOutVertically() + fadeOut()
        ) {
            ErrorBanner(
                message = uiState.errorMessage ?: "",
                onDismiss = onClearError
            )
        }

        if (uiState.errorMessage != null) {
            Spacer(modifier = Modifier.height(Space.s16))
        }

        // Social login buttons
        KhButton(
            text = strings.authContinueWithApple,
            onClick = onLoginWithApple,
            variant = KhButtonVariant.Wallet,
            loading = isLoading && uiState.loadingText.contains("Apple"),
            loadingText = strings.commonLoading,
            icon = {
                IconApple(color = Color.White, size = 20.dp)
            }
        )

        Spacer(modifier = Modifier.height(Space.s12))

        KhButton(
            text = strings.authContinueWithGoogle,
            onClick = onLoginWithGoogle,
            variant = KhButtonVariant.Outline,
            loading = isLoading && uiState.loadingText.contains("Google"),
            loadingText = strings.commonLoading,
            icon = {
                IconGoogle(size = 20.dp)
            }
        )

        Spacer(modifier = Modifier.height(Space.s24))

        KhDividerWithText(text = strings.authOrUseEmail)

        Spacer(modifier = Modifier.height(Space.s24))

        // Sign up name fields
        AnimatedVisibility(visible = !isLogin) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Space.s12)
                ) {
                    KhTextField(
                        value = uiState.firstName,
                        onValueChange = onFirstNameChange,
                        label = strings.onboardingFirstName,
                        placeholder = strings.onboardingFirstNamePlaceholder,
                        isError = uiState.firstNameError != null,
                        errorMessage = uiState.firstNameError,
                        enabled = !isLoading,
                        modifier = Modifier.weight(1f)
                    )

                    KhTextField(
                        value = uiState.lastName,
                        onValueChange = onLastNameChange,
                        label = "Last name",
                        placeholder = "Bennett",
                        isError = uiState.lastNameError != null,
                        errorMessage = uiState.lastNameError,
                        enabled = !isLoading,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(Space.s16))
            }
        }

        // Email field
        KhTextField(
            value = uiState.email,
            onValueChange = onEmailChange,
            label = strings.authEmail,
            placeholder = strings.authEmailPlaceholder,
            isError = uiState.emailError != null,
            errorMessage = uiState.emailError,
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(Space.s16))

        // Password field
        KhTextField(
            value = uiState.password,
            onValueChange = onPasswordChange,
            label = strings.authPassword,
            placeholder = strings.authPasswordPlaceholder,
            isPassword = !uiState.showPassword,
            isError = uiState.passwordError != null,
            errorMessage = uiState.passwordError,
            enabled = !isLoading,
            trailingIcon = {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onTogglePassword() },
                    contentAlignment = Alignment.Center
                ) {
                    if (uiState.showPassword) {
                        IconEyeOff(color = c.tx3, size = 20.dp)
                    } else {
                        IconEye(color = c.tx3, size = 20.dp)
                    }
                }
            }
        )

        // Forgot password link (login only)
        if (isLogin) {
            Spacer(modifier = Modifier.height(Space.s12))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = strings.authForgotPassword,
                    style = typography.small,
                    color = c.p6,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { onForgotPassword() }
                )
            }
        }

        Spacer(modifier = Modifier.height(Space.s24))

        // Submit button
        KhButton(
            text = if (isLogin) strings.authLogIn else strings.authSignUp,
            onClick = { if (isLogin) onLogin() else onSignUp() },
            variant = KhButtonVariant.Primary,
            loading = isLoading && !uiState.loadingText.contains("Apple") && !uiState.loadingText.contains("Google"),
            loadingText = uiState.loadingText
        )

        Spacer(modifier = Modifier.height(Space.s24))

        // Toggle mode
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (isLogin) "${strings.authNoAccount} " else "${strings.authHaveAccount} ",
                style = typography.body,
                color = c.tx2
            )
            Text(
                text = if (isLogin) strings.authSignUp else strings.authLogIn,
                style = typography.body.copy(fontWeight = FontWeight.SemiBold),
                color = c.p6,
                modifier = Modifier.clickable { onToggleMode() }
            )
        }

        Spacer(modifier = Modifier.height(Space.s32))

        // Terms
        Text(
            text = buildAnnotatedString {
                append("${strings.authTermsPrefix} ")
                withStyle(SpanStyle(color = c.p6, textDecoration = TextDecoration.Underline)) {
                    append(strings.authTermsOfService)
                }
                append(" ${strings.authAnd} ")
                withStyle(SpanStyle(color = c.p6, textDecoration = TextDecoration.Underline)) {
                    append(strings.authPrivacyPolicy)
                }
            },
            style = typography.small,
            color = c.tx3,
            textAlign = TextAlign.Center
        )
        }
    }
}

@Composable
private fun ErrorBanner(
    message: String,
    onDismiss: () -> Unit
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(Shapes.md)
            .background(c.errorBg)
            .padding(Space.s14),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = message,
            style = typography.small,
            color = c.error,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(Space.s8))

        Box(
            modifier = Modifier
                .size(24.dp)
                .clickable { onDismiss() },
            contentAlignment = Alignment.Center
        ) {
            IconX(color = c.error, size = 16.dp)
        }
    }
}

@Composable
private fun VerificationPendingContent(
    email: String,
    onResendEmail: () -> Unit,
    onOpenEmail: () -> Unit,
    isLoading: Boolean
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Space.screenPadding)
            .padding(top = Space.s64),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Email icon in circle
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(c.p50),
            contentAlignment = Alignment.Center
        ) {
            IconMail2(color = c.p6, size = 44.dp)
        }

        Spacer(modifier = Modifier.height(Space.s24))

        Text(
            text = "Verify your email",
            style = typography.h1,
            color = c.tx,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Space.s12))

        Text(
            text = "We've sent a verification link to",
            style = typography.body,
            color = c.tx2,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Space.s4))

        Text(
            text = email,
            style = typography.body.copy(fontWeight = FontWeight.SemiBold),
            color = c.tx,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Space.s8))

        Text(
            text = "Please check your inbox and click the link to continue.",
            style = typography.body,
            color = c.tx2,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Space.s32))

        KhButton(
            text = "Open email app",
            onClick = onOpenEmail,
            variant = KhButtonVariant.Primary
        )

        Spacer(modifier = Modifier.height(Space.s12))

        KhButton(
            text = "Resend verification email",
            onClick = onResendEmail,
            variant = KhButtonVariant.Secondary,
            loading = isLoading,
            loadingText = "Sending...",
            icon = {
                IconRefresh(color = c.p7, size = 18.dp)
            }
        )

        Spacer(modifier = Modifier.height(Space.s24))

        Text(
            text = "Didn't receive the email? Check your spam folder.",
            style = typography.small,
            color = c.tx3,
            textAlign = TextAlign.Center
        )
    }
}
