package com.skgezhil.josaa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skgezhil.josaa.ui.theme.SKGEzhilJoSAAHelperTheme

class ResultActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SKGEzhilJoSAAHelperTheme {
                // A surface container using the 'background' color from the theme
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    "SKGEzhil JoSAA Helper",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            navigationIcon = {
                                IconButton(onClick = {
                                    finish()
                                }) {
                                    Icon(
                                        imageVector = Icons.Filled.ArrowBack,
                                        contentDescription = "Localized description"
                                    )
                                }
                            },
                            actions = {
                                val isExpanded by rememberUpdatedState(newValue = expanded)
                                IconButton(onClick = {
                                    expanded = true
                                }) {
                                    Icon(
                                        imageVector = Icons.Filled.Menu,
                                        contentDescription = "Localized description"
                                    )
                                }



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
                                                start_activity("instagram", this@ResultActivity)
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
                                                start_activity("github", this@ResultActivity)
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
                                            onClick = {
                                                start_activity("youtube", this@ResultActivity)
                                            },
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
                                            onClick = {
                                                      start_activity("source-code", this@ResultActivity)
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

                                    }
                                }


                            }
                        )
                    }


                    ) { contentPadding ->
                    // Screen content
                    Surface {
                        Box(modifier = Modifier.padding(contentPadding)) {
                            ResultList(result_data = received_data)
                        }
                    }


                }
            }
        }
    }
}

@Composable
fun ResultList(result_data: List<GetDataClass>){
    LazyColumn{
        items(result_data){message ->
            ResultCard(message)
        }
    }
}

@Composable
fun ResultCard(card_data: GetDataClass){
    Surface(
        shadowElevation = 2.dp,
        modifier = Modifier.padding(all = 5.dp),
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)

    ) {

        Column {

            Column(
                modifier = Modifier.padding(all = 10.dp)
            ){
                Row(
                    modifier = Modifier.padding(bottom = 5.dp)
                ){
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
            ){
                Text(
                    text = "Chances: ${card_data.chances}%",
                )
            }
        }

    }
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
fun ResultCardPreview(){
    ResultCard(card_data = GetDataClass(
        "Indian Institute of Technology Hydrabad ",
        "Material Science and Metallurgical Engineering",
        "Gender-Neutral",
        "AI",
        "OBC-NCL",
        12301,
        2923,
        100))
}