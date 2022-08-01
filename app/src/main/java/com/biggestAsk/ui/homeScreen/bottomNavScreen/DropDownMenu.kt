package com.biggestAsk.ui.homeScreen.bottomNavScreen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.biggestAsk.ui.ui.theme.Custom_Blue
import com.biggestAsk.ui.ui.theme.ET_Bg
import com.example.biggestAsk.R

@Composable
fun DropDownMenu(suggestion:List<String>) {

    // Declaring a boolean value to store
    // the expanded state of the Text Field
    var mExpanded by remember { mutableStateOf(false) }

    // Create a list of cities
    val mCities = listOf("Mark Baggins", "Samantha Baggins", "Marina Kotenko")

    // Create a string value to store the selected city
    var mSelectedText by remember { mutableStateOf(suggestion.get(0)) }

    var mTextFieldSize by remember { mutableStateOf(Size.Zero) }

    // Up Icon when expanded and down icon when collapsed
    val icon = if (mExpanded)
        R.drawable.ic_baseline_arrow_drop_up_24
    else
        R.drawable.ic_baseline_arrow_drop_down_24

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(start = 13.dp, end = 12.dp, top = 12.dp)
    ) {

        // Create an Outlined Text Field
        // with icon and not expanded
        TextField(
            value = mSelectedText,
            onValueChange = { mSelectedText = it },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    // This value is used to assign to
                    // the DropDown the same width
                    mTextFieldSize = coordinates.size.toSize()
                }

                .clickable(indication = null, interactionSource = MutableInteractionSource()) {
                    mExpanded = !mExpanded
                },
            enabled = false,
            trailingIcon = {
                Icon(painter = painterResource(icon), "contentDescription",
                    Modifier.clickable { mExpanded = !mExpanded })
            },
            textStyle = MaterialTheme.typography.body1.copy(
                fontSize = 16.sp,
                color = Custom_Blue,
                fontWeight = FontWeight.Normal,
                lineHeight = 16.sp
            ),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = ET_Bg,
                cursorColor = Custom_Blue,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ), shape = RoundedCornerShape(20)
        )

        // Create a drop-down menu with list of cities,
        // when clicked, set the Text Field text as the city selected
        DropdownMenu(
            expanded = mExpanded,
            onDismissRequest = { mExpanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { mTextFieldSize.width.toDp() })
        ) {
            suggestion.forEach { label ->
                DropdownMenuItem(onClick = {
                    mSelectedText = label
                    mExpanded = false
                }) {
                    Text(text = label)
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DropDownMenuPreview() {
    DropDownMenu(mutableListOf())
}