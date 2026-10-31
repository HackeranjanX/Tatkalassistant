package com.example.tatkalassistant

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Navy = Color(0xFF081426)
private val Panel = Color(0xFF12243B)
private val Blue = Color(0xFF3B82F6)
private val Muted = Color(0xFF9FB1C8)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { TatkalApp() }
    }
}

@Composable
fun TatkalApp() {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("tatkal_local", Context.MODE_PRIVATE) }
    var train by remember { mutableStateOf(prefs.getString("train", "") ?: "") }
    var from by remember { mutableStateOf(prefs.getString("from", "") ?: "") }
    var to by remember { mutableStateOf(prefs.getString("to", "") ?: "") }
    var date by remember { mutableStateOf(prefs.getString("date", "") ?: "") }
    var travelClass by remember { mutableStateOf(prefs.getString("class", "SL") ?: "SL") }
    var quota by remember { mutableStateOf("Tatkal") }
    var passengerName by remember { mutableStateOf(prefs.getString("pname", "") ?: "") }
    var age by remember { mutableStateOf(prefs.getString("age", "") ?: "") }
    var gender by remember { mutableStateOf(prefs.getString("gender", "Male") ?: "Male") }
    var berth by remember { mutableStateOf("No preference") }

    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Blue, background = Navy, surface = Panel,
            onBackground = Color.White, onSurface = Color.White
        )
    ) {
        Column(
            Modifier.fillMaxSize().background(Navy).verticalScroll(rememberScrollState()).padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text("TATKAL ASSISTANT", color = Blue, fontSize = 13.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
            Text("Your booking companion", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
            Text("Prepare details before opening IRCTC Rail Connect.", color = Muted, fontSize = 14.sp)

            SectionCard("JOURNEY DETAILS") {
                Field("Train number", train) { train = it }
                Field("From station (code)", from) { from = it }
                Field("To station (code)", to) { to = it }
                Field("Journey date (DD-MM-YYYY)", date) { date = it }
                Text("Class", color = Muted, fontSize = 12.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("SL", "3A", "2A", "CC", "2S").forEach { item ->
                        FilterChip(selected = travelClass == item, onClick = { travelClass = item }, label = { Text(item) })
                    }
                }
                Text("Quota: Tatkal", color = Color.White, fontWeight = FontWeight.Medium)
            }

            SectionCard("PASSENGER DETAILS") {
                Field("Passenger name", passengerName) { passengerName = it }
                Field("Age", age, KeyboardType.Number) { age = it }
                Text("Gender", color = Muted, fontSize = 12.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Male", "Female", "Transgender").forEach {
                        FilterChip(selected = gender == it, onClick = { gender = it }, label = { Text(it) })
                    }
                }
                Text("Berth preference", color = Muted, fontSize = 12.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("No preference", "Lower", "Upper", "Side Lower").forEach {
                        FilterChip(selected = berth == it, onClick = { berth = it }, label = { Text(it) })
                    }
                }
            }

            Button(
                onClick = {
                    prefs.edit().putString("train", train).putString("from", from).putString("to", to)
                        .putString("date", date).putString("class", travelClass)
                        .putString("pname", passengerName).putString("age", age)
                        .putString("gender", gender).apply()
                    Toast.makeText(context, "Journey and passenger details saved on this phone", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp)
            ) { Text("SAVE DETAILS") }

            OutlinedButton(
                onClick = {
                    val summary = "Train: $train\nFrom: $from\nTo: $to\nDate: $date\nClass: $travelClass\nQuota: $quota\nPassenger: $passengerName\nAge: $age\nGender: $gender\nBerth: $berth"
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(ClipData.newPlainText("Tatkal booking details", summary))
                    Toast.makeText(context, "Details copied. Paste only where appropriate.", Toast.LENGTH_LONG).show()
                },
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp)
            ) { Text("COPY BOOKING SUMMARY") }

            Card(colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(16.dp)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("BOOKING CHECKLIST", color = Blue, fontWeight = FontWeight.Bold)
                    Text("1. Open the official IRCTC Rail Connect app.")
                    Text("2. Search and select your train, date and class.")
                    Text("3. Select Tatkal quota and enter passenger details manually.")
                    Text("4. Check all details on the final review screen.")
                    Text("5. Complete CAPTCHA, payment and confirmation yourself.")
                    Text("This companion does not control IRCTC or automate booking.")
                }
            }
            Text("Privacy: details are stored locally in this app's private preferences. Do not store passwords, OTPs, card details or UPI PINs here.", color = Muted, fontSize = 12.sp)
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
private fun SectionCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(18.dp)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(title, color = Blue, fontWeight = FontWeight.Bold, fontSize = 12.sp, letterSpacing = 1.sp)
            content()
        }
    }
}

@Composable
private fun Field(label: String, value: String, keyboard: KeyboardType = KeyboardType.Text, onChange: (String) -> Unit) {
    OutlinedTextField(
        value = value, onValueChange = onChange, label = { Text(label) },
        modifier = Modifier.fillMaxWidth(), singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboard),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Blue, focusedLabelColor = Blue,
            unfocusedBorderColor = Muted, focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        )
    )
}
