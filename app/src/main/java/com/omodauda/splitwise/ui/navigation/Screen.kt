package com.omodauda.splitwise.ui.navigation

sealed class Screen(val route: String) {
    object Onboarding: Screen("Onboarding")
    object Login: Screen("Login")
    object Signup: Screen("Signup")
    object ForgotPassword: Screen("ForgotPassword")

    object Home: Screen("Home")
//    object Groups: Screen(route = "Groups")
    object Activity: Screen(route = "Activity")
    object Friends: Screen(route = "Friends")
    object Profile: Screen(route = "Profile")
    object AddBill: Screen(route = "AddBill")
    object AddBillSuccess: Screen(route = "AddBillSuccess")

    // profile screens
    object AccountSettings: Screen(route = "AccountSettings")
    object OwedBillList: Screen(route = "OwedBillList")
    object OwingBillList: Screen(route = "OwingBillList")

    object BillDetails: Screen(route = "BillDetails/{billId}") {
        fun createRoute(billId: String) = "BillDetails/$billId"
    }

    object PaymentConfirmation: Screen(route = "PaymentConfirmation")
    object ConfirmPayment: Screen(route = "ConfirmPayment/{paymentId}") {
        fun createRoute(paymentId: String) = "ConfirmPayment/$paymentId"
    }
}