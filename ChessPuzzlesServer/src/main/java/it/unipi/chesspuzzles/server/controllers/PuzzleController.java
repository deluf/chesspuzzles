package it.unipi.chesspuzzles.server.controllers;

import it.unipi.chesspuzzles.server.ChessComApi;
import it.unipi.chesspuzzles.server.GlobalExceptionHandler;
import it.unipi.chesspuzzles.server.shared.BoardStyle;
import it.unipi.chesspuzzles.server.shared.PiecesStyle;
import it.unipi.chesspuzzles.server.shared.Puzzle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
@RequestMapping(path = "/puzzles")
public class PuzzleController {

    private static final Logger logger = LogManager.getLogger(PuzzleController.class);

    @GetMapping(path = "/random")
    public @ResponseBody Puzzle random() {
        try {
            return ChessComApi.randomPuzzle();
        } catch (IOException | InterruptedException e) {
            logger.error("Unable to fetch random puzzle from chess.com's API -> {}", e.getMessage());
            throw new GlobalExceptionHandler.ChessComApiNotWorkingException();
        }
    }

    @GetMapping(path = "/draw", produces = "image/png")
    public @ResponseBody byte[] draw(@RequestParam String fen, @RequestParam BoardStyle board, @RequestParam PiecesStyle pieces) {
        try {
            return ChessComApi.dynboard(fen, board, pieces);
        } catch (IOException | InterruptedException e) {
            logger.error("Unable to fetch board image from chess.com's API -> {}", e.getMessage());
            throw new GlobalExceptionHandler.ChessComApiNotWorkingException();
        }
    }

}
