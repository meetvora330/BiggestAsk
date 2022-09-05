package com.biggestAsk.ui.homeScreen.drawerScreens.yourAccount

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.biggestAsk.ui.activity.HomeActivity
import com.biggestAsk.ui.activity.MainActivity
import com.biggestAsk.ui.introScreen.findActivity
import com.biggestAsk.util.PreferenceProvider
import com.example.biggestAsk.R

@Composable
fun LogoutDialog(
    openLogoutDialog: MutableState<Boolean>,
    context: Context,
    homeActivity: HomeActivity
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                modifier = Modifier
                    .width(54.dp)
                    .height(59.dp)
                    .padding(top = 16.dp),
                painter = painterResource(id = R.drawable.logo_setting_delete_dialog),
                contentDescription = ""
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                text = stringResource(id = R.string.are_you_sure_you_want_to_logout),
                style = MaterialTheme.typography.body1.copy(
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W400,
                    lineHeight = 24.sp,
                    textAlign = TextAlign.Center,
                    letterSpacing = (-0.08).sp
                )
            )
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                color = Color(0xFFC7C7CC),
                thickness = 0.5.dp
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(), Arrangement.SpaceEvenly
        ) {
            Box(
                modifier = Modifier
                    .width(150.dp)
                    .height(52.dp)
                    .clickable(indication = null, interactionSource = MutableInteractionSource()) {
                        openLogoutDialog.value = false
                    }, contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.negative_btn_text),
                    style = MaterialTheme.typography.body2.copy(
                        color = Color(0xFF8E8E93),
                        fontWeight = FontWeight.W600,
                        fontSize = 17.sp,
                        lineHeight = 22.sp,
                        textAlign = TextAlign.Center
                    )
                )
            }
            Divider(
                color = Color(0xFFC7C7CC),
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .width(1.dp)
                    .height(55.dp),
                thickness = 0.5.dp
            )
            Box(
                modifier = Modifier
                    .width(150.dp)
                    .height(52.dp)
                    .clickable(indication = null, interactionSource = MutableInteractionSource()) {
                        PreferenceProvider(context).setValue("user_logout", true)
                        val intent = Intent(homeActivity, MainActivity::class.java)
                        context.startActivity(intent)
                        context
                            .findActivity()
                            ?.finish()
                        PreferenceProvider(context).clear()
                        PreferenceProvider(context).setValue("isIntroDone", true)
                    }, contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.positive_btn_text),
                    style = MaterialTheme.typography.body2.copy(
                        color = Color(0xFF007AFF),
                        fontWeight = FontWeight.W600,
                        fontSize = 17.sp,
                        lineHeight = 22.sp
                    )
                )
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun LogoutDialogPreview() {
    val dialog = remember {
        androidx.compose.runtime.mutableStateOf(false)
    }
    val context = LocalContext.current
    LogoutDialog(openLogoutDialog = dialog, context = context, homeActivity = HomeActivity())
}

//@Composable
//fun TempLogoutDialog() {
//    ConstraintLayout(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(top = 16.dp)
//    ) {
//        val (img_dialog, tv_tittle, tv_negative, tv_positive, horizontal_line, vertical_line) = createRefs()
//
//        Image(
//            modifier = Modifier
//                .width(54.dp)
//                .height(59.dp)
//                .constrainAs(img_dialog) {
//                    start.linkTo(parent.start)
//                    end.linkTo(parent.end)
//                    top.linkTo(parent.top)
//                },
//            painter = painterResource(id = R.drawable.logo_setting_delete_dialog),
//            contentDescription = ""
//        )
//        Text(
//            modifier = Modifier
//                .padding(top = 10.dp)
//                .constrainAs(),
//            text = stringResource(id = R.string.are_you_sure_you_want_to_logout),
//            style = MaterialTheme.typography.body1.copy(
//                color = Color.Black,
//                fontSize = 14.sp,
//                fontWeight = FontWeight.W400,
//                lineHeight = 24.sp,
//                textAlign = TextAlign.Center,
//                letterSpacing = (-0.08).sp
//            )
//        )
//        Divider(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(top = 10.dp),
//            color = Color(0xFFC7C7CC),
//            thickness = 0.5.dp
//        )
//
//    }
//}
//
//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun TempLogoutDialogPreview() {
//    TempLogoutDialog()
//}