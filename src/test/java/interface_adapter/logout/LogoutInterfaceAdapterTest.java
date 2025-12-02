package interface_adapter.logout;

import interface_adapter.ViewManagerModel;
import interface_adapter.ViewManagerState;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginViewModel;
import org.junit.jupiter.api.Test;
import use_case.logout.LogoutInputBoundary;
import use_case.logout.LogoutOutputBoundary;

import static org.junit.jupiter.api.Assertions.*;

public class LogoutInterfaceAdapterTest {

    // =========================================================
    // Part 1: LogoutController (dispatch to interactor)
    // =========================================================

    class FakeLogoutInteractor implements LogoutInputBoundary {
        boolean executeCalled = false;

        @Override
        public void execute() {
            executeCalled = true;
        }
    }

    @Test
    void testController_Execute_CallsInteractor() {
        FakeLogoutInteractor fakeInteractor = new FakeLogoutInteractor();
        LogoutController controller = new LogoutController(fakeInteractor);
        controller.execute();
        assertTrue(fakeInteractor.executeCalled);
    }

    // =========================================================
    // Part 2: LogoutPresenter (success handling)
    // =========================================================

    @Test
    void testPresenter_PrepareSuccessView_ResetsStateAndSwitchesView() {
        ViewManagerModel viewManagerModel = new ViewManagerModel();
        LoggedInViewModel loggedInViewModel = new LoggedInViewModel();
        LoginViewModel loginViewModel = new LoginViewModel();

        // ensure loggedInViewModel starts with non-empty username
        LoggedInState loggedState = loggedInViewModel.getState();
        loggedState.setUsername("loggedUser");
        loggedInViewModel.setState(loggedState);

        LogoutPresenter presenter =
                new LogoutPresenter(viewManagerModel, loggedInViewModel, loginViewModel);

        presenter.prepareSuccessView();

        // username should now be cleared
        assertEquals("", loggedInViewModel.getState().getUsername());

        // view manager should switch to login view
        ViewManagerState vmState = viewManagerModel.getState();
        assertEquals(loginViewModel.getViewName(), vmState.getCurrentView());
    }
}
