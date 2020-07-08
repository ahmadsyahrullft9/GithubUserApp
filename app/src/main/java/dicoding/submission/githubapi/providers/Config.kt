package dicoding.submission.githubapi.providers

import android.net.Uri
import dicoding.submission.githubapi.room.Config

object Config {

    val AUTHORITY = "dicoding.submission.githubapi"

    val USER_CONTENT_URI: Uri =
        Uri.Builder().scheme("content").authority(AUTHORITY).appendPath(Config.USER_TABLE).build()
}