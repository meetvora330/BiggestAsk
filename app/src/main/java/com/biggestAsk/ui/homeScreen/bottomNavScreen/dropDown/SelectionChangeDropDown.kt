package com.biggestAsk.ui.homeScreen.bottomNavScreen.dropDown

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.biggestAsk.data.model.request.ScreenQuestionStatusRequest
import com.biggestAsk.data.model.response.CommonResponse
import com.biggestAsk.data.source.network.NetworkResult
import com.biggestAsk.ui.activity.HomeActivity
import com.biggestAsk.ui.homeScreen.bottomNavScreen.updateFrequency
import com.biggestAsk.ui.main.viewmodel.BottomQuestionViewModel
import com.biggestAsk.ui.main.viewmodel.FrequencyViewModel
import com.biggestAsk.ui.ui.theme.Custom_Blue
import com.biggestAsk.ui.ui.theme.ET_Bg
import com.biggestAsk.util.Constants
import com.biggestAsk.util.PreferenceProvider
import com.example.biggestAsk.R

@Composable
fun selectionChangeDropDown(
    suggestions: List<String>,
    hint: String,
    modifier: Modifier,
    style: TextStyle,
    color: Color = ET_Bg,
    text: String = "",
    frequencyViewModel: FrequencyViewModel,
    isFrequencyChanged: Boolean = false,
    context: Context,
    homeActivity: HomeActivity,
    questionViewModel: BottomQuestionViewModel,
): String {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(text) }

    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    val icon = if (expanded)
        R.drawable.ic_baseline_arrow_drop_up_24
    else
        R.drawable.ic_baseline_arrow_drop_down_24

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = selectedText,
            onValueChange = { selectedText = it },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    //This value is used to assign to the DropDown the same width
                    textFieldSize = coordinates.size.toSize()
                }
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }) {
                    expanded = !expanded
                }, enabled = false, placeholder = {
                Text(hint, style = MaterialTheme.typography.body2)
            },
            trailingIcon = {
                Icon(painter = painterResource(id = icon), "",
                    Modifier.clickable { expanded = !expanded })
            }, shape = RoundedCornerShape(10.dp), colors = TextFieldDefaults.textFieldColors(
                backgroundColor = color,
                cursorColor = Custom_Blue,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ), textStyle = style
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
        ) {
            suggestions.forEach { label ->
                DropdownMenuItem(onClick = {
                    selectedText = label
                    expanded = false
                }) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                indication = null,
                                interactionSource = MutableInteractionSource()
                            ) {
                                if (isFrequencyChanged) {
                                    val provider = PreferenceProvider(context)
                                    expanded = !expanded
                                    selectedText = label
                                    val userId = provider.getIntValue(Constants.USER_ID, 0)
                                    val type = provider
                                        .getValue(Constants.TYPE, "")
                                    frequencyViewModel.screenQuestionStatus(
                                        screenQuestionStatusRequest = ScreenQuestionStatusRequest(
                                            type = type!!,
                                            user_id = userId,
                                            question_type = selectedText
                                        )
                                    )
                                    frequencyViewModel.screenQuestionStatus.observe(homeActivity) {
                                        if (it != null) {
                                            handleFrequencyChangedData(
                                                result = it,
                                                frequencyViewModel = frequencyViewModel,
                                                context = context,
                                                user_id = userId,
                                                type = type,
                                                questionViewModel = questionViewModel,
                                                homeActivity = homeActivity
                                            )
                                        }
                                    }
                                }
                            },
                        text = label,
                        style = MaterialTheme.typography.body2.copy(
                            fontWeight = FontWeight.W600,
                            fontSize = 16.sp,
                            color = Color.Black
                        ),
                    )
                }
            }
        }
    }
    return selectedText
}

private fun handleFrequencyChangedData(
    user_id: Int,
    type: String,
    questionViewModel: BottomQuestionViewModel,
    homeActivity: HomeActivity,
    result: NetworkResult<CommonResponse>,
    frequencyViewModel: FrequencyViewModel,
    context: Context,
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            frequencyViewModel.isLoading = true
        }
        is NetworkResult.Success -> {
            // bind data to the view
            updateFrequency(
                userId = user_id,
                questionViewModel = questionViewModel,
                type = type,
            )
            frequencyViewModel.isLoading = false
        }
        is NetworkResult.Error -> {
            // show error message
            frequencyViewModel.isLoading = false
            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
        }
    }
}