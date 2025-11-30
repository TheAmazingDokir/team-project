package use_case.signup;

import entity.User;
import entity.UserFactory;

/**
 * The Signup Interactor.
 */
public class SignupInteractor implements SignupInputBoundary {
    private final SignupUserDataAccessInterface userDataAccessObject;
    private final SignupProfileDataAccessInterface profileDataAccessObject;
    private final SignupOutputBoundary userPresenter;
    private final UserFactory userFactory;

    public SignupInteractor(SignupUserDataAccessInterface signupDataAccessInterface,
                            SignupProfileDataAccessInterface signupProfileDataAccessInterface,
                            SignupOutputBoundary signupOutputBoundary,
                            UserFactory userFactory) {
        this.userDataAccessObject = signupDataAccessInterface;
        this.profileDataAccessObject = signupProfileDataAccessInterface;
        this.userPresenter = signupOutputBoundary;
        this.userFactory = userFactory;
    }

    @Override
    public void execute(SignupInputData signupInputData) {
        if (userDataAccessObject.existsByNameAndPassword(signupInputData.getUsername(), signupInputData.getPassword())) {
            userPresenter.prepareFailView("User already exists.");
        }
        else if ("".equals(signupInputData.getPassword())) {
            userPresenter.prepareFailView("New password cannot be empty");
        }
        else if ("".equals(signupInputData.getUsername())) {
            userPresenter.prepareFailView("Username cannot be empty");
        }
        else if (!signupInputData.getPassword().equals(signupInputData.getRepeatPassword())){
            userPresenter.prepareFailView("Passwords do not match");
        }
        else {
            final int nextUserId = userDataAccessObject.getNextUserId();
            final int nextProfileId = profileDataAccessObject.getNextProfileId();
            // Take max of both to avoid overlaps
            final int userId = Math.max(nextUserId, nextProfileId);
            final User user = userFactory.create(userId, signupInputData.getUsername(), signupInputData.getPassword());
            userDataAccessObject.save(user);

            final SignupOutputData signupOutputData = new SignupOutputData(user.getName());
            userPresenter.prepareSuccessView(signupOutputData);
        }
    }

    @Override
    public void switchToLoginView() {
        userPresenter.switchToLoginView();
    }
}
