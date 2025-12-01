package use_case.change_profile_info;

import entity.EmployerProfile;
import entity.JobSeekerProfile;
import entity.UserProfile;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ChangeProfileInteractorManualTest {

    // =================================================================
    // Helper Classes: Fakes to simulate External Dependencies
    // =================================================================

    // 1. Fake Data Access Object (Database)
    class FakeDataAccess implements ChangeProfileDataAccessInterface {
        // Control flags
        boolean profileExists = false;

        // Verification flags
        boolean isCheckCalled = false;
        boolean isUploadCalled = false;
        boolean isChangeCalled = false;
        UserProfile capturedProfile;

        @Override
        public boolean CheckProfileExists(Integer userID) {
            this.isCheckCalled = true;
            return this.profileExists;
        }

        @Override
        public void ChangeProfileData(UserProfile userProfile) {
            this.isChangeCalled = true;
            this.capturedProfile = userProfile;
        }

        @Override
        public void UploadProfileData(UserProfile userProfile) {
            this.isUploadCalled = true;
            this.capturedProfile = userProfile;
        }
    }

    // 2. Fake Recommendation Access (Pinecone/Service)
    class FakeRecAccess implements ChangeProfileRecommendationAccess {
        boolean isUpsertCalled = false;
        List<UserProfile> capturedProfiles;

        @Override
        public void upsertProfiles(List<UserProfile> profiles) {
            this.isUpsertCalled = true;
            this.capturedProfiles = profiles;
        }
    }

    // 3. Fake Presenter (Output Boundary)
    class FakePresenter implements ChangeProfileOutputBoundary {
        boolean isSuccessCalled = false;
        boolean isFailCalled = false;
        ChangeProfileOutputData receivedData;
        String receivedError;

        @Override
        public void prepareSuccessView(ChangeProfileOutputData outputData) {
            this.isSuccessCalled = true;
            this.receivedData = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            this.isFailCalled = true;
            this.receivedError = errorMessage;
        }
    }

    // =================================================================
    // Test Cases
    // =================================================================

    @Test
    void testExecute_NewJobSeeker_ShouldUploadData() {
        // --- 1. Setup ---
        FakeDataAccess fakeDao = new FakeDataAccess();
        FakeRecAccess fakeRec = new FakeRecAccess();
        FakePresenter fakePresenter = new FakePresenter();

        // Configure DAO to say "Profile does NOT exist"
        fakeDao.profileExists = false;

        ChangeProfileInteractor interactor = new ChangeProfileInteractor(fakeDao, fakeRec, fakePresenter);

        // --- 2. Input Data (Job Seeker) ---
        ChangeProfileInputData input = new ChangeProfileInputData(
                "js@test.com", "123-456", "I code java", "Full Resume",
                false, // isEmployer = false
                "DevUser", 10
        );

        // --- 3. Act ---
        interactor.execute(input);

        // --- 4. Verify ---
        // Verify DAO interactions
        assertTrue(fakeDao.isCheckCalled);
        assertTrue(fakeDao.isUploadCalled, "Should call Upload for new profiles");
        assertFalse(fakeDao.isChangeCalled, "Should NOT call Change for new profiles");

        // Verify Data Integrity
        assertTrue(fakeDao.capturedProfile instanceof JobSeekerProfile);
        assertEquals("I code java", fakeDao.capturedProfile.getSummaryProfileAsString());

        // Verify Recommendation Service update
        assertTrue(fakeRec.isUpsertCalled);

        // Verify Presenter
        assertTrue(fakePresenter.isSuccessCalled);
        assertEquals(10, fakePresenter.receivedData.getUserID());
    }

    @Test
    void testExecute_NewEmployer_ShouldUploadData() {
        // --- 1. Setup ---
        FakeDataAccess fakeDao = new FakeDataAccess();
        FakeRecAccess fakeRec = new FakeRecAccess();
        FakePresenter fakePresenter = new FakePresenter();

        fakeDao.profileExists = false; // New user

        ChangeProfileInteractor interactor = new ChangeProfileInteractor(fakeDao, fakeRec, fakePresenter);

        // --- 2. Input Data (Employer) ---
        ChangeProfileInputData input = new ChangeProfileInputData(
                "emp@test.com", "999-999", "We hire", "Full Job Desc",
                true, // isEmployer = true
                "BossUser", 20
        );

        // --- 3. Act ---
        interactor.execute(input);

        // --- 4. Verify ---
        assertTrue(fakeDao.isUploadCalled);
        assertTrue(fakeDao.capturedProfile instanceof EmployerProfile);
        // Verify Employer specific logic (company name set to "Random Company" in Interactor)
        EmployerProfile resultProfile = (EmployerProfile) fakeDao.capturedProfile;
        assertEquals("Random Company", resultProfile.getCompanyName());

        assertTrue(fakePresenter.isSuccessCalled);
    }

    @Test
    void testExecute_ExistingEmployer_ShouldChangeData() {
        // --- 1. Setup ---
        FakeDataAccess fakeDao = new FakeDataAccess();
        FakeRecAccess fakeRec = new FakeRecAccess();
        FakePresenter fakePresenter = new FakePresenter();

        // Configure DAO to say "Profile ALREADY exists"
        fakeDao.profileExists = true;

        ChangeProfileInteractor interactor = new ChangeProfileInteractor(fakeDao, fakeRec, fakePresenter);

        // --- 2. Input Data (Employer) ---
        ChangeProfileInputData input = new ChangeProfileInputData(
                "emp@update.com", "888-888", "Updated Summary", "Updated Full",
                true, "BossUpdate", 30
        );

        // --- 3. Act ---
        interactor.execute(input);

        // --- 4. Verify ---
        assertTrue(fakeDao.isCheckCalled);
        assertFalse(fakeDao.isUploadCalled);
        assertTrue(fakeDao.isChangeCalled, "Should call ChangeProfileData for existing users");

        assertTrue(fakePresenter.isSuccessCalled);
    }

    @Test
    void testExecute_ExistingJobSeeker_ShouldChangeData() {
        // --- 1. Setup ---
        FakeDataAccess fakeDao = new FakeDataAccess();
        FakeRecAccess fakeRec = new FakeRecAccess();
        FakePresenter fakePresenter = new FakePresenter();

        fakeDao.profileExists = true; // Exists

        ChangeProfileInteractor interactor = new ChangeProfileInteractor(fakeDao, fakeRec, fakePresenter);

        // --- 2. Input Data (Job Seeker) ---
        ChangeProfileInputData input = new ChangeProfileInputData(
                "js@update.com", "777-777", "New Bio", "New Resume",
                false, "DevUpdate", 40
        );

        // --- 3. Act ---
        interactor.execute(input);

        // --- 4. Verify ---
        assertTrue(fakeDao.isChangeCalled);
        assertTrue(fakeDao.capturedProfile instanceof JobSeekerProfile);
        assertEquals("New Bio", fakeDao.capturedProfile.getSummaryProfileAsString());

        // Note: Based on the provided code logic in ChangeProfileInteractor.java,
        // specifically the 'else' block logic structure, verify that success was triggered correctly.
        assertTrue(fakePresenter.isSuccessCalled);
    }
}