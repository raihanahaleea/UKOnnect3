package com.example.ukonnect2.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ukonnect2.R
import java.text.SimpleDateFormat
import java.util.*

data class Equipment(
    val id: String,
    val nama: String,
    val stokTersedia: Int,
    val stokTotal: Int,
    val icon: Int
)

data class Loan(
    val id: String,
    val alatId: String,
    val namaEquipment: String,
    val waktu: Long,
    val jumlah: Int,
    val status: String // Dipinjam | Dikembalikan
)

class PeminjamanViewModel : androidx.lifecycle.ViewModel() {
    var alatList = mutableStateListOf(
        Equipment("1", "Bola Futsal Specs", 10, 10, R.drawable.ic_ball_pinjam),
        Equipment("2", "Raket Badminton Yonex", 6, 6, R.drawable.ic_racket),
        Equipment("3", "Bola Basket Molten GG7X", 3, 3, R.drawable.ic_basket)
    )
        private set

    var riwayat = mutableStateListOf<Loan>()
        private set

    fun pinjam(alat: Equipment, qty: Int) {
        val idx = alatList.indexOfFirst { it.id == alat.id }
        if (idx == -1) return
        val stok = alatList[idx].stokTersedia
        val realQty = qty.coerceIn(1, stok)
        if (realQty <= 0) return

        alatList[idx] = alatList[idx].copy(stokTersedia = stok - realQty)

        val existIdx = riwayat.indexOfFirst { it.alatId == alat.id && it.status == "Dipinjam" }
        val now = System.currentTimeMillis()
        if (existIdx != -1) {
            val ex = riwayat[existIdx]
            riwayat[existIdx] = ex.copy(jumlah = ex.jumlah + realQty, waktu = now)
        } else {
            riwayat.add(
                Loan(
                    id = UUID.randomUUID().toString(),
                    alatId = alat.id,
                    namaEquipment = alat.nama,
                    waktu = now,
                    jumlah = realQty,
                    status = "Dipinjam"
                )
            )
        }
    }

    fun kembalikan(loan: Loan, qty: Int) {
        if (loan.status != "Dipinjam") return
        val alatIdx = alatList.indexOfFirst { it.id == loan.alatId }
        if (alatIdx == -1) return

        val alat = alatList[alatIdx]
        val kembali = qty.coerceIn(1, loan.jumlah)

        alatList[alatIdx] = alat.copy(
            stokTersedia = (alat.stokTersedia + kembali).coerceAtMost(alat.stokTotal)
        )

        val loanIdx = riwayat.indexOfFirst { it.id == loan.id }
        if (loanIdx == -1) return
        val cur = riwayat[loanIdx]
        val sisa = cur.jumlah - kembali
        if (sisa > 0) {
            riwayat[loanIdx] = cur.copy(jumlah = sisa)
        } else {
            riwayat[loanIdx] = cur.copy(jumlah = 0, status = "Dikembalikan", waktu = System.currentTimeMillis())
        }
    }

    fun loansAktif(): List<Loan> = riwayat.filter { it.status == "Dipinjam" && it.jumlah > 0 }
    fun loansSelesai(): List<Loan> = riwayat.filter { it.status == "Dikembalikan" || it.jumlah == 0 }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PinjamScreen(viewModel: PeminjamanViewModel) { // ← pakai VM dari atas
    val tabTitles = listOf("Daftar Alat", "Sedang Dipinjam", "Riwayat Peminjaman")
    var selectedTabIndex by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text("Peminjaman Alat", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            })
        },
        containerColor = Color(0xFFF8FAFB)
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color.White,
                edgePadding = 8.dp,
                indicator = {},
                divider = {}
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                title,
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTabIndex == index) Color(0xFFFF6B00) else Color(0xFF5B5B5B),
                                maxLines = 1
                            )
                        }
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            when (selectedTabIndex) {
                0 -> DaftarAlat(viewModel)
                1 -> SedangDipinjamList(viewModel)
                2 -> RiwayatList(viewModel)
            }
        }
    }
}

@Composable
fun DaftarAlat(viewModel: PeminjamanViewModel) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items(viewModel.alatList, key = { it.id }) { alat ->
            EquipmentItem(alat = alat, onConfirm = { qty -> viewModel.pinjam(alat, qty) })
        }
    }
}

@Composable
fun SedangDipinjamList(viewModel: PeminjamanViewModel) {
    val fmt = remember { SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()) }
    val data = viewModel.loansAktif()

    if (data.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Belum ada alat yang sedang dipinjam.", color = Color.Gray)
        }
    } else {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(data, key = { it.id }) { loan ->
                var showDialog by remember { mutableStateOf(false) }

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(loan.namaEquipment, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(fmt.format(Date(loan.waktu)), color = Color(0xFF7C8795), fontSize = 13.sp)
                        Text("Jumlah dipinjam: ${loan.jumlah}", fontSize = 13.sp)
                        Spacer(Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = { showDialog = true },
                            border = BorderStroke(1.dp, Color(0xFF00A85A)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF00A85A))
                        ) { Text("Kembalikan", fontWeight = FontWeight.SemiBold) }
                    }
                }

                if (showDialog) {
                    var qty by remember { mutableStateOf(1) }
                    val maxQty = loan.jumlah

                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_ball_pinjam),
                                contentDescription = null,
                                tint = Color(0xFFFF7A00),
                                modifier = Modifier.size(48.dp)
                            )
                        },
                        title = { Text("Kembalikan Alat", fontWeight = FontWeight.Bold) },
                        text = {
                            Column {
                                Text("Pilih jumlah yang ingin dikembalikan.", color = Color(0xFF4E4E4E))
                                Spacer(Modifier.height(12.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    OutlinedButton(onClick = { if (qty > 1) qty-- }) { Text("-") }
                                    Text("$qty", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                    OutlinedButton(onClick = { if (qty < maxQty) qty++ }) { Text("+") }
                                    Spacer(Modifier.weight(1f))
                                    Text("Maks: $maxQty", fontSize = 12.sp, color = Color.Gray)
                                }
                            }
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    showDialog = false
                                    viewModel.kembalikan(loan, qty)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00A85A))
                            ) { Text("Kembalikan $qty", color = Color.White, fontWeight = FontWeight.Bold) }
                        },
                        dismissButton = { TextButton(onClick = { showDialog = false }) { Text("Batal") } },
                        containerColor = Color.White,
                        shape = RoundedCornerShape(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun RiwayatList(viewModel: PeminjamanViewModel) {
    val fmt = remember { SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()) }
    val data = viewModel.loansSelesai()

    if (data.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Belum ada riwayat peminjaman.", color = Color.Gray)
        }
    } else {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(data, key = { it.id }) { loan ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(loan.namaEquipment, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(fmt.format(Date(loan.waktu)), color = Color(0xFF7C8795), fontSize = 13.sp)
                        Text("Status: ${loan.status}", color = Color(0xFF00A85A), fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun EquipmentItem(
    alat: Equipment,
    onConfirm: (Int) -> Unit,
    minRowHeight: Dp = 72.dp,
    buttonWidth: Dp = 110.dp
) {
    var showConfirm by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .heightIn(min = minRowHeight),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = alat.icon),
                contentDescription = alat.nama,
                tint = Color(0xFFFF7A00),
                modifier = Modifier.size(36.dp)
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    alat.nama,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    "Stok tersedia: ${alat.stokTersedia} / ${alat.stokTotal}",
                    color = Color(0xFF00A85A),
                    fontSize = 13.sp
                )
            }
            Button(
                onClick = { showConfirm = true },
                enabled = alat.stokTersedia > 0,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (alat.stokTersedia > 0) Color(0xFFFF7A00) else Color(0xFFE0E6ED),
                    contentColor = if (alat.stokTersedia > 0) Color.White else Color.Gray
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .width(buttonWidth)
                    .height(44.dp)
            ) { Text("Pinjam", fontWeight = FontWeight.SemiBold) }
        }
    }

    if (showConfirm) {
        var qty by remember { mutableStateOf(1) }
        val maxQty = alat.stokTersedia

        AlertDialog(
            onDismissRequest = { showConfirm = false },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_ball_pinjam),
                    contentDescription = null,
                    tint = Color(0xFFFF7A00),
                    modifier = Modifier.size(48.dp)
                )
            },
            title = { Text("Konfirmasi Peminjaman", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Pilih jumlah untuk ${alat.nama}", color = Color(0xFF4E4E4E))
                    Spacer(Modifier.height(12.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(onClick = { if (qty > 1) qty-- }) { Text("-") }
                        Text("$qty", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        OutlinedButton(onClick = { if (qty < maxQty) qty++ }) { Text("+") }
                        Spacer(Modifier.weight(1f))
                        Text("Maks: $maxQty", fontSize = 12.sp, color = Color.Gray)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirm = false
                        onConfirm(qty)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF7A00))
                ) { Text("Ya, Pinjam $qty", color = Color.White, fontWeight = FontWeight.Bold) }
            },
            dismissButton = { TextButton(onClick = { showConfirm = false }) { Text("Batal") } },
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp)
        )
    }
}
