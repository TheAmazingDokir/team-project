package use_case.approve_reject_profile;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ApproveRejectProfileInteractorManualTest {

    @Test
    void testExecute_ApproveProfile() {
        // --- 1. Create a fake DAO manually ---
        // A simple class to track if methods were called
        class ExecutionTracker {
            boolean approveCalled = false;
            boolean rejectCalled = false;
        }
        ExecutionTracker tracker = new ExecutionTracker();

        ApproveRejectProfileMatchesDataAccessInterface fakeDao = new ApproveRejectProfileMatchesDataAccessInterface() {
            @Override
            public void approveProfile(int currentProfileId, int otherProfileId) {
                // Check if the IDs passed are correct
                if (currentProfileId == 10 && otherProfileId == 20) {
                    tracker.approveCalled = true;
                }
            }

            @Override
            public void rejectProfile(int currentProfileId, int otherProfileId) {
                tracker.rejectCalled = true;
            }
        };

        // --- 2. Initialize Interactor ---
        // FIX: Only passing fakeDao, because your original code only takes one argument
        ApproveRejectProfileInteractor interactor = new ApproveRejectProfileInteractor(fakeDao);

        // --- 3. Execute with isApproved = true ---
        ApproveRejectProfileInputData inputData = new ApproveRejectProfileInputData(10, 20, true);
        interactor.execute(inputData);

        // --- 4. Verify results ---
        assertTrue(tracker.approveCalled, "approveProfile should have been called in the DAO");
        assertFalse(tracker.rejectCalled, "rejectProfile should NOT have been called");
    }

    @Test
    void testExecute_RejectProfile() {
        // --- 1. Setup Tracker and Fake DAO ---
        class ExecutionTracker {
            boolean approveCalled = false;
            boolean rejectCalled = false;
        }
        ExecutionTracker tracker = new ExecutionTracker();

        ApproveRejectProfileMatchesDataAccessInterface fakeDao = new ApproveRejectProfileMatchesDataAccessInterface() {
            @Override
            public void approveProfile(int currentProfileId, int otherProfileId) {
                tracker.approveCalled = true;
            }

            @Override
            public void rejectProfile(int currentProfileId, int otherProfileId) {
                if (currentProfileId == 10 && otherProfileId == 20) {
                    tracker.rejectCalled = true;
                }
            }
        };

        // --- 2. Initialize Interactor ---
        ApproveRejectProfileInteractor interactor = new ApproveRejectProfileInteractor(fakeDao);

        // --- 3. Execute with isApproved = false ---
        ApproveRejectProfileInputData inputData = new ApproveRejectProfileInputData(10, 20, false);
        interactor.execute(inputData);

        // --- 4. Verify results ---
        assertTrue(tracker.rejectCalled, "rejectProfile should have been called in the DAO");
        assertFalse(tracker.approveCalled, "approveProfile should NOT have been called");
    }

    // =================================================================
    // This test ensures the OutputData class gets 100% coverage
    // even though the Interactor doesn't currently use it.
    // =================================================================
    @Test
    void testOutputData_Coverage() {
        // 1. Create the object
        int testId = 999;
        ApproveRejectProfileOutputData data = new ApproveRejectProfileOutputData(testId);

        // 2. Call the Getter method
        int resultId = data.getCurrentId();

        // 3. Verify
        assertEquals(testId, resultId);
    }
}