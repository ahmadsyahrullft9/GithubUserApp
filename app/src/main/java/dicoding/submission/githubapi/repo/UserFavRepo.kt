package dicoding.submission.githubapi.repo

import android.app.Application
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dicoding.submission.githubapi.models.User
import dicoding.submission.githubapi.room.MyDb
import dicoding.submission.githubapi.room.UserFavDao

class UserFavRepo(application: Application) {

    private var userFavDao: UserFavDao

    val TAG = "UserFavRepo"

    var userFavs: LiveData<List<User>>

    var id: Int = 0

    init {
        val myDb =
            MyDb.getInstance(application)
        userFavDao = myDb!!.userFavDao()
        userFavs = if (id > 0) userFavDao.getFav(id) else userFavDao.getAllFavs()
    }

    fun insert(user: User) {
        InsertUserFav(userFavDao).execute(user)
    }

    fun delete(user: User) {
        DeleteUserFav(userFavDao).execute(user)
    }

    fun search_user(id: Int) {
        this.id = id
        Log.d(TAG, "search_user: " + id)
        userFavs = if (id > 0) userFavDao.getFav(id) else userFavDao.getAllFavs()
    }

    class DeleteUserFav(userFavDao: UserFavDao) : AsyncTask<User, Void, Void>() {

        var userFavDao: UserFavDao? = userFavDao

        override fun doInBackground(vararg users: User?): Void? {
            userFavDao?.delete(users[0]!!)
            return null
        }
    }

    class InsertUserFav(userFavDao: UserFavDao) : AsyncTask<User, Void, Void>() {

        var userFavDao: UserFavDao? = userFavDao

        override fun doInBackground(vararg users: User?): Void? {
            userFavDao?.insert(users[0]!!)
            return null
        }

    }
}