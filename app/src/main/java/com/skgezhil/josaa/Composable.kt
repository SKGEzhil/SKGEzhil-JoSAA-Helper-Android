package com.skgezhil.josaa

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity


@Composable
fun MainScren() {

    LazyColumn {
        item {
            Dropdown3(label = "Institute Type", options = institute_type_optn)
        }
        item {
            Dropdown3(label = "Institute", options = institute_dropdown_string)
        }

        item {
            Dropdown3(label = "Program", options = program_dropdown_string)
        }

        item {
            Dropdown3(label = "Gender", options = gender_optn)
        }

        item {
            Dropdown3(label = "Quota", options = quote_optn)
        }

        item {
            Dropdown3(label = "Category", options = category_optn)
        }

        item {
            RankInput(label = "Common Rank")
        }

        item {
            RankInput(label = "Category Rank")
        }

    }

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun Dropdown3(label: String, options: List<String>) {
    val currentOptions by rememberUpdatedState(newValue = options)
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by rememberSaveable { mutableStateOf(currentOptions[0]) }
    var ExecutionState by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .padding(top = 20.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(start = 10.dp),
            fontSize = 22.sp
        )

        ExposedDropdownMenuBox(
            modifier = Modifier
                .padding(all = 10.dp)
                .clip(RoundedCornerShape(10.dp)),
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
        ) {
            TextField(
                modifier = Modifier.menuAnchor(),
                readOnly = true,
                value = selectedOptionText,
                onValueChange = {},
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
            ) {
                currentOptions.forEach { selectionOption ->
                    DropdownMenuItem(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp)),
                        text = { Text(selectionOption) },
                        onClick = {
                            selectedOptionText = selectionOption
                            option = selectionOption
                            expanded = false
                            DropdownManipulation(label, selectionOption)
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }

            }

        }

    }
}

fun DropdownManipulation(label: String, option: String) {
    if (label == "Institute Type") {
        submit_form.inst_type = option
        SendDropdown("institute_type", option)
        GetDropdown("institute")
    }
    if (label == "Institute") {
        submit_form.inst = option
        SendDropdown("institute", option)
        GetDropdown("program")
    }
    if (label == "Program") {
        submit_form.prog = option
    }
    if (label == "Gender") {
        submit_form.gender = option
    }
    if (label == "Quota") {
        submit_form.quota = option
    }
    if (label == "Category") {
        submit_form.category = option
    }
}

var text_2 by mutableStateOf("")

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun RankInput(label: String) {
    val text = remember { mutableStateOf("") }
    val enteredText = text.value
    text_2 = text.value
    if (label == "Common Rank") {
        submit_form.common_rank = enteredText
    }
    if (label == "Category Rank") {
        submit_form.category_rank = enteredText
    }
    val softwareKeyboardController = LocalSoftwareKeyboardController.current

    val change: (String) -> Unit = { it ->
        text.value = it  // it is supposed to be this
    }

    Column(
        modifier = Modifier
            .padding(top = 20.dp)
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(start = 10.dp),
            fontSize = 22.sp

        )

        TextField(
            value = text.value,
            keyboardActions = KeyboardActions(onDone = {
                val enteredText = text.value
                softwareKeyboardController?.hide()
                if (label == "Common Rank") {
                    submit_form.common_rank = enteredText
                }
                if (label == "Category Rank") {
                    submit_form.category_rank = enteredText
                }

                println("Entered value: $enteredText")
            }),
            modifier = Modifier
                .padding(all = 10.dp)
                .fillMaxWidth(1f)
                .clip(RoundedCornerShape(10.dp)),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            onValueChange = change,
            singleLine = true
        )
    }

}

@Composable
fun LoadingScreen(is_loading: Boolean) {

    val isLoading by rememberUpdatedState(newValue = is_loading)


    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(1f)
                .background(Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

}

@Composable
fun TopBarMenu(isExpanded: Boolean){
    MaterialTheme(shapes = MaterialTheme.shapes.copy(extraSmall = RoundedCornerShape(10.dp))) {
        DropdownMenu(
            expanded = isExpanded,
            modifier = Modifier.padding(end = 10.dp),
            onDismissRequest = {
                expanded = false
            }
        ) {

            Text(
                text = "SKGEzhil",
                modifier = Modifier
                    .padding(all = 10.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )



            DropdownMenuItem(
                text = { Text(text = "Instagram")},
                onClick = {


                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.instagram),
                        modifier = Modifier
                            .width(24.dp),
                        contentDescription = "instagram"
                    )
                }
            )
            DropdownMenuItem(
                text = { Text(text = "GitHub")},
                onClick = {
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.github),
                        modifier = Modifier
                            .width(24.dp),
                        contentDescription = "instagram"
                    )
                }
            )
            DropdownMenuItem(
                text = { Text(text = "YouTube")},
                onClick = {},
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.youtube),
                        modifier = Modifier
                            .width(24.dp),
                        contentDescription = "instagram"
                    )
                }
            )
            DropdownMenuItem(
                text = { Text(text = "Source Code")},
                onClick = {},
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.github),
                        modifier = Modifier
                            .width(24.dp),
                        contentDescription = "instagram"
                    )
                }
            )

        }
    }

}

@Preview(showBackground = true)
@Composable
fun Preview_top_menu(){
    TopBarMenu(true)
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    MainScren()
}

@Preview(showBackground = true)
@Composable
fun preview2() {
    val options1 = listOf("Option 1", "Option 2", "Option 3", "Option 4", "Option 5")
    Dropdown3("Text", options1)
}

@Preview(showBackground = true)
@Composable
fun preview3() {
    RankInput("Text")
}

@Preview(showBackground = true)
@Composable
fun preview4() {
    LoadingScreen(loading)
}

@Composable
fun Dropdown(){
    var isExpanded by remember { mutableStateOf(false)}
    var SelectedItem = "Select"
    Box(modifier = Modifier
        .wrapContentSize(Alignment.TopStart)
        .padding(all = 10.dp)
    ) {
        Column {
            Text(text = "Institute Type",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(bottom = 9.dp))
            Surface(
                shadowElevation = 1.dp,
                shape = RoundedCornerShape(10.dp)
            ) {
                Button(
                    onClick = { isExpanded = !isExpanded },
                    Modifier.width(250.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = Color.Gray),
                    shape = RoundedCornerShape (10.dp),
                ) {
                    Text(text = SelectedItem)
                }
            }
            DropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
                DropdownMenuItem(text = { Text("Institute Type")},
                    onClick = {
                        SelectedItem = "Institute Type"
                        isExpanded = !isExpanded
                    })
                DropdownMenuItem(text = { Text("Institute Type")},
                    onClick = {
                        SelectedItem = "Institute"
                        isExpanded = !isExpanded
                    })
                DropdownMenuItem(text = { Text("Institute Type")},
                    onClick = {
                        SelectedItem = "Institute Type"
                        isExpanded = !isExpanded
                    })
                DropdownMenuItem(text = { Text("Institute Type")},
                    onClick = {
                        SelectedItem = "Institute"
                        isExpanded = !isExpanded
                    })
                DropdownMenuItem(text = { Text("Institute Type")},
                    onClick = {
                        SelectedItem = "Institute Type"
                        isExpanded = !isExpanded
                    })
                DropdownMenuItem(text = { Text("Institute Type")},
                    onClick = {
                        SelectedItem = "Institute Type"
                        isExpanded = !isExpanded
                    })
            }

        }
    }
}