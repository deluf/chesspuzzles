package it.unipi.chesspuzzles.server.database;

import com.google.gson.Gson;

import it.unipi.chesspuzzles.server.shared.Favourite;

import javax.persistence.*;
import java.io.Serializable;

@SuppressWarnings("unused")
@Entity
@Table(name = "favourite", schema = "635502")
public class DatabaseFavourite implements Serializable {
// Implementation of Serializable interface is not strictly needed but reccomended

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "add_time", nullable = false)
    private Long addTime;

    @Column(name = "solved", nullable = false)
    private Boolean solved;

    @Column(name = "puzzle", columnDefinition = "TEXT", nullable = false)
    private String puzzle;

    public DatabaseFavourite(Favourite favourite) {
        Gson gson = new Gson();
        this.id = favourite.id;
        this.userId = favourite.userId;
        this.addTime = favourite.addTime;
        this.solved = favourite.solved;
        this.puzzle = gson.toJson(favourite.puzzle);
    }

    // Default constructor is required by jpa ...
    public DatabaseFavourite() {}

    // ... and so are getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }

    public Boolean getSolved() {
        return solved;
    }

    public void setSolved(Boolean solved) {
        this.solved = solved;
    }

    public String getPuzzle() {
        return this.puzzle;
    }

    public void setPuzzle(String puzzle) {
        this.puzzle = puzzle;
    }
}
