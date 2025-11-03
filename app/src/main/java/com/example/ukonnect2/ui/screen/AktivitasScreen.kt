package com.example.ukonnect2.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AktivitasScreen() {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Tambah aktivitas */ },
                containerColor = Color(0xFF0288D1)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah", tint = Color.White)
            }
        },
        containerColor = Color(0xFFF8FAFB)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // === HEADER ===
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /* menu */ }) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.Black)
                }
                Text(
                    text = "Aktivitas Olahraga",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                IconButton(onClick = { /* notif */ }) {
                    Icon(Icons.Default.Notifications, contentDescription = "Notifikasi", tint = Color.Black)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // === KALENDER PALSU (mock-up) ===
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "📅 Kalender di sini",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Kegiatan untuk: Kamis, 24 Oktober 2024",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            // === DAFTAR KEGIATAN ===
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    KegiatanCard(
                        nama = "Latihan Futsal Rutin",
                        lokasi = "Lapangan Futsal UNAND",
                        waktu = "16:00 - 18:00 WIB",
                        status = "Selesai",
                        statusColor = Color(0xFFD0F0C0)
                    )
                }
                item {
                    KegiatanCard(
                        nama = "Rapat Anggota Mingguan",
                        lokasi = "Sekretariat UKO",
                        waktu = "19:00 - 20:00 WIB",
                        status = "Belum Selesai",
                        statusColor = Color(0xFFFFF3CD)
                    )
                }
            }
        }
    }
}

@Composable
fun KegiatanCard(
    nama: String,
    lokasi: String,
    waktu: String,
    status: String,
    statusColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    modifier = Modifier.size(50.dp),
                    shape = CircleShape,
                    color = Color(0xFFE3F2FD)
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = null,
                        tint = Color(0xFF0288D1),
                        modifier = Modifier.padding(12.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = nama, fontWeight = FontWeight.Bold, color = Color.Black)
                    Text(text = lokasi, fontSize = 13.sp, color = Color.Gray)
                    Text(text = waktu, fontSize = 13.sp, color = Color.Gray)
                }
            }
            Surface(
                color = statusColor,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = status,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    fontSize = 12.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
