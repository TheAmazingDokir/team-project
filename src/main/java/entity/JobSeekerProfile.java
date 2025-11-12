package entity;

public class JobSeekerProfile extends UserProfile {
    private String resumeSummary;
    private String resumeFull;

    public JobSeekerProfile(Integer userId, String email, String phoneNumber, String profileUsername) {
        super(userId, email, phoneNumber, profileUsername);
    }

    public String getSummaryProfileAsString(){
        return resumeSummary;
    }

    public String getFullProfileAsString(){
        return resumeFull;
    }

    public void setResumeSummary(String resumeSummary) {
        this.resumeSummary = resumeSummary;
    }
    public void setResumeFull(String resumeFull) {
        this.resumeFull = resumeFull;
    }
}
