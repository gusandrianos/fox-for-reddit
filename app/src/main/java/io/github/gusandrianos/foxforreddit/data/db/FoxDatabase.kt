package io.github.gusandrianos.foxforreddit.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.gusandrianos.foxforreddit.data.models.Token

@Database(entities = [Token::class], version = 1)
abstract class FoxDatabase : RoomDatabase() {

    abstract fun tokenDao(): TokenDao
}
