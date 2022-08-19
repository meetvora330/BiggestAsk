package com.biggestAsk.ui.homeScreen.drawerScreens.yourSurrogateMother

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberImagePainter
import com.biggestAsk.data.model.response.GetIntendedProfileResponse
import com.biggestAsk.data.source.network.NetworkResult
import com.biggestAsk.ui.HomeActivity
import com.biggestAsk.ui.homeScreen.drawerScreens.notification.advancedShadow
import com.biggestAsk.ui.main.viewmodel.YourSurrogateMotherViewModel
import com.biggestAsk.ui.ui.theme.Custom_Blue
import com.biggestAsk.ui.ui.theme.ET_Bg
import com.biggestAsk.ui.ui.theme.Text_Accept_Terms
import com.biggestAsk.ui.ui.theme.light_gray
import com.biggestAsk.util.PreferenceProvider
import com.example.biggestAsk.R
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer

@Composable
fun YourSurrogateMother(
    homeActivity: HomeActivity,
    yourSurrogateMotherViewModel: YourSurrogateMotherViewModel,
    context: Context
) {
    val type = PreferenceProvider(context).getValue("type", "")
    val userId = PreferenceProvider(context).getIntValue("user_id", 0)
    LaunchedEffect(Unit) {
        if (type != null) {
            getIntendedProfile(
                userId = userId,
                type = type,
                homeActivity = homeActivity,
                yourSurrogateMotherViewModel = yourSurrogateMotherViewModel
            )
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 24.dp, bottom = 80.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                32.dp,
                Alignment.CenterHorizontally
            )
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 28.dp)
            ) {
                val (img_user) = createRefs()
                val painter = rememberImagePainter(
                    yourSurrogateMotherViewModel.surrogateMotherImg,
                    builder = { placeholder(R.drawable.ic_placeholder_your_account) }
                )
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
                            .height(88.dp)
                            .placeholder(
                                visible = yourSurrogateMotherViewModel.isSurrogateDataLoading,
                                color = Color.LightGray,
                                shape = RoundedCornerShape(10.dp),
                                highlight = PlaceholderHighlight.shimmer(
                                    highlightColor = Color.White,
                                )
                            ), shape = RoundedCornerShape(10.dp)

                    ) {
                        Image(
                            contentDescription = null,
                            modifier = Modifier
                                .width(88.dp)
                                .height(88.dp),
                            painter = if (yourSurrogateMotherViewModel.surrogateMotherImg != "") painter else painterResource(
                                id = R.drawable.ic_placeholder_your_account
                            )
                        )
                    }
                }

            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 35.dp)
        ) {
            Text(
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(start = 24.dp)
                    .placeholder(
                        visible = yourSurrogateMotherViewModel.isSurrogateDataLoading,
                        color = light_gray,
                        shape = RoundedCornerShape(8.dp),
                        highlight = PlaceholderHighlight.shimmer(
                            highlightColor = Color.White,
                        )
                    ),
                text = stringResource(id = R.string.register_tv_name),
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.W400,
                fontSize = 14.sp,
                color = Text_Accept_Terms
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, start = 24.dp, end = 24.dp)
                    .placeholder(
                        visible = yourSurrogateMotherViewModel.isSurrogateDataLoading,
                        color = light_gray,
                        shape = RoundedCornerShape(8.dp),
                        highlight = PlaceholderHighlight.shimmer(
                            highlightColor = Color.White,
                        )
                    ),
                value = yourSurrogateMotherViewModel.surrogateMotherFullName,
                onValueChange = {
                    yourSurrogateMotherViewModel.surrogateMotherFullName = it
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = ET_Bg,
                    cursorColor = Custom_Blue,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    textColor = Color.Black
                ), readOnly = true,
                maxLines = 1,
                textStyle = MaterialTheme.typography.body2,
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_icon_et_name_your_account),
                        "error",
                    )
                }
            )
            Text(
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(top = 16.dp, start = 24.dp)
                    .placeholder(
                        visible = yourSurrogateMotherViewModel.isSurrogateDataLoading,
                        color = light_gray,
                        shape = RoundedCornerShape(8.dp),
                        highlight = PlaceholderHighlight.shimmer(
                            highlightColor = Color.White,
                        )
                    ),
                text = "Phone number",
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.W400,
                fontSize = 14.sp,
                color = Text_Accept_Terms
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, start = 24.dp, end = 24.dp)
                    .placeholder(
                        visible = yourSurrogateMotherViewModel.isSurrogateDataLoading,
                        color = light_gray,
                        shape = RoundedCornerShape(8.dp),
                        highlight = PlaceholderHighlight.shimmer(
                            highlightColor = Color.White,
                        )
                    ),
                value = yourSurrogateMotherViewModel.surrogateMotherPhoneNumber,
                onValueChange = {
                    yourSurrogateMotherViewModel.surrogateMotherPhoneNumber = it
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = ET_Bg,
                    cursorColor = Custom_Blue,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    textColor = Color.Black
                ),
                readOnly = true,
                maxLines = 1,
                textStyle = MaterialTheme.typography.body2,
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_icon_et_phone_your_account),
                        "error",
                    )
                }
            )
            Text(
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(top = 16.dp, start = 24.dp)
                    .placeholder(
                        visible = yourSurrogateMotherViewModel.isSurrogateDataLoading,
                        color = light_gray,
                        shape = RoundedCornerShape(8.dp),
                        highlight = PlaceholderHighlight.shimmer(
                            highlightColor = Color.White,
                        )
                    ),
                text = stringResource(id = R.string.register_tv_email_text),
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.W400,
                fontSize = 14.sp,
                color = Text_Accept_Terms
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, start = 24.dp, end = 24.dp)
                    .placeholder(
                        visible = yourSurrogateMotherViewModel.isSurrogateDataLoading,
                        color = light_gray,
                        shape = RoundedCornerShape(8.dp),
                        highlight = PlaceholderHighlight.shimmer(
                            highlightColor = Color.White,
                        )
                    ),
                value = yourSurrogateMotherViewModel.surrogateMotherEmail,
                onValueChange = {
                    yourSurrogateMotherViewModel.surrogateMotherEmail = it
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = ET_Bg,
                    cursorColor = Custom_Blue,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    textColor = Color.Black
                ), readOnly = true,
                maxLines = 1,
                textStyle = MaterialTheme.typography.body2,
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_icon_et_email_your_account),
                        "error",
                    )
                }
            )
            Text(
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(top = 16.dp, start = 24.dp)
                    .placeholder(
                        visible = yourSurrogateMotherViewModel.isSurrogateDataLoading,
                        color = light_gray,
                        shape = RoundedCornerShape(8.dp),
                        highlight = PlaceholderHighlight.shimmer(
                            highlightColor = Color.White,
                        )
                    ),
                text = "Home address",
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.W400,
                fontSize = 14.sp,
                color = Text_Accept_Terms
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, start = 24.dp, end = 24.dp)
                    .placeholder(
                        visible = yourSurrogateMotherViewModel.isSurrogateDataLoading,
                        color = light_gray,
                        shape = RoundedCornerShape(8.dp),
                        highlight = PlaceholderHighlight.shimmer(
                            highlightColor = Color.White,
                        )
                    ),
                value = yourSurrogateMotherViewModel.surrogateMotherHomeAddress,
                onValueChange = {
                    yourSurrogateMotherViewModel.surrogateMotherHomeAddress = it
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = ET_Bg,
                    cursorColor = Custom_Blue,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    textColor = Color.Black
                ), readOnly = true,
                maxLines = 1,
                textStyle = MaterialTheme.typography.body2,
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_icon_et_location_your_account),
                        "error",
                    )
                }
            )
            Text(
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(top = 16.dp, start = 24.dp)
                    .placeholder(
                        visible = yourSurrogateMotherViewModel.isSurrogateDataLoading,
                        color = light_gray,
                        shape = RoundedCornerShape(8.dp),
                        highlight = PlaceholderHighlight.shimmer(
                            highlightColor = Color.White,
                        )
                    ),
                text = "Your date of birth",
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.W400,
                fontSize = 14.sp,
                color = Text_Accept_Terms
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, start = 24.dp, end = 24.dp)
                    .placeholder(
                        visible = yourSurrogateMotherViewModel.isSurrogateDataLoading,
                        color = light_gray,
                        shape = RoundedCornerShape(8.dp),
                        highlight = PlaceholderHighlight.shimmer(
                            highlightColor = Color.White,
                        )
                    ),
                value = yourSurrogateMotherViewModel.surrogateMotherDateOfBirth,
                onValueChange = {
                    yourSurrogateMotherViewModel.surrogateMotherDateOfBirth = it
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ), readOnly = true,
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = ET_Bg,
                    cursorColor = Custom_Blue,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    textColor = Color.Black
                ),
                maxLines = 1,
                textStyle = MaterialTheme.typography.body2,
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_icon_et_calendar_your_account),
                        "error",
                    )
                }
            )
            Text(
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(top = 16.dp, start = 24.dp)
                    .placeholder(
                        visible = yourSurrogateMotherViewModel.isSurrogateDataLoading,
                        color = light_gray,
                        shape = RoundedCornerShape(8.dp),
                        highlight = PlaceholderHighlight.shimmer(
                            highlightColor = Color.White,
                        )
                    ),
                text = "Your partner’s name",
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.W400,
                fontSize = 14.sp,
                color = Text_Accept_Terms
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, start = 24.dp, end = 24.dp)
                    .placeholder(
                        visible = yourSurrogateMotherViewModel.isSurrogateDataLoading,
                        color = light_gray,
                        shape = RoundedCornerShape(8.dp),
                        highlight = PlaceholderHighlight.shimmer(
                            highlightColor = Color.White,
                        )
                    ),
                value = yourSurrogateMotherViewModel.surrogatePartnerFullName,
                onValueChange = {
                    yourSurrogateMotherViewModel.surrogatePartnerFullName = it
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = ET_Bg,
                    cursorColor = Custom_Blue,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    textColor = Color.Black
                ), readOnly = true,
                maxLines = 1,
                textStyle = MaterialTheme.typography.body2,
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_icon_et_parent_name_your_account),
                        "error",
                    )
                }
            )
        }
        if (yourSurrogateMotherViewModel.intendedProfileResponseQuestionList.isNotEmpty()) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 37.dp, start = 24.dp, end = 24.dp)
                    .placeholder(
                        visible = yourSurrogateMotherViewModel.isSurrogateDataLoading,
                        color = light_gray,
                        shape = RoundedCornerShape(8.dp),
                        highlight = PlaceholderHighlight.shimmer(
                            highlightColor = Color.White,
                        )
                    ),
                text = stringResource(id = R.string.existing_question_profile),
                style = MaterialTheme.typography.body2.copy(
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    fontWeight = FontWeight.W900,
                    fontSize = 22.sp,
                    lineHeight = 28.sp
                )
            )
            yourSurrogateMotherViewModel.intendedProfileResponseQuestionList.forEachIndexed { index, item ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    elevation = 2.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 25.dp, end = 23.dp, top = 16.dp)
                        .placeholder(
                            visible = yourSurrogateMotherViewModel.isSurrogateDataLoading,
                            color = Color.LightGray,
                            shape = RoundedCornerShape(12.dp),
                            highlight = PlaceholderHighlight.shimmer(
                                highlightColor = Color.White,
                            )
                        )
                        .advancedShadow(
                            color = Color(
                                red = 27,
                                green = 25,
                                blue = 86,
                                alpha = 0.1f.toInt()
                            ),
                            alpha = 0f,
                            cornersRadius = 16.dp,
                            shadowBlurRadius = 0.2.dp,
                            offsetX = 0.dp,
                            offsetY = 4.dp
                        )
                ) {
                    Column {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 24.dp, top = 24.dp, end = 56.dp),
                            text = item.question,
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
                                text = yourSurrogateMotherViewModel.surrogateMotherFullName,
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
                                text = "${yourSurrogateMotherViewModel.intendedProfileResponseDaysList[index]} Day ago",
                                color = Color(0xFF9F9D9B),
                                style = MaterialTheme.typography.body1,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal,
                                textAlign = TextAlign.End
                            )
                        }
                        Text(
                            modifier = Modifier.padding(
                                start = 24.dp,
                                top = 4.dp,
                                bottom = 22.dp
                            ),
                            text = item.answer,
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
    }
}


@Composable
fun NoSurrogateAssigned() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.no_surrogate_found),
            style = MaterialTheme.typography.body2.copy(
                color = Color.Black,
                fontSize = 22.sp,
                fontWeight = FontWeight.W900,
                lineHeight = 24.sp,
                textAlign = TextAlign.Center
            )
        )
    }
}

fun getIntendedProfile(
    userId: Int,
    type: String,
    homeActivity: HomeActivity,
    yourSurrogateMotherViewModel: YourSurrogateMotherViewModel
) {
    yourSurrogateMotherViewModel.getIntendedParentProfile(type = type, userId = userId)
    yourSurrogateMotherViewModel.getIntendedProfileResponse.observe(homeActivity) {
        if (it != null) {
            handleGetIntendedProfileData(
                result = it,
                yourSurrogateMotherViewModel = yourSurrogateMotherViewModel
            )
        }
    }
}

private fun handleGetIntendedProfileData(
    result: NetworkResult<GetIntendedProfileResponse>,
    yourSurrogateMotherViewModel: YourSurrogateMotherViewModel
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            yourSurrogateMotherViewModel.isSurrogateDataLoading = true
        }
        is NetworkResult.Success -> {
            // bind data to the view
            yourSurrogateMotherViewModel.isSurrogateDataLoading = false
            if (result.data?.indended_user?.name != null) {
                yourSurrogateMotherViewModel.surrogateMotherFullName =
                    result.data.indended_user.name
            } else {
                yourSurrogateMotherViewModel.surrogateMotherFullName = "Not inserted"
            }
            if (result.data?.indended_user?.date_of_birth != null) {
                yourSurrogateMotherViewModel.surrogateMotherDateOfBirth =
                    result.data.indended_user.date_of_birth
            } else {
                yourSurrogateMotherViewModel.surrogateMotherDateOfBirth = "Not inserted"
            }
            if (result.data?.indended_user?.address != null) {
                yourSurrogateMotherViewModel.surrogateMotherHomeAddress =
                    result.data.indended_user.address
            } else {
                yourSurrogateMotherViewModel.surrogateMotherHomeAddress = "Not inserted"
            }
            if (result.data?.indended_user?.number != null) {
                yourSurrogateMotherViewModel.surrogateMotherPhoneNumber =
                    result.data.indended_user.number
            } else {
                yourSurrogateMotherViewModel.surrogateMotherPhoneNumber = "Not inserted"
            }
            if (result.data?.indended_user?.email != null) {
                yourSurrogateMotherViewModel.surrogateMotherEmail =
                    result.data.indended_user.email
            } else {
                yourSurrogateMotherViewModel.surrogateMotherEmail = "Not inserted"
            }
            if (result.data?.indended_user?.image != null) {
                yourSurrogateMotherViewModel.surrogateMotherImg = result.data.indended_user.image
            }
            yourSurrogateMotherViewModel.intendedProfileResponseQuestionList.clear()
            yourSurrogateMotherViewModel.intendedProfileResponseDaysList.clear()
            result.data?.question_ans.let {
                if (it != null) {
                    yourSurrogateMotherViewModel.intendedProfileResponseQuestionList.addAll(
                        it
                    )
                }
            }
            result.data?.days.let {
                if (it != null) {
                    yourSurrogateMotherViewModel.intendedProfileResponseDaysList.addAll(it)
                }
            }

        }
        is NetworkResult.Error -> {
            // show error message
            yourSurrogateMotherViewModel.isSurrogateDataLoading = false
            yourSurrogateMotherViewModel.isAnyErrorOccurred = true
        }
    }
}
