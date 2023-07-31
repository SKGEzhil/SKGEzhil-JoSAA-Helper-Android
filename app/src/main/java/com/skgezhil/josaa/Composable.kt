package com.skgezhil.josaa

import android.content.res.Configuration
import android.view.MotionEvent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import kotlinx.coroutines.DelicateCoroutinesApi

// --------------------------- Global Declaration -------------------------------
var rating_state by mutableStateOf(3)

// ----------------------------- Composables ------------------------------------
@Composable
fun MainScren() {

    val isDarkTheme = isSystemInDarkTheme()

    LazyColumn(
        modifier = Modifier.padding(top = 10.dp)
    ) {
        item {
            DropdownMenu(
                label = "Institute Type",
                options = institute_type_optn,
                isDarkTheme = isDarkTheme
            )
        }
        item {
            DropdownMenu(
                label = "Institute",
                options = institute_dropdown_string,
                isDarkTheme = isDarkTheme
            )
        }

        item {
            DropdownMenu(
                label = "Program",
                options = program_dropdown_string,
                isDarkTheme = isDarkTheme
            )
        }

        item {
            DropdownMenu(label = "Gender", options = gender_optn, isDarkTheme = isDarkTheme)
        }

        item {
            DropdownMenu(label = "Quota", options = quote_optn, isDarkTheme = isDarkTheme)
        }

        item {
            DropdownMenu(label = "Category", options = category_optn, isDarkTheme = isDarkTheme)
        }

        item {
            RankInput(label = "Common Rank", isDarkTheme = isDarkTheme)
        }

        item {
            RankInput(label = "Category Rank", isDarkTheme = isDarkTheme)
        }

    }

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun DropdownMenu(label: String, options: List<String>, isDarkTheme: Boolean) {
    val currentOptions by rememberUpdatedState(newValue = options)
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by rememberSaveable { mutableStateOf(currentOptions[0]) }

    Surface(
        color = MaterialTheme.colorScheme.tertiary,
        modifier = Modifier
            .padding(bottom = 10.dp)
            .padding(start = 5.dp)
            .padding(end = 5.dp),
        shadowElevation = 0.5.dp,
        shape = RoundedCornerShape(10.dp),
//        shadowElevation = 2.dp
    ) {
        Surface(
//            color = MaterialTheme.colorScheme.surfaceColorAtElevation(5.dp),
            color = if (isDarkTheme) MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp) else Color.White,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.padding(start = 3.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 10.dp)
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
                        colors = ExposedDropdownMenuDefaults.textFieldColors(

                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,

                            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(10.dp)
                        ),
                    )

                    MaterialTheme(
                        shapes = MaterialTheme.shapes.copy(
                            extraSmall = RoundedCornerShape(16.dp)
                        ),
                    ) {
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
                                        DropdownManipulation(label, selectionOption, isConnected)
                                        isOpen = selectionOption == "OPEN"
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,

                                    )
                            }
                        }
                    }


                }

            }
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun RankInput(label: String, isDarkTheme: Boolean) {
    val text = remember { mutableStateOf("") }
    val enteredText = text.value
    if (label == "Common Rank") {
        submit_form.common_rank = enteredText
    }
    if (label == "Category Rank") {
        submit_form.category_rank = enteredText
    }
    val softwareKeyboardController = LocalSoftwareKeyboardController.current

    val change: (String) -> Unit = { it ->
        text.value = it
    }

    Surface(
        color = MaterialTheme.colorScheme.tertiary,
        modifier = Modifier
            .padding(start = 5.dp)
            .padding(bottom = 10.dp)
            .padding(end = 5.dp),
        shape = RoundedCornerShape(10.dp),
        shadowElevation = 0.5.dp

    ) {
        Surface(
            color = if (isDarkTheme) MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp) else Color.White,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.padding(start = 3.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 10.dp)
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
                    colors = ExposedDropdownMenuDefaults.textFieldColors(
                        containerColor = if (label == "Category Rank") {
                            if (isOpen) {
                                Color(94, 54, 54, 55)
                            } else {
                                MaterialTheme.colorScheme.surfaceColorAtElevation(10.dp)
                            }
                        } else {
                            MaterialTheme.colorScheme.surfaceColorAtElevation(10.dp)
                        }

                    ),
                    placeholder = {
                        if (label == "Category Rank") {
                            if (isOpen) {
                                Text(text = "Nil")
                            } else {
                                Text(text = "Your Rank")
                            }
                        } else {
                            Text(text = "Your Rank")
                        }
                    },
                    readOnly = if (label == "Category Rank") {
                        isOpen
                    } else {
                        false
                    },
                    keyboardActions = KeyboardActions(onDone = {
                        val enteredText = text.value
                        softwareKeyboardController?.hide()
                        if (label == "Common Rank") {
                            submit_form.common_rank = enteredText
                        }
                        if (label == "Category Rank") {
                            submit_form.category_rank = enteredText
                        }

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

@ExperimentalComposeUiApi
@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Int
) {
    var ratingState by remember {
        mutableStateOf(rating)
    }

    var selected by remember {
        mutableStateOf(false)
    }
    val size by animateDpAsState(
        targetValue = if (selected) 56.dp else 55.dp,
        spring(Spring.DampingRatioMediumBouncy)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp)
            .padding(end = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 1..5) {
            Icon(
                painter = painterResource(id = R.drawable.ic_round_star_24),
                contentDescription = "star",
                modifier = modifier
                    .width(size)
                    .height(size)
                    .pointerInteropFilter {
                        when (it.action) {
                            MotionEvent.ACTION_DOWN -> {
                                selected = true
                                ratingState = i
                                rating_state = i
                            }

                            MotionEvent.ACTION_UP -> {
                                selected = false
                            }
                        }
                        true
                    },
                tint = if (i <= ratingState) MaterialTheme.colorScheme.primary else Color(
                    76,
                    65,
                    87,
                    104
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackgroundOverlay(
    color: Color = Color(0x99000000),
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .clickable { onClick() }
    ) {

    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RateDialog() {

    BackgroundOverlay(color = Color(0x99000000)) {}
    Dialog(
        onDismissRequest = { showRatingDialog = false },
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(),
            color = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),
            shadowElevation = 1.dp,
            shape = RoundedCornerShape(25.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Rate",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(start = 17.dp)
                        .padding(top = 10.dp)
                        .padding(bottom = 7.dp)
                        .fillMaxWidth()
                )

                RatingBar(rating = 3)

                Text(
                    text = "Feedback",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(start = 17.dp)
                        .padding(top = 10.dp)
                        .padding(bottom = 7.dp)
                        .fillMaxWidth()
                )

                var text by rememberSaveable(stateSaver = TextFieldValue.Saver) {
                    mutableStateOf(TextFieldValue("", TextRange(0, 7)))
                }

                TextField(
                    value = text,
                    shape = RoundedCornerShape(25.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    onValueChange = { text = it },
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .padding(end = 10.dp)
                        .padding(bottom = 12.dp)
                        .height(130.dp)
                        .fillMaxWidth(),

                    )

                FeedbackSubmitButton(rating_state, text.text)

            }
        }
    }

}

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun FeedbackSubmitButton(rating_state: Int, feedback: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(1.dp),
        color = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("")

            Surface(
                modifier = Modifier
                    .padding(all = 10.dp),
                shadowElevation = 2.dp,
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(50.dp)

            ) {
                Button(
                    onClick = {
                        FeedbackSubmit(rating_state, feedback)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Text("Submit")
                }
            }
        }
    }
}

@Composable
fun ResultList(result_data: List<GetDataClass>) {
    LazyColumn {
        items(result_data) { message ->
            ResultCard(message)
        }
    }
}

@Composable
fun ResultCard(card_data: GetDataClass) {
    Surface(
        shadowElevation = 2.dp,
        modifier = Modifier.padding(all = 5.dp),
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)

    ) {

        Column {

            Column(
                modifier = Modifier.padding(all = 10.dp)
            ) {
                Row(
                    modifier = Modifier.padding(bottom = 5.dp)
                ) {
                    Text(
                        text = "Institute:  ",
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = card_data.Institute)
                }

                Row {
                    Text(
                        text = "Program:  ",
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = card_data.Program)
                }

            }

            Box(
                modifier = Modifier
                    .background(color = colorResource(id = R.color.green_100))
                    .fillMaxWidth()
                    .padding(start = 10.dp)
            ) {
                Text(
                    text = "Chances: ${card_data.chances}%",
                )
            }
        }

    }
}

@Composable
fun Availability() {

    Box(modifier = Modifier.padding(5.dp)) {
        Surface(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .fillMaxWidth(),
            color = Color(255, 95, 95, 146)
        ) {
            Box(
                modifier = Modifier.padding(0.dp),
                contentAlignment = Center
            ) {
                Text(
                    text = "No viable options found based on your rank",
                    fontSize = 17.sp,
                    color = Color(255, 255, 255, 255),
                    modifier = Modifier
                        .padding(10.dp)
                )
            }
        }
    }

    if (!isAvailable) {
        isAvailable = true
    }

}


// ---------------------------------- Previews ----------------------------------

@Preview(
    showBackground = true, showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun AvailabilityPreview() {
    Availability()
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    MainScren()
}

@Preview(showBackground = true)
@Composable
fun DropdownMenuPreview() {
    val options1 = listOf("Option 1", "Option 2", "Option 3", "Option 4", "Option 5")
    DropdownMenu("Text", options1, false)
}

@Preview(showBackground = true)
@Composable
fun InputPreview() {
    RankInput("Text", false)
}

@Preview(showBackground = true)
@Composable
fun LoadingScreenPreview() {
    LoadingScreen(loading)
}

@Preview
@Composable
fun DialogPreview() {
    RateDialog()
}


@Preview(showBackground = true, showSystemUi = false)
@Composable
fun ResultCardPreview() {
    ResultCard(
        card_data = GetDataClass(
            "Indian Institute of Technology Hydrabad ",
            "Material Science and Metallurgical Engineering",
            "Gender-Neutral",
            "AI",
            "OBC-NCL",
            12301,
            2923,
            100
        )
    )
}
