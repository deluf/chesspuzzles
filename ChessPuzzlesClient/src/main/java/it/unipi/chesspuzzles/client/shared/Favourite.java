package it.unipi.chesspuzzles.client.shared;

@SuppressWarnings("unused")
public class Favourite {
    public Integer id;
    public Integer userId;
    public Long addTime;
    public Boolean solved;
    public Puzzle puzzle;

    public Favourite(Integer userId, Long addTime, Boolean solved, Puzzle puzzle) {
        this.userId = userId;
        this.addTime = addTime;
        this.solved = solved;
        this.puzzle = puzzle;
    }

    // Default constructor is required by gson
    public Favourite() {};
}
