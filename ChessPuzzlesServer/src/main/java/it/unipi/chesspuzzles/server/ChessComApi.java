package it.unipi.chesspuzzles.server;

import com.google.gson.Gson;

import it.unipi.chesspuzzles.server.shared.BoardStyle;
import it.unipi.chesspuzzles.server.shared.PiecesStyle;
import it.unipi.chesspuzzles.server.shared.Puzzle;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class ChessComApi {
    public static Puzzle randomPuzzle() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.chess.com/pub/puzzle/random"))
                .build();

        HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        if (response.statusCode() != 200) {
            throw new IOException("Unexpected status code " + response.statusCode());
        }

        Gson gson = new Gson();
        ChessComApiResponse apiResponse = gson.fromJson(response.body(), ChessComApiResponse.class);

        return new Puzzle(
                apiResponse.title,
                apiResponse.publish_time,
                apiResponse.fen,
                apiResponse.pgn
        );
    }

    public static byte[] dynboard(String fen, BoardStyle board, PiecesStyle pieces) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://www.chess.com/dynboard"
                        + "?fen=" + URLEncoder.encode(fen, StandardCharsets.UTF_8)
                        + "&board=" + board.name().toLowerCase()
                        + "&pieces=" + pieces.name().toLowerCase()
                        + "&size=2"))
                .build();

        HttpResponse<byte[]> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofByteArray()
        );

        if (response.statusCode() != 200) {
            throw new IOException("Unexpected status code " + response.statusCode());
        }

        return response.body();
    }

}
