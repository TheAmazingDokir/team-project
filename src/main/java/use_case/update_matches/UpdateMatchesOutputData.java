package use_case.update_matches;

import entity.UserMatches;
import entity.UserProfile;

import java.util.ArrayList;

public class UpdateMatchesOutputData {
    private final UserProfile userProfile;
    private final ArrayList<UserMatches> matchUser;
    private final ArrayList<String> userMatchesName;
    private final String resume;

    public UpdateMatchesOutputData(UserProfile userProfile, ArrayList<UserMatches> matchUser, ArrayList<String> userMatchesName, String resume) {
        this.userProfile = userProfile;
        this.matchUser = matchUser;
        this.userMatchesName = userMatchesName;
        this.resume = resume;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }
}
