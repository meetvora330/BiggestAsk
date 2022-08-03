package com.biggestAsk.ui.main.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    //Register Screen


    //Otp Verification


    var isNameEmpty: Boolean by mutableStateOf(false)


    var toolbarTittle: String by mutableStateOf("Home")

    //Bottom-Nav-Bar
    var bottomNavAddQues: String by mutableStateOf("")

    //CommunityScreen
    val isCommunityScreen: MutableLiveData<Boolean> = MutableLiveData(false)

    //Contact Providers Screen
    val isContactProvidersScreen: MutableLiveData<Boolean> = MutableLiveData(false)

    //Add New Community Screen
    val communityTittle: MutableLiveData<String> = MutableLiveData("")
    val communityDescription: MutableLiveData<String> = MutableLiveData("")
    val communityLinkForum: MutableLiveData<String> = MutableLiveData("")
    val communityLinkInstagram: MutableLiveData<String> = MutableLiveData("")

    var isEditable: MutableState<Boolean> = mutableStateOf(false)

    //Add Milestone Screen
    var isAddMilestoneScreen: MutableState<Boolean> = mutableStateOf(false)
    var isPermissionAllowed: MutableState<Boolean> = mutableStateOf(false)
    var milestoneName: MutableState<String> = mutableStateOf("")


    var addNewMilestoneLocation: MutableState<String> = mutableStateOf("")
    var btnSaveNotes: Boolean by mutableStateOf(false)

    //Add New Milestone BottomSheet

    val tittleMilestone: MutableState<String> = mutableStateOf("")
    val timeMilestone: MutableState<String> = mutableStateOf("")


    //BottomSheetQuestion&HomeScreen
    var bottomQuesHome: String by mutableStateOf("")

    //Intended Parents
    var isFatherClicked: Boolean by mutableStateOf(true)
    var isMotherClicked: Boolean by mutableStateOf(false)

    //Notification & Notification Details
    val isNotificationScreen: MutableLiveData<Boolean> = MutableLiveData(false)
    val isNotificationDetailsScreen: MutableState<Boolean> = mutableStateOf(false)

    //Setting Screen
    val isSettingSubAboutAppScreen: MutableState<Boolean> = mutableStateOf(false)
    val isSettingSubDetailedSettingScreen: MutableState<Boolean> = mutableStateOf(false)
    val isSettingSubTermsOfServiceScreen: MutableState<Boolean> = mutableStateOf(false)
    val isSettingSubPrivacyPolicyScreen: MutableState<Boolean> = mutableStateOf(false)


}