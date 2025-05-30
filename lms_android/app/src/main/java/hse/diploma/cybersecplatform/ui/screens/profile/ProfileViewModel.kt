package hse.diploma.cybersecplatform.ui.screens.profile

import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hse.diploma.cybersecplatform.data.model.UserData
import hse.diploma.cybersecplatform.domain.model.ErrorType
import hse.diploma.cybersecplatform.domain.repository.UserRepo
import hse.diploma.cybersecplatform.extensions.toErrorType
import hse.diploma.cybersecplatform.ui.state.ProfileState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepo,
) : ViewModel() {
    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Loading)
    val profileState: StateFlow<ProfileState> = _profileState.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            _profileState.value = ProfileState.Loading
            val result = userRepository.getUserProfile()
            if (result.isSuccess) {
                val user = result.getOrNull()!!
                _profileState.value =
                    ProfileState.Success(
                        ProfileUiState(
                            userData = user,
                        ),
                    )
            } else {
                _profileState.value = ProfileState.Error(result.exceptionOrNull()?.toErrorType(TAG) ?: ErrorType.Other)
            }
        }
    }

    fun updateProfile(
        username: String,
        fullName: String,
        email: String,
    ) {
        val currentImage = (profileState.value as? ProfileState.Success)?.uiState?.userData?.profileImage

        viewModelScope.launch {
            _profileState.value = ProfileState.Loading
            val data =
                UserData(
                    username = username,
                    fullName = fullName,
                    email = email,
                    profileImage = currentImage,
                )
            val result = userRepository.updateProfile(data)
            if (result.isSuccess) {
                val user = result.getOrNull()!!
                _profileState.value =
                    ProfileState.Success(
                        ProfileUiState(
                            userData = user,
                        ),
                    )
            } else {
                _profileState.value =
                    ProfileState.Error(
                        result.exceptionOrNull()?.toErrorType(TAG) ?: ErrorType.Other,
                    )
            }
        }
    }

    fun uploadPhoto(
        avatarUri: Uri?,
        contentResolver: ContentResolver,
    ) {
        if (avatarUri == null) return
        viewModelScope.launch {
            _profileState.value = ProfileState.Loading
            val result = userRepository.uploadAvatar(avatarUri, contentResolver)
            if (result.isSuccess) {
                loadProfile()
                val user = result.getOrNull()!!
                _profileState.value =
                    ProfileState.Success(
                        ProfileUiState(
                            userData = user,
                        ),
                    )
            } else {
                _profileState.value =
                    ProfileState.Error(
                        result.exceptionOrNull()?.toErrorType(TAG) ?: ErrorType.Other,
                    )
            }
        }
    }

    companion object {
        private const val TAG = "ProfileViewModel"
    }
}

data class ProfileUiState(
    val userData: UserData,
)
