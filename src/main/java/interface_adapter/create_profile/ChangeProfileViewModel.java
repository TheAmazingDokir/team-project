package interface_adapter.create_profile;

import interface_adapter.ViewModel;

public class ChangeProfileViewModel extends ViewModel<ChangeProfileState>{

    public ChangeProfileViewModel() {
        super("change profile");
        setState(new ChangeProfileState());
    }
}
