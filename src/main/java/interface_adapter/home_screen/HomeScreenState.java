package interface_adapter.home_screen;

public class HomeScreenState {
    private Integer currentId;
    private boolean isApprove = false;
    private Integer otherId;
    private String otherProfileName;
    private String otherProfileSummary;

    public Integer getCurrentId() {
        return currentId;
    }

    public boolean getIsApprove() {
        return isApprove;
    }

    public Integer getOtherId() {
        return otherId;
    }

    public String getOtherProfileName() {
        return otherProfileName;
    }

    public String getOtherProfileSummary() {
        return otherProfileSummary;
    }

    public void setCurrentId(Integer currentId) {
        this.currentId = currentId;
    }

    public void setIsApprove(boolean isApprove) {
        this.isApprove = isApprove;
    }

    public void setOtherId(Integer otherId) {
        this.otherId = otherId;
    }

    public void setOtherProfileSummary(String otherProfileSummary) {
        this.otherProfileSummary = otherProfileSummary;
    }

    public void setOtherProfileName(String otherProfileName) {
        this.otherProfileName = otherProfileName;
    }


}
