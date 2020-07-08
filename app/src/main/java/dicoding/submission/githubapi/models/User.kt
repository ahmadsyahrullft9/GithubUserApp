package dicoding.submission.githubapi.models


import android.content.ContentValues
import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import dicoding.submission.githubapi.room.Config


@Entity(tableName = Config.USER_TABLE)
class User() : Parcelable {

    @SerializedName("avatar_url")
    @ColumnInfo(name = Config.USER_COLUMN_AVATARURL)
    var avatarUrl: String? = null

    @SerializedName("events_url")
    var eventsUrl: String? = null

    @SerializedName("followers_url")
    var followersUrl: String? = null

    @SerializedName("following_url")
    var followingUrl: String? = null

    @SerializedName("gists_url")
    var gistsUrl: String? = null

    @SerializedName("gravatar_id")
    var gravatarId: String? = null

    @SerializedName("html_url")
    @ColumnInfo(name = Config.USER_COLUMN_HTMLURL)
    var htmlUrl: String? = null

    @PrimaryKey
    @SerializedName("id")
    @ColumnInfo(index = true, name = Config.USER_COLUMN_ID)
    var id: Int? = null

    @SerializedName("login")
    @ColumnInfo(name = Config.USER_COLUMN_LOGIN)
    var login: String? = null

    @SerializedName("node_id")
    var nodeId: String? = null

    @SerializedName("organizations_url")
    var organizationsUrl: String? = null

    @SerializedName("received_events_url")
    var receivedEventsUrl: String? = null

    @SerializedName("repos_url")
    var reposUrl: String? = null

    @SerializedName("site_admin")
    var siteAdmin: Boolean? = null

    @SerializedName("starred_url")
    var starredUrl: String? = null

    @SerializedName("subscriptions_url")
    var subscriptionsUrl: String? = null

    @SerializedName("type")
    var type: String? = null

    @SerializedName("url")
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

        fun fromContentValues(values: ContentValues): User? {
            val user = User()
            if (values.containsKey(Config.USER_COLUMN_ID)) {
                user.id = values.getAsInteger(Config.USER_COLUMN_ID)
            }
            if (values.containsKey(Config.USER_COLUMN_LOGIN)) {
                user.login = values.getAsString(Config.USER_COLUMN_LOGIN)
            }
            return user
        }
    }
}