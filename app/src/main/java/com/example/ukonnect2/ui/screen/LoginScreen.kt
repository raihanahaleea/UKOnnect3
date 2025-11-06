package com.example.ukonnect2.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ukonnect2.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val orangeColor = Color(0xFFFF9800)
    val blackColor = Color(0xFF000000)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // 🟠 Logo
            Image(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = "Logo UKOnnect",
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 12.dp)
            )

            // 🟠 Judul
            Text(
                "Login UKOnnect",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = orangeColor
            )

            // 🧑 Username Field
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = orangeColor,
                    unfocusedBorderColor = blackColor,
                    focusedLabelColor = orangeColor,
                    unfocusedLabelColor = blackColor,
                    cursorColor = orangeColor
                ),
                modifier = Modifier.fillMaxWidth(0.85f)
            )

            // 🔒 Password Field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            painter = painterResource(
                                id = if (passwordVisible) R.drawable.ic_eye_open
                                else R.drawable.ic_eye_closed
                            ),
                            contentDescription = if (passwordVisible) "Sembunyikan password" else "Tampilkan password",
                            tint = if (passwordVisible) orangeColor else blackColor
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = orangeColor,
                    unfocusedBorderColor = blackColor,
                    focusedLabelColor = orangeColor,
                    unfocusedLabelColor = blackColor,
                    cursorColor = orangeColor
                ),
                modifier = Modifier.fillMaxWidth(0.85f)
            )


            Button(
                onClick = {
                    if (username == "anggota" && password == "uko123") {
                        errorMessage = ""
                        onLoginSuccess()
                    } else {
                        errorMessage = "Username atau password salah!"
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = orangeColor),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .width(200.dp)
                    .height(50.dp)
            ) {
                Text("Login", fontSize = 18.sp, color = Color.White)
            }


            if (errorMessage.isNotEmpty()) {
                Text(errorMessage, color = Color.Red, fontSize = 14.sp)
            }
        }
    }
}
