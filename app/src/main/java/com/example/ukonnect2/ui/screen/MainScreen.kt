package com.example.ukonnect2.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ukonnect2.R

@Composable
fun MainScreen(
    peminjamanVM: PeminjamanViewModel,
    onGoPinjam: () -> Unit,
    onGoAbsensi: () -> Unit
)
 {
    val jumlahDipinjam = peminjamanVM.loansAktif().sumOf { it.jumlah }

    Scaffold(containerColor = Color(0xFFF8FAFB)) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        modifier = Modifier.size(50.dp),
                        shape = CircleShape,
                        color = Color(0xFFFFE0B2)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_profile),
                            contentDescription = "Profile",
                            tint = Color(0xFFFF9800),
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = "Selamat Datang, Saringan!",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B1B1B)
                    )
                }
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifikasi",
                        tint = Color(0xFF1B1B1B)
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            Column {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    StatCard("15", "Aktivitas Diikuti", R.drawable.ic_calendar)
                    StatCard("95%", "Total Kehadiran", R.drawable.ic_check)
                }
                Spacer(Modifier.height(16.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    StatCard(jumlahDipinjam.toString(), "Alat Dipinjam", R.drawable.ic_ball)
                    StatCard("48", "Foto di Galeri", R.drawable.ic_gallery)
                }
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Aksi Cepat",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B1B1B)
            )
            Spacer(Modifier.height(12.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                QuickActionButton(
                    label = "Scan QR Absen",
                    icon = R.drawable.ic_qr,
                    background = Color(0xFFFF9800),
                    textColor = Color.White,
                    onClick = onGoAbsensi
                )
                QuickActionButton(
                    label = "Pinjam Alat",
                    icon = R.drawable.ic_cart,
                    background = Color(0xFFFFE0B2),
                    textColor = Color(0xFFFF9800),
                    onClick = onGoPinjam
                )
            }
        }
    }
}

@Composable
fun StatCard(value: String, label: String, icon: Int) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(120.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = label,
                tint = Color(0xFFFF9800),
                modifier = Modifier.size(28.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text(text = value, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text(text = label, fontSize = 14.sp, color = Color.Gray)
        }
    }
}

@Composable
fun QuickActionButton(
    label: String,
    icon: Int,
    background: Color,
    textColor: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = background, contentColor = textColor),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .width(160.dp)
            .height(60.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = label,
                tint = textColor,
                modifier = Modifier.size(22.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(text = label, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}
