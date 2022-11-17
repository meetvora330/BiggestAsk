package com.biggestAsk.ui.homeScreen.drawerScreens.yourSurrogateMother

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import com.biggestAsk.ui.ui.theme.Custom_Blue
import com.example.biggestAsk.R
/**
 * surrogate present screen
 */
@Composable
fun SurrogateMotherPresent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 60.dp)
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
                contentDescription = stringResource(id = R.string.content_description),
            )
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .constrainAs(img_main_your_surrogate_mother) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(
                            parent.top,
                            margin = 40.dp
                        )
                    },
                painter = painterResource(id = R.drawable.ic_img_invitation_send_your_surrogate_mother),
                contentDescription = stringResource(id = R.string.content_description),
                contentScale = ContentScale.FillBounds
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 60.dp)
                    .constrainAs(tv_tittle_your_surrogate_mother) {
                        top.linkTo(img_icon_your_surrogate_mother.bottom, margin = 2.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                text = stringResource(id = R.string.your_surrogate_mother_invitation_sent),
                style = MaterialTheme.typography.body2.copy(
                    color = Custom_Blue,
                    fontWeight = FontWeight.W900,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                )
            )
            Button(
                onClick = {},
                modifier = Modifier
                    .width(264.dp)
                    .height(56.dp)
                    .padding(bottom = 0.dp)
                    .constrainAs(btn_add_surrogate_mother) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(
                            img_main_your_surrogate_mother.bottom,
                            margin = 60.dp
                        )
                    },
                enabled = false,
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
                    contentDescription = stringResource(id = R.string.content_description),
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
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SurrogateMotherPresentPreview() {
    SurrogateMotherPresent()
}