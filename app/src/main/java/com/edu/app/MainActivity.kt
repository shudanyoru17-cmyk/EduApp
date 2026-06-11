package com.edu.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.edu.app.feature.navigation.AppRoot
import com.edu.app.feature.navigation.AppViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val appViewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splash = installSplashScreen()
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        var keepOnScreen = true
        splash.setKeepOnScreenCondition { keepOnScreen }
        lifecycleScope.launch {
            appViewModel.isReady.collect { ready -> if (ready) keepOnScreen = false }
        }
        splash.setOnExitAnimationListener { provider ->
            provider.view.animate().alpha(0f).setDuration(250L)
                .withEndAction { provider.remove() }.start()
        }

        setContent { AppRoot(appViewModel = appViewModel) }
    }
}
