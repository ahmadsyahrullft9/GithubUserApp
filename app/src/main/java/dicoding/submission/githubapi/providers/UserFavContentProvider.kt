package dicoding.submission.githubapi.providers

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import dicoding.submission.githubapi.models.User
import dicoding.submission.githubapi.room.MyDb
import dicoding.submission.githubapi.room.UserFavDao


class UserFavContentProvider : ContentProvider() {

    companion object {

        private const val _DIR = 1
        private const val _ITEM = 2
        private val MATCHER = UriMatcher(UriMatcher.NO_MATCH)

        init {
            val TABLE_NAME = dicoding.submission.githubapi.room.Config.USER_TABLE
            MATCHER.addURI(Config.AUTHORITY, TABLE_NAME, _DIR)
            MATCHER.addURI(Config.AUTHORITY, "$TABLE_NAME/#", _ITEM)
        }
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        val code = MATCHER.match(uri)
        val context = context ?: return null
        val userFavDao: UserFavDao = MyDb.getInstance(context)!!.userFavDao()
        when (code) {
            _DIR -> return userFavDao.selectAll()
            _ITEM -> return userFavDao.selectById(ContentUris.parseId(uri).toInt())
        }
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val cUri = Config.USER_CONTENT_URI
        val context = context ?: return 0
        val delete: Int = when (MATCHER.match(uri)) {
            _ITEM -> MyDb.getInstance(context)?.userFavDao()!!
                .deleteById(ContentUris.parseId(uri).toInt())
            else -> 0
        }

        context.contentResolver?.notifyChange(cUri, null)

        return delete
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val cUri = Config.USER_CONTENT_URI
        val context = context ?: return null
        val inserted: Long = when (MATCHER.match(uri)) {
            _DIR -> MyDb.getInstance(context)?.userFavDao()!!
                .insert(User.fromContentValues(values!!)!!)
            else -> 0
        }

        context.contentResolver?.notifyChange(cUri, null)

        return Uri.parse("$cUri/$inserted")
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        val cUri = Config.USER_CONTENT_URI
        val context = context ?: return 0
        val update: Int = when (_ITEM) {
            MATCHER.match(uri) -> MyDb.getInstance(context)?.userFavDao()!!
                .update(User.fromContentValues(values!!)!!)
            else -> 0
        }

        context.contentResolver?.notifyChange(cUri, null)

        return update
    }
}
