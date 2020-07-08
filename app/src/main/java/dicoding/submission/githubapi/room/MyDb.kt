package dicoding.submission.githubapi.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dicoding.submission.githubapi.models.User

@Database(entities = arrayOf(User::class), version = 1, exportSchema = false)
abstract class MyDb : RoomDatabase() {

    abstract fun userFavDao(): UserFavDao

    companion object {
        private var instance: MyDb? = null

        @Synchronized
        fun getInstance(context: Context): MyDb? {
            if (instance == null) {
                instance =
                    Room.databaseBuilder<MyDb>(context.getApplicationContext(), MyDb::class.java, Config.DBNAME)
                        .fallbackToDestructiveMigration()
                        .build()
            }
            return instance
        }
    }
}