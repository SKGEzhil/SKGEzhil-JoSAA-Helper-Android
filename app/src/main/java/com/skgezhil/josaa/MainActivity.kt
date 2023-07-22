package com.skgezhil.josaa

// --------------------------------- Imports ----------------------------------

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.android.gms.tasks.Task
import com.google.android.play.core.review.ReviewException
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.review.model.ReviewErrorCode
import com.skgezhil.josaa.ui.theme.SKGEzhilJoSAAHelperTheme
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.security.cert.X509Certificate
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


// ------------------------------- Data Class --------------------------------------

data class GetDataClass(
    val Institute: String,
    val Program: String,
    val Gender: String,
    val Quota: String,
    val Category: String,
    val Open_rank: Int,
    val Close_rank: Int,
    val chances: Int
)

data class SendDataClass(
    var inst_type: String,
    var inst: String,
    var prog: String,
    var gender: String,
    var quota: String,
    var category: String,
    var common_rank: String,
    var category_rank: String
)

data class DropdownSendClass(val value: String, val drop_type: String)
data class InstituteDropdownClass(val institute: String)
data class ProgramDropdownClass(val program: String)
data class FeedbackClass(var rating: Int, var feedback_ : String)

// --------------------------------------- Variables -------------------------------------------

var feedback_send by mutableStateOf(FeedbackClass(0,""))
var snacbarMessage by mutableStateOf("")
var isConnected by mutableStateOf(false)
var showRatingDialog by mutableStateOf(false)
var showSnackbar by mutableStateOf(false)
var submit_form = SendDataClass("", "", "", "", "", "", "", "")
var received_data: List<GetDataClass> = listOf()
const val BaseUrl: String = "https://skgezhil-josaa.com"
var institute_dropdown: List<InstituteDropdownClass> = listOf()
var program_dropdown: List<ProgramDropdownClass> = listOf()
var option = ""
var dropdown_defaults by mutableStateOf(listOf("Select", "any"))
var institute_dropdown_string by mutableStateOf(listOf("Select", "any"))
var program_dropdown_string by mutableStateOf(listOf("Select", "any"))
var loading by mutableStateOf(false)
var institute_type_optn = listOf("All", "IIT", "NIT", "IIIT", "Other GFTIs")
var gender_optn = listOf("Select", "Gender-Neutral", "Female-only (including Supernumerary)")
var quote_optn = listOf("Select", "AI", "OS", "HS")
var category_optn = listOf(
    "Select",
    "OPEN",
    "OBC-NCL",
    "SC",
    "ST",
    "EWS"
)


// --------------------------- API Request functions ---------------------------------

fun SendData() = runBlocking {
    val serverUrl = "${BaseUrl}/submit-form"
    val mapper = jacksonObjectMapper()
    val datajson = mapper.writeValueAsString(submit_form)
    println(datajson)
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

fun SendFeedback() = runBlocking {
    val serverUrl = "${BaseUrl}/feedback-submit"
    val mapper = jacksonObjectMapper()
    val datajson = mapper.writeValueAsString(feedback_send)
    println(datajson)
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


suspend fun SendDropdown(dropdown_type: String, dropdown_value: String) = runBlocking {
    loading = true
    val serverUrl = "${BaseUrl}/send/dropdown"
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

        try {
            val url = URL(serverUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.doOutput = true
            connection.setRequestProperty("Content-Type", "application/json")

            val outputStreamWriter = OutputStreamWriter(connection.outputStream)
            outputStreamWriter.write(datajson.toString())
            outputStreamWriter.flush()

            val responseCode = connection.responseCode
            println("SENT")
            println("Response code: $responseCode")

            outputStreamWriter.close()
            connection.disconnect()
        } catch (e: Error) {
            println("Error: ${e}")
        }

    }
}

suspend fun GetDropdown(dropdown_type: String) = runBlocking {
    val serverUrl = "${BaseUrl}/dropdown/${dropdown_type}s"
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
            println("RECEIVED")
            println("Response data: $response")
            if (dropdown_type == "institute") {
                institute_dropdown = mapper.readValue(response.toString())
                println(institute_dropdown)
                ObjectToString()
            }
            if (dropdown_type == "program") {
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
    val serverUrl = "${BaseUrl}/result"
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
            println("Response data: $response")
            received_data = mapper.readValue(response.toString())
            println(received_data)
            loading = false
        } else {
            println("Error: Failed to receive data")
        }

        connection.disconnect()
    }
}

fun ObjectToString() {
    institute_dropdown_string =
        dropdown_defaults.union(institute_dropdown.map { institute -> institute.institute })
            .toList()
    loading = false
    println(institute_dropdown_string)

    program_dropdown_string =
        dropdown_defaults.union(program_dropdown.map { program -> program.program })
            .toList()
    loading = false
    println(program_dropdown_string)


}

// ----------------------------- Main Activity ---------------------------------------
var expanded by mutableStateOf(false)

class MainActivity : ComponentActivity() {

    private var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val notConnected = intent.getBooleanExtra(
                ConnectivityManager
                    .EXTRA_NO_CONNECTIVITY, false
            )
            if (notConnected) {
                println("No Internet")

                isConnected = false
            } else {
                isConnected = true
            }
        }
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(broadcastReceiver)
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()




        setContent {

            SKGEzhilJoSAAHelperTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                val scope = rememberCoroutineScope()
                val message by rememberUpdatedState(newValue = snacbarMessage)
                val isExpanded by rememberUpdatedState(newValue = expanded)
                val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

                if (showSnackbar) {
                    // show snackbar as a suspend function
                    scope.launch {
                        val job = scope.launch {
                            snackbarHostState.showSnackbar(
                                message = message,
                                withDismissAction = true,
                            )
                        }
                        delay(2000)
                        job.cancel()
                    }
                    showSnackbar = false
                }

                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = {
                        TopAppBar(
                            scrollBehavior = scrollBehavior,
                            title = {
                                Text(
                                    "SKGEzhil JoSAA Helper",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            actions = {

                                IconButton(
                                    onClick = { expanded = true },
                                ) {
                                    Icon(
                                        Icons.Filled.Menu,
                                        contentDescription = "Localized description"
                                    )
                                }


                                MaterialTheme(
                                    shapes = MaterialTheme.shapes.copy(
                                        extraSmall = RoundedCornerShape(
                                            10.dp
                                        )
                                    )
                                ) {
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
                                            text = { Text(text = "Instagram") },
                                            onClick = {
                                                start_activity("instagram", this@MainActivity)
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
                                            text = { Text(text = "GitHub") },
                                            onClick = {
                                                start_activity("github", this@MainActivity)
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
                                            text = { Text(text = "YouTube") },
                                            onClick = {
                                                start_activity("youtube", this@MainActivity)
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
                                            text = { Text(text = "Source Code") },
                                            onClick = {
                                                start_activity("source-code", this@MainActivity)
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
                                            text = { Text(text = "Rate App /\nWrite Review") },
                                            onClick = {
                                                expanded = false
//                                                showRatingDialog = true
                                                review()
                                            },
                                            leadingIcon = {
                                                Icon(
                                                    imageVector = Icons.Filled.Star,
                                                    contentDescription = ""
                                                )
                                            }
                                        )

                                        DropdownMenuItem(
                                            text = { Text(text = "Feedback") },
                                            onClick = {
                                                expanded = false
                                                showRatingDialog = true
                                            },
                                            leadingIcon = {
                                                Icon(
                                                    imageVector = Icons.Filled.Edit,
                                                    contentDescription = ""
                                                )
                                            }
                                        )


                                    }
                                }

                            }
                        )
                    },

                    floatingActionButton = {

                        if (!showRatingDialog) {
                            ExtendedFloatingActionButton(
                                onClick = {
                                    if (showRatingDialog) {
                                        println("CLICKED")
                                        showSnackbar = true
                                        showRatingDialog = false
                                    } else {
                                        loading = true
                                        SendData()
                                        GetData()
                                        val intent = Intent(this, ResultActivity::class.java)
                                        startActivity(intent)
                                    }

                                },
                                text = { Text(text = "Submit  ") },
                                icon = {}
                            )
                        }

                    },
                    floatingActionButtonPosition = FabPosition.End,


                    ) { contentPadding ->
                    // Screen content
                    Surface {
                        Box(modifier = Modifier.padding(contentPadding)) {

                            MainScren()
                            LoadingScreen(loading)

                            if(loadingdialog){
                                LoadingDialog(is_loading = loadingdialog)
                            }
                            if (showRatingDialog)
                                RateDialog()

                        }
                    }
                }
            }
        }
    }

    private fun review() {
        val manager = ReviewManagerFactory.create(this)
        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // We got the ReviewInfo object
                val reviewInfo = task.result
                println("Success")

                val flow: Task<Void?> = manager.launchReviewFlow(this, reviewInfo)
                flow.addOnCompleteListener { task1: Task<Void?>? -> }

            } else {
                // There was some problem, log or handle the error code.
                @ReviewErrorCode val reviewErrorCode =
                    (task.getException() as ReviewException).errorCode
            }
        }
    }


}


fun start_activity(activity_name: String, context: Context) {
    var url: String = ""
    when (activity_name) {
        "instagram" -> url = "https://instagram.com/skgezhil2005"
        "github" -> url = "https://github.com/skgezhil"
        "youtube" -> url = "https://youtube.com/skgezhil"
        "source-code" -> url = "https://github.com/SKGEzhil/SKGEzhil-JoSAA-Helper-Android"
    }


    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(browserIntent)
}

@OptIn(DelicateCoroutinesApi::class)
fun DropdownManipulation(label: String, option: String, isConnected: Boolean) {

    if (isConnected) {
        when (label) {
            "Institute Type" -> {
                Expanded = false
                submit_form.inst_type = option
                GlobalScope.launch(Dispatchers.Main) {
                    withContext(Dispatchers.IO) { SendDropdown("institute_type", option) }
                    // Handle the sendResponse if needed

                    withContext(Dispatchers.IO) { GetDropdown("institute") }


                }
                println("HELLO")

            }

            "Institute" -> {
                submit_form.inst = option
                runBlocking {
                    launch {
                        SendDropdown("institute", option)
                    }
                    launch {
                        GetDropdown("program")
                    }
                }
            }

            "Program" -> {
                submit_form.prog = option
            }

            "Category" -> {
                submit_form.category = option
            }

            "Quota" -> {
                submit_form.quota = option
            }

            "Gender" -> {
                submit_form.gender = option
            }
        }

    } else {
        snacbarMessage = "Please check your internet connection"
        showSnackbar = true
        println(" Check your connection")

    }


}
