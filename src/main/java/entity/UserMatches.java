package entity;

import java.util.ArrayList;
import java.util.List;

public class UserMatches {
    private String username;
    private int userId;
    private ArrayList<Integer> matchIds;
    private ArrayList<Integer> approvedIds;
    private ArrayList<Integer> rejectedIds;

    public UserMatches() {
        this.matchIds = new ArrayList<>();
        this.approvedIds = new ArrayList<>();
        this.rejectedIds = new ArrayList<>();
    }

    public int getUserId() {
        return userId;
    }

    public List<Integer> getMatches() {
        return matchIds;
    }

    public List<Integer> getApproved() {
        return approvedIds;
    }

    public List<Integer> getRejected() {
        return rejectedIds;
    }

    // Add methods
    public void addMatch(int matchId) {
        matchIds.add(matchId);
    }

    public void addApproved(int approvalId) {
        approvedIds.add(approvalId);
    }

    public void addRejected(int declineId) {
        rejectedIds.add(declineId);
    }
}
