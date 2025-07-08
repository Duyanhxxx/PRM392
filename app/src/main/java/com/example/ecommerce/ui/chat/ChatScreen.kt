package com.example.ecommerce.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.ecommerce.viewmodel.ChatViewModel

@Composable
fun ChatScreen(chatViewModel: ChatViewModel) { // ✅ Đúng tên tham số
    val messages by chatViewModel.messages.collectAsState()
    var input by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Trò chuyện với cửa hàng", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            messages.forEach { msg ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    contentAlignment = if (msg.isFromUser) Alignment.CenterEnd else Alignment.CenterStart
                ) {
                    Text(
                        text = msg.text,
                        modifier = Modifier
                            .background(
                                if (msg.isFromUser) MaterialTheme.colorScheme.primary else Color.LightGray,
                                shape = MaterialTheme.shapes.medium
                            )
                            .padding(8.dp),
                        color = if (msg.isFromUser) Color.White else Color.Black
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Nhập tin nhắn...") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                if (input.isNotBlank()) {
                    chatViewModel.sendMessage(input)
                    input = ""
                }
            }) {
                Text("Gửi")
            }
        }
    }
}

