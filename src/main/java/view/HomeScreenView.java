package view;

import interface_adapter.home_screen.*;

import Entities.*;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


public class HomeScreenView extends JPanel implements ActionListener, PropertyChangeListener {
    private final String viewName = "Home Screen";
    private final HomeScreenViewModel homeScreenViewModel;

    private final JButton homeButton;
    private final JButton editButton;
    private final JButton matchButton;
    private final JButton logoutButton;

    private final JLabel nameLabel = null;

    private final JTextArea homeScreenTextArea = new JTextArea(10, 40);
    private final JScrollPane scrollPane = new JScrollPane(homeScreenTextArea);

    private final JButton likeButton;
    private final JButton dislikeButton;

    private HomeScreenController homeScreenController = null;

    public HomeScreenView(HomeScreenViewModel homeScreenViewModel) {
        this.homeScreenViewModel = homeScreenViewModel;

        this.homeScreenTextArea.setEditable(false);
        this.homeScreenTextArea.setLineWrap(true);

        this.scrollPane.setBounds(new Rectangle(20,30));
        this.scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        final JPanel upperbuttonsPanel = new JPanel();
        upperbuttonsPanel.setLayout(new BoxLayout(upperbuttonsPanel, BoxLayout.X_AXIS));
        this.homeButton = new JButton("Home");
        this.editButton = new JButton("Edit");
        this.matchButton = new JButton("Match");
        this.logoutButton = new JButton("Logout");
        upperbuttonsPanel.add(homeButton);
        upperbuttonsPanel.add(editButton);
        upperbuttonsPanel.add(matchButton);
        upperbuttonsPanel.add(logoutButton);

        final JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));
        namePanel.add(nameLabel);

        final JPanel resumeInfo = new JPanel();
        resumeInfo.setLayout(new BoxLayout(resumeInfo,BoxLayout.Y_AXIS));
        resumeInfo.add(new JLabel("Summary"), this.scrollPane);

        final JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
        this.likeButton = new JButton("Like");
        this.dislikeButton = new JButton("Dislike");
        buttons.add(likeButton);
        buttons.add(dislikeButton);

        likeButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        if (evt.getSource().equals(likeButton)) {
                            final HomeScreenState currentState = homeScreenViewModel.getState();

                            homeScreenController.execute(
                                    currentState.getCurrentId(),
                                    currentState.getOtherId(),
                                    true);
                        }
                    }
                }
        );

        dislikeButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        if (evt.getSource().equals(dislikeButton)) {
                            final HomeScreenState currentState = homeScreenViewModel.getState();

                            homeScreenController.execute(
                                    currentState.getCurrentId(),
                                    currentState.getOtherId(),
                                    false);
                        }
                    }
                }
        );
        this.add(upperbuttonsPanel);
        this.add(namePanel);
        this.add(resumeInfo);
        this.add(buttons);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Click " + e.getActionCommand());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final HomeScreenState state = (HomeScreenState) evt.getNewValue();
        setFields(state);
    }

    private void setFields(HomeScreenState state) {
        // to make the userProfile class public
        this.nameLabel.setText(state.getOtherProfileSummary());
    }



    public String getViewName() {
        return viewName;
    }

    public void setHomeScreenController(HomeScreenController homeScreenController) {
        this.homeScreenController = homeScreenController;
    }
}
