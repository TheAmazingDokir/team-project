package miscilaneous.mock_flows;


import use_case.update_matches.UpdateMatchesOutputBoundary;
import use_case.update_matches.UpdateMatchesOutputData;

public class MockMatchPagePresenter implements UpdateMatchesOutputBoundary {
    public void prepareSuccessView(UpdateMatchesOutputData updateMatchesOutputData) {
        System.out.println(updateMatchesOutputData.getCurrentName());
    }

    @Override
    public void updateInfo(UpdateMatchesOutputData outputData) {
        System.out.println(outputData.getCurrentName());
    }
}
