package use_case.login;

import entity.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginInteractorTest {

    // =========================================================
    // Fakes
    // =========================================================

    class FakeUserDAO implements LoginUserDataAccessInterface {
        boolean existsFlag = false;
        int userId = 1;

        @Override
        public boolean existsByNameAndPassword(String username, String password) {
            return existsFlag;
        }

        @Override
        public int getUserIdByNameAndPassword(String username, String password) {
            return userId;
        }
    }

    class FakeProfileDAO implements LoginProfileDataAccessInterface {
        boolean profileExistsFlag = false;

        @Override
        public boolean CheckProfileExists(Integer userId) {
            return profileExistsFlag;
        }
    }

    class FakePresenter implements LoginOutputBoundary {
        boolean successCalled = false;
        boolean failCalled = false;
        LoginOutputData successData;
        String failMessage;

        @Override
        public void prepareSuccessView(LoginOutputData loginOutputData) {
            successCalled = true;
            successData = loginOutputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            failCalled = true;
            failMessage = errorMessage;
        }
    }

    // =========================================================
    // Tests
    // =========================================================

    @Test
    void login_Fails_WhenUserDoesNotExist() {
        FakeUserDAO userDao = new FakeUserDAO();
        FakeProfileDAO profileDao = new FakeProfileDAO();
        FakePresenter presenter = new FakePresenter();

        userDao.existsFlag = false;

        LoginInteractor interactor = new LoginInteractor(userDao, profileDao, presenter);

        LoginInputData input = new LoginInputData("badUser", "badPass");

        interactor.execute(input);

        assertTrue(presenter.failCalled);
        assertEquals("badUser: Username and/or password are incorrect", presenter.failMessage);
        assertFalse(presenter.successCalled);
    }

    @Test
    void login_Succeeds_WhenUserExists() {
        FakeUserDAO userDao = new FakeUserDAO();
        FakeProfileDAO profileDao = new FakeProfileDAO();
        FakePresenter presenter = new FakePresenter();

        userDao.existsFlag = true;
        userDao.userId = 10;
        profileDao.profileExistsFlag = true;

        LoginInteractor interactor = new LoginInteractor(userDao, profileDao, presenter);

        LoginInputData input = new LoginInputData("validUser", "validPass");

        interactor.execute(input);

        assertTrue(presenter.successCalled);
        assertFalse(presenter.failCalled);

        assertNotNull(presenter.successData);
        assertEquals(10, presenter.successData.getUserId());
        assertEquals("validUser", presenter.successData.getUsername());
        assertTrue(presenter.successData.hasProfile());
    }
}
