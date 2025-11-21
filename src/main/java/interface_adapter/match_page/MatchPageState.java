package interface_adapter.match_page;

import java.util.ArrayList;

public class MatchPageState {
    private Integer currentId;
    private ArrayList<UserMatches> matchUser;
    private ArrayList<String> userMatchesName;
    private String resume;

    public Integer getCurrentId() { return currentId; }

    public ArrayList<UserMatches> getUserMatches() { return matchUser; }

    public ArrayList<String> getUserMatchesName() { return userMatchesName; }

    public String getResume() { return resume; }

    public void setCurrentId(Integer currentId) {
        this.currentId = currentId;
    }

    public void setUserMatches(ArrayList<UserMatches> matchUser) {
        this.matchUser = matchUser;
    }

    public void setUserMatchesName(ArrayList<String> userMatchesName) {
        this.userMatchesName = userMatchesName;
    }

    public void setResume (String resume) { this.resume = resume; }
}
