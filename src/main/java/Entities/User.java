package Entities;

abstract class User {
    String username;
    String password;
    UserProfile profile;

    protected User(String username, String password, UserProfile profile) {
        this.username = username;
        this.password = password;
        this.profile = profile;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setProfile(UserProfile profile){
        this.profile = profile;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public UserProfile getProfile(){
        return this.profile;
    }
}
