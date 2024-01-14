package it.unipi.chesspuzzles.client;

import it.unipi.chesspuzzles.client.shared.*;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class ChessPuzzlesApi {

    public static final String SERVER_URL = "http://127.0.0.1:8080";

    /**
     * @return userId if the authentication was successful, 0 otherwise
     */
    public static int authenticate(String username, String password, AUTH_TYPE operation) throws IOException, InterruptedException {
        User user = new User(username, password);
        Gson gson = new Gson();
        String jsonUser = gson.toJson(user);

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL + "/users/" + operation.name().toLowerCase()))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonUser))
                .build();

        HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        if (response.statusCode() != 200) {
            throw new IOException("Unexpected status code " + response.statusCode());
        }

        return Integer.parseInt(response.body());
    }

    public static Favourite[] getFavourites(int userId) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL + "/users/" + userId + "/favourites/all"))
                .build();

        HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        if (response.statusCode() != 200) {
            throw new IOException("Unexpected status code " + response.statusCode());
        }

        Gson gson = new Gson();
        return gson.fromJson(response.body(), Favourite[].class);
    }

    public static void deleteFavourite(int userId, int favouriteId) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL + "/users/" + userId + "/favourites/" + favouriteId + "/delete"))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        if (response.statusCode() != 200) {
            throw new IOException("Unexpected status code " + response.statusCode());
        }
    }

    public static void markFavouriteAsSolved(int userId, int favouriteId) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL + "/users/" + userId + "/favourites/" + favouriteId + "/mark-as-solved"))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        if (response.statusCode() != 200) {
            throw new IOException("Unexpected status code " + response.statusCode());
        }
    }

    public static void markFavouriteAsToSolve(int userId, int favouriteId) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL + "/users/" + userId + "/favourites/" + favouriteId + "/mark-as-to-solve"))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        if (response.statusCode() != 200) {
            throw new IOException("Unexpected status code " + response.statusCode());
        }
    }

    public static Puzzle getRandomPuzzle() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL + "/puzzles/random"))
                .build();

        HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        if (response.statusCode() != 200) {
            throw new IOException("Unexpected status code " + response.statusCode());
        }

        Gson gson = new Gson();
        return gson.fromJson(response.body(), Puzzle.class);
    }

    public static void addPuzzleToFavourites(int userId, Puzzle puzzle) throws IOException, InterruptedException {
        Favourite favourite = new Favourite(
                userId, System.currentTimeMillis() / 1000, false, puzzle);
        Gson gson = new Gson();
        String jsonFavourite = gson.toJson(favourite);

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL + "/users/" + userId + "/favourites/add"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonFavourite))
                .build();

        HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        if (response.statusCode() != 200) {
            throw new IOException("Unexpected status code " + response.statusCode());
        }
    }

    public static byte[] drawBoard(String fen) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL + "/puzzles/draw"
                        + "?fen=" + URLEncoder.encode(fen, StandardCharsets.UTF_8)
                        + "&board=" + BoardStyle.GREEN
                        + "&pieces=" + PiecesStyle.NEO))
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

    public static void addAttemp(int userId, Attemp.TYPE type) throws IOException, InterruptedException {
        Attemp attemp = new Attemp(userId, LocalDateTime.now(), type);
        Gson gson = new Gson();
        String jsonAttemp = gson.toJson(attemp);

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL + "/users/" + userId + "/attemps/add"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonAttemp))
                .build();

        HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        if (response.statusCode() != 200) {
            throw new IOException("Unexpected status code " + response.statusCode());
        }
    }

    public static Attemp[] getAttempsAfter(int userId, LocalDateTime timestamp) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL + "/users/" + userId + "/attemps/after?timestamp=" + timestamp))
                .build();

        HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        if (response.statusCode() != 200) {
            throw new IOException("Unexpected status code " + response.statusCode());
        }

        Gson gson = new Gson();
        return gson.fromJson(response.body(), Attemp[].class);
    }

    public enum AUTH_TYPE {
        LOGIN,
        REGISTER
    }

}
