package dicoding.submission.githubapi.consumerapp.models


import android.os.Parcel
import android.os.Parcelable

class User() : Parcelable {

    var avatarUrl: String? = null

    var eventsUrl: String? = null

    var followersUrl: String? = null

    var followingUrl: String? = null

    var gistsUrl: String? = null

    var gravatarId: String? = null

    var htmlUrl: String? = null

    var id: Int? = null

    var login: String? = null

    var nodeId: String? = null

    var organizationsUrl: String? = null

    var receivedEventsUrl: String? = null

    var reposUrl: String? = null

    var siteAdmin: Boolean? = null

    var starredUrl: String? = null

    var subscriptionsUrl: String? = null

    var type: String? = null

    var url: String? = null

    constructor(parcel: Parcel) : this() {
        avatarUrl = parcel.readString()
        eventsUrl = parcel.readString()
        followersUrl = parcel.readString()
        followingUrl = parcel.readString()
        gistsUrl = parcel.readString()
        gravatarId = parcel.readString()
        htmlUrl = parcel.readString()
        id = parcel.readValue(Int::class.java.classLoader) as? Int
        login = parcel.readString()
        nodeId = parcel.readString()
        organizationsUrl = parcel.readString()
        receivedEventsUrl = parcel.readString()
        reposUrl = parcel.readString()
        siteAdmin = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        starredUrl = parcel.readString()
        subscriptionsUrl = parcel.readString()
        type = parcel.readString()
        url = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(avatarUrl)
        parcel.writeString(eventsUrl)
        parcel.writeString(followersUrl)
        parcel.writeString(followingUrl)
        parcel.writeString(gistsUrl)
        parcel.writeString(gravatarId)
        parcel.writeString(htmlUrl)
        parcel.writeValue(id)
        parcel.writeString(login)
        parcel.writeString(nodeId)
        parcel.writeString(organizationsUrl)
        parcel.writeString(receivedEventsUrl)
        parcel.writeString(reposUrl)
        parcel.writeValue(siteAdmin)
        parcel.writeString(starredUrl)
        parcel.writeString(subscriptionsUrl)
        parcel.writeString(type)
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}