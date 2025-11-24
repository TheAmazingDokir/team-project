package use_case.change_profile_info;

/**
 * Input Boundary for actions which are related to ChangeProfile.
 */
public interface ChangeProfileInputBoundary {

    /**
     * Executes the change profile use case.
     * @param changeProfileInputData the input data
     */
    void execute(ChangeProfileInputData changeProfileInputData);
}
