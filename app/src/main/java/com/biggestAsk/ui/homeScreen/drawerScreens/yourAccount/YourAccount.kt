package com.biggestAsk.ui.homeScreen.drawerScreens.yourAccount

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.biggestAsk.data.model.request.GetUserDetailsParentRequest
import com.biggestAsk.data.model.request.GetUserDetailsSurrogateRequest
import com.biggestAsk.data.model.response.GetUserDetailsParentResponse
import com.biggestAsk.data.model.response.GetUserDetailsSurrogateResponse
import com.biggestAsk.data.model.response.UpdateUserProfileResponse
import com.biggestAsk.data.source.network.NetworkResult
import com.biggestAsk.ui.HomeActivity
import com.biggestAsk.ui.main.viewmodel.YourAccountViewModel
import com.biggestAsk.ui.ui.theme.Custom_Blue
import com.biggestAsk.util.Constants
import com.biggestAsk.util.PreferenceProvider
import com.example.biggestAsk.R
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@Composable
fun YourAccountScreen(
    navHostController: NavHostController,
    yourAccountViewModel: YourAccountViewModel,
    homeActivity: HomeActivity,
) {
    val context = LocalContext.current
    val type = PreferenceProvider(context).getValue(Constants.TYPE, "")
    val userId = PreferenceProvider(context).getIntValue(Constants.USER_ID, 0)
    val bitmap = remember {
        mutableStateOf<Bitmap?>(null)
    }
    LaunchedEffect(Unit) {
        when(type){
            Constants.SURROGATE ->{
                yourAccountViewModel.isSurrogateApiCalled = true
                yourAccountViewModel.getUserDetailsSurrogate(GetUserDetailsSurrogateRequest(userId, type))
                yourAccountViewModel.getUserDetailResponseSurrogate.observe(homeActivity) {
                    if (it != null) {
                        handleUserDataSurrogate(
                            result = it,
                            yourAccountViewModel = yourAccountViewModel,
                            context = context
                        )
                    }
                }
            }
            Constants.PARENT ->{
                yourAccountViewModel.isParentApiCalled = true
                yourAccountViewModel.getUserDetailsParent(GetUserDetailsParentRequest(userId, type))
                yourAccountViewModel.getUserDetailResponseParent.observe(homeActivity) {
                    if (it != null) {
                        handleUserDataParent(
                            result = it,
                            yourAccountViewModel = yourAccountViewModel,
                            context = context
                        )
                    }
                }
            }
            else -> {
                Log.e("TAG", "YourAccountScreen: no surrogate no parent")
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 24.dp, bottom = 50.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterHorizontally)
        ) {
            when (type) {
                "surrogate" -> {
                    ConstraintLayout(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 28.dp)
                    ) {
                        val (img_camera, img_user) = createRefs()
                        if (bitmap.value == null) {
                            val painter = rememberImagePainter(
                                yourAccountViewModel.surrogateImg,
                                builder = {
                                    placeholder(R.drawable.ic_placeholder_your_account)
                                })
                            Image(
                                painter = painter,
                                contentDescription = null,
                                modifier = Modifier
                                    .width(88.dp)
                                    .height(88.dp)
                                    .constrainAs(img_user) {
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                        top.linkTo(parent.top)
                                    }
                            )
                        } else {
                            bitmap.value?.let {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .constrainAs(img_user) {
                                            start.linkTo(parent.start)
                                            end.linkTo(parent.end)
                                            top.linkTo(parent.top)
                                        },
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Card(
                                        modifier = Modifier
                                            .width(88.dp)
                                            .height(88.dp), shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Image(
                                            modifier = Modifier
                                                .clickable(
                                                    indication = null,
                                                    interactionSource = MutableInteractionSource()
                                                ) {
                                                },
                                            bitmap = it.asImageBitmap(),
                                            contentDescription = "",
                                            contentScale = ContentScale.FillBounds
                                        )
                                    }
                                }
                            }
                        }
                        Image(
                            modifier = Modifier
                                .constrainAs(img_camera) {
                                    start.linkTo(img_user.start)
                                    end.linkTo(img_user.end)
                                    top.linkTo(img_user.top)
                                    bottom.linkTo(img_user.bottom)
                                }
                                .alpha(if (yourAccountViewModel.isEditable.value) 1f else 0f)
                                .clickable(
                                    indication = null,
                                    interactionSource = MutableInteractionSource()
                                ) {
                                    //if (yourAccountViewModel.isEditable.value) {
                                    //    when {
                                    //        permissionState.status.isGranted -> {
                                    //            launcher.launch("image/*")
                                    //            yourAccountViewModel.isPermissionAllowed = false
                                    //        }
                                    //        permissionState.status.shouldShowRationale -> {
                                    //            permissionState.launchPermissionRequest()
                                    //            yourAccountViewModel.isPermissionAllowed = false
                                    //            yourAccountViewModel.isRational = true
                                    //        }
                                    //        !permissionState.status.isGranted -> {
                                    //            permissionState.launchPermissionRequest()
                                    //            yourAccountViewModel.isPermissionAllowed =
                                    //                yourAccountViewModel.isRational
                                    //        }
                                    //    }
                                    //}
                                },
                            painter = painterResource(id = R.drawable.ic_icon_camera_edit_img_your_account),
                            contentDescription = ""
                        )
//                        if (yourAccountViewModel.isPermissionAllowed) {
//                            AlertDialog(
//                                onDismissRequest = {
//                                    yourAccountViewModel.isPermissionAllowed = false
//                                },
//                                confirmButton = {
//                                    TextButton(onClick = {
//                                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                                        val uri = Uri.fromParts("package", context.packageName, null)
//                                        intent.data = uri
//                                        context.startActivity(intent)
//                                    })
//                                    { Text(text = "APP SETTINGS", color = Color.Red) }
//                                },
//                                dismissButton = {
//                                    TextButton(onClick = {
//                                        yourAccountViewModel.isPermissionAllowed = false
//                                    })
//                                    { Text(text = "CANCEL", color = Color.Red) }
//                                },
//                                title = { Text(text = "Permission Denied") },
//                                text = { Text(text = "Permission is denied, Please allow permission from App Settings") }
//                            )
//                        }
                    }
//                    Image(
//                        modifier = Modifier
//                            .width(88.dp)
//                            .height(88.dp)
//                            .clickable(indication = null,
//                                interactionSource = MutableInteractionSource()) {
//                                yourAccountViewModel.isFatherClicked = true
//                                yourAccountViewModel.isMotherClicked = false
//                            },
//                        painter = painterResource(
//                            id = if (!yourAccountViewModel.isFatherClicked) R.drawable.img_intended_parents_father_bg
//                            else R.drawable.img_intended_parents_father
//                        ),
//                        contentDescription = "",
//                    )
                }
                "parent" -> {
                    Column() {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(32.dp,
                                Alignment.CenterHorizontally)
                        ) {
                            val painter1 = rememberImagePainter(
                                yourAccountViewModel.parentImg1,
                                builder = {
                                    placeholder(R.drawable.ic_placeholder_your_account)
                                })
                            Image(
                                modifier = Modifier
                                    .width(88.dp)
                                    .height(88.dp)
                                    .clickable(indication = null,
                                        interactionSource = MutableInteractionSource()) {
                                        yourAccountViewModel.isParentClicked = true
                                        yourAccountViewModel.isMotherClicked = false
                                    },
                                painter = painter1,
                                contentDescription = "",
                            )
                            val painter2 = rememberImagePainter(
                                yourAccountViewModel.parentImg2,
                                builder = {
                                    placeholder(R.drawable.ic_placeholder_your_account)
                                })
                            Image(
                                modifier = Modifier
                                    .width(88.dp)
                                    .height(88.dp)
                                    .clickable(indication = null,
                                        interactionSource = MutableInteractionSource()) {
                                        yourAccountViewModel.isParentClicked = false
                                        yourAccountViewModel.isMotherClicked = true
                                    },
                                painter = painterResource(
                                    id = if (!yourAccountViewModel.isMotherClicked) R.drawable.img_intended_parents_mother_bg
                                    else R.drawable.img_nav_drawer
                                ),
                                contentDescription = "",
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(92.dp,
                                Alignment.CenterHorizontally)
                        ) {
                            Image(
                                modifier = Modifier,
                                painter = painterResource(
                                    id = R.drawable.ic_baseline_arrow_drop_up_24
                                ),
                                contentDescription = "",
                                alpha = if (!yourAccountViewModel.isParentClicked) 0f else 1f
                            )
                            Image(
                                modifier = Modifier,
                                painter = painterResource(
                                    id = R.drawable.ic_baseline_arrow_drop_up_24
                                ),
                                contentDescription = "",
                                alpha = if (yourAccountViewModel.isMotherClicked) 1f else 0f
                            )
                        }
                    }


                }
                else -> {
                    Log.e("TAG", "YourAccountScreen() --> no parent no surrogate")
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.wrapContentWidth(),
                    text = if (yourAccountViewModel.isSurrogateApiCalled){
                        yourAccountViewModel.surrogateFullName
                    }else{
                        if (yourAccountViewModel.isParentClicked) yourAccountViewModel.parentFullName
                        else yourAccountViewModel.parentPartnerName
                    },

                    style = MaterialTheme.typography.h2.copy(
                        color = Color.Black,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.W600,
                        lineHeight = 32.sp
                    )
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .wrapContentWidth()
                            .padding(end = 2.dp),
                        text =  if (yourAccountViewModel.isSurrogateApiCalled){
                            yourAccountViewModel.surrogateDateOfBirth
                        }else{
                            if (yourAccountViewModel.isParentClicked) yourAccountViewModel.parentDateOfBirth else "05/01/1995"
                        },
                        style = MaterialTheme.typography.body2.copy(
                            color = Color(0xFF7F7D7C),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500,
                            lineHeight = 22.sp
                        )
                    )
                    Text(
                        modifier = Modifier.wrapContentWidth(),
                        text = if (yourAccountViewModel.isParentClicked) "(37 Year)" else "(30 Year)",
                        style = MaterialTheme.typography.body2.copy(
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500,
                            lineHeight = 22.sp
                        )
                    )
                }
                Image(
                    modifier = Modifier.padding(top = 10.dp),
                    painter = painterResource(id = R.drawable.ic_img_intended_parents_liner),
                    contentDescription = ""
                )
                Text(
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(top = 18.dp),
                    text =  if (yourAccountViewModel.isSurrogateApiCalled){
                        yourAccountViewModel.surrogateHomeAddress
                    }else{
                        if (yourAccountViewModel.isParentClicked) yourAccountViewModel.parentHomeAddress
                        else "888 Main St,Seattle,WA 98006"
                    },
                    style = MaterialTheme.typography.body2.copy(
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W500,
                        lineHeight = 22.sp
                    )
                )
                Text(
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(top = 11.dp),
                    text =  if (yourAccountViewModel.isSurrogateApiCalled){
                        yourAccountViewModel.surrogatePhoneNumber
                    }else{
                        if (yourAccountViewModel.isParentClicked) yourAccountViewModel.parentPhoneNumber
                        else "+880 9589876"
                    },
                    style = MaterialTheme.typography.body2.copy(
                        color = Custom_Blue,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W500,
                        lineHeight = 22.sp
                    )
                )
                Text(
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(top = 16.dp),
                    text =  if (yourAccountViewModel.isSurrogateApiCalled){
                        yourAccountViewModel.surrogateEmail
                    }else{
                        if (yourAccountViewModel.isParentClicked) yourAccountViewModel.parentEmail else "samnthvan@gmail.ua"
                    },
                    style = MaterialTheme.typography.body2.copy(
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W500,
                        lineHeight = 22.sp
                    )
                )
                Text(
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(top = 16.dp),
                    text = "goodmark@gmail.com",
                    style = MaterialTheme.typography.body2.copy(
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W500,
                        lineHeight = 22.sp
                    )
                )
            }
        }
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            elevation = 2.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 25.dp, end = 23.dp, top = 34.dp)
        ) {
            Column {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, top = 24.dp, end = 56.dp),
                    text = "What is your favorite snack?",
                    color = Color.Black,
                    style = MaterialTheme.typography.body2.copy(
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W600,
                        lineHeight = 24.sp
                    ),
                    )
                Row {
                    Text(
                        modifier = Modifier.padding(start = 24.dp, top = 10.dp),
                        text = "Martha Smith",
                        style = MaterialTheme.typography.body2.copy(
                            color = Custom_Blue,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W600,
                            lineHeight = 22.sp
                        )
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, end = 24.dp),
                        text = "1 Day ago",
                        color = Color(0xFF9F9D9B),
                        style = MaterialTheme.typography.body1,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.End
                    )
                }
                Text(
                    modifier = Modifier.padding(start = 24.dp, top = 4.dp, bottom = 22.dp),
                    text = "Chocolate all the way!!",
                    style = MaterialTheme.typography.body2.copy(
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W600,
                        lineHeight = 22.sp
                    ),
                )
            }
        }
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            elevation = 2.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 25.dp, end = 23.dp, top = 16.dp, bottom = 18.dp)
        ) {
            Column {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, top = 24.dp, end = 56.dp),
                    text = "What is your favorite snack?",
                    style = MaterialTheme.typography.body2.copy(
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W600,
                        lineHeight = 24.sp
                    ),
                )
                Row {
                    Text(
                        modifier = Modifier.padding(start = 24.dp, top = 10.dp),
                        text = "Samantha  Jones",
                        style = MaterialTheme.typography.body2.copy(
                            color = Custom_Blue,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W600,
                            lineHeight = 22.sp
                        ),
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, end = 24.dp),
                        text = "1 Day ago",
                        color = Color(0xFF9F9D9B),
                        style = MaterialTheme.typography.body1,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.End
                    )
                }
                Text(
                    modifier = Modifier.padding(start = 24.dp, top = 4.dp, bottom = 22.dp),
                    text = "Basketball and Miami Heat",
                    style = MaterialTheme.typography.body2.copy(
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W600,
                        lineHeight = 22.sp
                    ),
                )
            }
        }
    }
}

private fun handleUserDataSurrogate(
    result: NetworkResult<GetUserDetailsSurrogateResponse>,
    yourAccountViewModel: YourAccountViewModel,
    context: Context,
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            yourAccountViewModel.isLoading = true
        }
        is NetworkResult.Success -> {
            // bind data to the view
            yourAccountViewModel.isLoading = false
            yourAccountViewModel.surrogateFullName = result.data?.name!!
            yourAccountViewModel.surrogatePhoneNumber = result.data.number!!
            yourAccountViewModel.surrogateEmail = result.data.email!!
            yourAccountViewModel.surrogateHomeAddress = result.data.address!!
            yourAccountViewModel.surrogateDateOfBirth = result.data.date_of_birth!!
            yourAccountViewModel.surrogatePartnerName = result.data.partner_name!!
            yourAccountViewModel.surrogateImg = result.data.image1!!
        }
        is NetworkResult.Error -> {
            // show error message
            yourAccountViewModel.isLoading = false
            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
        }
    }
}

private fun handleUserDataParent(
    result: NetworkResult<GetUserDetailsParentResponse>,
    yourAccountViewModel: YourAccountViewModel,
    context: Context,
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            yourAccountViewModel.isLoading = true
        }
        is NetworkResult.Success -> {
            // bind data to the view
            yourAccountViewModel.isLoading = false
            yourAccountViewModel.parentFullName = result.data?.parent_name!!
            yourAccountViewModel.parentPhoneNumber = result.data.parent_number!!
            yourAccountViewModel.parentEmail = result.data.parent_email!!
            yourAccountViewModel.parentHomeAddress = result.data.parent_address!!
            yourAccountViewModel.parentDateOfBirth = result.data.parent_date_of_birth!!
            yourAccountViewModel.parentPartnerName = result.data.parent_partner_name!!
            yourAccountViewModel.parentImg1 = result.data.parent_image1!!
            yourAccountViewModel.parentImg2 = result.data.parent_image2!!
        }
        is NetworkResult.Error -> {
            // show error message
            yourAccountViewModel.isLoading = false
            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
        }
    }
}

private fun handleUserUpdateData(
    result: NetworkResult<UpdateUserProfileResponse>,
    yourAccountViewModel: YourAccountViewModel,
    context: Context,
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            yourAccountViewModel.isLoading = true
        }
        is NetworkResult.Success -> {
            // bind data to the view
            yourAccountViewModel.isLoading = false

        }
        is NetworkResult.Error -> {
            // show error message
            yourAccountViewModel.isLoading = false
            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
        }
    }
}

private fun convertImageMultiPart(imagePath: String): MultipartBody.Part? {
    val file = File(imagePath)
    return MultipartBody.Part.createFormData(
        "image1",
        file.name,
        file.asRequestBody("image/*".toMediaTypeOrNull())
    )
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun YourAccountScreenPreview() {
    //    YourAccountScreen(YourAccountViewModel())
}
