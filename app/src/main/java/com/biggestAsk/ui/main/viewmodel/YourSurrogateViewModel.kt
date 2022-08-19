package com.biggestAsk.ui.main.viewmodel

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.biggestAsk.data.model.request.InviteSurrogateRequest
import com.biggestAsk.data.model.response.InviteSurrogateResponse
import com.biggestAsk.data.repository.InviteSurrogateRepository
import com.biggestAsk.data.source.network.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class YourSurrogateViewModel @Inject constructor(
    private val inviteSurrogateRepository: InviteSurrogateRepository,
    application: Application
) : AndroidViewModel(application) {
    //Your Surrogate Mother
    var isSurrogateInvited: MutableState<Boolean> = mutableStateOf(false)
    var isSurrogateAdded: MutableState<Boolean> = mutableStateOf(false)
    var textSurrogateDialogEmail: MutableState<String> = mutableStateOf("")
    val invitationSend: MutableState<Boolean> = mutableStateOf(false)
    var inviteSurrogateResponse: MutableLiveData<NetworkResult<InviteSurrogateResponse>> =
        MutableLiveData()

    fun inviteSurrogate(userId: Int, inviteSurrogateRequest: InviteSurrogateRequest) {
        inviteSurrogateResponse.value = NetworkResult.Loading()
        viewModelScope.launch {
            inviteSurrogateRepository.inviteSurrogate(
                userId = userId,
                inviteSurrogateRequest = inviteSurrogateRequest
            ).collect {
                inviteSurrogateResponse.value = it
            }
        }
    }

}