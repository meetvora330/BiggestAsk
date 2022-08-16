package com.biggestAsk.ui.homeScreen.drawerScreens.community

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.biggestAsk.data.model.request.GetCommunityRequest
import com.biggestAsk.data.model.response.GetCommunityResponse
import com.biggestAsk.data.source.network.NetworkResult
import com.biggestAsk.ui.HomeActivity
import com.biggestAsk.ui.emailVerification.ProgressBarTransparentBackground
import com.biggestAsk.ui.main.viewmodel.CommunityViewModel
import com.biggestAsk.ui.ui.theme.Custom_Blue
import com.biggestAsk.util.PreferenceProvider
import com.example.biggestAsk.R

@Composable
fun Community(
    communityViewModel: CommunityViewModel,
    homeActivity: HomeActivity,
) {
    val context = LocalContext.current
    val type = PreferenceProvider(context).getValue("type", "")
    val userId = PreferenceProvider(context).getIntValue("user_id", 0)
    LaunchedEffect(Unit) {
        getUpdatedCommunity(type!!, userId, communityViewModel = communityViewModel, homeActivity)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(
                rememberScrollState()
            )
    ) {
        Column(
            modifier = Modifier
                .padding(top = 24.dp, bottom = 55.dp)
        ) {
            communityViewModel.communityList.forEach { item ->
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, end = 24.dp, bottom = 16.dp)
                        .shadow(
                            shape = RoundedCornerShape(14.dp),
                            spotColor = Color(0xFFF4F4F4),
                            elevation = 5.dp,
                            clip = true
                        )
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            val painter = rememberImagePainter(
                                data = item.image,
                                builder = {
                                    placeholder(R.drawable.ic_placeholder_your_account)
                                })
                            Image(
                                modifier = Modifier
                                    .width(48.dp)
                                    .height(48.dp)
                                    .padding(top = 16.dp, start = 16.dp).clip(RoundedCornerShape(10.dp)),
                                contentScale = ContentScale.Crop,
                                painter = painter,
                                contentDescription = ""
                            )
                            Column(modifier = Modifier.padding(start = 16.dp)) {
                                Text(
                                    modifier = Modifier.padding(top = 16.dp),
                                    text = item.title,
                                    style = MaterialTheme.typography.body1,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.W600,
                                    color = Color.Black
                                )
                                Text(
                                    modifier = Modifier.padding(top = 8.dp),
                                    text = item.description,
                                    style = MaterialTheme.typography.body1,
                                    fontSize = 14.sp,
                                    color = Color(0xFF8995A3)
                                )
                            }
                        }
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp),
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
                                    text = "Instagram",
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
                                onClick = { }, shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(backgroundColor = Custom_Blue)
                            ) {
                                Text(
                                    text = "To Forum",
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
    }
    if (communityViewModel.isLoading) {
        ProgressBarTransparentBackground("Loading...")
    }
}

fun getUpdatedCommunity(
    type: String,
    user_id: Int,
    communityViewModel: CommunityViewModel,
    homeActivity: HomeActivity,
) {
    communityViewModel.getCommunity(getCommunityRequest = GetCommunityRequest(
        type = type,
        user_id = user_id
    ))

    communityViewModel.getCommunityResponse.observe(homeActivity) {
        if (it != null) {
            handleGetCommunityApi(
                result = it,
                communityViewModel = communityViewModel
            )
        } else {
            Log.e("TAG", "GetCommunityData is null: ")
        }
    }
}

private fun handleGetCommunityApi(
    result: NetworkResult<GetCommunityResponse>,
    communityViewModel: CommunityViewModel,
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            communityViewModel.isLoading = true
        }
        is NetworkResult.Success -> {
            // bind data to the view
            communityViewModel.isLoading = false
            communityViewModel.communityList = result.data!!.data.toMutableStateList()
        }
        is NetworkResult.Error -> {
            //show error message
            communityViewModel.isLoading = false
        }
    }
}

//@Preview(showSystemUi = true, showBackground = true)
//@Composable
//fun CommunityPreview() {
//    Community()
//}