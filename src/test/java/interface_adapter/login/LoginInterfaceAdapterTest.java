package interface_adapter.login;

import interface_adapter.ViewManagerModel;
import interface_adapter.ViewManagerState;
import interface_adapter.create_profile.ChangeProfileViewModel;
import interface_adapter.home_screen.HomeScreenState;
import interface_adapter.home_screen.HomeScreenViewModel;
import org.junit.jupiter.api.Test;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInputData;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginOutputData;

import static org.junit.jupiter.api.Assertions.*;

public class LoginInterfaceAdapterTest {

    // =========================================================
    // Part 1: LoginState (getters / setters)
    // =========================================================

    @Test
    void testLoginState_GettersAndSetters() {
        LoginState state = new LoginState();

        state.setUsername("user1");
        state.setPassword("pass1");
        state.setLoginError("bad login");

        assertEquals("user1", state.getUsername());
        assertEquals("pass1", state.getPassword());
        assertEquals("bad login", state.getLoginError());
    }

    // =========================================================
    // Part 2: LoginViewModel (basic initialization)
    // =========================================================

    @Test
    void testLoginViewModel_Initialization() {
        LoginViewModel viewModel = new LoginViewModel();

        assertEquals("log in", viewModel.getViewName());
        assertNotNull(viewModel.getState());
    }

    // =========================================================
    // Part 3: LoginPresenter (success / fail)
    // =========================================================

    @Test
    void testPresenter_PrepareSuccessView_WithProfile_GoesToHomeScreen() {
        ViewManagerModel viewManagerModel = new ViewManagerModel();
        LoginViewModel loginViewModel = new LoginViewModel();
        ChangeProfileViewModel changeProfileViewModel = new ChangeProfileViewModel();
        HomeScreenViewModel homeScreenViewModel = new HomeScreenViewModel();

        LoginPresenter presenter = new LoginPresenter(
                viewManagerModel,
                loginViewModel,
                changeProfileViewModel,
                homeScreenViewModel
        );

        LoginOutputData outputData = new LoginOutputData(42, "userX", true);

        presenter.prepareSuccessView(outputData);

        ViewManagerState vmState = viewManagerModel.getState();
        assertTrue(vmState.isLoggedIn());
        assertTrue(vmState.hasProfile());
        assertEquals(42, vmState.getUserId());
        assertEquals(homeScreenViewModel.getViewName(), vmState.getCurrentView());

        HomeScreenState homeState = homeScreenViewModel.getState();
        assertEquals(42, homeState.getCurrentId());

        // Login state should be reset (new LoginState)
        assertNotNull(loginViewModel.getState());
    }

    @Test
    void testPresenter_PrepareSuccessView_WithoutProfile_GoesToChangeProfile() {
        ViewManagerModel viewManagerModel = new ViewManagerModel();
        LoginViewModel loginViewModel = new LoginViewModel();
        ChangeProfileViewModel changeProfileViewModel = new ChangeProfileViewModel();
        HomeScreenViewModel homeScreenViewModel = new HomeScreenViewModel();

        LoginPresenter presenter = new LoginPresenter(
                viewManagerModel,
                loginViewModel,
                changeProfileViewModel,
                homeScreenViewModel
        );

        LoginOutputData outputData = new LoginOutputData(7, "userY", false);

        presenter.prepareSuccessView(outputData);

        ViewManagerState vmState = viewManagerModel.getState();
        assertTrue(vmState.isLoggedIn());
        assertFalse(vmState.hasProfile());
        assertEquals(7, vmState.getUserId());
        assertEquals(changeProfileViewModel.getViewName(), vmState.getCurrentView());
    }

    @Test
    void testPresenter_PrepareFailView_SetsLoginError() {
        ViewManagerModel viewManagerModel = new ViewManagerModel();
        LoginViewModel loginViewModel = new LoginViewModel();
        ChangeProfileViewModel changeProfileViewModel = new ChangeProfileViewModel();
        HomeScreenViewModel homeScreenViewModel = new HomeScreenViewModel();

        LoginPresenter presenter = new LoginPresenter(
                viewManagerModel,
                loginViewModel,
                changeProfileViewModel,
                homeScreenViewModel
        );

        String error = "Invalid credentials";
        presenter.prepareFailView(error);

        LoginState state = loginViewModel.getState();
        assertEquals(error, state.getLoginError());
    }

    // =========================================================
    // Part 4: LoginController (dispatching to interactor)
    // =========================================================

    class FakeLoginInteractor implements LoginInputBoundary {
        boolean executeCalled = false;
        LoginInputData receivedData;

        @Override
        public void execute(LoginInputData loginInputData) {
            executeCalled = true;
            receivedData = loginInputData;
        }
    }

    @Test
    void testController_Execute_PassesDataToInteractor() {
        FakeLoginInteractor fakeInteractor = new FakeLoginInteractor();
        LoginController controller = new LoginController(fakeInteractor);

        controller.execute("userA", "passA");

        assertTrue(fakeInteractor.executeCalled);
        assertNotNull(fakeInteractor.receivedData);
        assertEquals("userA", fakeInteractor.receivedData.getUsername());
        assertEquals("passA", fakeInteractor.receivedData.getPassword());
    }
}
