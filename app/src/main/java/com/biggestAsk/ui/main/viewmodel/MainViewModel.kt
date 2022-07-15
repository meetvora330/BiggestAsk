package com.biggestAsk.ui.main.viewmodel

import android.graphics.Bitmap
import androidx.compose.runtime.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.biggestAsk.ui.homeScreen.bottomDrawerNavGraph.MilestoneDetails
import com.biggestAsk.ui.questionScreen.ListQuestions
import com.biggestAsk.ui.questionScreen.Questions
import com.example.biggestAsk.R

class MainViewModel : ViewModel() {
    //Register Screen
    val selectedValueIntendParentRb = mutableStateOf(true)
    val selectedValueSurrogateMotherRb = mutableStateOf(false)

    //Otp Verification
    var isOtpValueVerified: Boolean by mutableStateOf(false)
    var ticks:Int by mutableStateOf(60)

    var textFullName: String by mutableStateOf("")
    var textEmail: String by mutableStateOf("")
    var isValidEmail: Boolean by mutableStateOf(false)
    var textPass: String by mutableStateOf("")
    var textReEnterPass: String by mutableStateOf("")
    var termCheckedState: Boolean by mutableStateOf(false)
    var isNameEmpty: Boolean by mutableStateOf(false)
    var isEmailEmpty: Boolean by mutableStateOf(false)
    var isPassEmpty: Boolean by mutableStateOf(false)
    var isRePassEmpty: Boolean by mutableStateOf(false)
    var loginTextEmail: String by mutableStateOf("")
    var loginTextPass: String by mutableStateOf("")
    var isLoginEmailEmpty: Boolean by mutableStateOf(false)
    var isLoginEmailValid: Boolean by mutableStateOf(false)
    var isLoginPassEmpty: Boolean by mutableStateOf(false)


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

    //Your Account Screen
    val isYourAccountScreen: MutableLiveData<Boolean> = MutableLiveData(false)
    var yourAccountFullName: MutableState<String> = mutableStateOf("Marina Kotenko")
    var yourAccountFullNameEmpty: Boolean by mutableStateOf(false)
    var yourAccountPhoneNumber: MutableState<String> = mutableStateOf("+880 9589876")
    var yourAccountPhoneNumberEmpty: Boolean by mutableStateOf(false)
    var yourAccountEmail: MutableState<String> = mutableStateOf("mailmarina.@mail.com")
    var yourAccountEmailIsValid: Boolean by mutableStateOf(false)
    var yourAccountEmailEmpty: Boolean by mutableStateOf(false)
    var yourAccountHomeAddress: MutableState<String> =
        mutableStateOf("888 Main St, Seattle, WA 98006")
    var yourAccountHomeAddressEmpty: Boolean by mutableStateOf(false)
    var yourAccountDateOfBirth: MutableState<String> = mutableStateOf("01/02/1988")
    var yourAccountDateOfBirthEmpty: Boolean by mutableStateOf(false)
    var yourAccountPartnerName: MutableState<String> = mutableStateOf("Tom Doe")
    var yourAccountPartnerNameEmpty: Boolean by mutableStateOf(false)
    var yourAccountPassword: MutableState<String> = mutableStateOf("...........")
    var yourAccountPasswordEmpty: Boolean by mutableStateOf(false)
    var isEditable: MutableState<Boolean> = mutableStateOf(false)

    //Add Milestone Screen
    var isAddMilestoneScreen: MutableState<Boolean> = mutableStateOf(false)
    var milestoneName: MutableState<String> = mutableStateOf("")
    var checkBoxShareWithParents: Boolean by mutableStateOf(true)
    var checkBoxShareWithBiggestAsk: Boolean by mutableStateOf(false)
    var addNewMilestoneNotes: MutableState<String> = mutableStateOf("")
    var addNewMilestoneLocation: MutableState<String> = mutableStateOf("")
    var btnSaveNotes: Boolean by mutableStateOf(false)

    //Add New Milestone BottomSheet
    var addNewMilestoneTittle: MutableState<String> = mutableStateOf("")
    var addNewMilestoneTittleEmpty: MutableState<Boolean> = mutableStateOf(false)
    var addNewMilestoneDate: MutableState<String> = mutableStateOf("")
    var addNewMilestoneDateEmpty: MutableState<Boolean> = mutableStateOf(false)
    var addNewMilestoneTime: MutableState<String> = mutableStateOf("")
    var addNewMilestoneTimeEmpty: MutableState<Boolean> = mutableStateOf(false)
    var addNewMilestoneLocationB: MutableState<String> = mutableStateOf("")
    var addNewMilestoneLocationBEmpty: MutableState<Boolean> = mutableStateOf(false)
    val imageList = mutableStateListOf<Bitmap>()
    var imageListIndex = mutableStateOf<Int>(-1)
    val tittleMilestone: MutableState<String> = mutableStateOf("")
    val timeMilestone: MutableState<String> = mutableStateOf("")


    //BottomSheetQuestion&HomeScreen
    var bottomQuesHome: String by mutableStateOf("")

    //Intended Parents
    var isFatherClicked: Boolean by mutableStateOf(true)
    var isMotherClicked: Boolean by mutableStateOf(false)

    var list by mutableStateOf(listOf<MilestoneDetails>())
    var isSelected: Boolean by mutableStateOf(false)
    var emptyList = mutableStateListOf<MilestoneDetails>(

    )
    var listData = mutableStateListOf<MilestoneDetails>(
        MilestoneDetails(
            "Milestone clearance exam",
            R.drawable.img_medical_calender_icon,
            "09/22/2021 at 9:30AM",
            R.drawable.img_milestone_location,
            false
        ),
        MilestoneDetails(
            "Embryo transfer",
            R.drawable.img_medical_calender_icon,
            "09/22/2021 at 9:30AM",
            R.drawable.img_milestone_location,
            false
        ),
        MilestoneDetails(
            "Beta test 1",
            R.drawable.img_medical_calender_icon,
            "09/22/2021 at 9:30AM",
            R.drawable.img_milestone_location,
            false
        ),
        MilestoneDetails(
            "Beta test 2",
            R.drawable.img_medical_calender_icon,
            "09/22/2021 at 9:30AM",
            R.drawable.img_milestone_location,
            false
        ),
        MilestoneDetails(
            "Beta test 3",
            R.drawable.img_medical_calender_icon,
            "09/22/2021 at 9:30AM",
            R.drawable.img_milestone_location,
            false
        )
    )

    //Notification & Notification Details
    val isNotificationScreen: MutableLiveData<Boolean> = MutableLiveData(false)
    val isNotificationDetailsScreen: MutableState<Boolean> = mutableStateOf(false)

    //Setting Screen
    val isSettingSubAboutAppScreen: MutableState<Boolean> = mutableStateOf(false)
    val isSettingSubDetailedSettingScreen: MutableState<Boolean> = mutableStateOf(false)
    val isSettingSubTermsOfServiceScreen: MutableState<Boolean> = mutableStateOf(false)
    val isSettingSubPrivacyPolicyScreen: MutableState<Boolean> = mutableStateOf(false)

    //Your Surrogate Mother
    var textSurrogateDialogPhoneNo = mutableStateOf("")
    val invitationSend: MutableState<Boolean> = mutableStateOf(false)



}