package data_access;

import entity.UserMatches;
import use_case.approve_reject_profile.ApproveRejectProfileMatchesDataAccessInterface;

import java.util.List;

public class ProfileMatchesDataAccessObject implements ApproveRejectProfileMatchesDataAccessInterface {
    public List<UserMatches> allUserMatches;

    public UserMatches getUserMatchesByUserId(int userId) {
        for  (UserMatches userMatches : allUserMatches) {
            if (userMatches.getUserId() == userId){
                return userMatches;
            }
        }
        return null;
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
