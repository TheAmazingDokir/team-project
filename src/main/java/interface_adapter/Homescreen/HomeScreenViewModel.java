package interface_adapter.Homescreen;

import interface_adapter.ViewModel;


public class HomeScreenViewModel extends ViewModel<HomeScreenState> {

    public HomeScreenViewModel() {
        super("Home Screen");
        setState(new HomeScreenState());
    }
}
