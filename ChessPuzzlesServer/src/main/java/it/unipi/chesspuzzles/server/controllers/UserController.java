package it.unipi.chesspuzzles.server.controllers;

import com.google.gson.Gson;

import it.unipi.chesspuzzles.server.GlobalExceptionHandler;
import it.unipi.chesspuzzles.server.database.DatabaseUser;
import it.unipi.chesspuzzles.server.database.UserRepository;
import it.unipi.chesspuzzles.server.shared.User;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.mindrot.jbcrypt.BCrypt;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@Controller
@RequestMapping(path = "/users")
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class);

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * @return the user id if the login was successful, 0 otherwise
     */
    @PostMapping(path = "/login")
    public @ResponseBody int login(@RequestBody User user) {
        if (user.username.isEmpty() || user.password.isEmpty()) {
            throw new GlobalExceptionHandler.BadRequestException("Username and password fields can't be empty");
        }

        DatabaseUser dbUser = userRepository.findByUsername(user.username);
        if (dbUser == null || !BCrypt.checkpw(user.password, dbUser.getPassword())) {
            return 0;
        }
        return dbUser.getId();
    }

    /**
     * @return the user id if the registration was successful, 0 otherwise
     */
    @PostMapping(path = "/register")
    public @ResponseBody int register(@RequestBody User user) {
        if (user.username.isEmpty() || user.password.isEmpty()) {
            throw new GlobalExceptionHandler.BadRequestException("Username and password fields can't be empty");
        }

        if (userRepository.findByUsername(user.username) != null) {
            return 0;
        }

        user.password = BCrypt.hashpw(user.password, BCrypt.gensalt());
        DatabaseUser dbUser = userRepository.save(new DatabaseUser(user));
        return dbUser.getId();
    }

    /**
     * One of the default users in init/users.json must have:
     *  id: 1
     *  username: test
     *  password: test
     */
    @PostConstruct
    private void postConstruct() {
        if (userRepository.count() > 0) {
            return;
        }

        try {
            Path filePath = Path.of(Objects.requireNonNull(
                    UserController.class.getResource("/init/users.json")).toURI());
            String usersJson = Files.readString(filePath);

            Gson gson = new Gson();
            User[] defaultUsers = gson.fromJson(usersJson, User[].class);
            for (User user : defaultUsers) {
                register(user);
                userRepository.setCustomId(user.id, user.username);
            }
        } catch (IOException | URISyntaxException e) {
            logger.fatal("Unable to populate user table -> {}", e.getMessage());
        }
    }

}