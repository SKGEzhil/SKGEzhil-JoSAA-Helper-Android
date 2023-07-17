package com.skgezhil.josaa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skgezhil.josaa.ui.theme.SKGEzhilJoSAAHelperTheme
import com.skgezhil.josaa.Message2

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
                                IconButton(onClick = {

                                }) {
                                    Icon(
                                        imageVector = Icons.Filled.Menu,
                                        contentDescription = "Localized description"
                                    )
                                }
                            }
                        )
                    }


                    ) { contentPadding ->
                    // Screen content
                    Surface {
                        Box(modifier = Modifier.padding(contentPadding)) {
                            ResultList(result_data = responsejson)
                        }
                    }


                }
            }
        }
    }
}

//data class Result_Data(val ResultData: String)
//
//@Composable
//fun ResultList(result_data: List<Message2>){
//    LazyColumn {
//        items(result_data) { message ->
//            ResultCard(message)
//        }
//    }
//}
//
//@Composable
//fun ResultCard(data: Message2){
//    Text(data.ResultData)
//}
//
//@Preview(showBackground = true)
//@Composable
//fun preview_result(){
//    ResultCard(data = Result_Data("Hello"))
//}

@Composable
fun ResultList(result_data: List<Message2>){
    LazyColumn{
        items(result_data){message ->
            ResultCard(message)
        }
    }
}

@Composable
fun ResultCard(card_data: Message2){
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
                    text = "Chances: 100%",
                )
            }
        }

    }
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
fun ResultCardPreview(){
    ResultCard(card_data = Message2(
        "Indian Institute of Technology Hydrabad ",
        "Material Science and Metallurgical Engineering",
        "Gender-Neutral",
        "AI",
        "OBC-NCL",
        12301,
        2923,
        100))
}