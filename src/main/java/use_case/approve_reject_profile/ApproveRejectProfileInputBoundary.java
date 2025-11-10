package use_case.approve_reject_profile;

import use_case.approve_reject_profile.ApproveRejectProfileInputData;

/**
 * The Approve Reject Profile Use Case
 */
public interface ApproveRejectProfileInputBoundary {
    /**
     * Execute the ApproveRejectProfile Use Case.
     * @param approveRejectProfileInputData the input data for this use case
     */
    void execute(ApproveRejectProfileInputData approveRejectProfileInputData);

}
