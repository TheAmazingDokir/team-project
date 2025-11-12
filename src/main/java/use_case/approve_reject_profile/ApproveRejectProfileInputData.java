package use_case.approve_reject_profile;

/**
 * The input data for Approve Reject Profile use case
 */
public class ApproveRejectProfileInputData {
    private int currentProfileId;
    private int otherProfileId;
    private boolean approved;

    public ApproveRejectProfileInputData(int currentProfileId, int otherProfileId, boolean approved) {
        this.currentProfileId = currentProfileId;
        this.otherProfileId = otherProfileId;
        this.approved = approved;
    }
    public int getCurrentProfileId() {
        return this.currentProfileId;
    }
    public int getOtherProfileId() {
        return this.otherProfileId;
    }
    public boolean getIsApproved() {
        return this.approved;
    }
}
