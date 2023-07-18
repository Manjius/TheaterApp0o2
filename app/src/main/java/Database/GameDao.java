package Database;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface GameDao {
    @Query("SELECT * FROM game")
    List<Game> getAll();

    @Query("SELECT * FROM game ORDER BY RANDOM() LIMIT 1")
    Game getRandom();

    @Query("DELETE FROM game")
    void deleteAll();
    @Insert
    void insert(Game game);

    @Delete
    void delete(Game game);

    @Update
    void update(Game game);

    @Query("SELECT * FROM Game WHERE type = :type AND complexity = 'warmup' AND size <= :size ORDER BY RANDOM()")
    List<Game> getWarmupGames(String type, int size);

    @Query("SELECT * FROM Game WHERE type = :type AND complexity = 'full' AND size <= :size AND durationPerPerson <= :duration ORDER BY RANDOM()")
    List<Game> getMainGames(String type, int size, int duration);

    @Query("SELECT * FROM Game")
    List<Game> getAllGames();

    @Query("SELECT * FROM Game WHERE type = :type AND complexity = 'warmup' AND size <= :size")
    List<Game> getAllWarmupGames(String type, int size);

    @Query("SELECT * FROM Game WHERE type = :type AND complexity = 'full game' AND size <= :size")
    List<Game> getAllMainGames(String type, int size);

}
