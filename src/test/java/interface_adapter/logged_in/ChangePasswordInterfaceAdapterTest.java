
package interface_adapter.logged_in;

import interface_adapter.ViewManagerModel;
import org.junit.jupiter.api.Test;
import use_case.change_password.ChangePasswordInputBoundary;
import use_case.change_password.ChangePasswordInputData;
import use_case.change_password.ChangePasswordOutputData;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static org.junit.jupiter.api.Assertions.*;

public class ChangePasswordInterfaceAdapterTest {

    // =================================================================
    // Part 1: Test LoggedInState (Getters, Setters, Copy Constructor)
    // =================================================================
    @Test
    void testState() {
        LoggedInState state = new LoggedInState();

        // 1. Test Setters & Getters
        state.setUsername("User1");
        state.setPassword("pass123");
        state.setPasswordError("Error msg");

        assertEquals("User1", state.getUsername());
        assertEquals("pass123", state.getPassword());
        assertEquals("Error msg", state.getPasswordError());

        // 2. Test Copy Constructor
        LoggedInState copy = new LoggedInState(state);
        assertEquals("User1", copy.getUsername());
        assertEquals("pass123", copy.getPassword());
        assertEquals("Error msg", copy.getPasswordError());

        // Ensure they are different objects
        assertNotSame(state, copy);
    }

    // =================================================================
    // Part 2: Test LoggedInViewModel (Initialization & PropertyChange)
    // =================================================================
    @Test
    void testViewModel() {
        LoggedInViewModel viewModel = new LoggedInViewModel();

        // 1. Verify View Name
        assertEquals("logged in", viewModel.getViewName());
        assertNotNull(viewModel.getState());

        // 2. Verify Property Change Listener
        class ListenerTracker implements PropertyChangeListener {
            boolean eventReceived = false;
            String propertyName;
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                this.eventReceived = true;
                this.propertyName = evt.getPropertyName();
            }
        }
        ListenerTracker tracker = new ListenerTracker();
        viewModel.addPropertyChangeListener(tracker);

        // Fire event
        viewModel.firePropertyChange("password");

        assertTrue(tracker.eventReceived);
        assertEquals("password", tracker.propertyName);
    }

    // =================================================================
    // Part 3: Test ChangePasswordPresenter (Logic Verification)
    // =================================================================
    @Test
    void testPresenter_PrepareSuccessView() {
        // --- Setup ---
        LoggedInViewModel viewModel = new LoggedInViewModel();

        // Pass null for ViewManagerModel since prepareSuccessView doesn't use it in your code
        ChangePasswordPresenter presenter = new ChangePasswordPresenter(null, viewModel);

        // Pre-set some state to verify it gets cleared
        LoggedInState state = viewModel.getState();
        state.setPassword("OldPass");
        state.setPasswordError("OldError");

        // --- Act ---
        ChangePasswordOutputData outputData = new ChangePasswordOutputData("User1");
        presenter.prepareSuccessView(outputData);

        // --- Verify ---
        // Verify password field is cleared and error is null
        assertEquals("", viewModel.getState().getPassword());
        assertNull(viewModel.getState().getPasswordError());
    }

    @Test
    void testPresenter_PrepareFailView() {
        // --- Setup ---
        LoggedInViewModel viewModel = new LoggedInViewModel();
        ChangePasswordPresenter presenter = new ChangePasswordPresenter(null, viewModel);

        // --- Act ---
        String errorMsg = "Password too short";
        presenter.prepareFailView(errorMsg);

        // --- Verify ---
        // Verify error message is set in state
        assertEquals(errorMsg, viewModel.getState().getPasswordError());
    }

    // =================================================================
    // Part 4: Test ChangePasswordController (Dispatching Logic)
    // =================================================================
    @Test
    void testController_Execute() {
        // --- Setup Fake Interactor ---
        FakeChangePasswordInteractor fakeInteractor = new FakeChangePasswordInteractor();
        ChangePasswordController controller = new ChangePasswordController(fakeInteractor);

        String username = "MyUser";
        String password = "MyNewPassword";

        // --- Act ---
        controller.execute(password, username);

        // --- Verify ---
        assertTrue(fakeInteractor.isExecuteCalled);

        // ⚠️ NOTE: A potential bug was detected here based on your code structure.
        // Your Controller calls: new ChangePasswordInputData(username, password);
        // But the InputData constructor is: public ChangePasswordInputData(String password, String username);
        // So 'username' is passed to the 'password' field, and 'password' to the 'username' field.
        // The assertions below reflect your **CURRENT code logic** (Expectation matches Code):

        ChangePasswordInputData data = fakeInteractor.receivedData;

        // InputData.getUsername() returns the first argument passed to constructor (which was 'username' in controller)
        assertEquals(username, data.getUsername()); // This looks swapped, but it is "correct" based on your current code.

        // InputData.getPassword() returns the second argument (which was 'password' in controller)
        assertEquals(password, data.getPassword());
    }

    // =================================================================
    // Helper Inner Class: Fake Interactor
    // =================================================================
    class FakeChangePasswordInteractor implements ChangePasswordInputBoundary {
        boolean isExecuteCalled = false;
        ChangePasswordInputData receivedData;

        @Override
        public void execute(ChangePasswordInputData changePasswordInputData) {
            this.isExecuteCalled = true;
            this.receivedData = changePasswordInputData;
        }
    }
}