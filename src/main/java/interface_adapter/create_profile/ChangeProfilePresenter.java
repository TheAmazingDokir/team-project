package interface_adapter.create_profile;

import use_case.change_profile_info.ChangeProfileOutputBoundary;
import use_case.change_profile_info.ChangeProfileOutputData;

class ChangeProfilePresenter implements ChangeProfileOutputBoundary {

    private final ChangeProfileViewModel changeProfileViewModel;

    public ChangeProfilePresenter(ChangeProfileViewModel changeProfileViewModel) {
        this.changeProfileViewModel = changeProfileViewModel;
    }

    @Override
    public void prepareSuccessView(ChangeProfileOutputData changeProfileOutputData) {
        System.out.println("ChangeProfilePresenter prepareSuccessView");
    }

    @Override
    public void prepareFailView(String errorMessage) {
        System.out.println("ChangeProfilePresenter prepareFailView");
    }
}
