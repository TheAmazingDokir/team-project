package use_case.login;

import entity.User;

/**
 * The Login Interactor.
 */
public class LoginInteractor implements LoginInputBoundary {
    private final LoginUserDataAccessInterface userDataAccessObject;
    private final LoginOutputBoundary loginPresenter;

    public LoginInteractor(LoginUserDataAccessInterface userDataAccessInterface,
                           LoginOutputBoundary loginOutputBoundary) {
        this.userDataAccessObject = userDataAccessInterface;
        this.loginPresenter = loginOutputBoundary;
    }

    @Override
    public void execute(LoginInputData loginInputData) {
        final String username = loginInputData.getUsername();
        final String password = loginInputData.getPassword();
        if (!userDataAccessObject.existsByNameAndPassword(username, password)) {
            loginPresenter.prepareFailView(username + ": Username and/or password are incorrect");
        }
        else {
            int userId = userDataAccessObject.getUserIdByNameAndPassword(username, password);

            final LoginOutputData loginOutputData = new LoginOutputData(userId, username);
            loginPresenter.prepareSuccessView(loginOutputData);
        }
    }
}
