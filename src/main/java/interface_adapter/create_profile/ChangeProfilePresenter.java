package interface_adapter.create_profile;

import interface_adapter.ViewManagerModel;
import use_case.change_profile_info.ChangeProfileOutputBoundary;
import use_case.change_profile_info.ChangeProfileOutputData;

public class ChangeProfilePresenter implements ChangeProfileOutputBoundary {

    private final ChangeProfileViewModel changeProfileViewModel;

    private final ViewManagerModel viewManagerModel;

    public ChangeProfilePresenter(ChangeProfileViewModel changeProfileViewModel, ViewManagerModel viewManagerModel) {
        this.changeProfileViewModel = changeProfileViewModel;
        this.viewManagerModel = viewManagerModel;

        if (this.viewManagerModel.getState().isHasProfile()) {
            System.out.println("PROFILE ALREADY EXISTS");
        }
        else {
            System.out.println("PROFILE DOESN'T ALREADY EXISTS");
        }
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
