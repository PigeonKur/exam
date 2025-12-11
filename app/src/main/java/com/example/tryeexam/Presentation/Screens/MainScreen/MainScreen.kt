package com.example.tryeexam.secondTrye.Presentation.Screens.MainScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tryeexam.Domain.model.Books
import com.example.tryeexam.Domain.model.User

@Composable
fun MainScreen(navController: NavController){
    val viewModel: MainViewModel = viewModel()

    LaunchedEffect(Unit) {
        viewModel.loadData()
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(15.dp)
    ){
        Row(
           modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text("Пользователи")
        }
    }
    Spacer(Modifier.height(8.dp))


    val books by viewModel.dispayedBooks.collectAsState()
    val errors by viewModel.error.collectAsState()


    if(errors.isNotEmpty()){
        Column(                modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center)
        {
            Text(errors, color = Color.Red)
            Spacer(Modifier.height(8.dp))
            Button(onClick = { viewModel.loadData() }) {
                Text("Повторить")
            }
        }
    }
    else if(books.isEmpty()){
        Column(                modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center)
        {
            Text("Нет пользователей")
        }
    }
    else{
        LazyColumn(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            items(books ) {
                    books -> UserCard(books = books, viewModel = viewModel)
            }
        }
    }

}


@Composable
fun UserCard(books: Books,viewModel: MainViewModel){
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = books.title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(4.dp))

            Text(
                text = "Автор: ${books.author}",
                color = Color.Gray
            )
            Spacer(Modifier.height(4.dp))

            Text(
                text = "Цена: ${books.price}",
                color = Color.Gray
            )

            Button(onClick = {viewModel.deleteUser(books.id)},
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.Red
                ),
                modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                    Icons.Default.Delete,
                    contentDescription = "Удалить",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Удалить")
            }
        }
    }
}