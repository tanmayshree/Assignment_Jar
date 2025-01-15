package com.myjar.jarassignment.ui.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.myjar.jarassignment.data.model.ComputerItem
import com.myjar.jarassignment.ui.vm.JarViewModel

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    viewModel: JarViewModel,
) {
    val navController = rememberNavController()
    val navigate = remember { mutableStateOf<String>("") }

    NavHost(modifier = modifier, navController = navController, startDestination = "item_list") {
        composable("item_list") {
            ItemListScreen(
                viewModel = viewModel,
                navController = navController
            )
        }
        composable("item_detail/{itemId}") { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId")
            ItemDetailScreen(itemId = itemId)
        }
    }
}

@Composable
fun ItemListScreen(
    viewModel: JarViewModel,
    navController: NavHostController
) {
    val searchText = remember { mutableStateOf("") }
    val items = viewModel.listStringData.collectAsState()

//    if (navigate.value.isNotBlank()) {
//        val currRoute = navController.currentDestination?.route.orEmpty()
//        if (!currRoute.contains("item_detail")) {
//            navController.navigate("item_detail/${navigate.value}")
//        }
//    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            BasicTextField(
                value = searchText.value,
                onValueChange = {
                    searchText.value = it
                },
                keyboardOptions = KeyboardOptions.Default,
                modifier = Modifier.border(1.dp, Color.Black).fillMaxWidth().padding(10.dp)
            )
        }
        items(items.value.filter { if (searchText.value.isBlank()) true else it.name.contains(searchText.value, ignoreCase = true) }) { item ->
            ItemCard(
                item = item,
                onClick = { navController.navigate("item_detail/${item.id}")}
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun ItemCard(item: ComputerItem, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Text(text = item.name, fontWeight = FontWeight.Bold, color = Color.Black)
        item.data?.color?.let { Text(text = it, fontWeight = FontWeight.Medium, color = Color.Black) }
        item.data?.capacity?.let { Text(text = it, fontWeight = FontWeight.Medium, color = Color.Black) }
        item.data?.price?.let { Text(text = it.toString(), fontWeight = FontWeight.Medium, color = Color.Black) }
        item.data?.description?.let { Text(text = it, fontWeight = FontWeight.Medium, color = Color.Black) }
    }
}

@Composable
fun ItemDetailScreen(itemId: String?) {
    // Fetch the item details based on the itemId
    // Here, you can fetch it from the ViewModel or repository
    Text(
        text = "Item Details for ID: $itemId",
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    )
}
