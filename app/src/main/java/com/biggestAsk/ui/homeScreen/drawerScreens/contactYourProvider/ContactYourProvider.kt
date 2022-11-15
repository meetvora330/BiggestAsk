package com.biggestAsk.ui.homeScreen.drawerScreens.contactYourProvider

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat.startActivity
import com.biggestAsk.data.model.request.GetContactRequest
import com.biggestAsk.data.model.response.GetContactResponse
import com.biggestAsk.data.source.network.NetworkResult
import com.biggestAsk.data.source.network.isInternetAvailable
import com.biggestAsk.ui.activity.HomeActivity
import com.biggestAsk.ui.emailVerification.ProgressBarTransparentBackground
import com.biggestAsk.ui.main.viewmodel.ContactYourProviderViewModel
import com.biggestAsk.ui.ui.theme.Custom_Blue
import com.biggestAsk.util.Constants
import com.biggestAsk.util.PreferenceProvider
import com.example.biggestAsk.R


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ContactYourProvider(
    homeActivity: HomeActivity,
    contactYourProviderViewModel: ContactYourProviderViewModel,
    context: Context,
) {
    val type = PreferenceProvider(context).getValue(Constants.TYPE, "")
    val userId = PreferenceProvider(context).getIntValue(Constants.USER_ID, 0)
    val openEditDialogCustomContact = remember { mutableStateOf(false) }
    val editTfTextFirstContact = remember {
        mutableStateOf("")
    }
    val editTfTextSecondContact = remember {
        mutableStateOf("")
    }
    val editTfTextThirdContact = remember {
        mutableStateOf("")
    }
    val editTfTextFourthContact = remember {
        mutableStateOf("")
    }
    val editedContactId = remember {
        mutableStateOf(0)
    }
    LaunchedEffect(Unit) {
        if (isInternetAvailable(context)) {
            getUpdatedContact(type!!, userId, contactYourProviderViewModel, homeActivity)
        } else {
            contactYourProviderViewModel.isDataNull = false
            contactYourProviderViewModel.contactList.clear()
            Toast.makeText(context, R.string.no_internet_available, Toast.LENGTH_SHORT).show()
        }
    }
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(bottom = 55.dp)
        .verticalScroll(rememberScrollState())) {
        contactYourProviderViewModel.contactList.forEachIndexed { index,item ->
            Surface(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 26.dp, end = 24.dp, top = 16.dp, bottom = 14.dp),
                shape = RoundedCornerShape(10.dp),
                elevation = 1.dp) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        val contactImage =
                            when (item.title) {
                                "Fertility Doctor" -> R.drawable.ic_fertility_doctor
                                "Agency Case Manager" -> R.drawable.ic_agency_case_manager
                                "Surrogacy Lawyer" -> R.drawable.ic_surrogacy_lawyer
                                "ObGyn" -> R.drawable.ic_obgyn
                                else -> null
                            }
                        contactImage?.let { painterResource(id = it) }?.let {
                            Image(
                                modifier = Modifier
                                    .width(60.dp)
                                    .height(60.dp)
                                    .padding(top = 16.dp, start = 16.dp),
                                painter = it,
                                contentScale = ContentScale.FillBounds,
                                contentDescription = stringResource(id = R.string.content_description),
                            )
                        }
                        Column(modifier = Modifier.padding(start = 16.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(
                                    modifier = Modifier.padding(top = 16.dp),
                                    text = item.title ?: "N/A",
                                    style = MaterialTheme.typography.body1,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.W600,
                                    color = Color.Black,
                                )
                                Image(
                                    modifier = Modifier
                                        .padding(top = 12.dp, end = 10.dp)
                                        .clickable(interactionSource = MutableInteractionSource(),
                                            indication = null) {
                                            openEditDialogCustomContact.value = true
                                            if (contactYourProviderViewModel.contactList[index].title != null) {
                                                editTfTextFirstContact.value = ""
                                                editTfTextFirstContact.value =
                                                    contactYourProviderViewModel.contactList[index].title.toString()
                                            }
                                            if (contactYourProviderViewModel.contactList[index].agency_name != null) {
                                                editTfTextSecondContact.value = ""
                                                editTfTextSecondContact.value =
                                                    contactYourProviderViewModel.contactList[index].agency_name.toString()
                                            }
                                            if (contactYourProviderViewModel.contactList[index].agency_email != null) {
                                                editTfTextThirdContact.value = ""
                                                editTfTextThirdContact.value =
                                                    contactYourProviderViewModel.contactList[index].agency_email.toString()
                                            }
                                            if (contactYourProviderViewModel.contactList[index].agency_number != null) {
                                                editTfTextFourthContact.value = ""
                                                editTfTextFourthContact.value =
                                                    contactYourProviderViewModel.contactList[index].agency_number.toString()
                                            }
                                            if (contactYourProviderViewModel.contactList[index].id!=null){
                                                editedContactId.value = 0
                                                editedContactId.value = contactYourProviderViewModel.contactList[index].id
                                            }
                                        },
                                    painter = painterResource(id = R.drawable.ic_icon_your_account_edit_disable),
                                    contentDescription = "edit_contact_your_providers"
                                )
                            }
                            Text(
                                modifier = Modifier.padding(top = 16.dp),
                                text = stringResource(id = R.string.agency_rep_name),
                                style = MaterialTheme.typography.body1,
                                fontSize = 14.sp,
                                color = Color(0xFF8995A3)
                            )
                            Text(
                                modifier = Modifier.padding(top = 3.dp),
                                text = item.agency_name ?: "N/A",
                                style = MaterialTheme.typography.body1,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W600,
                                color = Color.Black,
                                lineHeight = 22.sp
                            )
                            Text(
                                modifier = Modifier.padding(top = 16.dp),
                                text = stringResource(id = R.string.agency_contact_email),
                                style = MaterialTheme.typography.body1,
                                fontSize = 14.sp,
                                color = Color(0xFF8995A3)
                            )
                            Text(
                                modifier = Modifier.padding(top = 3.dp),
                                text = item.agency_email ?: "N/A",
                                style = MaterialTheme.typography.body1,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W600,
                                color = Color.Black,
                                lineHeight = 22.sp
                            )
                            Text(
                                modifier = Modifier.padding(top = 16.dp),
                                text = stringResource(id = R.string.agency_phone_number),
                                style = MaterialTheme.typography.body1,
                                fontSize = 14.sp,
                                color = Color(0xFF8995A3))
                            Text(modifier = Modifier.padding(top = 3.dp),
                                text = item.agency_number ?: "N/A",
                                style = MaterialTheme.typography.body1,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W600,
                                color = Color.Black,
                                lineHeight = 22.sp)
                        }
                    }
                    Divider(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 21.dp),
                        color = Color(0xFFF4F5F6),
                        thickness = 0.8.dp)
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(top = 12.dp, bottom = 12.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically) {
                        Button(modifier = Modifier
                            .width(140.dp)
                            .height(36.dp),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Color(0xFFF4F4F4)),
                            onClick = { },
                            elevation = ButtonDefaults.elevation(defaultElevation = 0.dp,
                                pressedElevation = 0.dp,
                                hoveredElevation = 0.dp,
                                disabledElevation = 0.dp,
                                focusedElevation = 0.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)) {
                            Text(text = stringResource(id = R.string.share),
                                style = MaterialTheme.typography.body1,
                                fontWeight = FontWeight.W600,
                                fontSize = 16.sp,
                                color = Custom_Blue,
                                textAlign = TextAlign.Center)
                        }
                        Button(modifier = Modifier
                            .width(140.dp)
                            .height(36.dp),
                            elevation = ButtonDefaults.elevation(defaultElevation = 0.dp,
                                pressedElevation = 0.dp,
                                hoveredElevation = 0.dp,
                                disabledElevation = 0.dp,
                                focusedElevation = 0.dp),
                            enabled = !item.agency_number.equals(null),
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL)
                                intent.data = Uri.parse("tel:" + item.agency_number)
                                startActivity(homeActivity, intent, Bundle())
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Custom_Blue)) {
                            Text(text = stringResource(id = R.string.call),
                                style = MaterialTheme.typography.body1,
                                fontWeight = FontWeight.W600,
                                fontSize = 16.sp,
                                color = Color.White,
                                textAlign = TextAlign.Center)
                        }
                    }
                }
            }
        }
    }
    if (openEditDialogCustomContact.value) {
        Dialog(properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = true,
        ),
            onDismissRequest = {
                openEditDialogCustomContact.value = false
                contactYourProviderViewModel.bitmap.value = null
                contactYourProviderViewModel.phoneErrorVisible = false
                editTfTextFirstContact.value = ""
                editTfTextSecondContact.value = ""
                editTfTextThirdContact.value = ""
                editTfTextFourthContact.value = ""
            }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = RoundedCornerShape(12.dp)
            ) {

                CreateContactDialog(
                    homeActivity = homeActivity,
                    contactYourProviderViewModel = contactYourProviderViewModel,
                    openDialogCustom = openEditDialogCustomContact,
                    tv_text_tittle = stringResource(R.string.update_contact),
                    tf_hint_tv1 = stringResource(id = R.string.the_happy_agency),
                    tf_hint_tv2 = stringResource(id = R.string.jane_doe),
                    tv_text_second = stringResource(id = R.string.contact_rep_name),
                    tv_text_third = stringResource(id = R.string.contact_email),
                    tf_hint_tv3 = stringResource(id = R.string.agency_email_hint),
                    tv_text_fourth = stringResource(id = R.string.contact_phone_number),
                    tf_hint_tv4 = stringResource(id = R.string.dialog_phone),
                    btn_text_add = stringResource(id = R.string.update_contact),
                    tf_text_first = editTfTextFirstContact,
                    tf_text_second = editTfTextSecondContact,
                    tf_text_third = editTfTextThirdContact,
                    tf_text_fourth = editTfTextFourthContact,
                    isEditDetails = true,
                contact_id = editedContactId.value)

            }
        }
    }

    if (contactYourProviderViewModel.isLoading) {
        ProgressBarTransparentBackground(stringResource(id = R.string.loading))
    }
    if (contactYourProviderViewModel.isDataNull) {
        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = stringResource(id = R.string.no_contact_found))
        }
    }

}

fun getUpdatedContact(
    type: String,
    user_id: Int,
    contactYourProviderViewModel: ContactYourProviderViewModel,
    homeActivity: HomeActivity,
) {
    contactYourProviderViewModel.getContact(getContactRequest = GetContactRequest(type = type,
        user_id = user_id))

    contactYourProviderViewModel.getContactResponse.observe(homeActivity) {
        if (it != null) {
            handleGetContactApi(
                result = it,
                contactYourProviderViewModel = contactYourProviderViewModel,
            )
        }
    }
}

private fun handleGetContactApi(
    result: NetworkResult<GetContactResponse>,
    contactYourProviderViewModel: ContactYourProviderViewModel,
) {
    when (result) {
        is NetworkResult.Loading -> {
            contactYourProviderViewModel.contactList.clear()
            // show a progress bar
            contactYourProviderViewModel.isLoading = true
            contactYourProviderViewModel.isDataNull = false
        }
        is NetworkResult.Success -> {
            // bind data to the view
            contactYourProviderViewModel.isLoading = false
            contactYourProviderViewModel.contactList = result.data!!.data.toMutableStateList()
            contactYourProviderViewModel.isDataNull =
                contactYourProviderViewModel.contactList.isEmpty()
        }
        is NetworkResult.Error -> {
            //show error message
            contactYourProviderViewModel.isLoading = false
        }
    }
}
