package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.ViewManagerState;
import interface_adapter.create_profile.ChangeProfileViewModel;
import interface_adapter.home_screen.HomeScreenViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.match_page.MatchPageController;
import interface_adapter.match_page.MatchPageState;
import interface_adapter.match_page.MatchPageViewModel;
import interface_adapter.signup.SignupViewModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;

public class TopMenuView extends JPanel {
    private final ViewManagerModel viewManagerModel;
    private final HomeScreenViewModel  homeScreenViewModel;
    private final ChangeProfileViewModel changeProfileViewModel;
    private final MatchPageViewModel matchPageViewModel;
    private final SignupViewModel signupViewModel;

    private final MatchPageController matchPageController;
    private final LogoutController logoutController;

    private final JButton homeButton;
    private final JButton editButton;
    private final JButton matchButton;
    private final JButton logoutButton;

    public TopMenuView(ViewManagerModel viewManagerModel,
                       HomeScreenViewModel  homeScreenViewModel,
                       ChangeProfileViewModel changeProfileViewModel,
                       MatchPageViewModel matchPageViewModel,
                       SignupViewModel signupViewModel,
                       MatchPageController matchPageController,
                       LogoutController logoutController) {
        this.viewManagerModel = viewManagerModel;
        this.homeScreenViewModel = homeScreenViewModel;
        this.changeProfileViewModel = changeProfileViewModel;
        this.matchPageViewModel = matchPageViewModel;
        this.signupViewModel = signupViewModel;
        this.matchPageController = matchPageController;
        this.logoutController = logoutController;

        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(Box.createHorizontalGlue());
        this.homeButton = new JButton("Home");
        this.editButton = new JButton("Edit");
        this.matchButton = new JButton("Match");
        this.logoutButton = new JButton("Logout");
        this.add(homeButton);
        this.add(editButton);
        this.add(matchButton);
        this.add(logoutButton);
        this.add(Box.createHorizontalGlue());
        this.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        homeButton.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    if (evt.getSource().equals(homeButton)) {
                        ViewManagerState state = viewManagerModel.getState();
                        if(state.isLoggedIn() && state.hasProfile() && !state.getCurrentView().equals(homeScreenViewModel.getViewName())){
                            state.setCurrentView(homeScreenViewModel.getViewName());
                            viewManagerModel.setState(state);
                            viewManagerModel.firePropertyChange();
                        }
                    }
                }
            }
        );

        editButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        if (evt.getSource().equals(editButton)) {
                            ViewManagerState state = viewManagerModel.getState();
                            if(state.isLoggedIn() && !state.getCurrentView().equals(changeProfileViewModel.getViewName())){
                                state.setCurrentView(changeProfileViewModel.getViewName());
                                viewManagerModel.setState(state);
                                viewManagerModel.firePropertyChange();
                            }
                        }
                    }
                }
        );

        matchButton.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    if (evt.getSource().equals(matchButton)) {
                        ViewManagerState state = viewManagerModel.getState();
                        if(state.isLoggedIn() && state.hasProfile() && !state.getCurrentView().equals(matchPageViewModel.getViewName())){
                            matchPageController.userIdtoUserMatchesProfiles(state.getUserId());

                            MatchPageState matchesState = matchPageViewModel.getState();
                            matchesState.setCurrentId(state.getUserId());
                            matchPageViewModel.setState(matchesState);
                            matchPageViewModel.firePropertyChange();

                            state.setCurrentView(matchPageViewModel.getViewName());
                            viewManagerModel.setState(state);
                            viewManagerModel.firePropertyChange();
                        }
                    }
                }
            }
        );

        logoutButton.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    if (evt.getSource().equals(logoutButton)) {
                        ViewManagerState state = viewManagerModel.getState();
                        if(state.isLoggedIn() && !state.getCurrentView().equals(signupViewModel.getViewName())){
                            logoutController.execute();

                            state.setCurrentView(signupViewModel.getViewName());
                            state.setLoggedIn(false);
                            viewManagerModel.setState(state);
                            viewManagerModel.firePropertyChange();
                        }
                    }
                }
            }
        );
    }
}
