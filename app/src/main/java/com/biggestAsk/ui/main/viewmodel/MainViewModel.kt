package com.biggestAsk.ui.main.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    var toolbarTittle: String by mutableStateOf("Home")

    //CommunityScreen
    val isCommunityScreen: MutableLiveData<Boolean> = MutableLiveData(false)

    var isEditable: MutableState<Boolean> = mutableStateOf(false)

    //Add Milestone Screen
    var isAddMilestoneScreen: MutableState<Boolean> = mutableStateOf(false)
    var isPermissionAllowed: MutableState<Boolean> = mutableStateOf(false)

    //Setting Screen
    val isSettingSubAboutAppScreen: MutableState<Boolean> = mutableStateOf(false)
    val isSettingSubDetailedSettingScreen: MutableState<Boolean> = mutableStateOf(false)
    val isSettingSubTermsOfServiceScreen: MutableState<Boolean> = mutableStateOf(false)
    val isSettingSubPrivacyPolicyScreen: MutableState<Boolean> = mutableStateOf(false)


}