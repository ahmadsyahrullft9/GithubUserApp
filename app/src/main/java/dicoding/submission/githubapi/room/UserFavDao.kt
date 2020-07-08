package dicoding.submission.githubapi.room

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import dicoding.submission.githubapi.models.User


@Dao
interface UserFavDao {

    @Insert
    fun insert(user: User): Long

    @Update
    fun update(user: User): Int

    @Delete
    fun delete(user: User): Int

    @Query("DELETE FROM fav_user")
    fun deleteAllFav()

    @Query("DELETE FROM " + Config.USER_TABLE + " WHERE " + Config.USER_COLUMN_ID + " = :id")
    fun deleteById(id: Int): Int

    @Query("SELECT * FROM fav_user ORDER BY id ASC")
    fun getAllFavs(): LiveData<List<User>>

    @Query("SELECT * FROM fav_user WHERE id LIKE :id")
    fun getFav(id: Int): LiveData<List<User>>

    @Query("SELECT * FROM fav_user ORDER BY id ASC")
    fun selectAll(): Cursor

    @Query("SELECT * FROM fav_user WHERE id LIKE :id")
    fun selectById(id: Int): Cursor
}