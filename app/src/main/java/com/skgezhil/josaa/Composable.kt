package com.skgezhil.josaa

import android.view.MotionEvent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.surfaceColorAtElevation
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
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex


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

var Expanded by mutableStateOf(false)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun Dropdown3(label: String, options: List<String>) {
    val currentOptions by rememberUpdatedState(newValue = options)
    var expanded by remember { mutableStateOf(Expanded) }
    var selectedOptionText by rememberSaveable { mutableStateOf(currentOptions[0]) }

    Surface(
        color = MaterialTheme.colorScheme.outline,
        modifier = Modifier
            .padding(top = 10.dp)
            .padding(start = 5.dp)
            .padding(end = 5.dp),
        shape = RoundedCornerShape(10.dp),
        shadowElevation = 2.dp
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surfaceColorAtElevation(5.dp),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.padding(start = 5.dp)
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
                            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(50.dp)
                        ),
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
                                    DropdownManipulation(label, selectionOption, isConnected)
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

var loadingdialog by mutableStateOf(false)
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

Surface(
    color = MaterialTheme.colorScheme.outline,
    modifier = Modifier
        .padding(top = 10.dp)
        .padding(start = 5.dp)
        .padding(end = 5.dp),
    shape = RoundedCornerShape(10.dp),
    shadowElevation = 2.dp

) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceColorAtElevation(5.dp),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.padding(start = 5.dp)
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
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(50.dp)
                ),
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
                .zIndex(1f)
                .background(Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

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

var rating_state by mutableStateOf(0)

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
                tint = if (i <= ratingState) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
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

        BackgroundOverlay(color = Color(0x99000000)) {

        }
        Dialog(
            onDismissRequest = { showRatingDialog = false },

            ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
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

                    RatingBar(rating = 0)

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

                    OutlinedTextField(
                        value = text,
                        shape = RoundedCornerShape(25.dp),
                        onValueChange = { text = it },
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .padding(end = 10.dp)
                            .padding(bottom = 12.dp)
                            .heightIn(120.dp, Dp.Infinity)
                            .fillMaxWidth()
                    )

                    ComposableWithFillMaxWidthAndButton(rating_state, text.text)

                }


            }
        }


    }

@Composable
fun ComposableWithFillMaxWidthAndButton(rating_state: Int, feedback: String) {
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
                        snacbarMessage = "Feedback Submitted Successfully"
                        println(rating_state)
                        println(feedback)
                        feedback_send.rating = rating_state
                        feedback_send.feedback_ = feedback
                        SendFeedback()
                        showRatingDialog = false
                        showSnackbar = true},
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
fun LoadingDialog(is_loading: Boolean){

    val loading by rememberUpdatedState(newValue = is_loading)

    if (is_loading){
        Dialog(onDismissRequest = { /*TODO*/ }) {
            LoadingScreen(is_loading = true)
        }
    }

}

    @Preview
    @Composable
    fun DialogPreview() {
        RateDialog()
    }

@Preview
@Composable
fun LoadingPreview(){
    LoadingDialog(true)
}