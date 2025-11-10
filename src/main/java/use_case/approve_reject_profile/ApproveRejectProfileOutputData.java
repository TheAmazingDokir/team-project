package use_case.approve_reject_profile;

/**
 * Output data for Approve Reject use case
 */
public class ApproveRejectProfileOutputData {
    private final int currentUserId;

    ApproveRejectProfileOutputData(int currentUserId) {
        this.currentUserId = currentUserId;
    }

    public int getCurrentUserId() {
        return currentUserId;
    }
}
