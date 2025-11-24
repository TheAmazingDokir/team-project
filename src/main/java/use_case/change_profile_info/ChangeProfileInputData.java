package use_case.change_profile_info;

/**
 * The Input Data for the ChangeProfile Use Case.
 */
public class ChangeProfileInputData {

    private final String email;
    private final String phoneNumber;
    private final String summary;
    private final String profile;
    private final Boolean isEmployer;
    private final String profileUsername;
    private final Integer profileID;


    public ChangeProfileInputData(String email,  String phoneNumber, String summary, String profile,
                                  Boolean isEmployer, String profileUsername, Integer profileID ) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.summary = summary;
        this.profile = profile;
        this.isEmployer = isEmployer;
        this.profileUsername = profileUsername;
        this.profileID = profileID;
    }

    public String getEmail() {return this.email;}

    public String getPhoneNumber() {return this.phoneNumber;}

    public String getSummary() {return this.summary;}

    public String getProfile() {return this.profile;}

    public Boolean getIsEmployer() {return this.isEmployer;}

    public String getProfileUsername() {return this.profileUsername;}

    public Integer getProfileID() {return this.profileID;}
}
