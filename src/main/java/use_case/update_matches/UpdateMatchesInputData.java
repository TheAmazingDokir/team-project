package use_case.update_matches;

public class UpdateMatchesInputData {
    private int profileId;

    public UpdateMatchesInputData(int profileId) {
        this.profileId = profileId;
    }

    public int getProfileId() {
        return profileId;
    }
}
