package interface_adapter.Homescreen;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;
import use_case.login.LoginOutputData;


public class HomeScreenPresenter implements HomeScreenOutputBoundary {

    private final HomeScreenViewModel homeScreenViewModel;
    private final ViewManagerModel viewManagerModel;

    public HomeScreenPresenter(ViewManagerModel viewManagerModel,
                               HomeScreenViewModel homeScreenViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.homeScreenViewModel = homeScreenViewModel;
    }

    @Override
    public void prepareSuccessView(ApproveRejectProfileOutputData response) {

        final HomeScreenState homeScreenState = homeScreenViewModel.getState();
        homeScreenState.setCurrentId(response.getCurrentId());
        homeScreenState.setOtherId(response.getOtherId());
        this.homeScreenViewModel.firePropertyChange();


        this.viewManagerModel.setState(homeScreenViewModel.getViewName());
        this.viewManagerModel.firePropertyChange();
    }

}
