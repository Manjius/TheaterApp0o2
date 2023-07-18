package Database;
import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Game.class}, version = 6)
public abstract class AppDatabase extends RoomDatabase {
    public abstract GameDao gameDao();
}
