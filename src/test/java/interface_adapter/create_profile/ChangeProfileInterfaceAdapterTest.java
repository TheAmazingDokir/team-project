package interface_adapter.create_profile;

import interface_adapter.ViewManagerModel;
import interface_adapter.ViewManagerState;
import interface_adapter.home_screen.HomeScreenState;
import interface_adapter.home_screen.HomeScreenViewModel;
import org.junit.jupiter.api.Test;
import use_case.change_profile_info.ChangeProfileInputBoundary;
import use_case.change_profile_info.ChangeProfileInputData;
import use_case.change_profile_info.ChangeProfileOutputData;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static org.junit.jupiter.api.Assertions.*;

public class ChangeProfileInterfaceAdapterTest {

    // =================================================================
    // Part 1: Test ChangeProfileState (Getters & Setters)
    // =================================================================
    @Test
    void testState_GettersAndSetters() {
        ChangeProfileState state = new ChangeProfileState();

        // 1. Set Data
        state.setProfileUsername("User123");
        state.setEmail("test@email.com");
        state.setPhoneNumber("123-456");
        state.setSummary("Bio Summary");
        state.setProfile("Full Profile Text");
        state.setIsEmployer(true);
        state.setErrorMessage("Error occurred");

        // 2. Verify Data
        assertEquals("User123", state.getProfileUsername());
        assertEquals("test@email.com", state.getEmail());
        assertEquals("123-456", state.getPhoneNumber());
        assertEquals("Bio Summary", state.getSummary());
        assertEquals("Full Profile Text", state.getProfile());
        assertTrue(state.getIsEmployer());
        assertEquals("Error occurred", state.getErrorMessage());
    }

    // =================================================================
    // Part 2: Test ChangeProfileViewModel (Initialization & PropertyChange)
    // =================================================================
    @Test
    void testViewModel_InitializationAndNotification() {
        ChangeProfileViewModel viewModel = new ChangeProfileViewModel();

        // 1. Verify View Name
        assertEquals("change profile", viewModel.getViewName());
        assertNotNull(viewModel.getState());

        // 2. Verify Property Change Listener
        class ListenerTracker implements PropertyChangeListener {
            boolean eventReceived = false;
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                this.eventReceived = true;
            }
        }
        ListenerTracker tracker = new ListenerTracker();
        viewModel.addPropertyChangeListener(tracker);

        // Fire event
        viewModel.firePropertyChange();

        assertTrue(tracker.eventReceived, "ViewModel should notify listeners when property changes");
    }

    // =================================================================
    // Part 3: Test ChangeProfilePresenter (Success & Fail Views)
    // =================================================================
    @Test
    void testPresenter_PrepareSuccessView() {
        // --- Setup ---
        ChangeProfileViewModel changeProfileViewModel = new ChangeProfileViewModel();
        ViewManagerModel viewManagerModel = new ViewManagerModel();
        HomeScreenViewModel homeScreenViewModel = new HomeScreenViewModel();

        ChangeProfilePresenter presenter = new ChangeProfilePresenter(
                changeProfileViewModel,
                viewManagerModel,
                homeScreenViewModel
        );

        // --- Act ---
        // Simulate output data (User ID 55 successful update)
        ChangeProfileOutputData outputData = new ChangeProfileOutputData(55);
        presenter.prepareSuccessView(outputData);

        // --- Verify ViewManagerModel State ---
        // It should switch to the Home Screen View
        ViewManagerState vmState = viewManagerModel.getState();
        assertTrue(vmState.isLoggedIn());
        assertEquals(55, vmState.getUserId());
        assertEquals(homeScreenViewModel.getViewName(), vmState.getCurrentView());

        // --- Verify HomeScreenViewModel State ---
        // It should have the current user ID set
        HomeScreenState homeState = homeScreenViewModel.getState();
        assertEquals(55, homeState.getCurrentId());

        // --- Verify ChangeProfileViewModel State ---
        // Ideally, it resets the state (checking for non-null mainly)
        assertNotNull(changeProfileViewModel.getState());
    }

    @Test
    void testPresenter_PrepareFailView() {
        // --- Setup ---
        ChangeProfileViewModel changeProfileViewModel = new ChangeProfileViewModel();
        // Nulls for others as they are not used in fail view logic
        ChangeProfilePresenter presenter = new ChangeProfilePresenter(
                changeProfileViewModel,
                null,
                null
        );

        // --- Act ---
        String errorMsg = "Update Failed";
        presenter.prepareFailView(errorMsg);

        // --- Verify ---
        // Error message should be in the state
        assertEquals(errorMsg, changeProfileViewModel.getState().getErrorMessage());
    }

    // =================================================================
    // Part 4: Test ChangeProfileController (Dispatching Logic)
    // =================================================================
    @Test
    void testController_Execute() {
        // --- 1. Setup Fake Interactor ---
        FakeChangeProfileInteractor fakeInteractor = new FakeChangeProfileInteractor();

        // --- 2. Setup ViewManagerModel ---
        ViewManagerModel viewManagerModel = new ViewManagerModel();

        // Create state object
        // Note: The parameter order must match your ViewManagerState constructor
        ViewManagerState vmState = new ViewManagerState(true, 99, true, "some view");

        // [Critical Fix] Explicitly call setter to ensure ID is set correctly
        vmState.setUserId(99);

        // Set state to ViewModel
        viewManagerModel.setState(vmState);

        // Debug print: Check console during test execution to verify if 99 is printed
        System.out.println("Debug: UserID in ViewModel is " + viewManagerModel.getState().getUserId());

        ChangeProfileController controller = new ChangeProfileController(fakeInteractor, viewManagerModel);

        // --- 3. Act ---
        controller.execute(
                "NewUser",
                "new@email.com",
                "999-9999",
                "New Summary",
                "New Full Profile",
                true
        );

        // --- 4. Verify ---
        assertTrue(fakeInteractor.isExecuteCalled);

        // Check if data was constructed correctly
        ChangeProfileInputData capturedData = fakeInteractor.receivedData;
        assertEquals("NewUser", capturedData.getProfileUsername());
        assertEquals("new@email.com", capturedData.getEmail());
        assertEquals("999-9999", capturedData.getPhoneNumber());
        assertEquals("New Summary", capturedData.getSummary());
        assertEquals("New Full Profile", capturedData.getProfile());
        assertTrue(capturedData.getIsEmployer());

        // CRITICAL CHECK
        // If this fails, check ChangeProfileInputData constructor to ensure 'this.profileID = profileID' is present.
        // Also ensures the Controller correctly pulled the ID (99) from the ViewManagerModel.
        assertEquals(99, capturedData.getProfileID());
    }

    // =================================================================
    // Helper Inner Class: Fake Interactor
    // =================================================================
    class FakeChangeProfileInteractor implements ChangeProfileInputBoundary {
        boolean isExecuteCalled = false;
        ChangeProfileInputData receivedData;

        @Override
        public void execute(ChangeProfileInputData changeProfileInputData) {
            this.isExecuteCalled = true;
            this.receivedData = changeProfileInputData;
        }
    }
}