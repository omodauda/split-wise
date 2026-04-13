package com.omodauda.splitwise.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.omodauda.splitwise.ui.components.toast.ToastHostState
import com.omodauda.splitwise.ui.features.auth.AuthViewModel
import com.omodauda.splitwise.ui.features.auth.forgotPassword.ForgotPasswordScreen
import com.omodauda.splitwise.ui.features.auth.login.LoginScreen
import com.omodauda.splitwise.ui.features.auth.onboarding.OnboardingScreen
import com.omodauda.splitwise.ui.features.auth.signup.SignupScreen

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    toastHostState: ToastHostState
) {
    navigation(
        startDestination = Screen.Onboarding.route,
        route = "auth_graph"
    ) {
        composable(route = Screen.Onboarding.route) {
            OnboardingScreen(
                goToLogin = {navController.navigate(Screen.Login.route)}
            )
        }
        composable(route = Screen.Login.route) {
            LoginScreen(
                goToSignup = {navController.navigate(Screen.Signup.route)},
                goToForgotPassword = {navController.navigate(Screen.ForgotPassword.route)},
                authViewModel = authViewModel,
                toastHostState = toastHostState
            )
        }
        composable(route = Screen.Signup.route) {
            SignupScreen(
               goBack = {navController.popBackStack()},
                authViewModel = authViewModel,
                toastHostState = toastHostState
            )
        }
        composable(route = Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                goBack = {navController.popBackStack()}
            )
        }
    }
}