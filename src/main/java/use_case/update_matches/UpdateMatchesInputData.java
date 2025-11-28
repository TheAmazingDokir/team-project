package use_case.update_matches;

public class UpdateMatchesInputData {
    private int user1Id;
    private int user2Id;
    private int currentId;

    public UpdateMatchesInputData(int currentId) {
        this.currentId = currentId;
    }
    public UpdateMatchesInputData(int user1Id, int user2Id) {
        this.user1Id = user1Id;
        this.user2Id = user2Id;
    }

    public int getUser1Id() {
        return user1Id;
    }
    public void setUser1Id(int user1Id) {
        this.user1Id = user1Id;
    }
    public int getUser2Id() {
        return user2Id;
    }
    public void setUser2Id(int user2Id) {
        this.user2Id = user2Id;
    }
    public int getCurrentId() {
        return currentId;
    }
    public void setCurrentId(int currentId) {
        this.currentId = currentId;
    }
}
