package com.teedee.firetv

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.compose.runtime.Composable

@Composable
fun PartyKeyScreen(
    navController: NavController,
    viewModel: PartyViewModel = viewModel()
) {
    val context = LocalContext.current
    var partyKey by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Enter or Generate Room Key", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = partyKey,
            onValueChange = { partyKey = it },
            label = { Text("Party Key") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Ascii),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = {
                partyKey = viewModel.generatePartyKey()
                Toast.makeText(context, "Created Party: $partyKey", Toast.LENGTH_SHORT).show()
            }) {
                Text("Generate")
            }

            Button(onClick = {
                if (partyKey.isNotBlank()) {
                    navController.navigate("watchparty/$partyKey")
                } else {
                    Toast.makeText(context, "Enter a valid key", Toast.LENGTH_SHORT).show()
                }
            }) {
                Text("Join Party")
            }
        }
    }
}
