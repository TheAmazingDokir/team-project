package interface_adapter.create_profile;

import use_case.change_profile_info.ChangeProfileInputBoundary;
import use_case.change_profile_info.ChangeProfileInputData;

public class ChangeProfileController {

    private final ChangeProfileInputBoundary changeProfileInteractor;

    public ChangeProfileController(ChangeProfileInputBoundary changeProfileInteractor) {
        this.changeProfileInteractor = changeProfileInteractor;
    }

    public void execute(String email, String phoneNumber, String summary, String profile, Boolean isEmployer) {
        final ChangeProfileInputData changeProfileInputData = new ChangeProfileInputData(email, phoneNumber, summary, profile, isEmployer);
        changeProfileInteractor.execute(changeProfileInputData);
    }
}
