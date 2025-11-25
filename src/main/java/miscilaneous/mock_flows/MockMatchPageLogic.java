package miscilaneous.mock_flows;

import data_access.PineconeDataAccessObject;
import data_access.ProfileMatchesDataAccessObject;
import data_access.mock_daos.UserProfileDataAccessMock;
import interface_adapter.match_page.*;
import org.openapitools.db_data.client.ApiException;
import use_case.update_matches.*;
import view.MatchPageView;

import javax.swing.*;

public class MockMatchPageLogic {
    public static void main(String[] args) throws ApiException {
        PineconeDataAccessObject pineconeAccess = new PineconeDataAccessObject();
        UserProfileDataAccessMock userProfileDataAccess = new UserProfileDataAccessMock();
        ProfileMatchesDataAccessObject profileMatchesDataAccess = new ProfileMatchesDataAccessObject();

        MatchPageViewModel matchPageViewModel = new MatchPageViewModel();
        MatchPageView matchPageView = new MatchPageView(matchPageViewModel);

        MatchPageState initState = matchPageViewModel.getState();
        initState.setCurrentId(12);

        //MatchPagePresenter matchPagePresenter = new MatchPagePresenter(matchPageViewModel);


        pineconeAccess.upsertProfiles(userProfileDataAccess.getAllUserProfiles());

        UpdateMatchesInteractor updateMatchesInteractor = new UpdateMatchesInteractor();

        MatchPageController matchPageController = new MatchPageController(updateMatchesInteractor);

        matchPageView.setMatchPageController(matchPageController);


        final JFrame application = new JFrame("Hirematch");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(matchPageView);
        application.pack();
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}
