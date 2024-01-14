package it.unipi.chesspuzzles.server.shared;

@SuppressWarnings("unused")
public class PuzzleState {
    public String fen;
    public String correctMove;

    // Default constructor is required by gson
    public PuzzleState() {
    }

    public PuzzleState(String fen, String correctMove) {
        this.fen = fen;
        this.correctMove = correctMove;
    }
}
