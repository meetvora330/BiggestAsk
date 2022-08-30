package com.biggestAsk.ui.homeScreen.drawerScreens.settingScreens

import android.content.Context
import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.biggestAsk.data.model.response.AboutAppResponse
import com.biggestAsk.data.source.network.NetworkResult
import com.biggestAsk.data.source.network.isInternetAvailable
import com.biggestAsk.ui.HomeActivity
import com.biggestAsk.ui.main.viewmodel.AboutAppViewModel
import com.example.biggestAsk.R

@Composable
fun AboutApp(
    homeActivity: HomeActivity,
    aboutAppViewModel: AboutAppViewModel,
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        if (isInternetAvailable(context)) {
            getAboutApp(aboutAppViewModel,context,homeActivity)
        } else {
            aboutAppViewModel.isDataNull = false
            aboutAppViewModel.aboutAppList.clear()
            Toast.makeText(context, R.string.no_internet_available, Toast.LENGTH_SHORT).show()
        }
    }
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
//            aboutAppViewModel.aboutAppList.forEach { item ->
                Image(
                    modifier = Modifier
                        .width(90.dp)
                        .height(90.dp),
                    painter = painterResource(id = R.drawable.logo_setting_privacy_policy),
                    contentDescription = ""
                )
                Text(
                    text = aboutAppViewModel.lastUpdatedDate,
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
                Text(
                    text = "item.title",
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
                Text(
                    text = "item.info",
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
                Text(
                    text = stringResource(id = R.string.setting_about_app_more_info_tittle),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp, start = 20.dp),
                    style = MaterialTheme.typography.body2.copy(
                        fontWeight = FontWeight.W600,
                        textAlign = TextAlign.Start,
                        fontSize = 22.sp
                    )
                )
                Text(
                    text = stringResource(id = R.string.setting_about_app_more_information_desc),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, start = 20.dp),
                    style = MaterialTheme.typography.body1.copy(
                        fontWeight = FontWeight.W400,
                        textAlign = TextAlign.Start,
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        color = Color.Black
                    )
                )
//            }
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
                        contentDescription = ""
                    )
                }
            }
        }

    }
}

fun getAboutApp(
    aboutAppViewModel: AboutAppViewModel,
    context: Context,
    homeActivity: HomeActivity,
) {
    aboutAppViewModel.getAboutApp()

    aboutAppViewModel.getAboutAppResponse.observe(homeActivity) {
        if (it != null) {
            handleAboutAppApi(
                result = it,
                aboutAppViewModel = aboutAppViewModel
            )
        } else {
            Log.e("TAG", "GetContactData is null: ")
        }
    }
}

private fun handleAboutAppApi(
    result: NetworkResult<AboutAppResponse>,
    aboutAppViewModel: AboutAppViewModel,
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            aboutAppViewModel.isLoading = true
            aboutAppViewModel.isDataNull = false
        }
        is NetworkResult.Success -> {
            // bind data to the view
            aboutAppViewModel.isLoading = false
            aboutAppViewModel.aboutAppList = result.data!!.about_app.toMutableStateList()
            aboutAppViewModel.lastUpdatedDate = result.data.about_app_date
            //aboutAppViewModel.aboutTitle = result.data.about_app[].title
            aboutAppViewModel.isDataNull = aboutAppViewModel.aboutAppList.isEmpty()
        }
        is NetworkResult.Error -> {
            //show error message
            aboutAppViewModel.isLoading = false
        }
    }
}