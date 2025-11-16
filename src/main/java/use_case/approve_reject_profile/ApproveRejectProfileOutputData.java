package use_case.approve_reject_profile;

/**
 * Output data for Approve Reject use case
 */
public class ApproveRejectProfileOutputData {
    private int currentId;

    public ApproveRejectProfileOutputData(int currentId) {
        this.currentId = currentId;
    }

    public int getCurrentId() {
        return currentId;
    }
}
