package use_case.logout;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LogoutInteractorTest {

    // ==========================================
    // Fake Presenter
    // ==========================================
    class FakePresenter implements LogoutOutputBoundary {
        boolean successCalled = false;

        @Override
        public void prepareSuccessView() {
            successCalled = true;
        }
    }

    // ==========================================
    // Fake DAO (not used, but required by constructor)
    // ==========================================
    class FakeUserDAO implements LogoutUserDataAccessInterface {}

    // ==========================================
    // Tests
    // ==========================================

    @Test
    void logout_CallsSuccessView() {
        FakePresenter presenter = new FakePresenter();
        FakeUserDAO userDao = new FakeUserDAO();

        LogoutInteractor interactor =
                new LogoutInteractor(userDao, presenter);

        interactor.execute();

        assertTrue(presenter.successCalled);
    }
}
