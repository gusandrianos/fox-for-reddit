package io.github.gusandrianos.foxforreddit.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.github.gusandrianos.foxforreddit.data.models.Token

@Dao
interface TokenDao {

    @Insert
    fun insert(token: Token)

    @Query("DELETE FROM token_table")
    fun delete()

    @get:Query("SELECT * FROM token_table")
    val token: List<Token>?
}
