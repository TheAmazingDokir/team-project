package interface_adapter.home_screen;


public class HomeScreenController {

    private final ApproveRejectProfileInputBoundary approveRejectProfileUseCaseInteractor;
    private final RecomendatiomProfileUseCaseInteractor recomendatiomProfileUseCaseInteractor;

    public HomeScreenController(ApproveRejectProfileInputBoundary approveRejectProfileUseCaseInteractor) {
        this.approveRejectProfileUseCaseInteractor = approveRejectProfileUseCaseInteractor;
        this.recomendatiomProfileUseCaseInteractor = new RecomendatiomProfileUseCaseInteractor();
    }


    /**
     * Executes the Home Screen Use Case.
     * @param currentId the id for the current object
     * @param otherId the id for the shown object
     * @param isApprove the Approve state between current and other
     */
    public void execute(Integer currentId, Integer otherId, boolean isApprove) {
        final ApproveRejectProfileInputData approveRejectProfileInputData =
                new ApproveRejectProfileInputData(currentId, otherId, isApprove);

        final RecomendatiomProfileInputData recomendatiomProfileInputData =
                new RecomendatiomProfileInputData(currentId);

        approveRejectProfileUseCaseInteractor.execute(approveRejectProfileInputDataInputData);

        recomendatiomProfileUseCaseInteractor.execute(recomendatiomProfileInputDataInputData);

    }
}


