package entity;

public class EmployerProfile extends UserProfile {
    private String companyName;
    private String jobApplicationSummary;
    private String jobApplicationFull;

    public EmployerProfile(Integer userId, String email, String phoneNumber, String profileUsername){
        super(userId, email, phoneNumber, profileUsername);
    }

    public void setCompanyName(String companyName) {this.companyName = companyName;}

    public String getCompanyName() {return this.companyName;}

    public String getSummaryProfileAsString(){
        return jobApplicationSummary;
    }

    public String getFullProfileAsString(){
        return jobApplicationFull;
    }

    public void setJobApplicationSummary(String jobApplicationSummary) {
        this.jobApplicationSummary = jobApplicationSummary;
    }

    public void setJobApplicationFull(String fullProfile){
        this.jobApplicationFull = fullProfile;
    }

}
