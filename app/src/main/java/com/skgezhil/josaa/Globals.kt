package com.skgezhil.josaa

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.gson.annotations.SerializedName

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
data class FeedbackClass(var rating: Int, var feedback_: String)


var feedback_send by mutableStateOf(FeedbackClass(0, ""))
var expanded by mutableStateOf(false)
var alert_message by mutableStateOf("")
var isConnected by mutableStateOf(false)
var showRatingDialog by mutableStateOf(false)
var showSnackbar by mutableStateOf(false)
var showToast by mutableStateOf(false)
var submit_form = SendDataClass("", "", "", "", "", "", "", "")
var received_data: List<GetDataClass> = listOf()
const val BaseUrl: String = "https://skgezhil-josaa.com"
var institute_dropdown: List<InstituteDropdownClass> = listOf()
var program_dropdown: List<ProgramDropdownClass> = listOf()
var option = ""
var dropdown_defaults by mutableStateOf(listOf("Select", "All"))
var institute_dropdown_string by mutableStateOf(listOf("Select", "All"))
var program_dropdown_string by mutableStateOf(listOf("Select", "All"))
var loading by mutableStateOf(false)
var isAvailable = true;
var isOpen by mutableStateOf(false);

var institute_type_optn = listOf("Select","All", "IIT", "NIT", "IIIT", "Other GFTIs")

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