package interface_adapter.create_profile;

import interface_adapter.ViewManagerModel;
import interface_adapter.ViewManagerState;
import use_case.change_profile_info.ChangeProfileInputBoundary;
import use_case.change_profile_info.ChangeProfileInputData;

public class ChangeProfileController {

    private final ChangeProfileInputBoundary changeProfileInteractor;
    private final ViewManagerModel viewManagerModel;

    public ChangeProfileController(ChangeProfileInputBoundary changeProfileInteractor, ViewManagerModel viewManagerModel) {
        this.changeProfileInteractor = changeProfileInteractor;
        this.viewManagerModel = viewManagerModel;
    }

    public void execute(String profileUsername, String email, String phoneNumber, String summary, String profile, Boolean isEmployer) {
        final ChangeProfileInputData changeProfileInputData = new ChangeProfileInputData(email, phoneNumber, summary, profile, isEmployer, profileUsername, viewManagerModel.getState().getUserId());
        changeProfileInteractor.execute(changeProfileInputData);
    }
}
