package com.nsicyber.mp3downloader

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nsicyber.mp3downloader.models.VideoInfo
import com.nsicyber.mp3downloader.screens.DownloadScreen
import com.nsicyber.mp3downloader.screens.InitialScreen
import com.nsicyber.mp3downloader.ui.theme.MP3DownloaderTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MP3DownloaderTheme {
                // A surface container using the 'background' color from the theme

                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "initial_screen") {

                    composable("initial_screen") {
                        var context = LocalContext.current

                        InitialScreen(
                            context,
                            navController = navController
                        )
                    }

                    composable(
                        "download_screen?model={model}",
                        arguments = listOf(
                            navArgument("model") {
                                type = NavType.StringType
                            }
                        )
                    ) {


                        val model = remember {
                            it.arguments?.getString("model")
                        }

                        DownloadScreen(
                            navController = navController,
                            model?.fromJson(VideoInfo::class.java)
                        )
                    }


                }


            }
        }
    }
}
