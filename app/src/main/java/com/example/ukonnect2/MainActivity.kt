package com.example.ukonnect2

import android.os.Bundle
import android.widget.Toast // 👈 Pastikan import ini ada
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
                val navController = rememberNavController() // 👈 Controller LUAR
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
                                navController.navigate("main") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        )
                    }

                    // 🏠 MAIN SCREEN (dengan BottomNav)
                    composable("main") {
                        val innerNav = rememberNavController() // 👈 Controller DALAM

                        Scaffold(
                            bottomBar = {
                                BottomNavBar(
                                    currentRoute = innerNav.currentBackStackEntryAsState().value?.destination?.route,
                                    // (A) Navigasi tab biasa pakai innerNav
                                    onNavigate = { route ->
                                        innerNav.navigate(route) {
                                            popUpTo(innerNav.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    // (B) Navigasi FAB Absen pakai navController LUAR
                                    onAbsenClick = {
                                        navController.navigate("qr_scanner")
                                    }
                                )
                            }
                        ) { innerPadding ->
                            // NavHost DALAM ini tidak berubah
                            NavHost(
                                navController = innerNav,
                                startDestination = "beranda",
                                modifier = Modifier.padding(innerPadding)
                            ) {
                                composable("beranda") {
                                    MainScreen(
                                        peminjamanVM = peminjamanVM,
                                        onGoPinjam = { innerNav.navigate("pinjam") },
                                        onGoAbsensi = { innerNav.navigate("absensi") },
                                        onGoProfil = { innerNav.navigate("profil") },
                                        onGoAktivitas = { innerNav.navigate("aktivitas") },
                                        onGoBeranda = { innerNav.navigate("beranda") }
                                    )
                                }
                                composable("aktivitas") { AktivitasScreen() }
                                composable("pinjam") { PinjamScreen(viewModel = peminjamanVM) }
                                composable("galeri") { GaleriScreen() }
                                composable("profil") { ProfilScreen() }
                                composable("absensi") {
                                    AbsensiScreen(
                                        onBack = { innerNav.popBackStack() }
                                    )
                                }
                            }
                        }
                    }

                    // ⭐️ RUTE FULL-SCREEN BARU ⭐️
                    // Ini adalah rute full-screen di luar 'main'
                    composable("qr_scanner") {
                        QrScannerScreen(
                            onBack = {
                                navController.popBackStack() // Kembali pakai navController
                            },
                            // Menangani apa yang terjadi SETELAH scan berhasil
                            onQrCodeScanned = { qrValue ->
                                // 1. Tampilkan hasil
                                Toast.makeText(applicationContext, "Hasil Scan: $qrValue", Toast.LENGTH_LONG).show()

                                // 2. Tutup layar scanner
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}