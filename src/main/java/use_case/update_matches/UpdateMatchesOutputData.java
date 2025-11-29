package use_case.update_matches;

import entity.UserProfile;

import java.util.ArrayList;

public class UpdateMatchesOutputData {
    private ArrayList<UserProfile> Users;
    private String currentName;

    public UpdateMatchesOutputData(ArrayList<UserProfile> Users) {
        this.Users = Users;
    }
    public UpdateMatchesOutputData(String currentName) {
        this.currentName = currentName;
    }
    public ArrayList<UserProfile> getUsers() {
        return Users;
    }
    public String getCurrentName() {
        return currentName;
    }
}
