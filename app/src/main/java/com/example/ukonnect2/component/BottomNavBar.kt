package com.example.ukonnect2.ui.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ukonnect2.R

data class BottomNavItem(
    val route: String,
    val title: String,
    val icon: Int
)

@Composable
fun BottomNavBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    val items = listOf(
        BottomNavItem("beranda", "Beranda", R.drawable.ic_home),
        BottomNavItem("aktivitas", "Aktivitas", R.drawable.ic_calendar),
        BottomNavItem("pinjam", "Pinjam", R.drawable.ic_borrow),
        BottomNavItem("galeri", "Galeri", R.drawable.ic_gallery),
        BottomNavItem("profil", "Profil", R.drawable.ic_profile)
    )

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                selected = selected,
                onClick = { onNavigate(item.route) },
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.title,
                        modifier = Modifier.size(24.dp), // 🔹 ukuran ikon standar Material Design
                        tint = if (selected) Color(0xFFFF9800) else Color(0xFF868181) // 🔹 warna aktif/nonaktif
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        fontSize = 11.sp, // 🔹 ukuran teks proporsional
                        color = if (selected) Color(0xFFFF9800) else Color.Gray
                    )
                },
                alwaysShowLabel = true
            )
        }
    }
}
