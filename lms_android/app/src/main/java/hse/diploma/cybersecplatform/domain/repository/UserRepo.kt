package hse.diploma.cybersecplatform.domain.repository

import android.content.ContentResolver
import android.net.Uri
import hse.diploma.cybersecplatform.data.model.UserData

interface UserRepo {
    suspend fun getUserProfile(): Result<UserData>

    suspend fun updateProfile(userData: UserData): Result<UserData>

    suspend fun uploadAvatar(
        contentUri: Uri,
        contentResolver: ContentResolver,
    ): Result<UserData>
}
