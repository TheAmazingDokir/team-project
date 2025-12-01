package interface_adapter.match_page;

import entity.EmployerProfile;
import entity.UserProfile;
import org.junit.jupiter.api.Test;
import use_case.update_matches.IUpdateMatchesInputBoundary;
import use_case.update_matches.UpdateMatchesInputData;
import use_case.update_matches.UpdateMatchesOutputData;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class MatchPageInterfaceAdapterTest {

    // =================================================================
    // Part 1: Test MatchPageState (Getters and Setters)
    // =================================================================
    @Test
    void testState_GettersAndSetters() {
        MatchPageState state = new MatchPageState();

        // 1. Prepare Data
        Integer currentId = 123;
        String name = "John Doe";
        String email = "john@example.com";
        String phone = "555-0199";
        String resume = "Experienced Java Developer";

        ArrayList<UserProfile> matches = new ArrayList<>();
        ArrayList<String> matchNames = new ArrayList<>();
        matchNames.add("Jane");

        // 2. Set Data
        state.setCurrentId(currentId);
        state.setName(name);
        state.setEmail(email);
        state.setPhone(phone);
        state.setResume(resume);
        state.setUserMatches(matches);
        state.setUserMatchesName(matchNames);

        // 3. Verify Data
        assertEquals(currentId, state.getCurrentId());
        assertEquals(name, state.getName());
        assertEquals(email, state.getEmail());
        assertEquals(phone, state.getPhone());
        assertEquals(resume, state.getResume());
        assertEquals(matches, state.getUserMatches());
        assertEquals(matchNames, state.getUserMatchesName());
    }

    // =================================================================
    // Part 2: Test MatchPageViewModel (Initialization & Notifications)
    // =================================================================
    @Test
    void testViewModel_InitializationAndNotification() {
        MatchPageViewModel viewModel = new MatchPageViewModel();

        // 1. Verify View Name
        assertEquals("Match Page", viewModel.getViewName());
        assertNotNull(viewModel.getState());

        // 2. Verify State Change
        MatchPageState newState = new MatchPageState();
        newState.setName("New Name");
        viewModel.setState(newState);
        assertEquals("New Name", viewModel.getState().getName());

        // 3. Verify Property Change Listener
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
    // Part 3: Test MatchPagePresenter (Logic Verification)
    // =================================================================
    @Test
    void testPresenter_PrepareSuccessView() {
        // --- Setup ---
        MatchPageViewModel viewModel = new MatchPageViewModel();
        MatchPagePresenter presenter = new MatchPagePresenter(viewModel);

        // Create a list of UserProfiles to simulate data from Interactor
        ArrayList<UserProfile> profiles = new ArrayList<>();
        EmployerProfile user1 = new EmployerProfile(1, "e1@test.com", "111", "UserOne");
        EmployerProfile user2 = new EmployerProfile(2, "e2@test.com", "222", "UserTwo");
        profiles.add(user1);
        profiles.add(user2);

        UpdateMatchesOutputData outputData = new UpdateMatchesOutputData(profiles);

        // --- Act ---
        presenter.prepareSuccessView(outputData);

        // --- Verify ---
        MatchPageState state = viewModel.getState();

        // 1. Verify user objects are stored
        assertEquals(2, state.getUserMatches().size());

        // 2. Verify logic: Extracting names into a separate list
        assertEquals(2, state.getUserMatchesName().size());
        assertEquals("UserOne", state.getUserMatchesName().get(0));
        assertEquals("UserTwo", state.getUserMatchesName().get(1));
    }

    @Test
    void testPresenter_UpdateInfo() {
        // --- Setup ---
        MatchPageViewModel viewModel = new MatchPageViewModel();
        MatchPagePresenter presenter = new MatchPagePresenter(viewModel);

        // Pre-requisite: The state must ALREADY have matches loaded to search through
        ArrayList<UserProfile> profiles = new ArrayList<>();
        EmployerProfile targetUser = new EmployerProfile(55, "target@email.com", "999-999", "TargetUser");

        // FIX: In your EmployerProfile code, getFullProfileAsString() returns jobApplicationSummary.
        // So we must use setJobApplicationSummary here to ensure the value is not null.
        targetUser.setJobApplicationSummary("My Full Resume Details");

        profiles.add(targetUser);

        // Manually set the state (simulating that prepareSuccessView ran previously)
        MatchPageState preState = new MatchPageState();
        preState.setUserMatches(profiles);
        viewModel.setState(preState);

        // --- Act ---
        // Simulate clicking on "TargetUser" to get their info
        UpdateMatchesOutputData outputData = new UpdateMatchesOutputData("TargetUser");
        presenter.updateInfo(outputData);

        // --- Verify ---
        MatchPageState finalState = viewModel.getState();

        // Verify specific user details were extracted and set in State
        assertEquals("TargetUser", finalState.getName());
        assertEquals("target@email.com", finalState.getEmail());
        assertEquals("999-999", finalState.getPhone());
        assertEquals("My Full Resume Details", finalState.getResume());
    }

    @Test
    void testPresenter_UpdateInfo_UserNotFound() {
        // --- Setup ---
        MatchPageViewModel viewModel = new MatchPageViewModel();
        MatchPagePresenter presenter = new MatchPagePresenter(viewModel);

        // State has users, but NOT the one we are looking for
        ArrayList<UserProfile> profiles = new ArrayList<>();
        profiles.add(new EmployerProfile(1, "a", "b", "ExistingUser"));

        MatchPageState preState = new MatchPageState();
        preState.setUserMatches(profiles);
        viewModel.setState(preState);

        // --- Act ---
        // Try to find a ghost user
        UpdateMatchesOutputData outputData = new UpdateMatchesOutputData("GhostUser");
        presenter.updateInfo(outputData);

        // --- Verify ---
        // Verify state did NOT change (fields should be null)
        assertNull(viewModel.getState().getName());
        assertNull(viewModel.getState().getEmail());
    }

    // =================================================================
    // Part 4: Test MatchPageController (Dispatching Logic)
    // =================================================================
    @Test
    void testController_Dispatch() {
        // --- Setup Fake Interactor ---
        FakeUpdateMatchesInteractor fakeInteractor = new FakeUpdateMatchesInteractor();
        MatchPageController controller = new MatchPageController(fakeInteractor);

        // --- Test 1: userIdtoUserMatchesProfiles ---
        controller.userIdtoUserMatchesProfiles(100);

        assertTrue(fakeInteractor.isGetMatchesCalled);
        assertEquals(100, fakeInteractor.receivedId);

        // --- Test 2: getInfo ---
        controller.getInfo("Alice");

        assertTrue(fakeInteractor.isGetInfoCalled);
        assertEquals("Alice", fakeInteractor.receivedName);
    }

    // =================================================================
    // Helper Inner Class: Fake Interactor for Controller Test
    // =================================================================
    class FakeUpdateMatchesInteractor implements IUpdateMatchesInputBoundary {
        boolean isGetMatchesCalled = false;
        boolean isGetInfoCalled = false;
        int receivedId;
        String receivedName;

        @Override
        public void updateMatch(UpdateMatchesInputData updateMatchesInputData) {
            // Not used by this controller
        }

        @Override
        public void userIdtoUserMatchesProfiles(UpdateMatchesInputData updateMatchesInputData) {
            this.isGetMatchesCalled = true;
            this.receivedId = updateMatchesInputData.getCurrentId();
        }

        @Override
        public void getInfo(UpdateMatchesInputData updateMatchesInputData) {
            this.isGetInfoCalled = true;
            this.receivedName = updateMatchesInputData.getCurrentName();
        }
    }
}