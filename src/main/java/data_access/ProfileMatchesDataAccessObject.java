package data_access;

import entity.UserMatches;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.approve_reject_profile.ApproveRejectProfileMatchesDataAccessInterface;
import use_case.recommend_profile.RecommendProfileMatchesDataAccessInterface;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ProfileMatchesDataAccessObject implements ApproveRejectProfileMatchesDataAccessInterface, RecommendProfileMatchesDataAccessInterface {
    private final List<UserMatches> allUserMatches;

    public ProfileMatchesDataAccessObject(){
        allUserMatches = new ArrayList<>();
        loadMatchesFromFile();
    }

    public UserMatches getUserMatchesByUserId(int userId) {
        for  (UserMatches userMatches : allUserMatches) {
            if (userMatches.getUserId() == userId){
                return userMatches;
            }
        }
        return null;
    }

    public List<Integer> getSeenProfileIds(int userId){
        ArrayList<Integer> seenProfileIds = new ArrayList<>();
        seenProfileIds.addAll(getApprovedProfileIds(userId));
        seenProfileIds.addAll(getRejectedProfileIds(userId));
        return seenProfileIds;
    }

    public List<Integer> getApprovedProfileIds(int userId){
        return getUserMatchesByUserId(userId).getApproved();
    }

    public List<Integer> getRejectedProfileIds(int userId){
        return getUserMatchesByUserId(userId).getRejected();
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

    @Override
    public void approveProfile(int currentProfileId, int otherProfileId) {
        getUserMatchesByUserId(currentProfileId).addApproved(otherProfileId);
    }
    @Override
    public void rejectProfile(int currentProfileId, int otherProfileId) {
        getUserMatchesByUserId(currentProfileId).addRejected(otherProfileId);
    }
}
