package com.skgezhil.josaa

import SampleData
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.skgezhil.josaa.ui.theme.SKGEzhilJoSAAHelperTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.security.cert.X509Certificate
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.BufferedReader
import java.io.InputStreamReader


data class Message(val author: String, val body: String)
data class Message2(val key1:String, val key2: String)
var responsejson: List<Message2> = listOf()

fun SendData() = runBlocking {
    val serverUrl = "https://8ee3-2405-201-e059-8037-1100-3048-ca9e-7e19.ngrok.io/data"
    val data = Message("Hello", "World")
    val mapper = jacksonObjectMapper()
    val datajson = mapper.writeValueAsString(data)
    println(datajson)
    println(data)
    // Create a TrustManager that trusts all certificates
    val trustAllCertificates = arrayOf<TrustManager>(object : X509TrustManager {
        override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            // Do nothing, trusting all certificates
        }

        override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            // Do nothing, trusting all certificates
        }

        override fun getAcceptedIssuers(): Array<X509Certificate> {
            return emptyArray()
        }
    })

    // Install the TrustManager
    try {
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, trustAllCertificates, java.security.SecureRandom())
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.socketFactory)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    withContext(Dispatchers.IO) {
        val url = URL(serverUrl)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.doOutput = true
        connection.setRequestProperty("Content-Type", "application/json")

        val outputStreamWriter = OutputStreamWriter(connection.outputStream)
        outputStreamWriter.write(datajson.toString())
        outputStreamWriter.flush()

        val responseCode = connection.responseCode
        println("Response code: $responseCode")

        outputStreamWriter.close()
        connection.disconnect()
    }
}

fun GetData() = runBlocking {
    val serverUrl = "https://1a2b-2405-201-e059-8037-1100-3048-ca9e-7e19.ngrok.io/recieve"
    val mapper = jacksonObjectMapper()


    withContext(Dispatchers.IO) {
        val url = URL(serverUrl)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        val responseCode = connection.responseCode
        println("Response code: $responseCode")

        if (responseCode == HttpURLConnection.HTTP_OK) {
            val reader = BufferedReader(InputStreamReader(connection.inputStream))
            val response = StringBuilder()
            var line: String?

            while (reader.readLine().also { line = it } != null) {
                response.append(line)
            }

            reader.close()
            println("Response data: ${response.toString()}")
            responsejson = mapper.readValue(response.toString())
            println(responsejson)
        } else {
            println("Error: Failed to receive data")
        }

        connection.disconnect()
    }
}


class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val options1 = listOf("Option 1", "SKGEzhil", "Option 3", "Option 4", "Option 5")
        val options2 = listOf("SKGEzhil", "SKGEzhil", "Option 3", "Option 4", "Option 5")

        setContent {
            SKGEzhilJoSAAHelperTheme {

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
                    },

                    floatingActionButton = {
                        ExtendedFloatingActionButton(
                            onClick = {
                                SendData()
                                GetData()
                                val intent = Intent(this, ResultActivity::class.java)
                                startActivity(intent)
                            },
                            text = { Text(text = "Submit  ") },
                            icon = {}
                        )

                    },
                    floatingActionButtonPosition = FabPosition.End,


                ) { contentPadding ->
                    // Screen content
                    Surface {
                        Box(modifier = Modifier.padding(contentPadding)) {
                            LazyColumn {
                                item {
                                    Dropdown3(label = "Institute Type", options = options1)
                                }
                                item {
                                    Dropdown3(label = "Institute", options = options2)
                                }

                                item {
                                    Dropdown3(label = "Program", options = options2)
                                }

                                item {
                                    Dropdown3(label = "Gender", options = options2)
                                }

                                item {
                                    Dropdown3(label = "Quota", options = options2)
                                }

                                item {
                                    Dropdown3(label = "Category", options = options2)
                                }

                            }
                        }
                    }


                }



            }
        }
    }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dropdown2() {

    var isExpanded by remember {
        mutableStateOf(false)
    }

    var gender by remember {
        mutableStateOf("")
    }

    Box(modifier = Modifier.padding(2.dp),
        contentAlignment = Alignment.TopStart
    ){
        ExposedDropdownMenuBox(expanded = isExpanded, onExpandedChange = { isExpanded = it }) {
            TextField(value = gender, onValueChange = {}, readOnly = true, trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            }, colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier.menuAnchor())

            ExposedDropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
                DropdownMenuItem(text = { Text(text = "Male")}, onClick = { gender = "Male"
                    isExpanded = false})
                DropdownMenuItem(text = { Text(text = "Female")}, onClick = { gender = "Female"
                    isExpanded = false})
            }
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dropdown3(label:String, options:List<String>){
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0]) }

    Column(
        modifier = Modifier
            .padding(top = 20.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = label,
            fontWeight = Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(start = 10.dp),
            fontSize = 22.sp
        )

        // We want to react on tap/press on TextField to show menu
        ExposedDropdownMenuBox(
            modifier = Modifier.padding(all = 10.dp),
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
        ) {
            TextField(
                // The `menuAnchor` modifier must be passed to the text field for correctness.
                modifier = Modifier.menuAnchor(),
                readOnly = true,
                value = selectedOptionText,
                onValueChange = {},
//            label = { Text("Label") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                options.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption) },
                        onClick = {
                            selectedOptionText = selectionOption
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }
    }

}

@Preview(showBackground = true, device = "id:pixel_6")
@Composable
fun preview(){
    Dropdown()
}

@Preview(showBackground = true)
@Composable
fun preview2(){
    val options1 = listOf("Option 1", "Option 2", "Option 3", "Option 4", "Option 5")
    Dropdown3("Hello", options1)
}