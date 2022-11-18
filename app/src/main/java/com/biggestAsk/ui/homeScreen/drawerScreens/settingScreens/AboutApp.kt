package com.biggestAsk.ui.homeScreen.drawerScreens.settingScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.biggestAsk.data.model.response.GetAboutAppResponse
import com.biggestAsk.data.source.network.NetworkResult
import com.biggestAsk.ui.activity.HomeActivity
import com.biggestAsk.ui.emailVerification.ProgressBarTransparentBackground
import com.biggestAsk.ui.main.viewmodel.AboutAppViewModel
import com.example.biggestAsk.R

/**
 * about app screen
 */
@Composable
fun AboutApp(
    homeActivity: HomeActivity,
    aboutAppViewModel: AboutAppViewModel,
) {
    LaunchedEffect(Unit) {
        getAboutApp(aboutAppViewModel, homeActivity)
    }
    if (aboutAppViewModel.isLoading) {
        aboutAppViewModel.aboutAppList.clear()
        ProgressBarTransparentBackground(stringResource(id = R.string.loading))
    } else
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 55.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier
                        .width(90.dp)
                        .height(90.dp),
                    painter = painterResource(id = R.drawable.logo_setting_privacy_policy),
                    contentDescription = stringResource(id = R.string.content_description),
                )
                Text(
                    text = if (aboutAppViewModel.lastUpdatedDate != "") "Last Updated on ${aboutAppViewModel.lastUpdatedDate}" else "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 29.dp),
                    style = MaterialTheme.typography.body1.copy(
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.W400
                    ),
                )
                aboutAppViewModel.aboutAppList.forEach { item ->
                    item.title?.let {
                        Text(
                            text = it,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 40.dp, start = 20.dp),
                            style = MaterialTheme.typography.body2.copy(
                                fontWeight = FontWeight.W600,
                                textAlign = TextAlign.Start,
                                fontSize = 22.sp,
                                color = Color.Black
                            )
                        )
                    }
                    item.info?.let {
                        Text(
                            text = it,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 24.dp, start = 20.dp),
                            style = MaterialTheme.typography.body1.copy(
                                fontWeight = FontWeight.W400,
                                textAlign = TextAlign.Start,
                                fontSize = 16.sp,
                                lineHeight = 24.sp,
                                color = Color(0xFF9B9BA8)
                            )
                        )
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp, bottom = 20.dp)
                ) {
                    listAboutAppImageItem.forEach { img ->
                        Image(
                            modifier = Modifier,
                            painter = painterResource(id = img.img),
                            contentDescription = stringResource(id = R.string.content_description),
                        )
                    }
                }
            }

        }
}

fun getAboutApp(
    aboutAppViewModel: AboutAppViewModel,
    homeActivity: HomeActivity,
) {
    aboutAppViewModel.getAboutApp()

    aboutAppViewModel.getAboutAppResponse.observe(homeActivity) {
        if (it != null) {
            handleAboutAppApi(
                result = it,
                aboutAppViewModel = aboutAppViewModel
            )
        }
    }
}

private fun handleAboutAppApi(
    result: NetworkResult<GetAboutAppResponse>,
    aboutAppViewModel: AboutAppViewModel,
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            aboutAppViewModel.isLoading = true
            aboutAppViewModel.isDataNull = false
            aboutAppViewModel.aboutAppList.clear()
        }
        is NetworkResult.Success -> {
            // bind data to the view
            aboutAppViewModel.isLoading = false
            aboutAppViewModel.aboutAppList = result.data!!.about_app.toMutableStateList()
            aboutAppViewModel.lastUpdatedDate = result.data.about_app_date
            aboutAppViewModel.isDataNull = aboutAppViewModel.aboutAppList.isEmpty()
        }
        is NetworkResult.Error -> {
            //show error message
            aboutAppViewModel.isLoading = false
        }
    }
}