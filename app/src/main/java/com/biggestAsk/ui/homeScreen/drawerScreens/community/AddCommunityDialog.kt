package com.biggestAsk.ui.homeScreen.drawerScreens.community

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.TextUtils
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.biggestAsk.ui.ui.theme.Custom_Blue
import com.biggestAsk.ui.ui.theme.Text_Accept_Terms
import com.example.biggestAsk.R

@Composable
fun AddCommunityDialog(
    openDialogCustom: MutableState<Boolean>,
    tf_text_first: MutableState<String>,
    tf_text_second: MutableState<String>,
    tf_text_third: MutableState<String>,
    tf_text_fourth: MutableState<String>,
    tf_hint_tv1: String,
    tv_text_tittle: String,
    tf_hint_tv2: String,
    tv_text_second: String,
    tf_hint_tv3: String,
    tv_text_third: String,
    tf_hint_tv4: String,
    tv_text_fourth: String,
    btn_text_add: String
) {
    val tfTextFirstEmpty = remember {
        mutableStateOf(false)
    }
    val tfTextSecondEmpty = remember {
        mutableStateOf(false)
    }
    val tfTextThirdEmpty = remember {
        mutableStateOf(false)
    }
    val tfTextFourthEmpty = remember {
        mutableStateOf(false)
    }
    val focusManager = LocalFocusManager.current
    var imageData by remember {
        mutableStateOf<Uri?>(null)
    }
    val context = LocalContext.current
    val bitmap = remember {
        mutableStateOf<Bitmap?>(null)
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ) { uri: Uri? ->
        imageData = uri
    }
    imageData.let {
        val uri = it
        if (uri != null) {
            if (Build.VERSION.SDK_INT < 28) {
                bitmap.value = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                bitmap.value = ImageDecoder.decodeBitmap(source)
            }
        }
    }
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
            val (dialog_community_tittle, icon_close_dialog) = createRefs()
            Text(
                modifier = Modifier.constrainAs(dialog_community_tittle) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                },
                text = tv_text_tittle,
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
                        bottom.linkTo(parent.bottom, margin = 5.dp)
                    }
                    .clickable {
                        openDialogCustom.value = false
                    },
                imageVector = Icons.Default.Close,
                contentDescription = ""
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 21.dp, start = 12.dp, end = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.tittle),
                style = MaterialTheme.typography.h1,
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.W900,
                letterSpacing = (-0.24).sp
            )
            TextField(
                shape = RoundedCornerShape(9.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                value = tf_text_first.value,
                onValueChange = {
                    tf_text_first.value = it
                    tfTextFirstEmpty.value = false
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
                ),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color(0xFFF2F2F7),
                    cursorColor = Custom_Blue,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                textStyle = MaterialTheme.typography.body2,
                placeholder = {
                    Text(
                        text = tf_hint_tv1,
                        color = Text_Accept_Terms
                    )
                },
                maxLines = 1,
            )
            if (tfTextFirstEmpty.value) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = stringResource(id = R.string.enter_tittle),
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.error,
                    fontSize = 12.sp
                )
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                text = tv_text_second,
                style = MaterialTheme.typography.h1,
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.W900,
                letterSpacing = (-0.24).sp
            )
            TextField(
                shape = RoundedCornerShape(9.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                value = tf_text_second.value,
                onValueChange = {
                    tf_text_second.value = it
                    tfTextSecondEmpty.value = false
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
                ),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color(0xFFF2F2F7),
                    cursorColor = Custom_Blue,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                textStyle = MaterialTheme.typography.body2,
                placeholder = {
                    Text(
                        text = tf_hint_tv2,
                        color = Text_Accept_Terms
                    )
                },
                maxLines = 1,
            )
            if (tfTextSecondEmpty.value) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "Enter $tv_text_second",
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.error,
                    fontSize = 12.sp
                )
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                text = tv_text_third,
                style = MaterialTheme.typography.h1,
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.W900,
                letterSpacing = (-0.24).sp
            )
            TextField(
                shape = RoundedCornerShape(9.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                value = tf_text_third.value,
                onValueChange = {
                    tf_text_third.value = it
                    tfTextThirdEmpty.value = false
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
                ),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color(0xFFF2F2F7),
                    cursorColor = Custom_Blue,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                textStyle = MaterialTheme.typography.body2,
                placeholder = {
                    Text(
                        text = tf_hint_tv3,
                        color = Text_Accept_Terms
                    )
                },
                maxLines = 1,
            )
            if (tfTextThirdEmpty.value) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "Enter $tv_text_third",
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.error,
                    fontSize = 12.sp
                )
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                text = tv_text_fourth,
                style = MaterialTheme.typography.h1,
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.W900,
                letterSpacing = (-0.24).sp
            )
            TextField(
                shape = RoundedCornerShape(9.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                value = tf_text_fourth.value,
                onValueChange = {
                    tf_text_fourth.value = it
                    tfTextFourthEmpty.value = false
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text, imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                }),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color(0xFFF2F2F7),
                    cursorColor = Custom_Blue,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                textStyle = MaterialTheme.typography.body2,
                placeholder = {
                    Text(
                        text = tf_hint_tv4,
                        color = Text_Accept_Terms
                    )
                },
                maxLines = 1,
            )
            if (tfTextFourthEmpty.value) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "Enter $tv_text_fourth",
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.error,
                    fontSize = 12.sp
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 26.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_icon_attachment_community_add_logo),
                    contentDescription = ""
                )
                Text(
                    modifier = Modifier
                        .padding(start = 11.dp)
                        .clickable(
                            indication = null,
                            interactionSource = MutableInteractionSource()
                        ) {
                            launcher.launch("image/*")
                        },
                    text = stringResource(id = R.string.add_logo),
                    style = MaterialTheme.typography.body1,
                    color = Color(0xFF007AFF),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W400,
                )
            }
            Button(
                onClick = {
                    when {
                        TextUtils.isEmpty(tf_text_first.value) &&
                                TextUtils.isEmpty(tf_text_second.value) &&
                                TextUtils.isEmpty(tf_text_third.value) &&
                                TextUtils.isEmpty(tf_text_fourth.value) -> {
                            tfTextFirstEmpty.value = true
                            tfTextSecondEmpty.value = true
                            tfTextThirdEmpty.value = true
                            tfTextFourthEmpty.value = true

                        }
                        TextUtils.isEmpty(tf_text_first.value) -> {
                            tfTextFirstEmpty.value = true
                        }
                        TextUtils.isEmpty(tf_text_second.value) -> {
                            tfTextSecondEmpty.value = true
                        }
                        TextUtils.isEmpty(tf_text_third.value) -> {
                            tfTextThirdEmpty.value = true
                        }
                        TextUtils.isEmpty(tf_text_fourth.value) -> {
                            tfTextFourthEmpty.value = true
                        }
                        else -> {
                            openDialogCustom.value = false
                        }
                    }
                },
                modifier = Modifier
                    .padding(top = 31.dp, bottom = 12.dp)
                    .fillMaxWidth()
                    .height(45.dp),
                elevation = ButtonDefaults.elevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp,
                    disabledElevation = 0.dp,
                    hoveredElevation = 0.dp,
                    focusedElevation = 0.dp
                ),
                shape = RoundedCornerShape(30),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Custom_Blue,
                )
            ) {
                Text(
                    text = btn_text_add,
                    color = Color.White,
                    style = MaterialTheme.typography.body2,
                    lineHeight = 20.sp,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W600
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddCommunityDialogPreview() {
    AddCommunityDialog(
        openDialogCustom =
        remember {
            mutableStateOf(true)
        },
        tf_text_first = remember { mutableStateOf("") },
        tf_text_second = remember { mutableStateOf("") },
        tf_text_third = remember { mutableStateOf("") },
        tf_text_fourth = remember { mutableStateOf("") },
        tv_text_tittle = "Create Community",
        tf_hint_tv1 = "The Happy Agency",
        tf_hint_tv2 = "Jane Doe",
        tv_text_second = "Description",
        tv_text_third = "Link to Forum",
        tf_hint_tv3 = "Jane Doe",
        tv_text_fourth = "Link to Instagram",
        tf_hint_tv4 = "(222)-333-4444",
        btn_text_add = "+ Add a new Community."
    )
}