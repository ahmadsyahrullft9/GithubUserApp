package dicoding.submission.githubapi.models

class NetState(val status: NetStatus, val message: String) {

    companion object {
        val SUCCESS: NetState
        val LOADING: NetState
        val ERROR: NetState

        init {
            SUCCESS =
                NetState(
                    NetStatus.SUCCESS,
                    "request berhasil"
                )
            LOADING =
                NetState(
                    NetStatus.RUNNING,
                    "request sedang berjalan"
                )
            ERROR =
                NetState(
                    NetStatus.FAILED,
                    "terjadi kesalahan"
                )
        }
    }
}