package it.unipi.chesspuzzles.server.controllers;

import com.google.gson.Gson;

import it.unipi.chesspuzzles.server.GlobalExceptionHandler;
import it.unipi.chesspuzzles.server.database.AttempRepository;
import it.unipi.chesspuzzles.server.database.DatabaseAttemp;
import it.unipi.chesspuzzles.server.shared.Attemp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping(path = "/users/{userId}/attemps")
public class AttempController {

    private static final Logger logger = LogManager.getLogger(UserController.class);

    private final AttempRepository attempRepository;

    public AttempController(AttempRepository attempRepository) {
        this.attempRepository = attempRepository;
    }

    @GetMapping(path = "/after")
    public @ResponseBody List<Attemp> after(
            @PathVariable Integer userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime timestamp) {
        Iterable<DatabaseAttemp> dbAttemps = attempRepository.findAllByUserIdAndTimestampAfter(userId, timestamp);
        ArrayList<Attemp> attemps = new ArrayList<>();
        for (DatabaseAttemp dbAttemp : dbAttemps) {
            attemps.add(new Attemp(dbAttemp));
        }
        return attemps;
    }

    @PostMapping(path = "/add")
    public @ResponseBody void add(@PathVariable Integer userId, @RequestBody Attemp attemp) {
        if (!Objects.equals(attemp.userId, userId)) {
            throw new GlobalExceptionHandler.UnauthorizedException();
        }
        attempRepository.save(new DatabaseAttemp(attemp));
    }

    @PostConstruct
    private void postConstruct() {
        if (attempRepository.count() > 0) {
            return;
        }

        try {
            Path filePath = Path.of(Objects.requireNonNull(
                    UserController.class.getResource("/init/attemps.json")
            ).toURI());
            String usersJson = Files.readString(filePath);

            Gson gson = new Gson();
            Attemp[] defaultAttemps = gson.fromJson(usersJson, Attemp[].class);
            for (Attemp attemp : defaultAttemps) {
                add(1, attemp);
            }
        } catch (IOException | URISyntaxException e) {
            logger.fatal("Unable to populate attemps table -> {}", e.getMessage());
        }
    }

}
