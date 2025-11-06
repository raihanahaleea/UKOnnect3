package com.example.ukonnect2.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ukonnect2.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AbsensiScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Absensi Kehadiran",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "Kembali",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FAFB))
                .padding(innerPadding)
                .padding(16.dp)
        ) {
                }
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Riwayat Absensi Saya",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(Modifier.height(12.dp))

            // 🧾 Daftar Riwayat Absensi
            val riwayatList = listOf(
                AbsensiItem("25 Oktober 2023", "Latihan Fisik Bersama", "Hadir"),
                AbsensiItem("24 Oktober 2023", "Latihan Rutin Futsal", "Terlambat"),
                AbsensiItem("22 Oktober 2023", "Meeting Anggota", "Hadir"),
                AbsensiItem("20 Oktober 2023", "Latihan Teknik Basket", "Hadir")
            )

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                riwayatList.forEach { item ->
                    RiwayatCard(item)
                }
            }
        }


data class AbsensiItem(
    val tanggal: String,
    val kegiatan: String,
    val status: String
)

@Composable
fun RiwayatCard(item: AbsensiItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFF4F4F4),
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_calendar),
                    contentDescription = "Tanggal",
                    tint = Color.Black,
                    modifier = Modifier.padding(10.dp)
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.tanggal,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = item.kegiatan,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            val statusColor = when (item.status) {
                "Hadir" -> Color(0xFFC8E6C9)
                "Terlambat" -> Color(0xFFFFF3E0)
                else -> Color.LightGray
            }

            val textColor = when (item.status) {
                "Hadir" -> Color(0xFF2E7D32)
                "Terlambat" -> Color(0xFFF57C00)
                else -> Color.Black
            }

            Surface(
                color = statusColor,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = item.status,
                    color = textColor,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp
                )
            }
        }
    }
}
