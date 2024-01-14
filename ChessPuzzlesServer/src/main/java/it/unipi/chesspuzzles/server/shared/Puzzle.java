package it.unipi.chesspuzzles.server.shared;

import com.github.bhlangonijr.chesslib.Board;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class Puzzle {

    private static final Logger logger = LogManager.getLogger(Puzzle.class);

    public String title;
    public Long publishTime;
    public ArrayList<PuzzleState> puzzleStates;

    // Default constructor is required by gson
    public Puzzle() {}

    public Puzzle(String title, Long publishTime, String fen, String pgn) {
        this.title = title;
        this.publishTime = publishTime;

        Board board = new Board();
        board.loadFromFen(fen);
        ArrayList<String> moves = parseMovesFromPgn(pgn);

        puzzleStates = new ArrayList<>();
        for (String move : moves) {
            puzzleStates.add(new PuzzleState(board.getFen(), move));
            board.doMove(move);
        }
        puzzleStates.add(new PuzzleState(board.getFen(), "#"));
    }

    /**
     * Only parses the first line of moves, which *should* be sufficient for a puzzle
     */
    public static ArrayList<String> parseMovesFromPgn(String pgn) {
        Pattern movesLinePattern = Pattern.compile("(?<=\\r\\n\\r\\n).+", Pattern.MULTILINE);
        Matcher movesLineMatcher = movesLinePattern.matcher(pgn);
        String movesLine = movesLineMatcher.find() ? movesLineMatcher.group() : null;

        if (movesLine == null) {
            logger.fatal("Unable to parse puzzle moves -> {}", pgn);
            throw new RuntimeException("Unable to parse puzzle moves");
        }

        Pattern movesPattern = Pattern.compile("([^- .0-9]\\w+[+#]?)+");
        Matcher movesMatcher = movesPattern.matcher(movesLine);
        ArrayList<String> moves = new ArrayList<>();

        while (movesMatcher.find()) {
            moves.add(movesMatcher.group());
        }
        return moves;
    }

}
