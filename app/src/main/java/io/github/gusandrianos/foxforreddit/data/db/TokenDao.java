package io.github.gusandrianos.foxforreddit.data.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.github.gusandrianos.foxforreddit.data.models.Token;

@Dao
public interface TokenDao {
    @Insert
    void insert(Token token);

    @Query("DELETE FROM token_table")
    void delete();

    @Query("SELECT * FROM token_table")
    List<Token> getToken();
}
