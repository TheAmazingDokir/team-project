package interface_adapter.match_page;

import interface_adapter.ViewModel;


public class MatchPageViewModel extends ViewModel<MatchPageState> {

    public MatchPageViewModel() {
        super("Match Page");
        setState(new MatchPageState());
    }

}
