package com.biggestAsk.ui.homeScreen.drawerScreens.contactYourProvider

import com.example.biggestAsk.R

data class ContactYourProviderItems(
    val AIcon: Int,
    val title: String,
    val ATvRepName: String,
    val ARepName: String,
    val ATvRepEmail: String,
    val ARepEmail: String,
    val TvRepPhone: String,
    val APhoneNumber: String
)

val ListContactProviders = mutableListOf(
    ContactYourProviderItems(
        AIcon = R.drawable.img_contact_providers_item1,
        title = "The Happy Agency",
        ATvRepName = "Agency rep name",
        ARepName = "Jane Doe",
        ATvRepEmail = "Agency email",
        ARepEmail = "happyagency@gmail.com",
        TvRepPhone = "Agency phone number",
        APhoneNumber = "(222)-333-4444"
    ),
    ContactYourProviderItems(
        AIcon = R.drawable.img_contact_providers_item2,
        title = "The Happy Agency",
        ATvRepName = "Agency rep name",
        ARepName = "Jane Doe",
        ATvRepEmail = "Agency email",
        ARepEmail = "happyagency@gmail.com",
        TvRepPhone = "Agency phone number",
        APhoneNumber = "(222)-333-4444"
    ),
    ContactYourProviderItems(
        AIcon = R.drawable.img_contact_providers_item1,
        title = "The Happy Agency",
        ATvRepName = "Agency rep name",
        ARepName = "Jane Doe",
        ATvRepEmail = "Agency email",
        ARepEmail = "happyagency@gmail.com",
        TvRepPhone = "Agency phone number",
        APhoneNumber = "(222)-333-4444"
    )

)