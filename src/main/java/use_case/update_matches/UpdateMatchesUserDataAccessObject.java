package use_case.update_matches;

import entity.UserMatches;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains implementations of necessary methods for the Update Matches use case
 */
public class UpdateMatchesUserDataAccessObject implements IUpdateMatchesUserDataAccess{
    private final List<UserMatches> allUserMatches;

    public UpdateMatchesUserDataAccessObject(){
        allUserMatches = new ArrayList<>();
        loadMatchesFromFile();
    }

    /**
     * Gets the UserMatches object for the given User ID
     * @param userId User ID for the needed User object
     * @return UserMatches object for the give User ID
     */
    public UserMatches getUserMatchesFromId(int userId) {
        for  (UserMatches userMatches : allUserMatches) {
            if (userMatches.getUserId() == userId){
                return userMatches;
            }
        }
        return null;
    }

    private void loadMatchesFromFile(){
        try{
            String matchesJson = Files.readString(Paths.get("src/main/java/miscilaneous/mock_data/MockMatches.json"));
            JSONArray matchesArray = new JSONArray(matchesJson);

            for (int i = 0; i < matchesArray.length(); i++) {
                JSONObject js = matchesArray.getJSONObject(i);

                int userID = js.getInt("userId");
                UserMatches userMatches = new UserMatches(userID);

                JSONArray approvedJson = js.getJSONArray("approvedIds");
                for (int j = 0; j < approvedJson.length(); j++) {
                    userMatches.addApproved(approvedJson.getInt(j));
                }
                List<Integer> rejectedIds = new ArrayList<>();
                JSONArray rejectedJson = js.getJSONArray("rejectedIds");
                for (int j = 0; j < rejectedJson.length(); j++) {
                    userMatches.addRejected(rejectedJson.getInt(j));
                }
                allUserMatches.add(userMatches);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
