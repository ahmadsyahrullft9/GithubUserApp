package dicoding.submission.githubapi.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dicoding.submission.githubapi.models.DetailUser
import dicoding.submission.githubapi.models.User
import dicoding.submission.githubapi.models.NetState
import dicoding.submission.githubapi.repo.UserRepo

class UserViewModel : ViewModel() {

    var networkState: LiveData<NetState>
    var userListLiveData: LiveData<List<User>>
    var userDetailLiveData: LiveData<DetailUser>

    private var userRepo: UserRepo = UserRepo()

    init {
        userListLiveData = userRepo.userListLiveData
        networkState = userRepo.networkState
        userDetailLiveData = userRepo.userDetailLiveData
    }

    fun getUser() {
        userRepo.getUser()
    }

    fun getFollowers(username: String) {
        userRepo.getFollowers(username)
    }

    fun getFollowing(username: String) {
        userRepo.getFollowing(username)
    }

    fun searchUser(search_text: String) {
        userRepo.searchUser(search_text)
    }

    fun detailUser(username: String) {
        userRepo.detailUser(username)
    }
}