package dicoding.submission.githubapi.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import dicoding.submission.githubapi.models.User
import dicoding.submission.githubapi.repo.UserFavRepo

class UserFavViewModel(application: Application) : AndroidViewModel(application) {

    var userFavRepo: UserFavRepo
    var userFavs: LiveData<List<User>>

    init {
        userFavRepo = UserFavRepo(application)
        userFavs = userFavRepo.userFavs
    }

    fun insert(user: User) {
        userFavRepo.insert(user)
    }

    fun search_username(id: Int) {
        userFavRepo.search_user(id)
    }

    fun delete(user: User) {
        userFavRepo.delete(user)
    }

    fun getAllUserFavs(): LiveData<List<User>> {
        return userFavs
    }
}