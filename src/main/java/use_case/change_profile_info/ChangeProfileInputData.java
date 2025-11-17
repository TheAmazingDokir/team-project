package use_case.change_profile_info;

/**
 * The Input Data for the ChangeProfile Use Case.
 */
public class ChangeProfileInputData {

    private final String email;
    private final String phoneNumber;
    private final Integer profileID;
    private final String profileUsername;

    private final boolean isEmployer;
    private final boolean isJobSeeker;

    private final String resumeSummary;
    private final String resumeFull;

    private final String companyName;
    private final String jobApplicationSummary;
    private final String jobApplicationFull;

    public ChangeProfileInputData(String email, String phoneNumber, Integer profileID,
                                  String profileUsername, boolean isEmployer, boolean isJobSeeker,
                                  String resumeSummary, String resumeFull, String companyName,
                                  String jobApplicationSummary, String jobApplicationFull) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.profileID = profileID;
        this.profileUsername = profileUsername;
        this.isEmployer = isEmployer;
        this.isJobSeeker = isJobSeeker;
        this.resumeSummary = resumeSummary;
        this.resumeFull = resumeFull;
        this.companyName = companyName;
        this.jobApplicationSummary = jobApplicationSummary;
        this.jobApplicationFull = jobApplicationFull;
    }

    Integer getProfileID() {
        return profileID;
    }

    String getPhoneNumber() {
        return phoneNumber;
    }

    String getEmail() {return email;}

    String getProfileUsername() {return profileUsername;}

    boolean isEmployer() {return isEmployer;}

    boolean isJobSeeker() {return isJobSeeker;}

    String getResumeSummary() {return resumeSummary;}

    String getResumeFull() {return resumeFull;}

    String getCompanyName() {return companyName;}

    String getJobApplicationSummary() {return jobApplicationSummary;}

    String getJobApplicationFull() {return jobApplicationFull;}



}
