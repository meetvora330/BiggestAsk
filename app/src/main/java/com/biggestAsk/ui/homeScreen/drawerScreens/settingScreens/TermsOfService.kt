package com.biggestAsk.ui.homeScreen.drawerScreens.settingScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.biggestAsk.R

@Composable
fun TermsOfService() {
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
                text = "Last Updated on June 2, 2020",
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
                text = stringResource(id = R.string.setting_terms_of_service_tittle),
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
                text = stringResource(id = R.string.setting_terms_of_service_desc),
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
                text = stringResource(id = R.string.setting_terms_of_service_service_tittle),
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
                text = stringResource(id = R.string.setting_terms_of_service_service_desc),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 20.dp),
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
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun TermsOfServicePreview() {
    TermsOfService()
}