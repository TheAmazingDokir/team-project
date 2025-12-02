package use_case.signup;

import entity.User;
import entity.UserFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SignupInteractorTest {

    // =========================================================
    // Fakes
    // =========================================================

    class FakeUserDataAccess implements SignupUserDataAccessInterface {
        boolean existsFlag = false;
        boolean saveCalled = false;
        User savedUser;
        int nextUserId = 1;

        @Override
        public boolean existsByNameAndPassword(String username, String password) {
            return existsFlag;
        }

        @Override
        public int getNextUserId() {
            return nextUserId;
        }

        @Override
        public void save(User user) {
            saveCalled = true;
            savedUser = user;
        }
    }

    class FakeProfileDataAccess implements SignupProfileDataAccessInterface {
        int nextProfileId = 1;

        @Override
        public int getNextProfileId() {
            return nextProfileId;
        }
    }

    class FakePresenter implements SignupOutputBoundary {
        boolean successCalled = false;
        boolean failCalled = false;
        String failMessage;
        SignupOutputData successData;
        boolean switchToLoginCalled = false;

        @Override
        public void prepareSuccessView(SignupOutputData signupOutputData) {
            successCalled = true;
            successData = signupOutputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            failCalled = true;
            failMessage = errorMessage;
        }

        @Override
        public void switchToLoginView() {
            switchToLoginCalled = true;
        }
    }

    // =========================================================
    // Tests
    // =========================================================

    @Test
    void execute_UserAlreadyExists_Fails() {
        // Setup
        FakeUserDataAccess userDao = new FakeUserDataAccess();
        FakeProfileDataAccess profileDao = new FakeProfileDataAccess();
        FakePresenter presenter = new FakePresenter();
        UserFactory userFactory = new UserFactory();

        userDao.existsFlag = true;

        SignupInteractor interactor =
                new SignupInteractor(userDao, profileDao, presenter, userFactory);

        SignupInputData input = new SignupInputData(
                "existingUser", "pass", "pass"
        );

        // Act
        interactor.execute(input);

        // Verify
        assertTrue(presenter.failCalled);
        assertEquals("User already exists.", presenter.failMessage);
        assertFalse(presenter.successCalled);
        assertFalse(userDao.saveCalled);
    }

    @Test
    void execute_EmptyPassword_Fails() {
        FakeUserDataAccess userDao = new FakeUserDataAccess();
        FakeProfileDataAccess profileDao = new FakeProfileDataAccess();
        FakePresenter presenter = new FakePresenter();
        UserFactory userFactory = new UserFactory();

        userDao.existsFlag = false;

        SignupInteractor interactor =
                new SignupInteractor(userDao, profileDao, presenter, userFactory);

        SignupInputData input = new SignupInputData(
                "newUser", "", ""  // empty password
        );

        interactor.execute(input);

        assertTrue(presenter.failCalled);
        assertEquals("New password cannot be empty", presenter.failMessage);
        assertFalse(presenter.successCalled);
        assertFalse(userDao.saveCalled);
    }

    @Test
    void execute_EmptyUsername_Fails() {
        FakeUserDataAccess userDao = new FakeUserDataAccess();
        FakeProfileDataAccess profileDao = new FakeProfileDataAccess();
        FakePresenter presenter = new FakePresenter();
        UserFactory userFactory = new UserFactory();

        userDao.existsFlag = false;

        SignupInteractor interactor =
                new SignupInteractor(userDao, profileDao, presenter, userFactory);

        SignupInputData input = new SignupInputData(
                "", "password", "password"  // empty username
        );

        interactor.execute(input);

        assertTrue(presenter.failCalled);
        assertEquals("Username cannot be empty", presenter.failMessage);
        assertFalse(presenter.successCalled);
        assertFalse(userDao.saveCalled);
    }

    @Test
    void execute_PasswordMismatch_Fails() {
        FakeUserDataAccess userDao = new FakeUserDataAccess();
        FakeProfileDataAccess profileDao = new FakeProfileDataAccess();
        FakePresenter presenter = new FakePresenter();
        UserFactory userFactory = new UserFactory();

        userDao.existsFlag = false;

        SignupInteractor interactor =
                new SignupInteractor(userDao, profileDao, presenter, userFactory);

        SignupInputData input = new SignupInputData(
                "user", "password1", "password2"  // mismatch
        );

        interactor.execute(input);

        assertTrue(presenter.failCalled);
        assertEquals("Passwords do not match", presenter.failMessage);
        assertFalse(presenter.successCalled);
        assertFalse(userDao.saveCalled);
    }

    @Test
    void execute_ValidSignup_SavesUserAndSucceeds() {
        FakeUserDataAccess userDao = new FakeUserDataAccess();
        FakeProfileDataAccess profileDao = new FakeProfileDataAccess();
        FakePresenter presenter = new FakePresenter();
        UserFactory userFactory = new UserFactory();

        userDao.existsFlag = false;
        userDao.nextUserId = 5;
        profileDao.nextProfileId = 3; // max(5,3) = 5 expected user id

        SignupInteractor interactor =
                new SignupInteractor(userDao, profileDao, presenter, userFactory);

        SignupInputData input = new SignupInputData(
                "newUser", "strongPass", "strongPass"
        );

        interactor.execute(input);

        assertFalse(presenter.failCalled);
        assertTrue(presenter.successCalled);
        assertTrue(userDao.saveCalled);
        assertNotNull(userDao.savedUser);
        assertEquals("newUser", userDao.savedUser.getName());
        assertEquals(5, userDao.savedUser.getUserId());
    }

    @Test
    void switchToLoginView_DelegatesToPresenter() {
        FakeUserDataAccess userDao = new FakeUserDataAccess();
        FakeProfileDataAccess profileDao = new FakeProfileDataAccess();
        FakePresenter presenter = new FakePresenter();
        UserFactory userFactory = new UserFactory();

        SignupInteractor interactor =
                new SignupInteractor(userDao, profileDao, presenter, userFactory);

        interactor.switchToLoginView();

        assertTrue(presenter.switchToLoginCalled);
    }
}
