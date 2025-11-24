package interface_adapter;

public class ViewManagerState {
    private boolean loggedIn;
    private int userId;
    private boolean hasProfile;
    private String currentView;

    public ViewManagerState(boolean loggedIn, int userId, boolean hasProfile, String currentView) {
        this.loggedIn = loggedIn;
        this.userId = userId;
        this.hasProfile = hasProfile;
        this.currentView = currentView;
    }


    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isHasProfile() {
        return hasProfile;
    }

    public void setHasProfile(boolean hasProfile) {
        this.hasProfile = hasProfile;
    }

    public String getCurrentView() {
        return currentView;
    }

    public void setCurrentView(String currentView) {
        this.currentView = currentView;
    }
}
