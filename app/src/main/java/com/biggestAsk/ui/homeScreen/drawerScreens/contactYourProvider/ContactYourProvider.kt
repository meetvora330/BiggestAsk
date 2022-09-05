package com.biggestAsk.ui.homeScreen.drawerScreens.contactYourProvider

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import coil.compose.rememberImagePainter
import com.biggestAsk.data.model.request.GetContactRequest
import com.biggestAsk.data.model.response.GetContactResponse
import com.biggestAsk.data.source.network.NetworkResult
import com.biggestAsk.data.source.network.isInternetAvailable
import com.biggestAsk.ui.HomeActivity
import com.biggestAsk.ui.emailVerification.ProgressBarTransparentBackground
import com.biggestAsk.ui.main.viewmodel.ContactYourProviderViewModel
import com.biggestAsk.ui.ui.theme.Custom_Blue
import com.biggestAsk.util.Constants
import com.biggestAsk.util.PreferenceProvider
import com.example.biggestAsk.R


@Composable
fun ContactYourProvider(
    homeActivity: HomeActivity,
    contactYourProviderViewModel: ContactYourProviderViewModel,
    context: Context,
) {
    val type = PreferenceProvider(context).getValue(Constants.TYPE, "")
    val userId = PreferenceProvider(context).getIntValue(Constants.USER_ID, 0)
    LaunchedEffect(Unit) {
        if (isInternetAvailable(context)) {
            getUpdatedContact(type!!, userId, contactYourProviderViewModel, homeActivity)
        } else {
            contactYourProviderViewModel.isDataNull = false
            contactYourProviderViewModel.contactList.clear()
            Toast.makeText(context, R.string.no_internet_available, Toast.LENGTH_SHORT).show()
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 55.dp)
            .verticalScroll(
                rememberScrollState()
            )
    ) {
        contactYourProviderViewModel.contactList.forEach { item ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 26.dp, end = 24.dp, top = 16.dp, bottom = 14.dp),
                shape = RoundedCornerShape(10.dp),
                elevation = 1.dp
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        val painter = rememberImagePainter(
                            item.image,
                            builder = {
                                placeholder(R.drawable.ic_placeholder_your_account)
                            })
                        Image(
                            modifier = Modifier
                                .width(48.dp)
                                .height(48.dp)
                                .padding(top = 16.dp, start = 16.dp),
                            painter = painter,
                            contentDescription = "",
                        )
                        Column(modifier = Modifier.padding(start = 16.dp)) {
                            Text(
                                modifier = Modifier.padding(top = 16.dp),
                                text = item.title,
                                style = MaterialTheme.typography.body1,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W600,
                                color = Color.Black,
                            )
                            Text(
                                modifier = Modifier.padding(top = 16.dp),
                                text = "Agency rep name",
                                style = MaterialTheme.typography.body1,
                                fontSize = 14.sp,
                                color = Color(0xFF8995A3)
                            )
                            Text(
                                modifier = Modifier.padding(top = 3.dp),
                                text = item.agency_name,
                                style = MaterialTheme.typography.body1,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W600,
                                color = Color.Black,
                                lineHeight = 22.sp
                            )
                            Text(
                                modifier = Modifier.padding(top = 16.dp),
                                text = "Agency email",
                                style = MaterialTheme.typography.body1,
                                fontSize = 14.sp,
                                color = Color(0xFF8995A3)
                            )
                            Text(
                                modifier = Modifier.padding(top = 3.dp),
                                text = item.agency_email,
                                style = MaterialTheme.typography.body1,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W600,
                                color = Color.Black,
                                lineHeight = 22.sp
                            )
                            Text(
                                modifier = Modifier.padding(top = 16.dp),
                                text = "Agency phone number",
                                style = MaterialTheme.typography.body1,
                                fontSize = 14.sp,
                                color = Color(0xFF8995A3)
                            )
                            Text(
                                modifier = Modifier.padding(top = 3.dp),
                                text = item.agency_number,
                                style = MaterialTheme.typography.body1,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W600,
                                color = Color.Black,
                                lineHeight = 22.sp
                            )
                        }
                    }
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 21.dp),
                        color = Color(0xFFF4F5F6),
                        thickness = 0.8.dp
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(top = 12.dp, bottom = 12.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            modifier = Modifier
                                .width(140.dp)
                                .height(36.dp), shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Color(0xFFF4F4F4)),
                            onClick = { },
                            elevation = ButtonDefaults.elevation(
                                defaultElevation = 0.dp,
                                pressedElevation = 0.dp,
                                hoveredElevation = 0.dp,
                                disabledElevation = 0.dp,
                                focusedElevation = 0.dp
                            ),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
                        ) {
                            Text(
                                text = stringResource(id = R.string.share),
                                style = MaterialTheme.typography.body1,
                                fontWeight = FontWeight.W600,
                                fontSize = 16.sp,
                                color = Custom_Blue,
                                textAlign = TextAlign.Center
                            )
                        }
                        Button(
                            modifier = Modifier
                                .width(140.dp)
                                .height(36.dp), elevation = ButtonDefaults.elevation(
                                defaultElevation = 0.dp,
                                pressedElevation = 0.dp,
                                hoveredElevation = 0.dp,
                                disabledElevation = 0.dp,
                                focusedElevation = 0.dp
                            ),
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL)
                                intent.data = Uri.parse("tel:" + item.agency_number)
                                startActivity(homeActivity, intent, Bundle())
                            }, shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Custom_Blue)
                        ) {
                            Text(
                                text = stringResource(id = R.string.call),
                                style = MaterialTheme.typography.body1,
                                fontWeight = FontWeight.W600,
                                fontSize = 16.sp,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

            }
        }

    }
    if (contactYourProviderViewModel.isLoading) {
        ProgressBarTransparentBackground(stringResource(id = R.string.loading))
    }
    if (contactYourProviderViewModel.isDataNull) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
    contactYourProviderViewModel.getContact(
        getContactRequest = GetContactRequest(
            type = type,
            user_id = user_id
        )
    )

    contactYourProviderViewModel.getContactResponse.observe(homeActivity) {
        if (it != null) {
            handleGetContactApi(
                result = it,
                contactYourProviderViewModel = contactYourProviderViewModel,
            )
        } else {
            Log.e("TAG", "GetContactData is null: ")
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

//@Preview(showSystemUi = true, showBackground = true)
//@Composable
//fun ContactYourProviderPreview() {
//    ContactYourProvider(
//        modifier = Modifier
//    )
//}