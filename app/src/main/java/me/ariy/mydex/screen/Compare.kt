package me.ariy.mydex.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.kanyidev.searchable_dropdown.SearchableExpandedDropDownMenu
import me.ariy.mydex.data.AppDatabase
import me.ariy.mydex.data.MapTypeConverter
import me.ariy.mydex.data.pokemon.PokemonEntity

@Composable
fun CompareScreen(navController: NavHostController) {
    val context = LocalContext.current
    val db = AppDatabase.getInstance(context).pokemonDao()
    val pokemons = mutableListOf<String>()
    for(i in db.getAll()){
        pokemons.add(i.uuid)
    }

    var pokemon1 = remember { mutableStateOf( PokemonEntity("","","","", "", "","",0,0)) }
    var pokemon2 = remember { mutableStateOf( PokemonEntity("","","","", "", "","",0,0)) }

    Column() {

        SearchableExpandedDropDownMenu(
            listOfItems = pokemons, // provide the list of items of any type you want to populated in the dropdown,
            modifier = Modifier.fillMaxWidth(),
            onDropDownItemSelected = { item -> // Returns the item selected in the dropdown
                Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                pokemon1.value = db.findByName(item)
            },
            enable = false,
            placeholder = "Select a pokemon",
            openedIcon = Icons.Default.Close,
            closedIcon = Icons.Default.ArrowDropDown,
            parentTextFieldCornerRadius = 12.dp,
            dropdownItem = { name -> // Provide a Composable that will be used to populate the dropdown and that takes a type i.e String,Int or even a custom type
                Text(name)
            },
        )

        SearchableExpandedDropDownMenu(
            listOfItems = pokemons, // provide the list of items of any type you want to populated in the dropdown,
            modifier = Modifier.fillMaxWidth(),
            onDropDownItemSelected = { item -> // Returns the item selected in the dropdown
                Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                pokemon2.value = db.findByName(item)
            },
            enable = false,
            placeholder = "Select a pokemon",
            openedIcon = Icons.Default.Close,
            closedIcon = Icons.Default.ArrowDropDown,
            parentTextFieldCornerRadius = 12.dp,
            dropdownItem = { name -> // Provide a Composable that will be used to populate the dropdown and that takes a type i.e String,Int or even a custom type
                Text(name)
            },
        )

        Button(onClick = {
            var stats1 = MapTypeConverter.stringToMap(pokemon1.value.baseStats)
            var stats2 = MapTypeConverter.stringToMap(pokemon2.value.baseStats)
            if((stats1["hp"]?.toInt()!! > stats2["hp"]?.toInt()!!)){
                Toast.makeText(context, "1 is better than 2 for HP", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "2 is better than 1 for HP", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text(text = "Compare Pokemons")
        }
        if(pokemon1.value.uuid.isNotEmpty()) {
            Text(text = pokemon1.value.uuid)
        }
        if(pokemon2.value.uuid.isNotEmpty()) {
            Text(text = pokemon2.value.uuid)
        }
    }
}