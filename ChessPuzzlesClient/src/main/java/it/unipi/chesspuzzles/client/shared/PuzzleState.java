package it.unipi.chesspuzzles.client.shared;

@SuppressWarnings("unused")
public class PuzzleState {
    public String fen;
    public String correctMove;

    // Default constructor is required by gson
    public PuzzleState() {}
}
