package miscilaneous.mock_flows;

import data_access.PineconeDataAccessObject;
import data_access.ProfileMatchesDataAccessObject;
import data_access.mock_daos.UserProfileDataAccessMock;
import interface_adapter.home_screen.HomeScreenController;
import interface_adapter.home_screen.HomeScreenPresenter;
import interface_adapter.home_screen.HomeScreenState;
import interface_adapter.home_screen.HomeScreenViewModel;
import org.openapitools.db_data.client.ApiException;
import use_case.approve_reject_profile.ApproveRejectProfileInteractor;
import use_case.recommend_profile.RecommendProfileInputData;
import use_case.recommend_profile.RecommendProfileInteractor;
import view.HomeScreenView;

import javax.swing.*;

public class MockHomePageLogic {
    public static void main(String[] args) throws ApiException {
        PineconeDataAccessObject pineconeAccess = new PineconeDataAccessObject();
        UserProfileDataAccessMock userProfileDataAccess = new UserProfileDataAccessMock();
        ProfileMatchesDataAccessObject profileMatchesDataAccess = new ProfileMatchesDataAccessObject();

        HomeScreenViewModel homeScreenViewModel = new HomeScreenViewModel();
        HomeScreenView homeScreenView = new HomeScreenView(homeScreenViewModel);

        HomeScreenState initState = homeScreenViewModel.getState();
        initState.setCurrentId(12);

        HomeScreenPresenter homeScreenPresenter = new HomeScreenPresenter(homeScreenViewModel);


        pineconeAccess.upsertProfiles(userProfileDataAccess.getAllUserProfiles());

        ApproveRejectProfileInteractor approveRejectProfileInteractor = new ApproveRejectProfileInteractor(profileMatchesDataAccess);
        RecommendProfileInteractor recommendProfileInteractor = new RecommendProfileInteractor(userProfileDataAccess, pineconeAccess, homeScreenPresenter, profileMatchesDataAccess);

//        HomeScreenController homeScreenController = new HomeScreenController(approveRejectProfileInteractor,
//                recommendProfileInteractor);

//        homeScreenView.setHomeScreenController(homeScreenController);

        final RecommendProfileInputData recomendatiomProfileInputData = new RecommendProfileInputData(12);
        recommendProfileInteractor.execute(recomendatiomProfileInputData);

        final JFrame application = new JFrame("Hirematch");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(homeScreenView);
        application.pack();
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}
