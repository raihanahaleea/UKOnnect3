package com.example.ukonnect2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ukonnect2.ui.component.BottomNavBar
import com.example.ukonnect2.ui.screen.*
import com.example.ukonnect2.ui.theme.UKOnnect2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UKOnnect2Theme {
                val navController = rememberNavController()
                val peminjamanVM: PeminjamanViewModel = viewModel()

                // 🔹 Root Navigasi — mulai dari login
                NavHost(
                    navController = navController,
                    startDestination = "login"
                ) {

                    // 🔐 LOGIN SCREEN
                    composable("login") {
                        LoginScreen(
                            onLoginSuccess = {
                                // ✅ Pindah ke halaman utama dengan BottomNav
                                navController.navigate("main") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        )
                    }

                    // 🏠 MAIN SCREEN DENGAN BOTTOM NAV
                    composable("main") {
                        val innerNav = rememberNavController()

                        Scaffold(
                            bottomBar = {
                                BottomNavBar(
                                    currentRoute = innerNav.currentBackStackEntryAsState().value?.destination?.route,
                                    onNavigate = { route ->
                                        innerNav.navigate(route) {
                                            popUpTo(innerNav.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        ) { innerPadding ->
                            NavHost(
                                navController = innerNav,
                                startDestination = "beranda",
                                modifier = Modifier.padding(innerPadding)
                            ) {
                                composable("beranda") {
                                    MainScreen(
                                        peminjamanVM = peminjamanVM,
                                        onGoPinjam = { innerNav.navigate("pinjam") },
                                        onGoAbsensi = { innerNav.navigate("absensi") }
                                    )
                                }
                                composable("aktivitas") { AktivitasScreen() }
                                composable("pinjam") { PinjamScreen(viewModel = peminjamanVM) }
                                composable("galeri") { GaleriScreen() }
                                composable("profil") { ProfilScreen() }
                                composable("absensi") {
                                    AbsensiScreen(onBack = { innerNav.popBackStack() })
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
