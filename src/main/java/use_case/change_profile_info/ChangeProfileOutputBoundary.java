package use_case.change_profile_info;

/**
 * The output boundary for the ChangeProfile Use Case.
 */
public interface ChangeProfileOutputBoundary {
    /**
     * Prepares the success view for the ChangeProfile Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(ChangeProfileOutputData outputData);

    /**
     * Prepares the failure view for the ChangeProfile Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}
