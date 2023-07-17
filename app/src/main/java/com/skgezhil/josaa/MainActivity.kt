package com.skgezhil.josaa

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.animateSizeAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skgezhil.josaa.ui.theme.SKGEzhilJoSAAHelperTheme
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
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


data class Message2(
    val Institute: String,
    val Program: String,
    val Gender: String,
    val Quota: String,
    val Category: String,
    val Open_rank: Int,
    val Close_rank: Int,
    val chances: Int
)

data class Message(
    var inst_type: String,
    var inst: String,
    var prog: String,
    var gender: String,
    var quota: String,
    var category: String,
    var Common_rank: Int,
    var Category_rank: Int
)

var submit_form = Message(
    "",
    "",
    "",
    "",
    "",
    "",
    1,
    1
)

var responsejson: List<Message2> = listOf()


fun SendData() = runBlocking {
    val serverUrl = "https://c35c-2405-201-e059-8037-259c-e917-de38-c9f6.ngrok.io/submit-form"
    val data = Message("IIT", "any", "any", "Gender-Neutral", "AI", "OBC-NCL", 12301, 2923)
    val mapper = jacksonObjectMapper()
    val datajson = mapper.writeValueAsString(submit_form)
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

data class DropdownSendClass(val value: String, val drop_type: String)
data class InstituteDropdownClass(val institute: String)
data class ProgramDropdownClass(val  program: String)

var institute_dropdown: List<InstituteDropdownClass> = listOf()
var program_dropdown: List<ProgramDropdownClass> = listOf()

fun SendDropdown(dropdown_type: String, dropdown_value: String) = runBlocking {
    loading = true
    val serverUrl = "https://c35c-2405-201-e059-8037-259c-e917-de38-c9f6.ngrok.io/send/dropdown"
    val data = DropdownSendClass(dropdown_value, dropdown_type)
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

fun GetDropdown(dropdown_type: String) = runBlocking {
    val serverUrl = "https://c35c-2405-201-e059-8037-259c-e917-de38-c9f6.ngrok.io/dropdown/${dropdown_type}s"
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
            loading = false
            println("Response data: ${response.toString()}")
            if(dropdown_type == "institute"){
                institute_dropdown = mapper.readValue(response.toString())
                println(institute_dropdown)
                ObjectToString()
            }
            if(dropdown_type == "program"){
                program_dropdown = mapper.readValue(response.toString())
                println(program_dropdown)
                ObjectToString()
            }

        } else {
            println("Error: Failed to receive data")
        }

        connection.disconnect()
    }
}

fun GetData() = runBlocking {
    val serverUrl = "https://c35c-2405-201-e059-8037-259c-e917-de38-c9f6.ngrok.io/result"
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

var type_selected = false
var institute_selected = false
var program_selected = false
var option = ""
var dropdown_defaults by mutableStateOf(listOf("Select", "any"))
var institute_dropdown_string by mutableStateOf(listOf("Select", "any"))
var program_dropdown_string by mutableStateOf(listOf("Select", "any"))
var loading = false
fun ObjectToString(){
    institute_dropdown_string = dropdown_defaults.union(institute_dropdown.map { institute -> institute.institute })
        .toList()
    println(institute_dropdown_string)

    program_dropdown_string = dropdown_defaults.union(program_dropdown.map { program -> program.program })
        .toList()
    println(program_dropdown_string)


}

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var institute_optn = listOf("")
        var program_optn = listOf("SKGEzhil", "SKGEzhil", "Option 3", "Option 4", "Option 5")
        var institute_type_optn = listOf("All", "IIT", "NIT", "IIIT", "Other GFTIs")
        var gender_optn = listOf("Select", "Gender-Neutral", "Female-only (including Supernumerary)")
        var quote_optn = listOf("Select","AI", "OS", "HS")
        var category_optn = listOf(
            "Select",
            "OPEN",
            "OBC-NCL",
            "SC",
            "ST",
            "EWS"
        )

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

                                item{
                                    RankInput("Common Rank")
                                }

                                item {
                                    RankInput("Category Rank")
                                }

                            }
                            var isLoading by remember { mutableStateOf(loading) }
                            if (isLoading == true){
                                LoadingScreen()
                            }
                        }
                    }


                }



            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dropdown3(label:String, options:List<String>){
    val currentOptions by rememberUpdatedState(newValue = options)
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(currentOptions[0]) }

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
            modifier = Modifier
                .padding(all = 10.dp)
                .clip(RoundedCornerShape(10.dp)),
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

                            if(label == "Institute Type"){
                                loading = true
                                submit_form.inst_type = selectionOption
                                println("Instute Type = "+ submit_form.inst_type)
                                SendDropdown("institute_type", selectionOption)
                                GetDropdown("institute")
                            }
                            if(label == "Institute"){
                                submit_form.inst = selectionOption
                                SendDropdown("institute", selectionOption)
                                GetDropdown("program")
                            }
                            if (label == "Program"){
                                submit_form.prog = selectionOption
                            }
                            if (label == "Gender"){
                                submit_form.gender = selectionOption
                            }
                            if (label == "Quota"){
                                submit_form.quota = selectionOption
                            }
                            if (label == "Category"){
                                submit_form.category = selectionOption
                            }


                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun RankInput(label: String){
    var text = remember { mutableStateOf("")}
    val softwareKeyboardController = LocalSoftwareKeyboardController.current

    val change : (String) -> Unit = { it ->
        text.value = it  // it is supposed to be this
    }

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

        TextField(
            value = text.value,
            keyboardActions = KeyboardActions(onDone = {
                // Get the value from the text field
                val enteredText = text.value
                softwareKeyboardController?.hide()
                if(label == "Common Rank"){
                    submit_form.Common_rank = enteredText.toInt()
                }
                if (label == "Category Rank"){
                    submit_form.Category_rank = enteredText.toInt()
                }
                // Do whatever you want with the enteredText
                // For example, you can store it in a variable or process it further
                println("Entered value: $enteredText")
            }),
            modifier = Modifier
                .padding(all = 10.dp)
                .clip(RoundedCornerShape(10.dp))
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            onValueChange = change,
            singleLine = true
        )
    }

}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}



@Preview(showBackground = true)
@Composable
fun preview2(){
    val options1 = listOf("Option 1", "Option 2", "Option 3", "Option 4", "Option 5")
    Dropdown3("Text", options1)
}

@Preview(showBackground = true)
@Composable
fun preview3(){
    RankInput("Text")
}

@Preview(showBackground = true)
@Composable
fun preview4(){
    LoadingScreen()
}