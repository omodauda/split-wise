package com.example.splitwise

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.splitwise.ui.features.auth.AuthViewModel
import com.example.splitwise.ui.features.auth.AuthViewModelFactory
import com.example.splitwise.ui.theme.SplitWiseTheme

class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory((application as SplitWiseApplication).appContainer.authRepository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition {
            authViewModel.isAuthenticated.value == null
        }
        enableEdgeToEdge()
        setContent {
            SplitWiseTheme {
                SplitWiseApp()
            }
        }
    }
}

@Preview(
    name = "Light Mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Dark Mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun Preview() {
    SplitWiseTheme {
        SplitWiseApp()
    }
}