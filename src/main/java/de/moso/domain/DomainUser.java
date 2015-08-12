package de.moso.domain;

public class DomainUser {
    private String username;
    private String token;

    public DomainUser(String username, String token) {
        this.username = username;
        this.token = token;
    }

    public String getUsername() {
        return username;
    }
    public String getToken() {return token; }

    @Override
    public String toString() {
        return username;
    }
}
