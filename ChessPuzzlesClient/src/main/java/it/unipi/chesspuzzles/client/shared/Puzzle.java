package it.unipi.chesspuzzles.client.shared;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class Puzzle {
    public String title;
    public Long publishTime;
    public ArrayList<PuzzleState> puzzleStates;

    // Default constructor is required by gson
    public Puzzle() {}
}
