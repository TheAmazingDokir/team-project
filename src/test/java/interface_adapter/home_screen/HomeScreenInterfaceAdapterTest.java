package interface_adapter.home_screen;

import entity.EmployerProfile;
import org.junit.jupiter.api.Test;
import use_case.approve_reject_profile.ApproveRejectProfileInputBoundary;
import use_case.approve_reject_profile.ApproveRejectProfileInputData;
import use_case.approve_reject_profile.ApproveRejectProfileOutputData;
import use_case.recommend_profile.RecommendProfileInputData;
import use_case.recommend_profile.RecommendProfileInteractor;
import use_case.recommend_profile.RecommendProfileOutputData;
import use_case.update_matches.IUpdateMatchesInputBoundary;
import use_case.update_matches.UpdateMatchesInputData;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static org.junit.jupiter.api.Assertions.*;

public class HomeScreenInterfaceAdapterTest {

    // =================================================================
    // Part 1: Test HomeScreenState (Basic Data Container)
    // =================================================================
    @Test
    void testState_GettersAndSetters() {
        HomeScreenState state = new HomeScreenState();

        // 1. Test Setters
        state.setCurrentId(100);
        state.setIsApprove(true);
        state.setOtherId(200);
        state.setOtherProfileName("Alice");
        state.setOtherProfileSummary("Java Dev");

        // 2. Test Getters
        assertEquals(100, state.getCurrentId());
        assertTrue(state.getIsApprove());
        assertEquals(200, state.getOtherId());
        assertEquals("Alice", state.getOtherProfileName());
        assertEquals("Java Dev", state.getOtherProfileSummary());
    }

    // =================================================================
    // Part 2: Test HomeScreenViewModel (State Management & Notification)
    // =================================================================
    @Test
    void testViewModel_InitializationAndNotification() {
        HomeScreenViewModel viewModel = new HomeScreenViewModel();

        // 1. Check Initial State
        assertEquals("home screen", viewModel.getViewName());
        assertNotNull(viewModel.getState());

        // 2. Test State Update
        HomeScreenState newState = new HomeScreenState();
        newState.setCurrentId(999);
        viewModel.setState(newState);
        assertEquals(999, viewModel.getState().getCurrentId());

        // 3. Test Property Change Listener (Verify firePropertyChange works)
        // Create a tracker to record if the listener was fired
        class ListenerTracker implements PropertyChangeListener {
            boolean eventReceived = false;
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                this.eventReceived = true;
                assertEquals("state", evt.getPropertyName());
            }
        }
        ListenerTracker tracker = new ListenerTracker();

        viewModel.addPropertyChangeListener(tracker);
        viewModel.firePropertyChange();

        assertTrue(tracker.eventReceived, "ViewModel should notify listeners when property changes");
    }

    // =================================================================
    // Part 3: Test HomeScreenPresenter (Format Data & Update ViewModel)
    // =================================================================
    @Test
    void testPresenter_PrepareSuccessViews() {
        // --- Setup ---
        HomeScreenViewModel viewModel = new HomeScreenViewModel();
        HomeScreenPresenter presenter = new HomeScreenPresenter(viewModel);

        // --- Test 1: Recommend Profile Success View ---
        // Create fake profile data using EmployerProfile as an example
        EmployerProfile fakeProfile = new EmployerProfile(55, "e@mail.com", "123", "Boss");
        fakeProfile.setJobApplicationFull("We need a dev");
        // Note: Presenter uses getSummaryProfileAsString(), which maps to Full Profile in Employer entity logic

        RecommendProfileOutputData recOutput = new RecommendProfileOutputData(fakeProfile);

        // Act
        presenter.prepareSuccessView(recOutput);

        // Verify that the ViewModel state was updated correctly
        HomeScreenState state = viewModel.getState();
        assertEquals(55, state.getOtherId());
        assertEquals("Boss", state.getOtherProfileName());
        assertEquals("We need a dev", state.getOtherProfileSummary());

        // --- Test 2: Approve/Reject Success View ---
        ApproveRejectProfileOutputData arOutput = new ApproveRejectProfileOutputData(77);

        // Act
        presenter.prepareSuccessView(arOutput);

        // Verify that the current ID in state was updated
        assertEquals(77, viewModel.getState().getCurrentId());
    }

    // =================================================================
    // Part 4: Test HomeScreenController (User Input Dispatching)
    // =================================================================
    @Test
    void testController_ExecutionFlow() {
        // --- 1. Setup Fakes ---
        FakeApproveRejectInteractor fakeApprove = new FakeApproveRejectInteractor();
        FakeRecommendInteractor fakeRecommend = new FakeRecommendInteractor();
        FakeUpdateMatchesInteractor fakeUpdate = new FakeUpdateMatchesInteractor();

        HomeScreenController controller = new HomeScreenController(fakeApprove, fakeRecommend, fakeUpdate);

        // --- 2. Test execute(3 args) - e.g., Clicking Approve/Reject button ---
        // Simulating: Current ID 10, Other ID 20, Approved = true
        controller.execute(10, 20, true);

        // Verify ApproveReject was called with correct data
        assertTrue(fakeApprove.isCalled);
        assertEquals(10, fakeApprove.receivedCurrentId);
        assertEquals(20, fakeApprove.receivedOtherId);
        assertTrue(fakeApprove.receivedDecision);

        // Verify UpdateMatches was called
        assertTrue(fakeUpdate.isUpdateMatchCalled);

        // Verify Recommend was called (to show the next profile after clicking)
        assertTrue(fakeRecommend.isCalled);

        // --- 3. Test execute(1 arg) - e.g., Initial Screen Load ---
        // Reset the fakes first
        fakeApprove.isCalled = false;
        fakeUpdate.isUpdateMatchCalled = false;
        fakeRecommend.isCalled = false;

        controller.execute(99);

        // Verify only Recommend is called
        assertTrue(fakeRecommend.isCalled);
        assertFalse(fakeApprove.isCalled);
        assertFalse(fakeUpdate.isUpdateMatchCalled);
    }


    // =================================================================
    // Helper Inner Classes (Fakes for Controller Test)
    // =================================================================

    // Fake ApproveReject Interactor
    class FakeApproveRejectInteractor implements ApproveRejectProfileInputBoundary {
        boolean isCalled = false;
        int receivedCurrentId;
        int receivedOtherId;
        boolean receivedDecision;

        @Override
        public void execute(ApproveRejectProfileInputData inputData) {
            this.isCalled = true;
            this.receivedCurrentId = inputData.getCurrentProfileId();
            this.receivedOtherId = inputData.getOtherProfileId();
            this.receivedDecision = inputData.getIsApproved();
        }
    }

    // Fake UpdateMatches Interactor
    class FakeUpdateMatchesInteractor implements IUpdateMatchesInputBoundary {
        boolean isUpdateMatchCalled = false;

        @Override
        public void updateMatch(UpdateMatchesInputData inputData) {
            this.isUpdateMatchCalled = true;
        }

        @Override
        public void userIdtoUserMatchesProfiles(UpdateMatchesInputData inputData) {}
        @Override
        public void getInfo(UpdateMatchesInputData inputData) {}
    }

    // Fake Recommend Interactor
    // TRICK: Extends the concrete class to satisfy the Controller's constructor requirement
    class FakeRecommendInteractor extends RecommendProfileInteractor {
        boolean isCalled = false;

        public FakeRecommendInteractor() {
            // Pass nulls to super constructor since we override the execute method anyway.
            // This avoids executing real logic that would crash on nulls.
            super(null, null, null, null);
        }

        @Override
        public void execute(RecommendProfileInputData inputData) {
            this.isCalled = true;
        }
    }
}