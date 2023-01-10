package kth.se.id1212.model;

public class UserBean {
    int ID;
    String username;
    boolean admin; // TODO This seems like a security risk...
    int gamesPlayed;

    public UserBean(int ID, String username, boolean admin, int gamesPlayed) {
        this.ID = ID;
        this.username = username;
        this.admin = admin;
        this.gamesPlayed = gamesPlayed;
    }

    public int getID() {
        return ID;
    }

    public String getUsername() {
        return username;
    }
}
