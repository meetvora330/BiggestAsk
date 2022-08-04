package com.biggestAsk.ui.homeScreen.drawerScreens.yourSurrogateMother

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.biggestAsk.data.model.LoginStatus
import com.biggestAsk.data.model.request.InviteSurrogateRequest
import com.biggestAsk.data.model.response.InviteSurrogateResponse
import com.biggestAsk.data.source.network.NetworkResult
import com.biggestAsk.ui.HomeActivity
import com.biggestAsk.ui.emailVerification.ProgressBarTransparentBackground
import com.biggestAsk.ui.homeScreen.bottomDrawerNavGraph.BottomNavScreen
import com.biggestAsk.ui.main.viewmodel.YourSurrogateViewModel
import com.biggestAsk.ui.ui.theme.Custom_Blue
import com.biggestAsk.ui.ui.theme.ET_Bg
import com.biggestAsk.util.Constants
import com.biggestAsk.util.PreferenceProvider
import com.example.biggestAsk.R
import java.util.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun YourSurrogateMother(
    surrogateViewModel: YourSurrogateViewModel,
    context: Context,
    homeActivity: HomeActivity,
    navHostController: NavHostController
) {
    val openDialogSurrogateMother = remember { mutableStateOf(false) }
    val isSurrogateConnected =
        PreferenceProvider(context).getBooleanValue("is_surrogate_connected", false)
    surrogateViewModel.invitationSend.value = isSurrogateConnected
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(
//                /*top =  if (viewModel.invitationSend.value) 20.dp else 30.dp,*/
//                bottom = /*if (viewModel.invitationSend.value) 30.dp else*/ 60.dp
//            )
//            .verticalScroll(rememberScrollState())
//    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(bottom = 60.dp)
        ) {
            val (tv_tittle_your_surrogate_mother, img_main_your_surrogate_mother, btn_add_surrogate_mother) = createRefs()
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .constrainAs(img_main_your_surrogate_mother) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(
                            if (surrogateViewModel.invitationSend.value) parent.top else tv_tittle_your_surrogate_mother.top,
                            margin = if (surrogateViewModel.invitationSend.value) 40.dp else 0.dp
                        )
                        bottom.linkTo(parent.bottom)
                    },
                painter = painterResource(id = if (surrogateViewModel.invitationSend.value) R.drawable.ic_img_invitation_send_your_surrogate_mother else R.drawable.ic_img_add_your_surrogate_mother),
                contentDescription = "",
                contentScale = ContentScale.FillBounds
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = if (surrogateViewModel.invitationSend.value) 60.dp else 22.dp)
                    .constrainAs(tv_tittle_your_surrogate_mother) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                text = if (surrogateViewModel.invitationSend.value) stringResource(id = R.string.your_surrogate_mother_invitation_sent) else stringResource(
                    id = R.string.your_surrogate_mother_invitation_not_sent
                ),
                style = MaterialTheme.typography.body2.copy(
                    color = if (surrogateViewModel.invitationSend.value) Custom_Blue else Color.Black,
                    fontWeight = FontWeight.W900,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                )
            )
            Button(
                onClick = {
                    openDialogSurrogateMother.value = true
                },
                modifier = Modifier
                    .width(264.dp)
                    .height(56.dp)
                    .padding(bottom = 0.dp)
                    .constrainAs(btn_add_surrogate_mother) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    },
                enabled = !surrogateViewModel.invitationSend.value,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Custom_Blue,
                ),
                elevation = ButtonDefaults.elevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp,
                    disabledElevation = 0.dp,
                    hoveredElevation = 0.dp,
                    focusedElevation = 0.dp
                ),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_img_your_surrogate_mother_add_btn),
                    contentDescription = ""
                )
                Text(
                    modifier = Modifier.padding(start = 12.dp),
                    text = stringResource(id = R.string.add_surrogate_mother),
                    style = MaterialTheme.typography.body2.copy(
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W900,
                        lineHeight = 24.sp
                    )
                )
            }
            if (openDialogSurrogateMother.value) {
                Dialog(properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = false,
                    usePlatformDefaultWidth = true
                ), onDismissRequest = { openDialogSurrogateMother.value = false }) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        YourSurrogateDialog(
                            surrogateViewModel = surrogateViewModel,
                            openDialogCustom = openDialogSurrogateMother,
                            context = context,
                            homeActivity = homeActivity,
                            navHostController = navHostController
                        )
                    }
                }
            }
        }
//    }
    if (surrogateViewModel.isSurrogateInvited.value) {
        ProgressBarTransparentBackground(loadingText = "Adding...")
    }

}


@Composable
fun YourSurrogateDialog(
    surrogateViewModel: YourSurrogateViewModel,
    openDialogCustom: MutableState<Boolean>,
    context: Context,
    homeActivity: HomeActivity,
    navHostController: NavHostController
) {
    val isPhoneEmpty = remember {
        mutableStateOf(false)
    }
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 20.dp)
        ) {
            val (dialog_your_surrogate_mother_tittle, icon_close_dialog) = createRefs()
            Text(
                modifier = Modifier.constrainAs(dialog_your_surrogate_mother_tittle) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                },
                text = stringResource(id = R.string.invite_surrogate_dialog_tittle),
                style = MaterialTheme.typography.h1,
                fontSize = 22.sp,
                fontWeight = FontWeight.W900,
                color = Color.Black,
                lineHeight = 28.sp
            )
            Icon(
                modifier = Modifier
                    .width(25.dp)
                    .height(25.dp)
                    .constrainAs(icon_close_dialog) {
                        end.linkTo(parent.end, margin = 17.dp)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                    .clickable {
                        openDialogCustom.value = false
                    },
                imageVector = Icons.Default.Close,
                contentDescription = ""
            )
        }
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, start = 12.dp),
            text = stringResource(id = R.string.invite_surrogate_dialog_email_text),
            style = MaterialTheme.typography.body2.copy(
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.W900,
                lineHeight = 20.sp
            )
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, start = 12.dp, end = 12.dp),
            value = surrogateViewModel.textSurrogateDialogEmail.value,
            onValueChange = {
                surrogateViewModel.textSurrogateDialogEmail.value = it
                isPhoneEmpty.value = false
            },
            textStyle = MaterialTheme.typography.body2.copy(
                color = Color.Black,
                fontWeight = FontWeight.W400,
                lineHeight = 13.sp
            ),
            placeholder = {
                Text(
                    text = stringResource(id = R.string.invite_surrogate_email_hint),
                    style = MaterialTheme.typography.body1.copy(
                        color = Color.Gray,
                        fontSize = 16.sp,
                        lineHeight = 13.sp
                    )
                )
            },
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = ET_Bg,
                cursorColor = Custom_Blue,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
            }),
            singleLine = true
        )
        if (isPhoneEmpty.value) {
            Text(
                text = stringResource(id = R.string.enter_email),
                modifier = Modifier.padding(start = 12.dp, top = 1.dp),
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.error,
                fontSize = 12.sp
            )
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(top = 24.dp, start = 12.dp, end = 12.dp, bottom = 13.dp),
            onClick = {
                if (TextUtils.isEmpty(surrogateViewModel.textSurrogateDialogEmail.value)) {
                    isPhoneEmpty.value = true
                } else {
                    openDialogCustom.value = false
                    val provider = PreferenceProvider(context)
                    val userId = provider.getIntValue("user_id", 0)
                    val type = provider.getValue("type", "")
                    val inviteSurrogateRequest = InviteSurrogateRequest(
                        email = surrogateViewModel.textSurrogateDialogEmail.value,
                        type = type!!
                    )
                    surrogateViewModel.inviteSurrogate(
                        userId = userId,
                        inviteSurrogateRequest = inviteSurrogateRequest
                    )
                    surrogateViewModel.inviteSurrogateResponse.observe(homeActivity) {
                        if (it != null) {
                            handleUserData(
                                result = it,
                                surrogateViewModel = surrogateViewModel,
                                context = context,
                                openDialogCustom = openDialogCustom,
                                navHostController = navHostController
                            )
                        }
                    }
                }
            }, elevation = ButtonDefaults.elevation(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp,
                disabledElevation = 0.dp,
                hoveredElevation = 0.dp,
                focusedElevation = 0.dp
            ), shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Custom_Blue
            )
        ) {
            Text(
                text = "Send invitation",
                style = MaterialTheme.typography.body2.copy(
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W600,
                    lineHeight = 20.sp
                )
            )
        }
    }
}

private fun handleUserData(
    result: NetworkResult<InviteSurrogateResponse>,
    surrogateViewModel: YourSurrogateViewModel,
    context: Context,
    openDialogCustom: MutableState<Boolean>,
    navHostController: NavHostController
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            Log.e("TAG", "handleUserData() --> Loading  $result")
            surrogateViewModel.isSurrogateInvited.value = true
        }

        is NetworkResult.Success -> {
            // bind data to the view
            Log.e("TAG", "handleUserData() --> Success  $result")
            Toast.makeText(context, result.data?.message, Toast.LENGTH_SHORT).show()
            PreferenceProvider(context).setValue("is_surrogate_connected", true)
            surrogateViewModel.invitationSend.value = true
            surrogateViewModel.isSurrogateInvited.value = false
            val provider = PreferenceProvider(context)
            provider.setValue(
                Constants.LOGIN_STATUS,
                LoginStatus.MILESTONE_DATE_NOT_ADDED.name.lowercase(Locale.getDefault())
            )
            navHostController.navigate(BottomNavScreen.SurrogateParentNotAssignScreen.route)
        }

        is NetworkResult.Error -> {
            // show error message
            Log.e("TAG", "handleUserData() --> Error ${result.message}")
            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
            openDialogCustom.value = true
            surrogateViewModel.isSurrogateInvited.value = false
        }
    }
}

