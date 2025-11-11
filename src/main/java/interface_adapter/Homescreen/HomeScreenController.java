package interface_adapter.Homescreen;


public class HomeScreenController {

    private final ApproveRejectProfileInputBoundary approveRejectProfileUseCaseInteractor;

    public HomeScreenController(ApproveRejectProfileInputBoundary approveRejectProfileUseCaseInteractor) {
        this.approveRejectProfileUseCaseInteractor = approveRejectProfileUseCaseInteractor;
    }


    /**
     * Executes the Home Screen Use Case.
     * @param
     */
    public void execute(String currentId, String otherId, boolean isApprove) {
        final ApproveRejectProfileInputData approveRejectProfileInputData =
                new ApproveRejectProfileInputData(currentId, otherId, isApprove);

        approveRejectProfileUseCaseInteractor.execute(approveRejectProfileInputDataInputData);
    }
}


