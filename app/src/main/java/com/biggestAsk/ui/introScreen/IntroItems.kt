package com.biggestAsk.ui.introScreen

import com.example.biggestAsk.R

data class SampleOnBoard(
    val image: Int,
    val tittle: String = "",
    val description: String
)

val onBoardItem = listOf(
    SampleOnBoard(
        image = R.drawable.ic_img_intro_1,
        tittle = "We help manage the \n surrogacy process",
        description = "One place to hold the contact information of\n your fertility clinic, surrogacy agency,\n surrogacy lawyer and/or ObGyn."
    ),
    SampleOnBoard(
        image = R.drawable.ic_img_intro_2,
        tittle = "Get notified about \n upcoming appointments",
        description = "Enter, share and receive dates and\ninformation related to important upcoming\n appointments related to your surrogacy\njourney"
    ), SampleOnBoard(
        image = R.drawable.ic_img_intro_3,
        tittle = "Get to know your surrogacy \n journey partner",
        description = "As the journey unfolds, answer important\nand personal questions about yourself\nwhich will then be shared with your\nsurrogacy partner to help establish a\nmeaningful, special bond"
    ),
    SampleOnBoard(
        image = R.drawable.ic_img_intro_04,
        tittle = "Join a Community",
        description = "Either through The Biggest Ask\ncommunity or through your agency's\ncommunity, stay connected and ask our\nmembers any questions you have about\nthe journey. "
    )
)