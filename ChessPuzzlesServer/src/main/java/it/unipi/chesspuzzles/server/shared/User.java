package it.unipi.chesspuzzles.server.shared;

@SuppressWarnings("unused")
public class User {
    public Integer id;
    public String username;
    public String password;

    // Default constructor is required by gson
    public User() {}
}
