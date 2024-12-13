package data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import data.model.Userdata

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: Userdata)

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Int): Userdata?
    @Query("SELECT * FROM users LIMIT 1")
    suspend fun getUser(): Userdata?
    @Query("DELETE FROM users WHERE name = :username")
    suspend fun Delete(username: String)

    // Retrieve a user by their username
    @Query("SELECT * FROM users WHERE name = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): Userdata?
    // Delete all users
    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()

}
