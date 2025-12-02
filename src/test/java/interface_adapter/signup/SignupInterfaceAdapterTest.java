package interface_adapter.signup;

import interface_adapter.ViewManagerModel;
import interface_adapter.ViewManagerState;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;
import org.junit.jupiter.api.Test;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInputData;
import use_case.signup.SignupOutputData;

import static org.junit.jupiter.api.Assertions.*;

public class SignupInterfaceAdapterTest {

    // =========================================================
    // Part 1: SignupState (getters / setters)
    // =========================================================

    @Test
    void testSignupState_GettersAndSetters() {
        SignupState state = new SignupState();

        state.setUsername("user1");
        state.setUsernameError("name error");
        state.setPassword("pass1");
        state.setPasswordError("pass error");
        state.setRepeatPassword("pass1");
        state.setRepeatPasswordError("repeat error");

        assertEquals("user1", state.getUsername());
        assertEquals("name error", state.getUsernameError());
        assertEquals("pass1", state.getPassword());
        assertEquals("pass error", state.getPasswordError());
        assertEquals("pass1", state.getRepeatPassword());
        assertEquals("repeat error", state.getRepeatPasswordError());
    }

    // =========================================================
    // Part 2: SignupViewModel (basic initialization)
    // =========================================================

    @Test
    void testSignupViewModel_Initialization() {
        SignupViewModel viewModel = new SignupViewModel();

        assertEquals("sign up", viewModel.getViewName());
        assertNotNull(viewModel.getState());
    }

    // =========================================================
    // Part 3: SignupPresenter (success / fail / switch)
    // =========================================================

    @Test
    void testPresenter_PrepareSuccessView_SetsLoginAndSwitchesView() {
        ViewManagerModel viewManagerModel = new ViewManagerModel();
        SignupViewModel signupViewModel = new SignupViewModel();
        LoginViewModel loginViewModel = new LoginViewModel();

        SignupPresenter presenter =
                new SignupPresenter(viewManagerModel, signupViewModel, loginViewModel);

        SignupOutputData outputData = new SignupOutputData("newUser");

        presenter.prepareSuccessView(outputData);

        // Login state should have username set
        LoginState loginState = loginViewModel.getState();
        assertEquals("newUser", loginState.getUsername());

        // View manager should switch to login view
        ViewManagerState vmState = viewManagerModel.getState();
        assertEquals(loginViewModel.getViewName(), vmState.getCurrentView());
    }

    @Test
    void testPresenter_PrepareFailView_SetsErrorOnSignupState() {
        ViewManagerModel viewManagerModel = new ViewManagerModel();
        SignupViewModel signupViewModel = new SignupViewModel();
        LoginViewModel loginViewModel = new LoginViewModel();

        SignupPresenter presenter =
                new SignupPresenter(viewManagerModel, signupViewModel, loginViewModel);

        String error = "Username already taken";
        presenter.prepareFailView(error);

        SignupState state = signupViewModel.getState();
        assertEquals(error, state.getUsernameError());
    }

    @Test
    void testPresenter_SwitchToLoginView_ChangesCurrentView() {
        ViewManagerModel viewManagerModel = new ViewManagerModel();
        SignupViewModel signupViewModel = new SignupViewModel();
        LoginViewModel loginViewModel = new LoginViewModel();

        SignupPresenter presenter =
                new SignupPresenter(viewManagerModel, signupViewModel, loginViewModel);

        // Ensure starting from some non-login view
        ViewManagerState initialState = viewManagerModel.getState();
        initialState.setCurrentView("sign up");
        viewManagerModel.setState(initialState);

        presenter.switchToLoginView();

        ViewManagerState vmState = viewManagerModel.getState();
        assertEquals(loginViewModel.getViewName(), vmState.getCurrentView());
    }

    // =========================================================
    // Part 4: SignupController (dispatching to interactor)
    // =========================================================

    class FakeSignupInteractor implements SignupInputBoundary {
        boolean executeCalled = false;
        boolean switchToLoginCalled = false;
        SignupInputData receivedData;

        @Override
        public void execute(SignupInputData signupInputData) {
            executeCalled = true;
            receivedData = signupInputData;
        }

        @Override
        public void switchToLoginView() {
            switchToLoginCalled = true;
        }
    }

    @Test
    void testController_Execute_PassesDataToInteractor() {
        FakeSignupInteractor fakeInteractor = new FakeSignupInteractor();
        SignupController controller = new SignupController(fakeInteractor);

        controller.execute("userA", "passA", "passA");

        assertTrue(fakeInteractor.executeCalled);
        assertNotNull(fakeInteractor.receivedData);
        assertEquals("userA", fakeInteractor.receivedData.getUsername());
        assertEquals("passA", fakeInteractor.receivedData.getPassword());
        assertEquals("passA", fakeInteractor.receivedData.getRepeatPassword());
    }

    @Test
    void testController_SwitchToLoginView_CallsInteractor() {
        FakeSignupInteractor fakeInteractor = new FakeSignupInteractor();
        SignupController controller = new SignupController(fakeInteractor);

        controller.switchToLoginView();

        assertTrue(fakeInteractor.switchToLoginCalled);
    }
}
