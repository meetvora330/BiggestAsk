package com.biggestAsk.ui.homeScreen.drawerScreens.yourSurrogateMother

import android.text.TextUtils
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import com.biggestAsk.ui.main.viewmodel.MainViewModel
import com.biggestAsk.ui.ui.theme.Custom_Blue
import com.biggestAsk.ui.ui.theme.ET_Bg
import com.example.biggestAsk.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun YourSurrogateMother(viewModel: MainViewModel) {
    val openDialogSurrogateMother = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                /*top =  if (viewModel.invitationSend.value) 20.dp else 30.dp,*/
                bottom = /*if (viewModel.invitationSend.value) 30.dp else*/ 60.dp
            )
            .verticalScroll(rememberScrollState())
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            val (img_icon_your_surrogate_mother, tv_tittle_your_surrogate_mother, img_main_your_surrogate_mother, btn_add_surrogate_mother) = createRefs()
            Image(
                modifier = Modifier
                    .padding(top = 30.dp)
                    .zIndex(1f)
                    .constrainAs(img_icon_your_surrogate_mother) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                painter = painterResource(id = R.drawable.ic_empty_icon_surrogate_mother),
                contentDescription = "",
            )
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .constrainAs(img_main_your_surrogate_mother) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(
                            if (viewModel.invitationSend.value) parent.top else tv_tittle_your_surrogate_mother.top,
                            margin = if (viewModel.invitationSend.value) 40.dp else 0.dp
                        )
                    },
                painter = painterResource(id = if (viewModel.invitationSend.value) R.drawable.ic_img_invitation_send_your_surrogate_mother else R.drawable.ic_img_add_your_surrogate_mother),
                contentDescription = "",
                contentScale = ContentScale.FillBounds
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = if (viewModel.invitationSend.value) 60.dp else 22.dp)
                    .constrainAs(tv_tittle_your_surrogate_mother) {
                        top.linkTo(img_icon_your_surrogate_mother.bottom, margin = 2.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                text = if (viewModel.invitationSend.value) stringResource(id = R.string.your_surrogate_mother_invitation_sent) else stringResource(
                    id = R.string.your_surrogate_mother_invitation_not_sent
                ),
                style = MaterialTheme.typography.body2.copy(
                    color = if (viewModel.invitationSend.value) Custom_Blue else Color.Black,
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
                        top.linkTo(
                            img_main_your_surrogate_mother.bottom,
                            margin = if (viewModel.invitationSend.value) 60.dp else 19.dp
                        )
                    },
                enabled = !viewModel.invitationSend.value,
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
                            viewModel = viewModel,
                            openDialogCustom = openDialogSurrogateMother
                        )
                    }
                }
            }

        }
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun YourSurrogateMotherPreview() {
    YourSurrogateMother(MainViewModel())
}

@Composable
fun YourSurrogateDialog(viewModel: MainViewModel, openDialogCustom: MutableState<Boolean>) {
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
            text = stringResource(id = R.string.invite_surrogate_dialog_phone_number_text),
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
            value = viewModel.textSurrogateDialogPhoneNo.value,
            onValueChange = {
                viewModel.textSurrogateDialogPhoneNo.value = it
                isPhoneEmpty.value = false
            },
            textStyle = MaterialTheme.typography.body2.copy(
                color = Color.Black,
                fontWeight = FontWeight.W400,
                lineHeight = 13.sp
            ),
            placeholder = {
                Text(
                    text = stringResource(id = R.string.invite_surrogate_phone_number_hint),
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
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
            }),
            singleLine = true
        )
        if (isPhoneEmpty.value) {
            Text(
                text = stringResource(id = R.string.enter_phone_number),
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
                if (TextUtils.isEmpty(viewModel.textSurrogateDialogPhoneNo.value)) {
                    isPhoneEmpty.value = true
                } else {
                    viewModel.invitationSend.value = true
                    openDialogCustom.value = false
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun YourSurrogateMotherDialog() {
    YourSurrogateDialog(MainViewModel(), remember { mutableStateOf(true) })
}
