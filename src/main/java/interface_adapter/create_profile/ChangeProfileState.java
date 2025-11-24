package interface_adapter.create_profile;

public class ChangeProfileState {
    private String email;
    private String phoneNumber;
    private String summary;
    private String profile;
    private Boolean isEmployer;
    private String profileUsername;

    public void setEmail(String email) {this.email = email;}
    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}
    public void setSummary(String summary) {this.summary = summary;}
    public void setProfile(String profile) {this.profile = profile;}
    public void setIsEmployer(Boolean isEmployer) {this.isEmployer = isEmployer;}
    public void setProfileUsername(String profileUsername) {this.profileUsername = profileUsername;}

    public String getEmail() {return email;}
    public String getPhoneNumber() {return phoneNumber;}
    public String getSummary() {return summary;}
    public String getProfile() {return profile;}
    public Boolean getIsEmployer() {return isEmployer;}
    public String getProfileUsername() {return profileUsername;}
}
