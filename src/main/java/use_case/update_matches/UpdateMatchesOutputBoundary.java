package use_case.update_matches;

public interface UpdateMatchesOutputBoundary {
    void prepareSuccessView(UpdateMatchesOutputData outputData);

    void updateInfo(UpdateMatchesOutputData outputData);
}
