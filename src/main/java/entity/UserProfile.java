package entity;

public abstract class UserProfile {
    Integer userId;
    String email;
    String phoneNumber;
    String profileUsername;

    protected UserProfile(Integer userId, String email, String phoneNumber, String profileUsername) {
        this.userId = userId;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.profileUsername = profileUsername;
    }

    public void setUserId(Integer userId) {this.userId = userId; }

    public void setEmail(String email) {this.email = email; }

    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber; }

    public void setProfileUsername(String profileUsername) {this.profileUsername = profileUsername; }

    public Integer getUserId() {return this.userId; }

    public String getEmail() { return this.email; }

    public String getPhoneNumber() { return this.phoneNumber; }

    public String getProfileUsername() { return this.profileUsername; }

    public abstract String getFullProfileAsString();

    public abstract String getSummaryProfileAsString();
















}
