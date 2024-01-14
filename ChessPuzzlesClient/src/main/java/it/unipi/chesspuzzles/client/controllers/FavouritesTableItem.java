package it.unipi.chesspuzzles.client.controllers;

import it.unipi.chesspuzzles.client.shared.Favourite;

import java.text.SimpleDateFormat;
import java.util.Date;

// Getters and setters are required by javafx's tableView
@SuppressWarnings("unused")
public class FavouritesTableItem {
    private Favourite favourite;
    private String title;
    private String publishTime;
    private String addTime;
    private boolean solved;

    public FavouritesTableItem(Favourite favourite) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        this.favourite = favourite;
        title = favourite.puzzle.title;
        publishTime = dateFormat.format(new Date(favourite.puzzle.publishTime * 1000));
        addTime = dateFormat.format(new Date(favourite.addTime * 1000));
        solved = favourite.solved;
    }

    public Favourite getFavourite() {
        return favourite;
    }

    public void setFavourite(Favourite favourite) {
        this.favourite = favourite;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }
}
