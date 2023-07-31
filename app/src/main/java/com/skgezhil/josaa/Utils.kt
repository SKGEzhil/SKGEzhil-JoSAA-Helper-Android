package com.skgezhil.josaa

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.android.gms.tasks.Task
import com.google.android.play.core.review.ReviewManagerFactory
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


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
        sslContext.init(null, trustAllCertificates, SecureRandom())
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

suspend fun SendFeedback() = runBlocking {
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
        sslContext.init(null, trustAllCertificates, SecureRandom())
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
        sslContext.init(null, trustAllCertificates, SecureRandom())
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
            println("RESPONSE LENGTH: "+ response.length)
            if (response.length == 2){
                isAvailable = false
            }
            println(isAvailable)
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

fun Review(context: Context) {
    if (isConnected) {
        val manager = ReviewManagerFactory.create(context)
        val request = manager.requestReviewFlow()
        try {
            request.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // We got the ReviewInfo object
                    val reviewInfo = task.result
                    println("Success")

                    val flow: Task<Void?> = manager.launchReviewFlow(context as Activity, reviewInfo)
                    flow.addOnCompleteListener { task1: Task<Void?>? -> }

                } else {
                    println("FAILED")
                    showToast("Rating Already Submitted")
                    // There was some problem, log or handle the error code.
//                        @ReviewErrorCode val reviewErrorCode =
//                            (task.exception as ReviewException).errorCode
                }
            }
        } catch (e: java.lang.ClassCastException){
            println("ERROR")
        }

    } else {
        showSnackbar("Please check your internet connection")
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
                submit_form.inst_type = option
                GlobalScope.launch(Dispatchers.Main) {
                    withContext(Dispatchers.IO) { SendDropdown("institute_type", option) }
                    withContext(Dispatchers.IO) { GetDropdown("institute") }
                }
            }

            "Institute" -> {
                submit_form.inst = option
                GlobalScope.launch(Dispatchers.Main) {
                    withContext(Dispatchers.IO) { SendDropdown("institute", option) }
                    withContext(Dispatchers.IO) { GetDropdown("program") }
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
        showSnackbar("Please check your internet connection")
        println(" Check your connection")

    }

}

@OptIn(DelicateCoroutinesApi::class)
fun FeedbackSubmit(rating_state: Int, feedback: String) {
    if (isConnected) {
        showToast("Feedback Submitted Successfully")
        println(rating_state)
        println(feedback)
        feedback_send.rating = rating_state
        feedback_send.feedback_ = feedback
        GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) { SendFeedback() }
        }
        showRatingDialog = false

    } else {
        showSnackbar("Please check your internet connection")
    }

}

fun showToast(message: String){
    alert_message = message
    showToast = true
}

fun showSnackbar(message: String){
    alert_message = message
    showSnackbar = true
}