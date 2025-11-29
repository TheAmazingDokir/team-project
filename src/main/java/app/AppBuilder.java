package app;

import data_access.MongoDBProfileDataAccessObject;
import data_access.MongoDBUserDataAccessObject;
import data_access.PineconeDataAccessObject;
import data_access.ProfileMatchesDataAccessObject;
import entity.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.ViewManagerState;
import interface_adapter.create_profile.ChangeProfileController;
import interface_adapter.create_profile.ChangeProfilePresenter;
import interface_adapter.create_profile.ChangeProfileViewModel;
import interface_adapter.home_screen.HomeScreenController;
import interface_adapter.home_screen.HomeScreenPresenter;
import interface_adapter.home_screen.HomeScreenViewModel;
import interface_adapter.logged_in.ChangePasswordController;
import interface_adapter.logged_in.ChangePasswordPresenter;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.logout.LogoutPresenter;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;
import use_case.approve_reject_profile.ApproveRejectProfileInteractor;
import use_case.change_password.ChangePasswordInputBoundary;
import use_case.change_password.ChangePasswordInteractor;
import use_case.change_password.ChangePasswordOutputBoundary;
import use_case.change_profile_info.ChangeProfileInputBoundary;
import use_case.change_profile_info.ChangeProfileInteractor;
import use_case.change_profile_info.ChangeProfileOutputBoundary;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.logout.LogoutInputBoundary;
import use_case.logout.LogoutInteractor;
import use_case.logout.LogoutOutputBoundary;
import use_case.recommend_profile.RecommendProfileInteractor;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import view.*;

import javax.swing.*;
import java.awt.*;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    final UserFactory userFactory = new UserFactory();
    final ViewManagerModel viewManagerModel = new ViewManagerModel();
    ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    // set which data access implementation to use, can be any
    // of the classes from the data_access package

    // DAO using a shared external database for users and profiles
    final MongoDBUserDataAccessObject userDataAccessObject = new MongoDBUserDataAccessObject(userFactory);
    final MongoDBProfileDataAccessObject profileDataAccessObject = new MongoDBProfileDataAccessObject();
    final ProfileMatchesDataAccessObject profileMatchesDataAccessObject = new ProfileMatchesDataAccessObject();

    // DAO for service to obtain semantic recommendations
    PineconeDataAccessObject pineconeAccess = new PineconeDataAccessObject();

    private SignupView signupView;
    private SignupViewModel signupViewModel;
    private LoginViewModel loginViewModel;
    private LoggedInViewModel loggedInViewModel;
    private LoggedInView loggedInView;
    private LoginView loginView;

    private HomeScreenViewModel homeScreenViewModel;
    private HomeScreenView homeScreenView;

    // Shared use cases that are used among several pages
    private ApproveRejectProfileInteractor  approveRejectProfileInteractor;
    private RecommendProfileInteractor  recommendProfileInteractor;

    private ChangeProfileInteractor changeProfileInteractor;
    private ChangeProfileViewModel changeProfileViewModel;
    private ChangeProfileView changeProfileView;
    private ChangeProfileController changeProfileController;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    public AppBuilder addSignupView() {
        signupViewModel = new SignupViewModel();
        signupView = new SignupView(signupViewModel);
        cardPanel.add(signupView, signupView.getViewName());
        return this;
    }

    public AppBuilder addLoginView() {
        loginViewModel = new LoginViewModel();
        loginView = new LoginView(loginViewModel);
        cardPanel.add(loginView, loginView.getViewName());
        return this;
    }

    public AppBuilder addLoggedInView() {
        loggedInViewModel = new LoggedInViewModel();
        loggedInView = new LoggedInView(loggedInViewModel);
        cardPanel.add(loggedInView, loggedInView.getViewName());
        return this;
    }

    public AppBuilder addChangeProfileView() {
        changeProfileViewModel = new ChangeProfileViewModel();

        final ChangeProfileOutputBoundary changeProfileOutputBoundary = new ChangeProfilePresenter(changeProfileViewModel,
                viewManagerModel, homeScreenViewModel);

        changeProfileInteractor = new ChangeProfileInteractor(new MongoDBProfileDataAccessObject(), changeProfileOutputBoundary);
        changeProfileController = new ChangeProfileController(changeProfileInteractor, viewManagerModel);
        changeProfileView = new ChangeProfileView(changeProfileViewModel);
        changeProfileView.setChangeProfileController(changeProfileController);
        cardPanel.add(changeProfileView, changeProfileView.getViewName());
        return this;
    }

    public AppBuilder addHomeScreenUseCase(){
        homeScreenViewModel = new HomeScreenViewModel();
        homeScreenView = new HomeScreenView(homeScreenViewModel);
        final HomeScreenPresenter homeScreenPresenter = new HomeScreenPresenter(homeScreenViewModel);

        approveRejectProfileInteractor = new ApproveRejectProfileInteractor(profileMatchesDataAccessObject);
        recommendProfileInteractor = new RecommendProfileInteractor(profileDataAccessObject, pineconeAccess,
                homeScreenPresenter, profileMatchesDataAccessObject);

        final HomeScreenController homeScreenController =
                new HomeScreenController(approveRejectProfileInteractor, recommendProfileInteractor);
        homeScreenView.setHomeScreenController(homeScreenController);
        cardPanel.add(homeScreenView, homeScreenView.getViewName());
        return this;
    }

    public AppBuilder addSignupUseCase() {
        final SignupOutputBoundary signupOutputBoundary = new SignupPresenter(viewManagerModel,
                signupViewModel, loginViewModel);
        final SignupInputBoundary userSignupInteractor = new SignupInteractor(
                userDataAccessObject, signupOutputBoundary, userFactory);

        SignupController controller = new SignupController(userSignupInteractor);
        signupView.setSignupController(controller);
        return this;
    }

    public AppBuilder addLoginUseCase() {
        final LoginOutputBoundary loginOutputBoundary = new LoginPresenter(viewManagerModel,
                loginViewModel, changeProfileViewModel, homeScreenViewModel);
        final LoginInputBoundary loginInteractor = new LoginInteractor(
                userDataAccessObject, profileDataAccessObject, loginOutputBoundary);

        LoginController loginController = new LoginController(loginInteractor);
        loginView.setLoginController(loginController);
        return this;
    }

    public AppBuilder addChangePasswordUseCase() {
        final ChangePasswordOutputBoundary changePasswordOutputBoundary = new ChangePasswordPresenter(viewManagerModel,
                loggedInViewModel);

        final ChangePasswordInputBoundary changePasswordInteractor =
                new ChangePasswordInteractor(userDataAccessObject, changePasswordOutputBoundary, userFactory);

        ChangePasswordController changePasswordController = new ChangePasswordController(changePasswordInteractor);
        loggedInView.setChangePasswordController(changePasswordController);
        return this;
    }

    /**
     * Adds the Logout Use Case to the application.
     * @return this builder
     */
    public AppBuilder addLogoutUseCase() {
        final LogoutOutputBoundary logoutOutputBoundary = new LogoutPresenter(viewManagerModel,
                loggedInViewModel, loginViewModel);

        final LogoutInputBoundary logoutInteractor =
                new LogoutInteractor(userDataAccessObject, logoutOutputBoundary);

        final LogoutController logoutController = new LogoutController(logoutInteractor);
        loggedInView.setLogoutController(logoutController);
        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("User Login Example");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);
        ViewManagerState state  = viewManagerModel.getState();
        state.setCurrentView(signupView.getViewName());
        viewManagerModel.setState(state);
        viewManagerModel.firePropertyChange();

        return application;
    }


}
