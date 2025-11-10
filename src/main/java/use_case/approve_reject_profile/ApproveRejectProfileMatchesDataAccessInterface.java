package use_case.approve_reject_profile;

/**
 * The DAO interface for Approve Reject Profile use case
 */
public interface ApproveRejectProfileMatchesDataAccessInterface {
    /**
     * Updates the approved Ids of current user with other profile id
     * @param currentProfileId the id of the profile of current user
     * @param otherProfileId the id of the profile of other user who was approved/rejected
     */
    void approveProfile(int currentProfileId, int otherProfileId);
    void rejectProfile(int currentProfileId, int otherProfileId);
}
