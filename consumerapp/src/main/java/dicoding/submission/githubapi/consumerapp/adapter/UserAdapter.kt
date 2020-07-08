package dicoding.submission.githubuserapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dicoding.submission.githubapi.consumerapp.R
import dicoding.submission.githubapi.consumerapp.models.CrudState
import dicoding.submission.githubapi.consumerapp.models.User
import kotlinx.android.synthetic.main.item_user.view.*

class UserAdapter(
    private var context: Context?,
    private var userList: List<User>,
    listener: Listener
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var listener: Listener? = listener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun setData(userList: List<User>) {
        this.userList = userList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.onBind(user)
        holder.itemView.setOnClickListener({
            listener?.callback(user, CrudState.READ)
        })
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("CheckResult")
        fun onBind(user: User) {
            itemView.txt_title.text = user.login
            itemView.txt_subtitle.text = user.htmlUrl

            val requestOption = RequestOptions()
            requestOption.placeholder(R.drawable.placeholder_user)
            requestOption.error(R.drawable.placeholder_user)

            Glide.with(itemView.context)
                .load(user.avatarUrl)
                .apply(requestOption)
                .into(itemView.iv_item)

        }
    }

    interface Listener {
        fun callback(user: User, crudState: CrudState)
    }
}