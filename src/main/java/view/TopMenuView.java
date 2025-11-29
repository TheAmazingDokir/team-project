package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.ViewManagerState;
import interface_adapter.create_profile.ChangeProfileViewModel;
import interface_adapter.home_screen.HomeScreenViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.signup.SignupViewModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;

public class TopMenuView extends JPanel {
    private final ViewManagerModel viewManagerModel;
    private final HomeScreenViewModel  homeScreenViewModel;
    private final ChangeProfileViewModel changeProfileViewModel;
    private final SignupViewModel signupViewModel;

    private final LogoutController logoutController;

    private final JButton homeButton;
    private final JButton editButton;
    private final JButton matchButton;
    private final JButton logoutButton;

    public TopMenuView(ViewManagerModel viewManagerModel,
                       HomeScreenViewModel  homeScreenViewModel,
                       ChangeProfileViewModel changeProfileViewModel,
                       SignupViewModel signupViewModel,
                       LogoutController logoutController) {
        this.viewManagerModel = viewManagerModel;
        this.homeScreenViewModel = homeScreenViewModel;
        this.changeProfileViewModel = changeProfileViewModel;
        this.signupViewModel = signupViewModel;
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
                        if(!state.getCurrentView().equals(homeScreenViewModel.getViewName()) && state.hasProfile()){
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
                            if(!state.getCurrentView().equals(changeProfileViewModel.getViewName())){
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
                        if(!state.getCurrentView().equals(homeScreenViewModel.getViewName()) && state.hasProfile()){
                            state.setCurrentView(homeScreenViewModel.getViewName());
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
                        if(!state.getCurrentView().equals(signupViewModel.getViewName())){
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
