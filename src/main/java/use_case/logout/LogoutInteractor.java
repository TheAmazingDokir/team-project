package use_case.logout;

/**
 * The Logout Interactor.
 */
public class LogoutInteractor implements LogoutInputBoundary {
    private LogoutOutputBoundary logoutPresenter;

    public LogoutInteractor(LogoutUserDataAccessInterface userDataAccessInterface,
                            LogoutOutputBoundary logoutOutputBoundary) {
        this.logoutPresenter = logoutOutputBoundary;
    }

    @Override
    public void execute() {
        logoutPresenter.prepareSuccessView();
    }
}

