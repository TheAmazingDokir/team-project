package interface_adapter.Homescreen;

public class HomeScreenState {
    private String currentId = "";
    private boolean isApprove = false;
    private String otherId = "";

    public String getCurrentId() {
        return currentId;
    }

    public boolean getIsApprove() {
        return isApprove;
    }

    public String getOtherId() {
        return otherId;
    }

    public void setCurrentId(String currentId) {
        this.currentId = currentId;
    }

    public void setIsApprove(boolean isApprove) {
        this.isApprove = isApprove;
    }

    public void setOtherId(String otherId) {
        this.otherId = otherId;
    }

}
