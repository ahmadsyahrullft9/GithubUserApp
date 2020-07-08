package dicoding.submission.githubapi.consumerapp.providers

import android.net.Uri

object Config {

    val AUTHORITY = "dicoding.submission.githubapi"

    const val USER_TABLE = "fav_user"
    const val USER_COLUMN_ID = "id"
    const val USER_COLUMN_LOGIN = "login"
    const val USER_COLUMN_HTMLURL = "htmlUrl"
    const val USER_COLUMN_AVATARURL = "avatarUrl"

    val USER_CONTENT_URI: Uri =
        Uri.Builder().scheme("content").authority(AUTHORITY).appendPath(USER_TABLE).build()
}