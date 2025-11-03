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
                val peminjamanVM: PeminjamanViewModel = viewModel()   // ViewModel dibagikan ke semua screen

                Scaffold(
                    bottomBar = {
                        BottomNavBar(
                            currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route,
                            onNavigate = { route ->
                                navController.navigate(route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                ) { inner ->
                    NavHost(
                        navController = navController,
                        startDestination = "beranda",
                        modifier = Modifier.padding(inner)
                    ) {

                        // 🏠 Beranda (MainScreen)
                        composable("beranda") {
                            MainScreen(
                                peminjamanVM = peminjamanVM,
                                onGoPinjam = { navController.navigate("pinjam") },
                                onGoAbsensi = { navController.navigate("absensi") } // ✅ navigasi ke absensi
                            )
                        }

                        // 📅 Aktivitas
                        composable("aktivitas") { AktivitasScreen() }

                        // 🛒 Pinjam
                        composable("pinjam") {
                            PinjamScreen(viewModel = peminjamanVM)
                        }

                        // 🖼️ Galeri
                        composable("galeri") { GaleriScreen() }

                        // 👤 Profil
                        composable("profil") { ProfilScreen() }

                        // 🧾 Absensi (baru ditambahkan)
                        composable("absensi") {
                            AbsensiScreen(onBack = { navController.popBackStack() })
                        }
                    }
                }
            }
        }
    }
}
