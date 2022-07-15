package com.biggestAsk.ui.homeScreen.drawerScreens.intendParents

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.biggestAsk.ui.main.viewmodel.MainViewModel
import com.biggestAsk.ui.ui.theme.Custom_Blue
import com.example.biggestAsk.R

@Composable
fun IntendParentsScreen(viewModel: MainViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 24.dp, bottom = 50.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterHorizontally)
        ) {
            Image(
                modifier = Modifier
                    .width(88.dp)
                    .height(88.dp)
                    .clickable(indication = null, interactionSource = MutableInteractionSource()) {
                        viewModel.isFatherClicked = true
                        viewModel.isMotherClicked = false
                    },
                painter = painterResource(
                    id = if (!viewModel.isFatherClicked) R.drawable.img_intended_parents_father_bg
                    else R.drawable.img_intended_parents_father
                ),
                contentDescription = "",
            )
            Image(
                modifier = Modifier
                    .width(88.dp)
                    .height(88.dp)
                    .clickable(indication = null, interactionSource = MutableInteractionSource()) {
                        viewModel.isFatherClicked = false
                        viewModel.isMotherClicked = true
                    },
                painter = painterResource(
                    id = if (!viewModel.isMotherClicked) R.drawable.img_intended_parents_mother_bg
                    else R.drawable.img_nav_drawer
                ),
                contentDescription = "",
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(92.dp, Alignment.CenterHorizontally)
        ) {
            Image(
                modifier = Modifier,
                painter = painterResource(
                    id = R.drawable.ic_baseline_arrow_drop_up_24
                ),
                contentDescription = "",
                alpha = if (!viewModel.isFatherClicked) 0f else 1f
            )
            Image(
                modifier = Modifier,
                painter = painterResource(
                    id = R.drawable.ic_baseline_arrow_drop_up_24
                ),
                contentDescription = "",
                alpha = if (viewModel.isMotherClicked) 1f else 0f
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.wrapContentWidth(),
                    text = if (viewModel.isFatherClicked) "Mark Baggins" else "Samantha Jones",
                    style = MaterialTheme.typography.h2.copy(
                        color = Color.Black,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.W600,
                        lineHeight = 32.sp
                    )
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .wrapContentWidth()
                            .padding(end = 2.dp),
                        text = if (viewModel.isFatherClicked) "01/02/1988" else "05/01/1995",
                        style = MaterialTheme.typography.body2.copy(
                            color = Color(0xFF7F7D7C),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500,
                            lineHeight = 22.sp
                        )
                    )
                    Text(
                        modifier = Modifier.wrapContentWidth(),
                        text = if (viewModel.isFatherClicked) "(37 Year)" else "(30 Year)",
                        style = MaterialTheme.typography.body2.copy(
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500,
                            lineHeight = 22.sp
                        )
                    )
                }
                Image(
                    modifier = Modifier.padding(top = 10.dp),
                    painter = painterResource(id = R.drawable.ic_img_intended_parents_liner),
                    contentDescription = ""
                )
                Text(
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(top = 18.dp),
                    text = "888 Main St,Seattle,WA 98006",
                    style = MaterialTheme.typography.body2.copy(
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W500,
                        lineHeight = 22.sp
                    )
                )
                Text(
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(top = 11.dp),
                    text = "+880 9589876",
                    style = MaterialTheme.typography.body2.copy(
                        color = Custom_Blue,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W500,
                        lineHeight = 22.sp
                    )
                )
                Text(
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(top = 16.dp),
                    text = if (viewModel.isFatherClicked) "marktvan@gmail.ua" else "samnthvan@gmail.ua",
                    style = MaterialTheme.typography.body2.copy(
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W500,
                        lineHeight = 22.sp
                    )
                )
                Text(
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(top = 16.dp),
                    text = "goodmark@gmail.com",
                    style = MaterialTheme.typography.body2.copy(
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W500,
                        lineHeight = 22.sp
                    )
                )
            }
        }
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            elevation = 2.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 25.dp, end = 23.dp, top = 34.dp)
        ) {
            Column {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, top = 24.dp, end = 56.dp),
                    text = "What is your favorite snack?",
                    color = Color.Black,
                    style = MaterialTheme.typography.body2.copy(
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W600,
                        lineHeight = 24.sp
                    ),

                    )
                Row {
                    Text(
                        modifier = Modifier.padding(start = 24.dp, top = 10.dp),
                        text = "Martha Smith",
                        style = MaterialTheme.typography.body2.copy(
                            color = Custom_Blue,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W600,
                            lineHeight = 22.sp
                        )
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, end = 24.dp),
                        text = "1 Day ago",
                        color = Color(0xFF9F9D9B),
                        style = MaterialTheme.typography.body1,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.End
                    )
                }
                Text(
                    modifier = Modifier.padding(start = 24.dp, top = 4.dp, bottom = 22.dp),
                    text = "Chocolate all the way!!",
                    style = MaterialTheme.typography.body2.copy(
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W600,
                        lineHeight = 22.sp
                    ),
                )
            }
        }
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            elevation = 2.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 25.dp, end = 23.dp, top = 16.dp, bottom = 18.dp)
        ) {
            Column {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, top = 24.dp, end = 56.dp),
                    text = "What is your favorite snack?",
                    style = MaterialTheme.typography.body2.copy(
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W600,
                        lineHeight = 24.sp
                    ),
                )
                Row {
                    Text(
                        modifier = Modifier.padding(start = 24.dp, top = 10.dp),
                        text = "Samantha  Jones",
                        style = MaterialTheme.typography.body2.copy(
                            color = Custom_Blue,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W600,
                            lineHeight = 22.sp
                        ),
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, end = 24.dp),
                        text = "1 Day ago",
                        color = Color(0xFF9F9D9B),
                        style = MaterialTheme.typography.body1,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.End
                    )
                }
                Text(
                    modifier = Modifier.padding(start = 24.dp, top = 4.dp, bottom = 22.dp),
                    text = "Basketball and Miami Heat",
                    style = MaterialTheme.typography.body2.copy(
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W600,
                        lineHeight = 22.sp
                    ),
                )
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun IntendScreenPreview() {
    IntendParentsScreen(MainViewModel())
}