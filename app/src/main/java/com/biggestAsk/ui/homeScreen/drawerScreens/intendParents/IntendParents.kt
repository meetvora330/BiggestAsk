package com.biggestAsk.ui.homeScreen.drawerScreens.intendParents

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.biggestAsk.data.model.response.GetIntendedProfileResponse
import com.biggestAsk.data.source.network.NetworkResult
import com.biggestAsk.ui.HomeActivity
import com.biggestAsk.ui.main.viewmodel.IntendedParentsViewModel
import com.biggestAsk.ui.ui.theme.Custom_Blue
import com.biggestAsk.util.PreferenceProvider
import com.example.biggestAsk.R
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer

@Composable
fun IntendParentsScreen(
    homeActivity: HomeActivity,
    context: Context,
    intendedParentsViewModel: IntendedParentsViewModel
) {
    val type = PreferenceProvider(context).getValue("type", "")
    val userId = PreferenceProvider(context).getIntValue("user_id", 0)
    LaunchedEffect(Unit) {
        if (type != null) {
            getIntendedProfile(
                userId = userId,
                type = type,
                homeActivity = homeActivity,
                intendedParentsViewModel = intendedParentsViewModel,
            )
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 24.dp, bottom = 50.dp)
    ) {
        val painterFather = rememberImagePainter(
            intendedParentsViewModel.imageFather,
            builder = { placeholder(R.drawable.ic_placeholder_your_account) }
        )
        val painterMother = rememberImagePainter(
            intendedParentsViewModel.imageMother,
            builder = { placeholder(R.drawable.ic_placeholder_your_account) }
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterHorizontally)
        ) {
            Card(
                modifier = Modifier
                    .width(88.dp)
                    .height(88.dp)
                    .placeholder(
                        visible = intendedParentsViewModel.isIntendedParentsDataLoading,
                        color = Color.LightGray,
                        shape = RoundedCornerShape(10.dp),
                        highlight = PlaceholderHighlight.shimmer(
                            highlightColor = Color.White,
                        )
                    ), shape = RoundedCornerShape(10.dp)
            ) {
                Image(
                    modifier = Modifier
                        .width(88.dp)
                        .height(88.dp)
                        .clickable(
                            indication = null,
                            interactionSource = MutableInteractionSource()
                        ) {
                            intendedParentsViewModel.isFatherClicked = true
                            intendedParentsViewModel.isMotherClicked = false
                        },
                    painter = if (intendedParentsViewModel.imageFather != "") painterFather else painterResource(
                        id = R.drawable.ic_placeholder_your_account
                    ),
                    contentDescription = "",
                )
            }
            Card(
                modifier = Modifier
                    .width(88.dp)
                    .height(88.dp)
                    .placeholder(
                        visible = intendedParentsViewModel.isIntendedParentsDataLoading,
                        color = Color.LightGray,
                        shape = RoundedCornerShape(10.dp),
                        highlight = PlaceholderHighlight.shimmer(
                            highlightColor = Color.White,
                        )
                    ), shape = RoundedCornerShape(10.dp)

            ) {
                Image(
                    modifier = Modifier
                        .width(88.dp)
                        .height(88.dp)
                        .clickable(
                            indication = null,
                            interactionSource = MutableInteractionSource()
                        ) {
                            intendedParentsViewModel.isFatherClicked = false
                            intendedParentsViewModel.isMotherClicked = true
                        },
                    painter = if (intendedParentsViewModel.imageMother != "") painterMother else painterResource(
                        id = R.drawable.ic_placeholder_your_account
                    ),
                    contentDescription = "",
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(92.dp, Alignment.CenterHorizontally)
        ) {
            Image(
                modifier = Modifier,
                painter = painterResource(
                    id = R.drawable.ic_baseline_arrow_drop_up_24
                ),
                contentDescription = "",
                alpha = if (!intendedParentsViewModel.isFatherClicked) 0f else 1f
            )
            Image(
                modifier = Modifier,
                painter = painterResource(
                    id = R.drawable.ic_baseline_arrow_drop_up_24
                ),
                contentDescription = "",
                alpha = if (intendedParentsViewModel.isMotherClicked) 1f else 0f
            )
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
                        modifier = Modifier
                            .wrapContentWidth()
                            .placeholder(
                                visible = intendedParentsViewModel.isIntendedParentsDataLoading,
                                color = Color.LightGray,
                                shape = RoundedCornerShape(10.dp),
                                highlight = PlaceholderHighlight.shimmer(
                                    highlightColor = Color.White,
                                )
                            ),
                        text = if (intendedParentsViewModel.isFatherClicked) intendedParentsViewModel.parentFullName else intendedParentsViewModel.motherFullName,
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
                                .padding(end = 2.dp)
                                .placeholder(
                                    visible = intendedParentsViewModel.isIntendedParentsDataLoading,
                                    color = Color.LightGray,
                                    shape = RoundedCornerShape(10.dp),
                                    highlight = PlaceholderHighlight.shimmer(
                                        highlightColor = Color.White,
                                    )
                                ),
                            text = if (intendedParentsViewModel.isFatherClicked) intendedParentsViewModel.parentDateOfBirth else intendedParentsViewModel.motherDateOfBirth,
                            style = MaterialTheme.typography.body2.copy(
                                color = Color(0xFF7F7D7C),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W500,
                                lineHeight = 22.sp
                            )
                        )
                    Text(
                        modifier = Modifier
                            .wrapContentWidth()
                            .placeholder(
                                visible = intendedParentsViewModel.isIntendedParentsDataLoading,
                                color = Color.LightGray,
                                shape = RoundedCornerShape(10.dp),
                                highlight = PlaceholderHighlight.shimmer(
                                    highlightColor = Color.White,
                                )
                            ),
                        text = if (intendedParentsViewModel.isFatherClicked) "(37 Year)" else "(30 Year)",
                        style = MaterialTheme.typography.body2.copy(
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500,
                            lineHeight = 22.sp
                        )
                    )
                }
                Image(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .placeholder(
                            visible = intendedParentsViewModel.isIntendedParentsDataLoading,
                            color = Color.LightGray,
                            shape = RoundedCornerShape(10.dp),
                            highlight = PlaceholderHighlight.shimmer(
                                highlightColor = Color.White,
                            )
                        ),
                    painter = painterResource(id = R.drawable.ic_img_intended_parents_liner),
                    contentDescription = ""
                )
                    Text(
                        modifier = Modifier
                            .wrapContentWidth()
                            .padding(top = 18.dp)
                            .placeholder(
                                visible = intendedParentsViewModel.isIntendedParentsDataLoading,
                                color = Color.LightGray,
                                shape = RoundedCornerShape(10.dp),
                                highlight = PlaceholderHighlight.shimmer(
                                    highlightColor = Color.White,
                                )
                            ),
                        text = if (intendedParentsViewModel.isFatherClicked) intendedParentsViewModel.parentHomeAddress else intendedParentsViewModel.motherHomeAddress,
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
                            .padding(top = 11.dp)
                            .placeholder(
                                visible = intendedParentsViewModel.isIntendedParentsDataLoading,
                                color = Color.LightGray,
                                shape = RoundedCornerShape(10.dp),
                                highlight = PlaceholderHighlight.shimmer(
                                    highlightColor = Color.White,
                                )
                            ),
                        text = if (intendedParentsViewModel.isFatherClicked) intendedParentsViewModel.parentPhoneNumber else intendedParentsViewModel.motherPhoneNumber,
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
                            .padding(top = 16.dp)
                            .placeholder(
                                visible = intendedParentsViewModel.isIntendedParentsDataLoading,
                                color = Color.LightGray,
                                shape = RoundedCornerShape(10.dp),
                                highlight = PlaceholderHighlight.shimmer(
                                    highlightColor = Color.White,
                                )
                            ),
                        text = if (intendedParentsViewModel.isFatherClicked) intendedParentsViewModel.parentEmail else intendedParentsViewModel.motherEmail,
                        style = MaterialTheme.typography.body2.copy(
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500,
                            lineHeight = 22.sp
                        )
                    )
            }
        }
//        if (intendedParentsViewModel.intendedProfileResponseQuestionList.isNotEmpty()){
            intendedParentsViewModel.intendedProfileResponseQuestionList.forEachIndexed { index, questionAn ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    elevation = 2.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 25.dp, end = 23.dp, top = 34.dp)
                        .placeholder(
                            visible = intendedParentsViewModel.isIntendedParentsDataLoading,
                            color = Color.LightGray,
                            shape = RoundedCornerShape(10.dp),
                            highlight = PlaceholderHighlight.shimmer(
                                highlightColor = Color.White,
                            )
                        )
                ) {
                    Column {
                        questionAn.question?.let {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 24.dp, top = 24.dp, end = 56.dp),
                                text = it,
                                color = Color.Black,
                                style = MaterialTheme.typography.body2.copy(
                                    color = Color.Black,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.W600,
                                    lineHeight = 24.sp
                                ),

                                )
                        }
                        Row {
                            if (questionAn.user_name!=""){
                                questionAn.user_name?.let {
                                    Text(
                                        modifier = Modifier.padding(start = 24.dp, top = 10.dp),
                                        text = it,
                                        style = MaterialTheme.typography.body2.copy(
                                            color = Custom_Blue,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.W600,
                                            lineHeight = 22.sp
                                        )
                                    )
                                }
                            }
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp, end = 24.dp),
                                text = "${intendedParentsViewModel.intendedProfileResponseDaysList[index]} Day ago",
                                color = Color(0xFF9F9D9B),
                                style = MaterialTheme.typography.body1,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal,
                                textAlign = TextAlign.End
                            )
                        }
                        if (questionAn.answer!=""){
                            questionAn.answer?.let {
                                Text(
                                    modifier = Modifier.padding(start = 24.dp, top = 4.dp, bottom = 22.dp),
                                    text = it,
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
//            }
        }
    }
}

fun getIntendedProfile(
    userId: Int,
    type: String,
    homeActivity: HomeActivity,
    intendedParentsViewModel: IntendedParentsViewModel,
) {
    intendedParentsViewModel.getIntendedParentProfile(type = type, userId = userId)
    intendedParentsViewModel.getIntendedProfileResponse.observe(homeActivity) {
        if (it != null) {
            handleGetIntendedProfileDataParent(
                result = it,
                intendedParentsViewModel = intendedParentsViewModel,
            )
        }
    }
}

fun handleGetIntendedProfileDataParent(
    result: NetworkResult<GetIntendedProfileResponse>,
    intendedParentsViewModel: IntendedParentsViewModel,
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            intendedParentsViewModel.isIntendedParentsDataLoading = true
        }
        is NetworkResult.Success -> {
            // bind data to the view
            intendedParentsViewModel.isIntendedParentsDataLoading = false
            if (result.data?.indended_user?.name != null) {
                intendedParentsViewModel.parentFullName =
                    result.data.indended_user.name
            }
            if (result.data?.indended_user?.partner_name != null) {
                intendedParentsViewModel.motherFullName =
                    result.data.indended_user.partner_name
            }
            if (result.data?.indended_user?.date_of_birth != null) {
                intendedParentsViewModel.parentDateOfBirth =
                    result.data.indended_user.date_of_birth
            }
            if (result.data?.indended_user?.partner_date_of_birth != null) {
                intendedParentsViewModel.motherDateOfBirth =
                    result.data.indended_user.partner_date_of_birth
            }
            if (result.data?.indended_user?.address != null) {
                intendedParentsViewModel.parentHomeAddress =
                    result.data.indended_user.address
            }
            if (result.data?.indended_user?.partner_address != null) {
                intendedParentsViewModel.motherHomeAddress =
                    result.data.indended_user.address
            }
            if (result.data?.indended_user?.number != null) {
                intendedParentsViewModel.parentPhoneNumber =
                    result.data.indended_user.number
            }
            if (result.data?.indended_user?.partner_number != null) {
                intendedParentsViewModel.motherPhoneNumber =
                    result.data.indended_user.number
            }
            if (result.data?.indended_user?.email != null) {
                intendedParentsViewModel.parentEmail =
                    result.data.indended_user.email
            }
            if (result.data?.indended_user?.partner_email != null) {
                intendedParentsViewModel.motherEmail =
                    result.data.indended_user.email
            }
            if (result.data?.indended_user?.image1 != null) {
                intendedParentsViewModel.imageFather = result.data.indended_user.image1
            }
            if (result.data?.indended_user?.image2 != null) {
                intendedParentsViewModel.imageMother = result.data.indended_user.image2
            }
            intendedParentsViewModel.intendedProfileResponseQuestionList.clear()
            intendedParentsViewModel.intendedProfileResponseDaysList.clear()
            result.data?.question_ans.let {
                if (it != null) {
                    intendedParentsViewModel.intendedProfileResponseQuestionList.addAll(
                        it
                    )
                }
            }
            result.data?.days.let {
                if (it != null) {
                    intendedParentsViewModel.intendedProfileResponseDaysList.addAll(it)
                }
            }

        }
        is NetworkResult.Error -> {
            // show error message
            intendedParentsViewModel.isIntendedParentsDataLoading = false
            intendedParentsViewModel.isAnyErrorOccurred = true
        }
    }
}