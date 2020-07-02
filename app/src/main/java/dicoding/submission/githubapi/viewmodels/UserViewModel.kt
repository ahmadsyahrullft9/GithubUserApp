package dicoding.submission.githubapi.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dicoding.submission.githubapi.models.DetailUser
import dicoding.submission.githubapi.models.SearchResponse
import dicoding.submission.githubapi.models.User
import dicoding.submission.githubapi.network.ApiClient
import dicoding.submission.githubapi.network.NetState
import dicoding.submission.githubapi.network.NetStatus
import dicoding.submission.githubapi.apiinterface.UserApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel : ViewModel() {

    val TAG = this.javaClass.simpleName

    private var detailUserCall: Call<DetailUser>? = null
    private var getUserCall: Call<List<User>>? = null
    private var searchUserCall: Call<SearchResponse>? = null

    private val netState = MutableLiveData<NetState>()
    val networkState: LiveData<NetState>
        get() = netState

    private val userListMudata = MutableLiveData<List<User>>()
    val userListLiveData: LiveData<List<User>>
        get() = userListMudata

    private val userDetailMudata = MutableLiveData<DetailUser>()
    val userDetailLiveData: LiveData<DetailUser>
        get() = userDetailMudata

    fun getUser() {
        getUserCall = ApiClient.githubApi().create(UserApi::class.java).get_user()
        request_listuser()
    }

    fun getFollowers(username: String) {
        getUserCall = ApiClient.githubApi().create(UserApi::class.java).get_followers(username)
        request_listuser()
    }

    fun getFollowing(username: String) {
        getUserCall = ApiClient.githubApi().create(UserApi::class.java).get_following(username)
        request_listuser()
    }

    private fun request_listuser() {
        netState.postValue(NetState.LOADING)
        getUserCall!!.enqueue(object : Callback<List<User>> {
            override fun onResponse(
                call: Call<List<User>>,
                response: Response<List<User>>
            ) {
                if (!call.isCanceled) {
                    if (response.isSuccessful) {
                        if (response.body()!!.size > 0) {
                            netState.postValue(NetState.SUCCESS)
                            userListMudata.postValue(response.body())
                        } else {
                            netState.postValue(
                                NetState(
                                    NetStatus.FAILED,
                                    "data tidak ditemukan"
                                )
                            )
                            userListMudata.postValue(ArrayList())
                        }
                    } else {
                        //pesan error
                        val msg =
                            if (response.code() == 404) "data tidak ditemukan" else "terjadi kesalahan saat request data"
                        //pesan error
                        netState.postValue(
                            NetState(
                                NetStatus.FAILED,
                                msg
                            )
                        )
                        userListMudata.postValue(ArrayList())
                    }
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                //pesan error
                Log.d(TAG, t.message.toString())
                netState.postValue(NetState(NetStatus.FAILED, "terjadi kesalahan, periksa pengaturan jaringan anda"))
                userListMudata.postValue(ArrayList())
            }

        })
    }

    fun searchUser(search_text: String) {
        netState.postValue(NetState.LOADING)
        searchUserCall = ApiClient.githubApi().create(UserApi::class.java)
            .search_user(search_text)
        searchUserCall!!.enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                if (!call.isCanceled) {
                    if (response.isSuccessful) {
                        val searchResponse: SearchResponse = response.body()!!
                        if (searchResponse.totalCount > 0 && searchResponse.users.size > 0) {
                            netState.postValue(NetState.SUCCESS)
                            userListMudata.postValue(searchResponse.users)
                        } else {
                            //pesan kosong
                            netState.postValue(
                                NetState(
                                    NetStatus.FAILED,
                                    "data user tidak ditemukan"
                                )
                            )
                            userListMudata.postValue(ArrayList())
                        }
                    } else {
                        //pesan error
                        val msg =
                            if (response.code() == 404) "data tidak ditemukan" else "terjadi kesalahan saat request data"
                        //pesan error
                        netState.postValue(
                            NetState(
                                NetStatus.FAILED,
                                msg
                            )
                        )
                        userListMudata.postValue(ArrayList())
                    }
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                //pesan error
                Log.d(TAG, t.message.toString())
                netState.postValue(NetState(NetStatus.FAILED, "terjadi kesalahan, periksa pengaturan jaringan anda"))
                userListMudata.postValue(ArrayList())
            }

        })
    }

    fun detailUser(username: String) {
        netState.postValue(NetState.LOADING)
        detailUserCall = ApiClient.githubApi().create(UserApi::class.java)
            .detail_user(username)
        detailUserCall!!.enqueue(object : Callback<DetailUser> {
            override fun onResponse(call: Call<DetailUser>, response: Response<DetailUser>) {
                if (!call.isCanceled) {
                    if (response.isSuccessful) {
                        netState.postValue(NetState.SUCCESS)
                        userDetailMudata.postValue(response.body())
                    } else {
                        val msg =
                            if (response.code() == 404) "data tidak ditemukan" else "terjadi kesalahan saat request data"
                        //pesan error
                        netState.postValue(
                            NetState(
                                NetStatus.FAILED,
                                msg
                            )
                        )
                    }
                }
            }

            override fun onFailure(call: Call<DetailUser>, t: Throwable) {
                Log.d(TAG, t.message.toString())
                netState.postValue(NetState(NetStatus.FAILED, "terjadi kesalahan, periksa pengaturan jaringan anda"))
            }
        })
    }
}