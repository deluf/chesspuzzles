package it.unipi.chesspuzzles.server.controllers;

import com.google.gson.Gson;

import it.unipi.chesspuzzles.server.GlobalExceptionHandler;
import it.unipi.chesspuzzles.server.database.DatabaseFavourite;
import it.unipi.chesspuzzles.server.database.FavouriteRepository;
import it.unipi.chesspuzzles.server.shared.Favourite;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping(path = "/users/{userId}/favourites")
public class FavouriteController {

    private static final Logger logger = LogManager.getLogger(UserController.class);

    private final FavouriteRepository favouriteRepository;

    public FavouriteController(FavouriteRepository favouriteRepository) {
        this.favouriteRepository = favouriteRepository;
    }

    @GetMapping(path = "/all")
    public @ResponseBody List<Favourite> all(@PathVariable Integer userId) {
        Iterable<DatabaseFavourite> dbFavourites = favouriteRepository.findAllByUserId(userId);
        ArrayList<Favourite> favourites = new ArrayList<>();
        for (DatabaseFavourite dbFavourite : dbFavourites) {
            favourites.add(new Favourite(dbFavourite));
        }
        return favourites;
    }

    @PostMapping(path = "/add")
    public @ResponseBody void add(@PathVariable Integer userId, @RequestBody Favourite favourite) {
        if (!Objects.equals(favourite.userId, userId)) {
            throw new GlobalExceptionHandler.UnauthorizedException();
        }
        favouriteRepository.save(new DatabaseFavourite(favourite));
    }

    private DatabaseFavourite findFavourite(Integer userId, Integer favouriteId) {
        Optional<DatabaseFavourite> optionalDbFavourite = favouriteRepository.findById(favouriteId);
        if (optionalDbFavourite.isEmpty()) {
            return null;
        }

        DatabaseFavourite dbFavourite = optionalDbFavourite.get();
        if (!Objects.equals(dbFavourite.getUserId(), userId)) {
            throw new GlobalExceptionHandler.UnauthorizedException();
        }

        return dbFavourite;
    }

    @PostMapping(path = "/{favouriteId}/delete")
    public @ResponseBody void delete(@PathVariable Integer userId, @PathVariable Integer favouriteId) {
        if (findFavourite(userId, favouriteId) == null) {
            return;
        }

        favouriteRepository.deleteById(favouriteId);
    }

    @PostMapping(path = "/{favouriteId}/mark-as-solved")
    public @ResponseBody void markAsSolved(@PathVariable Integer userId, @PathVariable Integer favouriteId) {
        DatabaseFavourite dbFavourite = findFavourite(userId, favouriteId);
        if (dbFavourite == null) {
            return;
        }

        if (dbFavourite.getSolved()) {
            return;
        }

        dbFavourite.setSolved(true);
        favouriteRepository.save(dbFavourite);
    }

    @PostMapping(path = "/{favouriteId}/mark-as-to-solve")
    public @ResponseBody void markAsToSolve(@PathVariable Integer userId, @PathVariable Integer favouriteId) {
        DatabaseFavourite dbFavourite = findFavourite(userId, favouriteId);
        if (dbFavourite == null) {
            return;
        }

        if (!dbFavourite.getSolved()) {
            return;
        }

        dbFavourite.setSolved(false);
        favouriteRepository.save(dbFavourite);
    }

    @PostConstruct
    private void postConstruct() {
        if (favouriteRepository.count() > 0) {
            return;
        }

        try {
            Path filePath = Path.of(Objects.requireNonNull(
                    UserController.class.getResource("/init/favourites.json")
            ).toURI());
            String usersJson = Files.readString(filePath);

            Gson gson = new Gson();
            Favourite[] defaultFavourites = gson.fromJson(usersJson, Favourite[].class);
            for (Favourite favourite : defaultFavourites) {
                add(1, favourite);
            }
        } catch (IOException | URISyntaxException e) {
            logger.fatal("Unable to populate favourites table -> {}", e.getMessage());
        }
    }

}
