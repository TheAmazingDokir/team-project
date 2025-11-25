package interface_adapter.home_screen;

import interface_adapter.ViewModel;


public class HomeScreenViewModel extends ViewModel<HomeScreenState> {

    public HomeScreenViewModel() {
        super("home screen");
        setState(new HomeScreenState());
    }
}
