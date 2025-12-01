package use_case.signup;

public interface SignupProfileDataAccessInterface {
    /**
     * Retrieves next created profile id from database
     * @return the not taken profile id
     */
    int getNextProfileId();
}
