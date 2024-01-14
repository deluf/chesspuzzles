package it.unipi.chesspuzzles.server.shared;

import com.google.gson.Gson;

import it.unipi.chesspuzzles.server.database.DatabaseFavourite;

@SuppressWarnings("unused")
public class Favourite {
    public Integer id;
    public Integer userId;
    public Long addTime;
    public Boolean solved;
    public Puzzle puzzle;

    public Favourite(DatabaseFavourite databaseFavourite) {
        Gson gson = new Gson();
        this.id = databaseFavourite.getId();
        this.userId = databaseFavourite.getUserId();
        this.addTime = databaseFavourite.getAddTime();
        this.solved = databaseFavourite.getSolved();
        this.puzzle = gson.fromJson(databaseFavourite.getPuzzle(), Puzzle.class);
    }

    // Default constructor is required by gson
    public Favourite() {}
}
