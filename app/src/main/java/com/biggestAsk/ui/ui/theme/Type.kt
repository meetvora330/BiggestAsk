package com.biggestAsk.ui.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.biggestAsk.R

// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)
private val f37MoonBold = FontFamily(
    Font(R.font.f37moon_bold)
)
private val f37MoonRegular = FontFamily(
    Font(R.font.f37moon_regular)
)
private val f37MoonExtraBold = FontFamily(
    Font(R.font.f37moon_extra_bold)
)
private val F37Moon = FontFamily(
    Font(R.font.f37moon_light),
    Font(R.font.f37moo_lightit),
    Font(R.font.f37moon_black),
    Font(R.font.f37moon_blackit),
    Font(R.font.f37moon_bold),
    Font(R.font.f37moon_boldit),
    Font(R.font.f37moon_demi),
    Font(R.font.f37moon_demit),
    Font(R.font.f37moon_extra_bold),
    Font(R.font.f37moon_extra_boldit),
    Font(R.font.f37moon_regular),
    Font(R.font.f37moon_regularit),
    Font(R.font.f37moon_thin),
    Font(R.font.f37moon_thinlt)
)
private val robotoRegular = FontFamily(
    Font(R.font.roboto_regular)
)
private val f37MoonBoldIt = FontFamily(
    Font(R.font.f37moon_boldit)
)
val F37MoonTypography = Typography(
    h1 = TextStyle(
        fontFamily = f37MoonBold,
        fontWeight = FontWeight.W600,
        fontSize = 32.sp,
    ),
    h2 = TextStyle(
        fontFamily = f37MoonBold,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
    ),
    h3 = TextStyle(
        fontFamily = F37Moon,
        fontWeight = FontWeight.W500,
        fontSize = 20.sp
    ),
    h4 = TextStyle(
        fontFamily = f37MoonBoldIt,
        fontWeight = FontWeight.W400,
        fontSize = 16.sp
    ),
    h5 = TextStyle(
        fontFamily = F37Moon,
        fontWeight = FontWeight.W400,
        fontSize = 14.sp
    ),
    subtitle1 = TextStyle(
        fontFamily = robotoRegular,
        fontWeight = FontWeight.W400,
        fontSize = 16.sp
    ),
    subtitle2 = TextStyle(
        fontFamily = F37Moon,
        fontWeight = FontWeight.W400,
        fontSize = 14.sp
    ),
    body1 = TextStyle(
        fontFamily = f37MoonRegular,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = Color.DarkGray,
    ),
    body2 = TextStyle(
        fontFamily = f37MoonBold,
        fontSize = 16.sp
    )
)