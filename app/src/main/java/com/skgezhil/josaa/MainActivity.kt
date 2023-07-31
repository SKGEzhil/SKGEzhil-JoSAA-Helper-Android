package com.skgezhil.josaa

// --------------------------------- Imports ----------------------------------

import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.android.gms.tasks.Task
import com.google.android.play.core.review.ReviewManagerFactory
import com.skgezhil.josaa.ui.theme.SKGEzhilJoSAAHelperTheme
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
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

// ----------------------------- Main Activity ---------------------------------------
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
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class,
        ExperimentalTextApi::class
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        setContent {
            SKGEzhilJoSAAHelperTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                val scope = rememberCoroutineScope()
                val message by rememberUpdatedState(newValue = alert_message)
                val isExpanded by rememberUpdatedState(newValue = expanded)
                val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

                // show snackbar as a suspend function
                if (showSnackbar) {
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

                //show Toast
                if (showToast) {
                    Toast.makeText(this, alert_message, Toast.LENGTH_SHORT).show()
                    showToast = false
                }

                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    modifier = Modifier
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
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
                                            text = buildAnnotatedString {
                                                append("SKGEzhil ")
                                                withStyle(
                                                    SpanStyle(
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 17.sp,

                                                    )
                                                ) {
                                                    append(" (Developer)")
                                                }
                                                withStyle(
                                                    SpanStyle(
                                                        fontSize = 15.sp,
                                                    fontWeight = FontWeight.Normal
                                                    )
                                                ){
                                                    append("\nA fresher @ IIT Hyderabad")
                                                }


                                            },
                                            modifier = Modifier
                                                .padding(top = 10.dp)
                                                .padding(bottom = 10.dp)
                                                .padding(start = 10.dp)
                                                .padding(end = 0.dp),
                                            fontSize = 22.sp,
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
                                                Review(this@MainActivity)                                     },
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

                                    loading = true
                                    SendData()
                                    GetData()
                                    val intent = Intent(this, ResultActivity::class.java)
                                    startActivity(intent)

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

                        val isDarkTheme = isSystemInDarkTheme()

                        Box(
                            modifier = Modifier
                            .padding(contentPadding)
                                .background(brush = Brush.linearGradient(
                                    colors = if (isDarkTheme) listOf(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.surface) else listOf(MaterialTheme.colorScheme.inverseOnSurface, MaterialTheme.colorScheme.inverseOnSurface),
                                    start = Offset(0f, 0f),
                                    end = Offset.Infinite
                                )),
                        ) {

                            MainScren()
                            LoadingScreen(loading)

                            if (showRatingDialog)
                                RateDialog()

                        }
                    }
                }
            }
        }
    }
}

