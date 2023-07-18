package Database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity
public class Game implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String description;
    private String type;
    private int size;
    private int durationPerPerson;
    private String complexity;

    // Default constructor
    public Game() {
    }

    // New constructor
    public Game(String name, String description, String type, int size, int durationPerPerson, String complexity) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.size = size;
        this.durationPerPerson = durationPerPerson;
        this.complexity = complexity;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public int getSize() {
        return size;
    }

    public int getDurationPerPerson() {
        return durationPerPerson;
    }

    public String getComplexity() {
        return complexity;
    }



    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setDurationPerPerson(int durationPerPerson) {
        this.durationPerPerson = durationPerPerson;
    }

    public void setComplexity(String complexity) {
        this.complexity = complexity;
    }

}
