package interface_adapter.match_page;

import java.util.ArrayList;
import entity.UserMatches;
import entity.UserProfile;

public class MatchPageState {
    private Integer currentId;
    private ArrayList<UserProfile> matchUser;
    private ArrayList<String> userMatchesName;
    private String resume;
    private String name;
    private String email;
    private String phone;

    public Integer getCurrentId() { return currentId; }

    public ArrayList<UserProfile> getUserMatches() { return matchUser; }

    public ArrayList<String> getUserMatchesName() { return userMatchesName; }

    public String getResume() { return resume; }

    public String getName() { return name; }

    public String getEmail() { return email; }

    public String getPhone() { return phone; }

    public void setCurrentId(Integer currentId) {
        this.currentId = currentId;
    }

    public void setUserMatches(ArrayList<UserProfile> matchUser) {
        this.matchUser = matchUser;
    }

    public void setUserMatchesName(ArrayList<String> userMatchesName) {
        this.userMatchesName = userMatchesName;
    }

    public void setResume (String resume) { this.resume = resume; }

    public void setName (String name) { this.name = name; }

    public void setEmail (String email) { this.email = email; }

    public void setPhone (String phone) { this.phone = phone; }
}
