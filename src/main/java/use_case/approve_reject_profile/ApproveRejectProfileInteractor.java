package use_case.approve_reject_profile;

/**
 * The Approve Reject Profile Interactor
 */
public class ApproveRejectProfileInteractor implements ApproveRejectProfileInputBoundary {
    private ApproveRejectProfileMatchesDataAccessInterface profileMatchesDataAccessInterface;
    public ApproveRejectProfileInteractor(ApproveRejectProfileMatchesDataAccessInterface profileMatchesDataAccessInterface) {
        this.profileMatchesDataAccessInterface = profileMatchesDataAccessInterface;
    }
    @Override
    public void execute(ApproveRejectProfileInputData approveRejectProfileInputData) {
        int currentId = approveRejectProfileInputData.getCurrentProfileId();
        int otherId = approveRejectProfileInputData.getOtherProfileId();

        if (approveRejectProfileInputData.getIsApproved()){
            profileMatchesDataAccessInterface.approveProfile(currentId, otherId);
        }else{
            profileMatchesDataAccessInterface.rejectProfile(currentId,  otherId);
        }
    }
}
