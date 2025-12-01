package use_case.change_password;

import entity.User;
import entity.UserFactory;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ChangePasswordInteractorTest {

    // =================================================================
    // Test 1: Success Scenario (Valid password, User exists)
    // =================================================================
    @Test
    void successTest() {
        // 1. Arrange
        ChangePasswordInputData inputData = new ChangePasswordInputData("newPassword123", "Paul");

        // Create a fake DAO that simulates finding a user and saving the new password
        ChangePasswordUserDataAccessInterface userDataAccessObject = new ChangePasswordUserDataAccessInterface() {
            @Override
            public void changePassword(User user) {
                // Verify the DAO was called with the correct new password
                assertEquals("newPassword123", user.getPassword());
                assertEquals("Paul", user.getName());
            }

            @Override
            public int getUserIdByNameAndPassword(String name, String password) {
                // Simulate finding the user (return a valid ID, e.g., 123)
                // Note: The Interactor passes the *new* password here in the input data,
                // which might be a logical quirk in the original code, but we simulate finding the ID anyway.
                return 123;
            }
        };

        // Create a fake Presenter to capture the success output
        ChangePasswordOutputBoundary userPresenter = new ChangePasswordOutputBoundary() {
            @Override
            public void prepareSuccessView(ChangePasswordOutputData outputData) {
                // Verify the output data contains the correct username
                assertEquals("Paul", outputData.getUsername());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Use case failure is unexpected.");
            }
        };

        UserFactory userFactory = new UserFactory();
        ChangePasswordInteractor interactor = new ChangePasswordInteractor(userDataAccessObject, userPresenter, userFactory);

        // 2. Act
        interactor.execute(inputData);
    }

    // =================================================================
    // Test 2: Failure Scenario (Empty Password)
    // =================================================================
    @Test
    void failEmptyPasswordTest() {
        // 1. Arrange - Empty password
        ChangePasswordInputData inputData = new ChangePasswordInputData("", "Paul");

        // Fake DAO (should not be called for saving, lookup might happen depending on logic order)
        ChangePasswordUserDataAccessInterface userDataAccessObject = new ChangePasswordUserDataAccessInterface() {
            @Override
            public void changePassword(User user) {
                fail("Should not attempt to save an invalid user.");
            }

            @Override
            public int getUserIdByNameAndPassword(String name, String password) {
                return 123;
            }
        };

        // Fake Presenter to verify failure message
        ChangePasswordOutputBoundary userPresenter = new ChangePasswordOutputBoundary() {
            @Override
            public void prepareSuccessView(ChangePasswordOutputData outputData) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                // Verify the specific error message
                assertEquals("New password cannot be empty", errorMessage);
            }
        };

        UserFactory userFactory = new UserFactory();
        ChangePasswordInteractor interactor = new ChangePasswordInteractor(userDataAccessObject, userPresenter, userFactory);

        // 2. Act
        interactor.execute(inputData);
    }

    // =================================================================
    // Test 3: Failure Scenario (User Not Found / Invalid Credentials)
    // =================================================================
    @Test
    void failUserNotFoundTest() {
        // 1. Arrange
        ChangePasswordInputData inputData = new ChangePasswordInputData("password123", "GhostUser");

        // Fake DAO returns -1 to simulate user not found
        ChangePasswordUserDataAccessInterface userDataAccessObject = new ChangePasswordUserDataAccessInterface() {
            @Override
            public void changePassword(User user) {
                fail("Should not save user if ID is not found.");
            }

            @Override
            public int getUserIdByNameAndPassword(String name, String password) {
                return -1; // Indicates failure
            }
        };

        // Fake Presenter to verify failure message
        ChangePasswordOutputBoundary userPresenter = new ChangePasswordOutputBoundary() {
            @Override
            public void prepareSuccessView(ChangePasswordOutputData outputData) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                // Verify the specific error message
                assertEquals("Invalid username or password", errorMessage);
            }
        };

        UserFactory userFactory = new UserFactory();
        ChangePasswordInteractor interactor = new ChangePasswordInteractor(userDataAccessObject, userPresenter, userFactory);

        // 2. Act
        interactor.execute(inputData);
    }

    // =================================================================
    // Test 4: Input/Output Data Coverage (Getters)
    // =================================================================
    @Test
    void testDataObjects() {
        // Test Input Data
        ChangePasswordInputData input = new ChangePasswordInputData("pass", "user");
        assertEquals("pass", input.getPassword());
        assertEquals("user", input.getUsername());

        // Test Output Data
        ChangePasswordOutputData output = new ChangePasswordOutputData("user");
        assertEquals("user", output.getUsername());
    }
}