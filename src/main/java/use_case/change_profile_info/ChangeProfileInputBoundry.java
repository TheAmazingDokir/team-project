package use_case.change_profile_info;

/**
 * Input Boundary for actions which are related to ChangeProfile.
 */
public interface ChangeProfileInputBoundry{

    /**
     * Executes the change profile use case.
     * @param changeProfileInputData the input data
     */
    void execute(ChangeProfileInputData changeProfileInputData);
}
