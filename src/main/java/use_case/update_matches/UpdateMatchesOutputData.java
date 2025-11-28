package use_case.update_matches;

import entity.UserProfile;

import java.util.ArrayList;

public class UpdateMatchesOutputData {
    private final ArrayList<UserProfile> Users;

    public UpdateMatchesOutputData(ArrayList<UserProfile> Users) {
        this.Users = Users;
    }
    public ArrayList<UserProfile> getUsers() {
        return Users;
    }
}
