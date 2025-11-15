package interface_adapter.home_screen;

import interface_adapter.ViewManagerModel;


public class HomeScreenPresenter implements ApproveRejectProfileOutputBoundary, RecommendProfileOutputBoundary {

    private final HomeScreenViewModel homeScreenViewModel;
//    private final ViewManagerModel viewManagerModel;

    public HomeScreenPresenter(HomeScreenViewModel homeScreenViewModel) {
//        this.viewManagerModel = viewManagerModel; ViewManagerModel viewManagerModel,
        this.homeScreenViewModel = homeScreenViewModel;
    }

    @Override
    public void prepareSuccessView(ApproveRejectProfileOutputData response) {

        final HomeScreenState homeScreenState = homeScreenViewModel.getState();
        homeScreenState.setCurrentId(response.getCurrentId());
        this.homeScreenViewModel.firePropertyChange();


//        this.viewManagerModel.setState(homeScreenViewModel.getViewName());
//        this.viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareSuccessView(RecommendProfileOutputData response) {

        final HomeScreenState homeScreenState = homeScreenViewModel.getState();
        homeScreenState.setOtherId(response.getId());
        homeScreenState.setOtherProfileSummary(response.getSummary);
        this.homeScreenViewModel.firePropertyChange();


//        this.viewManagerModel.setState(homeScreenViewModel.getViewName());
//        this.viewManagerModel.firePropertyChange();
    }

}
