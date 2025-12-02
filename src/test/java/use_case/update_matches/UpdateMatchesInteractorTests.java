package use_case.update_matches;

import entity.UserMatches;
import entity.UserProfile;
import entity.EmployerProfile;
import entity.JobSeekerProfile;
import data_access.MongoDBProfileDataAccessObject;
import data_access.MongoDBUserMatchesDataAccessObject;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for UpdateMatchesInteractor using real MongoDB
 * These tests will create real data in the database and clean it up after
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UpdateMatchesInteractorTests {

    private static UpdateMatchesInteractor interactor;
    private static MongoDBUserMatchesDataAccessObject realUserMatchesDAO;
    private static MongoDBProfileDataAccessObject realProfileDAO;
    private static TestPresenter testPresenter;

    // Test user IDs - using high numbers to avoid conflicts
    private static final int USER1_ID = 9001;
    private static final int USER2_ID = 9002;
    private static final int USER3_ID = 9003;
    private static final int USER4_ID = 9004;

    @BeforeAll
    static void setUpAll() {
        // Initialize real DAOs
        realUserMatchesDAO = new MongoDBUserMatchesDataAccessObject();
        realProfileDAO = new MongoDBProfileDataAccessObject();
        testPresenter = new TestPresenter();

        // Create interactor with real DAOs
        interactor = new UpdateMatchesInteractor(testPresenter);
        interactor.dao = realUserMatchesDAO;
        interactor.profileDao = realProfileDAO;

        System.out.println("Setting up test data in MongoDB...");
        setupTestData();
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("Cleaning up test data from MongoDB...");
        cleanupTestData();
    }

    @BeforeEach
    void setUp() {
        // Reset presenter state before each test
        testPresenter.prepareCalled = false;
        testPresenter.updateCalled = false;
        testPresenter.lastProfiles = null;
        testPresenter.lastName = null;
    }

    /**
     * Creates fake test users in the actual MongoDB database
     */
    private static void setupTestData() {
        try {
            // Create test profiles
            EmployerProfile employer1 = new EmployerProfile(USER1_ID, "employer1@test.com", "111-1111", "TestEmployer1");
            employer1.setCompanyName("Test Company 1");
            employer1.setJobApplicationSummary("Looking for developers");
            employer1.setJobApplicationFull("Full job description here");

            JobSeekerProfile jobSeeker1 = new JobSeekerProfile(USER2_ID, "jobseeker1@test.com", "222-2222", "TestJobSeeker1");
            jobSeeker1.setResumeSummary("Java Developer");
            jobSeeker1.setResumeFull("Full resume here");

            EmployerProfile employer2 = new EmployerProfile(USER3_ID, "employer2@test.com", "333-3333", "TestEmployer2");
            employer2.setCompanyName("Test Company 2");
            employer2.setJobApplicationSummary("Looking for designers");
            employer2.setJobApplicationFull("Full job description here");

            JobSeekerProfile jobSeeker2 = new JobSeekerProfile(USER4_ID, "jobseeker2@test.com", "444-4444", "TestJobSeeker2");
            jobSeeker2.setResumeSummary("UX Designer");
            jobSeeker2.setResumeFull("Full resume here");

            // Upload profiles to MongoDB (skip if already exists)
            if (!realProfileDAO.CheckProfileExists(USER1_ID)) {
                realProfileDAO.UploadProfileData(employer1);
            }
            if (!realProfileDAO.CheckProfileExists(USER2_ID)) {
                realProfileDAO.UploadProfileData(jobSeeker1);
            }
            if (!realProfileDAO.CheckProfileExists(USER3_ID)) {
                realProfileDAO.UploadProfileData(employer2);
            }
            if (!realProfileDAO.CheckProfileExists(USER4_ID)) {
                realProfileDAO.UploadProfileData(jobSeeker2);
            }

            // Create initial UserMatches objects (they will be created automatically by getUserMatchesbyId if not exists)
            UserMatches um1 = realUserMatchesDAO.getUserMatchesbyId(USER1_ID);
            UserMatches um2 = realUserMatchesDAO.getUserMatchesbyId(USER2_ID);
            UserMatches um3 = realUserMatchesDAO.getUserMatchesbyId(USER3_ID);
            UserMatches um4 = realUserMatchesDAO.getUserMatchesbyId(USER4_ID);

            // Clear any existing matches/approvals from previous test runs
            um1.getMatches().clear();
            um1.getApproved().clear();
            um1.getRejected().clear();
            um2.getMatches().clear();
            um2.getApproved().clear();
            um2.getRejected().clear();
            um3.getMatches().clear();
            um3.getApproved().clear();
            um3.getRejected().clear();
            um4.getMatches().clear();
            um4.getApproved().clear();
            um4.getRejected().clear();

            // Save clean state
            realUserMatchesDAO.changeUserMatchesData(um1);
            realUserMatchesDAO.changeUserMatchesData(um2);
            realUserMatchesDAO.changeUserMatchesData(um3);
            realUserMatchesDAO.changeUserMatchesData(um4);

            System.out.println("Test data created successfully!");
        } catch (Exception e) {
            System.err.println("Error setting up test data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Removes fake test users from the actual MongoDB database
     */
    private static void cleanupTestData() {
        try {
            // Delete test profiles
            UserProfile p1 = realProfileDAO.getProfileById(USER1_ID);
            UserProfile p2 = realProfileDAO.getProfileById(USER2_ID);
            UserProfile p3 = realProfileDAO.getProfileById(USER3_ID);
            UserProfile p4 = realProfileDAO.getProfileById(USER4_ID);

            if (p1 != null) realProfileDAO.DeleteProfile(p1);
            if (p2 != null) realProfileDAO.DeleteProfile(p2);
            if (p3 != null) realProfileDAO.DeleteProfile(p3);
            if (p4 != null) realProfileDAO.DeleteProfile(p4);

            // Delete test user matches
            if (realUserMatchesDAO.checkUserMatchesExists(USER1_ID)) {
                realUserMatchesDAO.deleteUserMatches(new UserMatches(USER1_ID));
            }
            if (realUserMatchesDAO.checkUserMatchesExists(USER2_ID)) {
                realUserMatchesDAO.deleteUserMatches(new UserMatches(USER2_ID));
            }
            if (realUserMatchesDAO.checkUserMatchesExists(USER3_ID)) {
                realUserMatchesDAO.deleteUserMatches(new UserMatches(USER3_ID));
            }
            if (realUserMatchesDAO.checkUserMatchesExists(USER4_ID)) {
                realUserMatchesDAO.deleteUserMatches(new UserMatches(USER4_ID));
            }

            System.out.println("Test data cleaned up successfully!");
        } catch (Exception e) {
            System.err.println("Error cleaning up test data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ========== INTERACTOR TESTS ==========

    @Test
    @Order(1)
    void testUpdateMatch_BothApproved_CreatesMatch() {
        System.out.println("Test 1: Both users approve each other");

        // Get fresh state from DB
        UserMatches um1 = realUserMatchesDAO.getUserMatchesbyId(USER1_ID);
        UserMatches um2 = realUserMatchesDAO.getUserMatchesbyId(USER2_ID);

        // Clear any existing data
        um1.getMatches().clear();
        um1.getApproved().clear();
        um2.getMatches().clear();
        um2.getApproved().clear();

        // Set up mutual approval
        um1.getApproved().add(USER2_ID);
        um2.getApproved().add(USER1_ID);

        realUserMatchesDAO.changeUserMatchesData(um1);
        realUserMatchesDAO.changeUserMatchesData(um2);

        // Create input and execute
        UpdateMatchesInputData input = new UpdateMatchesInputData(USER1_ID, USER2_ID);
        interactor.updateMatch(input);

        // Verify from database
        UserMatches um1After = realUserMatchesDAO.getUserMatchesbyId(USER1_ID);
        UserMatches um2After = realUserMatchesDAO.getUserMatchesbyId(USER2_ID);

        assertTrue(um1After.getMatches().contains(USER2_ID), "User 1 should have User 2 in matches");
        assertTrue(um2After.getMatches().contains(USER1_ID), "User 2 should have User 1 in matches");

        System.out.println("✓ Match created successfully in database");
    }

    @Test
    @Order(2)
    void testUpdateMatch_OnlyOneApproved_NoMatch() {
        System.out.println("Test 2: Only one user approves");

        // Get fresh state from DB
        UserMatches um3 = realUserMatchesDAO.getUserMatchesbyId(USER3_ID);
        UserMatches um4 = realUserMatchesDAO.getUserMatchesbyId(USER4_ID);

        // Clear any existing data
        um3.getMatches().clear();
        um3.getApproved().clear();
        um4.getMatches().clear();
        um4.getApproved().clear();

        // Only USER3 approves USER4 (not mutual)
        um3.getApproved().add(USER4_ID);

        realUserMatchesDAO.changeUserMatchesData(um3);
        realUserMatchesDAO.changeUserMatchesData(um4);

        // Create input and execute
        UpdateMatchesInputData input = new UpdateMatchesInputData(USER3_ID, USER4_ID);
        interactor.updateMatch(input);

        // Verify no match was created
        UserMatches um3After = realUserMatchesDAO.getUserMatchesbyId(USER3_ID);
        UserMatches um4After = realUserMatchesDAO.getUserMatchesbyId(USER4_ID);

        assertFalse(um3After.getMatches().contains(USER4_ID), "User 3 should NOT have User 4 in matches");
        assertFalse(um4After.getMatches().contains(USER3_ID), "User 4 should NOT have User 3 in matches");

        System.out.println("✓ No match created (as expected)");
    }

    @Test
    @Order(3)
    void testUserIdtoUserMatchesProfiles_WithMatches() {
        System.out.println("Test 3: Retrieve matched profiles");

        // Set up: USER1 has matches with USER2 and USER3
        UserMatches um1 = realUserMatchesDAO.getUserMatchesbyId(USER1_ID);
        um1.getMatches().clear();
        um1.addMatch(USER2_ID);
        um1.addMatch(USER3_ID);
        realUserMatchesDAO.changeUserMatchesData(um1);

        // Execute
        UpdateMatchesInputData input = new UpdateMatchesInputData(USER1_ID);
        interactor.userIdtoUserMatchesProfiles(input);

        // Verify presenter was called with correct profiles
        assertTrue(testPresenter.prepareCalled, "Presenter should be called");
        assertNotNull(testPresenter.lastProfiles, "Profiles should not be null");
        assertEquals(2, testPresenter.lastProfiles.size(), "Should have 2 matched profiles");

        // Verify the profiles are correct
        ArrayList<Integer> profileIds = new ArrayList<>();
        for (UserProfile profile : testPresenter.lastProfiles) {
            profileIds.add(profile.getUserId());
        }
        assertTrue(profileIds.contains(USER2_ID), "Should contain User 2 profile");
        assertTrue(profileIds.contains(USER3_ID), "Should contain User 3 profile");

        System.out.println("✓ Retrieved matched profiles successfully");
    }

    @Test
    @Order(4)
    void testUserIdtoUserMatchesProfiles_NoMatches() {
        System.out.println("Test 4: Retrieve profiles with no matches");

        // Set up: USER4 has no matches
        UserMatches um4 = realUserMatchesDAO.getUserMatchesbyId(USER4_ID);
        um4.getMatches().clear();
        realUserMatchesDAO.changeUserMatchesData(um4);

        // Execute
        UpdateMatchesInputData input = new UpdateMatchesInputData(USER4_ID);
        interactor.userIdtoUserMatchesProfiles(input);

        // Verify presenter was called with empty list
        assertTrue(testPresenter.prepareCalled, "Presenter should be called");
        assertNotNull(testPresenter.lastProfiles, "Profiles should not be null");
        assertEquals(0, testPresenter.lastProfiles.size(), "Should have 0 matched profiles");

        System.out.println("✓ Correctly handled empty matches");
    }

    @Test
    @Order(5)
    void testGetInfo_PassesNameCorrectly() {
        System.out.println("Test 5: Get info with name");

        String testName = "TestUserName";
        UpdateMatchesInputData input = new UpdateMatchesInputData(testName);

        interactor.getInfo(input);

        assertTrue(testPresenter.updateCalled, "Presenter updateInfo should be called");
        assertEquals(testName, testPresenter.lastName, "Name should match");

        System.out.println("✓ Name passed correctly to presenter");
    }

    @Test
    @Order(6)
    void testUpdateMatchesInputData_AllConstructorsAndSetters() {
        System.out.println("Test 6: UpdateMatchesInputData constructors and setters");

        // Test constructor with currentId
        UpdateMatchesInputData data1 = new UpdateMatchesInputData(100);
        assertEquals(100, data1.getCurrentId());

        // Test constructor with name
        UpdateMatchesInputData data2 = new UpdateMatchesInputData("Alice");
        assertEquals("Alice", data2.getCurrentName());

        // Test constructor with two IDs
        UpdateMatchesInputData data3 = new UpdateMatchesInputData(1, 2);
        assertEquals(1, data3.getUser1Id());
        assertEquals(2, data3.getUser2Id());

        // Test all setters
        data3.setUser1Id(10);
        data3.setUser2Id(20);
        data3.setCurrentId(30);
        data3.setCurrentName("Bob");

        assertEquals(10, data3.getUser1Id());
        assertEquals(20, data3.getUser2Id());
        assertEquals(30, data3.getCurrentId());
        assertEquals("Bob", data3.getCurrentName());

        System.out.println("✓ All constructors and setters work correctly");
    }

    @Test
    @Order(7)
    void testUpdateMatchesOutputData_Constructors() {
        System.out.println("Test 7: UpdateMatchesOutputData constructors");

        // Test constructor with profiles list
        ArrayList<UserProfile> profiles = new ArrayList<>();
        UserProfile p1 = realProfileDAO.getProfileById(USER1_ID);
        UserProfile p2 = realProfileDAO.getProfileById(USER2_ID);
        profiles.add(p1);
        profiles.add(p2);

        UpdateMatchesOutputData output1 = new UpdateMatchesOutputData(profiles);
        assertEquals(2, output1.getUsers().size());
        assertEquals(profiles, output1.getUsers());

        // Test constructor with name
        UpdateMatchesOutputData output2 = new UpdateMatchesOutputData("TestName");
        assertEquals("TestName", output2.getCurrentName());

        System.out.println("✓ Output data constructors work correctly");
    }

    @Test
    @Order(8)
    void testNoArgConstructor_WithNullPresenter() {
        System.out.println("Test 8: No-arg constructor behavior");

        UpdateMatchesInteractor nullPresenterInteractor = new UpdateMatchesInteractor();
        nullPresenterInteractor.dao = realUserMatchesDAO;
        nullPresenterInteractor.profileDao = realProfileDAO;

        // Set up mutual approval for test
        UserMatches um1 = realUserMatchesDAO.getUserMatchesbyId(USER1_ID);
        UserMatches um2 = realUserMatchesDAO.getUserMatchesbyId(USER2_ID);
        um1.getMatches().clear();
        um2.getMatches().clear();
        um1.getApproved().clear();
        um2.getApproved().clear();
        um1.getApproved().add(USER2_ID);
        um2.getApproved().add(USER1_ID);
        realUserMatchesDAO.changeUserMatchesData(um1);
        realUserMatchesDAO.changeUserMatchesData(um2);

        // updateMatch should work without presenter
        UpdateMatchesInputData input = new UpdateMatchesInputData(USER1_ID, USER2_ID);
        assertDoesNotThrow(() -> nullPresenterInteractor.updateMatch(input));

        // Verify match was created
        UserMatches um1After = realUserMatchesDAO.getUserMatchesbyId(USER1_ID);
        assertTrue(um1After.getMatches().contains(USER2_ID));

        // Methods that call presenter should throw NPE
        UpdateMatchesInputData input2 = new UpdateMatchesInputData(USER1_ID);
        assertThrows(NullPointerException.class, () ->
                nullPresenterInteractor.userIdtoUserMatchesProfiles(input2));

        UpdateMatchesInputData input3 = new UpdateMatchesInputData("Test");
        assertThrows(NullPointerException.class, () ->
                nullPresenterInteractor.getInfo(input3));

        System.out.println("✓ No-arg constructor behavior verified");
    }

    @Test
    @Order(9)
    void testUpdateMatch_User2ApprovesUser1Only_NoMatch() {
        System.out.println("Test 9: Only User 2 approves User 1 (reversed scenario)");

        // Get fresh state from DB
        UserMatches um1 = realUserMatchesDAO.getUserMatchesbyId(USER1_ID);
        UserMatches um2 = realUserMatchesDAO.getUserMatchesbyId(USER2_ID);

        // Clear any existing data
        um1.getMatches().clear();
        um1.getApproved().clear();
        um2.getMatches().clear();
        um2.getApproved().clear();

        // Only USER2 approves USER1 (not mutual)
        um2.getApproved().add(USER1_ID);

        realUserMatchesDAO.changeUserMatchesData(um1);
        realUserMatchesDAO.changeUserMatchesData(um2);

        // Create input and execute
        UpdateMatchesInputData input = new UpdateMatchesInputData(USER1_ID, USER2_ID);
        interactor.updateMatch(input);

        // Verify no match was created
        UserMatches um1After = realUserMatchesDAO.getUserMatchesbyId(USER1_ID);
        UserMatches um2After = realUserMatchesDAO.getUserMatchesbyId(USER2_ID);

        assertFalse(um1After.getMatches().contains(USER2_ID), "User 1 should NOT have User 2 in matches");
        assertFalse(um2After.getMatches().contains(USER1_ID), "User 2 should NOT have User 1 in matches");

        System.out.println("✓ No match created when only User 2 approves");
    }

    @Test
    @Order(10)
    void testUpdateMatch_NeitherUserApproves_NoMatch() {
        System.out.println("Test 10: Neither user approves the other");

        // Get fresh state from DB
        UserMatches um3 = realUserMatchesDAO.getUserMatchesbyId(USER3_ID);
        UserMatches um4 = realUserMatchesDAO.getUserMatchesbyId(USER4_ID);

        // Clear any existing data - neither approves the other
        um3.getMatches().clear();
        um3.getApproved().clear();
        um4.getMatches().clear();
        um4.getApproved().clear();

        realUserMatchesDAO.changeUserMatchesData(um3);
        realUserMatchesDAO.changeUserMatchesData(um4);

        // Create input and execute
        UpdateMatchesInputData input = new UpdateMatchesInputData(USER3_ID, USER4_ID);
        interactor.updateMatch(input);

        // Verify no match was created
        UserMatches um3After = realUserMatchesDAO.getUserMatchesbyId(USER3_ID);
        UserMatches um4After = realUserMatchesDAO.getUserMatchesbyId(USER4_ID);

        assertFalse(um3After.getMatches().contains(USER4_ID), "User 3 should NOT have User 4 in matches");
        assertFalse(um4After.getMatches().contains(USER3_ID), "User 4 should NOT have User 3 in matches");

        System.out.println("✓ No match created when neither approves");
    }

    @Test
    @Order(11)
    void testUserIdtoUserMatchesProfiles_SingleMatch() {
        System.out.println("Test 11: Retrieve single matched profile");

        // Set up: USER2 has only one match with USER1
        UserMatches um2 = realUserMatchesDAO.getUserMatchesbyId(USER2_ID);
        um2.getMatches().clear();
        um2.addMatch(USER1_ID);
        realUserMatchesDAO.changeUserMatchesData(um2);

        // Execute
        UpdateMatchesInputData input = new UpdateMatchesInputData(USER2_ID);
        interactor.userIdtoUserMatchesProfiles(input);

        // Verify presenter was called with exactly 1 profile
        assertTrue(testPresenter.prepareCalled, "Presenter should be called");
        assertNotNull(testPresenter.lastProfiles, "Profiles should not be null");
        assertEquals(1, testPresenter.lastProfiles.size(), "Should have exactly 1 matched profile");
        assertEquals(USER1_ID, testPresenter.lastProfiles.get(0).getUserId(), "Should be User 1's profile");

        System.out.println("✓ Retrieved single matched profile successfully");
    }

    @Test
    @Order(12)
    void testUserIdtoUserMatchesProfiles_MultipleMatches() {
        System.out.println("Test 12: Retrieve multiple matched profiles (3+)");

        // Set up: USER1 has matches with USER2, USER3, and USER4
        UserMatches um1 = realUserMatchesDAO.getUserMatchesbyId(USER1_ID);
        um1.getMatches().clear();
        um1.addMatch(USER2_ID);
        um1.addMatch(USER3_ID);
        um1.addMatch(USER4_ID);
        realUserMatchesDAO.changeUserMatchesData(um1);

        // Execute
        UpdateMatchesInputData input = new UpdateMatchesInputData(USER1_ID);
        interactor.userIdtoUserMatchesProfiles(input);

        // Verify presenter was called with 3 profiles
        assertTrue(testPresenter.prepareCalled, "Presenter should be called");
        assertNotNull(testPresenter.lastProfiles, "Profiles should not be null");
        assertEquals(3, testPresenter.lastProfiles.size(), "Should have 3 matched profiles");

        // Verify all three profiles are present
        ArrayList<Integer> profileIds = new ArrayList<>();
        for (UserProfile profile : testPresenter.lastProfiles) {
            profileIds.add(profile.getUserId());
        }
        assertTrue(profileIds.contains(USER2_ID), "Should contain User 2");
        assertTrue(profileIds.contains(USER3_ID), "Should contain User 3");
        assertTrue(profileIds.contains(USER4_ID), "Should contain User 4");

        System.out.println("✓ Retrieved multiple matched profiles successfully");
    }

    @Test
    @Order(13)
    void testGetInfo_DifferentNames() {
        System.out.println("Test 13: Get info with different names");

        String[] testNames = {"Alice", "Bob", "Charlie", ""};

        for (String name : testNames) {
            UpdateMatchesInputData input = new UpdateMatchesInputData(name);
            interactor.getInfo(input);

            assertTrue(testPresenter.updateCalled, "Presenter updateInfo should be called for: " + name);
            assertEquals(name, testPresenter.lastName, "Name should match for: " + name);

            // Reset for next iteration
            testPresenter.updateCalled = false;
            testPresenter.lastName = null;
        }

        System.out.println("✓ All different names passed correctly");
    }

    @Test
    @Order(14)
    void testUpdateMatch_AlreadyMatched_Idempotent() {
        System.out.println("Test 14: Matching already matched users (idempotent check)");

        // Get fresh state and set up mutual approval AND existing match
        UserMatches um1 = realUserMatchesDAO.getUserMatchesbyId(USER1_ID);
        UserMatches um2 = realUserMatchesDAO.getUserMatchesbyId(USER2_ID);

        um1.getMatches().clear();
        um1.getApproved().clear();
        um2.getMatches().clear();
        um2.getApproved().clear();

        // Set up: already matched AND still approved
        um1.getApproved().add(USER2_ID);
        um2.getApproved().add(USER1_ID);
        um1.addMatch(USER2_ID);
        um2.addMatch(USER1_ID);

        realUserMatchesDAO.changeUserMatchesData(um1);
        realUserMatchesDAO.changeUserMatchesData(um2);

        // Execute matching again
        UpdateMatchesInputData input = new UpdateMatchesInputData(USER1_ID, USER2_ID);
        interactor.updateMatch(input);

        // Verify they're still matched (should handle duplicate adds gracefully)
        UserMatches um1After = realUserMatchesDAO.getUserMatchesbyId(USER1_ID);
        UserMatches um2After = realUserMatchesDAO.getUserMatchesbyId(USER2_ID);

        assertTrue(um1After.getMatches().contains(USER2_ID), "User 1 should still have User 2");
        assertTrue(um2After.getMatches().contains(USER1_ID), "User 2 should still have User 1");

        System.out.println("✓ Idempotent behavior verified");
    }

    // ========== TEST PRESENTER ==========

    static class TestPresenter implements UpdateMatchesOutputBoundary {
        boolean prepareCalled = false;
        boolean updateCalled = false;
        ArrayList<UserProfile> lastProfiles = null;
        String lastName = null;

        @Override
        public void prepareSuccessView(UpdateMatchesOutputData response) {
            this.prepareCalled = true;
            this.lastProfiles = response.getUsers();
        }

        @Override
        public void updateInfo(UpdateMatchesOutputData response) {
            this.updateCalled = true;
            this.lastName = response.getCurrentName();
        }
    }
}