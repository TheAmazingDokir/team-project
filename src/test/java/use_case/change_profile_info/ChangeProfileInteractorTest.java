package use_case.change_profile_info;

import entity.EmployerProfile;
import entity.JobSeekerProfile;
import entity.UserProfile;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChangeProfileInteractorTest {

    private static class TestDataAccess implements ChangeProfileDataAccessInterface {
        boolean profileExists;

        boolean uploadCalled;
        boolean changeCalled;
        boolean throwOnWrite;

        UserProfile lastProfile;

        TestDataAccess(boolean profileExists) {
            this.profileExists = profileExists;
        }

        @Override
        public boolean CheckProfileExists(Integer userID) {
            return profileExists;
        }

        @Override
        public void ChangeProfileData(UserProfile userProfile) {
            if (throwOnWrite) {
                throw new RuntimeException("write failed");
            }
            changeCalled = true;
            lastProfile = userProfile;
        }

        @Override
        public void UploadProfileData(UserProfile userProfile) {
            if (throwOnWrite) {
                throw new RuntimeException("write failed");
            }
            uploadCalled = true;
            lastProfile = userProfile;
        }
    }

    private static class TestRecAccess implements ChangeProfileRecommendationAccess {
        boolean upsertCalled;
        List<UserProfile> seenProfiles = new ArrayList<>();

        @Override
        public void upsertProfiles(List<UserProfile> profiles) {
            upsertCalled = true;
            seenProfiles = profiles;
        }
    }

    private static class TestPresenter implements ChangeProfileOutputBoundary {
        boolean successCalled;
        boolean failCalled;
        ChangeProfileOutputData successData;
        String failMessage;

        @Override
        public void prepareSuccessView(ChangeProfileOutputData outputData) {
            successCalled = true;
            successData = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            failCalled = true;
            failMessage = errorMessage;
        }
    }

    @Test
    void newJobSeeker_profileUploaded_andSuccessShown() {
        TestDataAccess dao = new TestDataAccess(false);
        TestRecAccess rec = new TestRecAccess();
        TestPresenter presenter = new TestPresenter();
        ChangeProfileInteractor interactor = new ChangeProfileInteractor(dao, rec, presenter);

        int profileId = 10;
        ChangeProfileInputData input = new ChangeProfileInputData(
                "js@test.com",
                "111-111",
                "summary",
                "full resume",
                false,
                "jobUser",
                profileId
        );

        interactor.execute(input);

        assertTrue(dao.uploadCalled);
        assertFalse(dao.changeCalled);
        assertNotNull(dao.lastProfile);
        assertTrue(dao.lastProfile instanceof JobSeekerProfile);
        assertEquals("summary", dao.lastProfile.getSummaryProfileAsString());

        assertTrue(rec.upsertCalled);
        assertEquals(1, rec.seenProfiles.size());
        assertSame(dao.lastProfile, rec.seenProfiles.get(0));

        assertTrue(presenter.successCalled);
        assertFalse(presenter.failCalled);
        assertEquals(profileId, presenter.successData.getUserID());
    }

    @Test
    void newEmployer_profileUploaded_andSuccessShown() {
        TestDataAccess dao = new TestDataAccess(false);
        TestRecAccess rec = new TestRecAccess();
        TestPresenter presenter = new TestPresenter();
        ChangeProfileInteractor interactor = new ChangeProfileInteractor(dao, rec, presenter);

        int profileId = 20;
        ChangeProfileInputData input = new ChangeProfileInputData(
                "emp@test.com",
                "222-222",
                "we hire",
                "job description",
                true,
                "bossUser",
                profileId
        );

        interactor.execute(input);

        assertTrue(dao.uploadCalled);
        assertFalse(dao.changeCalled);
        assertTrue(dao.lastProfile instanceof EmployerProfile);

        EmployerProfile employer = (EmployerProfile) dao.lastProfile;
        assertEquals("Random Company", employer.getCompanyName());
        assertEquals("we hire", employer.getSummaryProfileAsString());

        assertTrue(rec.upsertCalled);
        assertEquals(1, rec.seenProfiles.size());

        assertTrue(presenter.successCalled);
        assertFalse(presenter.failCalled);
        assertEquals(profileId, presenter.successData.getUserID());
    }

    @Test
    void existingJobSeeker_profileUpdated_andSuccessShown() {
        TestDataAccess dao = new TestDataAccess(true);
        TestRecAccess rec = new TestRecAccess();
        TestPresenter presenter = new TestPresenter();
        ChangeProfileInteractor interactor = new ChangeProfileInteractor(dao, rec, presenter);

        int profileId = 30;
        ChangeProfileInputData input = new ChangeProfileInputData(
                "js@update.com",
                "333-333",
                "updated summary",
                "updated full",
                false,
                "updatedUser",
                profileId
        );

        interactor.execute(input);

        assertFalse(dao.uploadCalled);
        assertTrue(dao.changeCalled);
        assertTrue(dao.lastProfile instanceof JobSeekerProfile);
        assertEquals("updated summary", dao.lastProfile.getSummaryProfileAsString());

        assertTrue(rec.upsertCalled);
        assertTrue(presenter.successCalled);
        assertFalse(presenter.failCalled);
        assertEquals(profileId, presenter.successData.getUserID());
    }

    @Test
    void existingEmployer_profileUpdated_andSuccessShown() {
        TestDataAccess dao = new TestDataAccess(true);
        TestRecAccess rec = new TestRecAccess();
        TestPresenter presenter = new TestPresenter();
        ChangeProfileInteractor interactor = new ChangeProfileInteractor(dao, rec, presenter);

        int profileId = 40;
        ChangeProfileInputData input = new ChangeProfileInputData(
                "emp@update.com",
                "444-444",
                "updated employer summary",
                "updated employer full",
                true,
                "updatedBoss",
                profileId
        );

        interactor.execute(input);

        assertFalse(dao.uploadCalled);
        assertTrue(dao.changeCalled);
        assertTrue(dao.lastProfile instanceof EmployerProfile);

        EmployerProfile employer = (EmployerProfile) dao.lastProfile;
        assertEquals("Random Company", employer.getCompanyName());
        assertEquals("updated employer summary", employer.getSummaryProfileAsString());

        assertTrue(rec.upsertCalled);
        assertTrue(presenter.successCalled);
        assertFalse(presenter.failCalled);
        assertEquals(profileId, presenter.successData.getUserID());
    }
}
