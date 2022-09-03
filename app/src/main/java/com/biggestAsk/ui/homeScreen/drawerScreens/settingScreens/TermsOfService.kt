package com.biggestAsk.ui.homeScreen.drawerScreens.settingScreens

import android.util.Log
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
import com.biggestAsk.data.model.response.GetTermsOfServiceResponse
import com.biggestAsk.data.source.network.NetworkResult
import com.biggestAsk.ui.activity.HomeActivity
import com.biggestAsk.ui.emailVerification.ProgressBarTransparentBackground
import com.biggestAsk.ui.main.viewmodel.TermsOfServiceViewModel
import com.example.biggestAsk.R

@Composable
fun TermsOfService(
    homeActivity: HomeActivity,
    termsOfServiceViewModel: TermsOfServiceViewModel,
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        getTermsOfService(termsOfServiceViewModel, homeActivity)
//        if (isInternetAvailable(context)) {
//        } else {
//            termsOfServiceViewModel.isDataNull = false
//            termsOfServiceViewModel.termsOfServiceList.clear()
//            termsOfServiceViewModel.lastUpdatedDate = ""
//            Toast.makeText(context, R.string.no_internet_available, Toast.LENGTH_SHORT).show()
//        }
    }
    if (termsOfServiceViewModel.isLoading) {
        termsOfServiceViewModel.termsOfServiceList.clear()
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
                    .padding(top = 30.dp, bottom = 10.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier
                        .width(90.dp)
                        .height(90.dp),
                    painter = painterResource(id = R.drawable.logo_setting_privacy_policy),
                    contentDescription = ""
                )
                Text(
                    text = if (termsOfServiceViewModel.lastUpdatedDate != "") "Last Updated on ${termsOfServiceViewModel.lastUpdatedDate}" else "",
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
                termsOfServiceViewModel.termsOfServiceList.forEach { item ->
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
                    //                Text(
                    //                    text = stringResource(id = R.string.setting_terms_of_service_service_tittle),
                    //                    modifier = Modifier
                    //                        .fillMaxWidth()
                    //                        .padding(top = 24.dp, start = 20.dp),
                    //                    style = MaterialTheme.typography.body2.copy(
                    //                        fontWeight = FontWeight.W600,
                    //                        textAlign = TextAlign.Start,
                    //                        fontSize = 22.sp
                    //                    )
                    //                )
                    //                Text(
                    //                    text = stringResource(id = R.string.setting_terms_of_service_service_desc),
                    //                    modifier = Modifier
                    //                        .fillMaxWidth()
                    //                        .padding(top = 16.dp, start = 20.dp),
                    //                    style = MaterialTheme.typography.body1.copy(
                    //                        fontWeight = FontWeight.W400,
                    //                        textAlign = TextAlign.Start,
                    //                        fontSize = 16.sp,
                    //                        lineHeight = 24.sp,
                    //                        color = Color(0xFF9B9BA8)
                    //                    )
                    //                )
                }
            }
        }
}

fun getTermsOfService(
    termsOfServiceViewModel: TermsOfServiceViewModel,
    homeActivity: HomeActivity,
) {
    termsOfServiceViewModel.getAboutApp()

    termsOfServiceViewModel.getTermsOfServiceResponse.observe(homeActivity) {
        if (it != null) {
            handleTermsOfServiceApi(
                result = it,
                termsOfServiceViewModel = termsOfServiceViewModel
            )
        } else {
            Log.e("TAG", "TermsOfServiceApiData is null: ")
        }
    }
}

private fun handleTermsOfServiceApi(
    result: NetworkResult<GetTermsOfServiceResponse>,
    termsOfServiceViewModel: TermsOfServiceViewModel,
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            termsOfServiceViewModel.isLoading = true
            termsOfServiceViewModel.isDataNull = false
            termsOfServiceViewModel.termsOfServiceList.clear()
        }
        is NetworkResult.Success -> {
            // bind data to the view
            termsOfServiceViewModel.isLoading = false
            termsOfServiceViewModel.termsOfServiceList =
                result.data!!.terms_of_service.toMutableStateList()
            termsOfServiceViewModel.lastUpdatedDate = result.data.terms_of_service_date
            termsOfServiceViewModel.isDataNull =
                termsOfServiceViewModel.termsOfServiceList.isEmpty()
        }
        is NetworkResult.Error -> {
            //show error message
            termsOfServiceViewModel.isLoading = false
        }
    }
}