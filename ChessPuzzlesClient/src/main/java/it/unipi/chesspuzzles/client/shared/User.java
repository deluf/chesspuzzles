package it.unipi.chesspuzzles.client.shared;

@SuppressWarnings("unused")
public class User {
    public String username;
    public String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Default constructor is required by gson
    public User() {}
}
