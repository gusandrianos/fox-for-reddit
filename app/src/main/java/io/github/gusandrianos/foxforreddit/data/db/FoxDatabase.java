package io.github.gusandrianos.foxforreddit.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import io.github.gusandrianos.foxforreddit.data.models.Token;

@Database(entities = Token.class, version = 1)
public abstract class FoxDatabase extends RoomDatabase {

    private static FoxDatabase instance;

    public abstract TokenDao tokenDao();

    public static synchronized FoxDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    FoxDatabase.class, "fox_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
