package use_case.change_profile_info;

/**
 * Output Data for the ChangeProfile Use Case.
 */
public class ChangeProfileOutputData {
    private final Integer userID;

    public ChangeProfileOutputData(Integer userID) {
        this.userID = userID;
    }

    public Integer getUserID() {
        return this.userID;
    }
}
