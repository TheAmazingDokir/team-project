package interface_adapter.login;

import interface_adapter.ViewManagerModel;
import interface_adapter.ViewManagerState;
import interface_adapter.create_profile.ChangeProfileViewModel;
import interface_adapter.home_screen.HomeScreenState;
import interface_adapter.home_screen.HomeScreenViewModel;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginOutputData;

/**
 * The Presenter for the Login Use Case.
 */
public class LoginPresenter implements LoginOutputBoundary {

    private final LoginViewModel loginViewModel;
    private final ViewManagerModel viewManagerModel;
    private final ChangeProfileViewModel changeProfileViewModel;
    private final HomeScreenViewModel homeScreenViewModel;

    public LoginPresenter(ViewManagerModel viewManagerModel,
                          LoginViewModel loginViewModel,
                          ChangeProfileViewModel changeProfileViewModel,
                          HomeScreenViewModel homeScreenViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.loginViewModel = loginViewModel;
        this.changeProfileViewModel = changeProfileViewModel;
        this.homeScreenViewModel = homeScreenViewModel;
    }

    @Override
    public void prepareSuccessView(LoginOutputData response) {
        // On success, update the state
        // and clear everything from the LoginViewModel's state
        loginViewModel.setState(new LoginState());

        // switch to the logged in view
        ViewManagerState state = this.viewManagerModel.getState();

        state.setLoggedIn(true);
        state.setUserId(response.getUserId());

        if(response.hasProfile()){
            HomeScreenState homeScreenState = homeScreenViewModel.getState();
            homeScreenState.setCurrentId(response.getUserId());
            homeScreenViewModel.setState(homeScreenState);
            homeScreenViewModel.firePropertyChange();

            state.setCurrentView(homeScreenViewModel.getViewName());
        }else{
            state.setCurrentView(changeProfileViewModel.getViewName());
        }

        this.viewManagerModel.setState(state);
        this.viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String error) {
        final LoginState loginState = loginViewModel.getState();
        loginState.setLoginError(error);
        loginViewModel.firePropertyChange();
    }
}
