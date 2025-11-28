package view;

import interface_adapter.home_screen.*;

import entity.*;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


public class HomeScreenView extends JPanel implements ActionListener, PropertyChangeListener {
    private final String viewName = "home screen";
    private final HomeScreenViewModel homeScreenViewModel;

    private final JButton homeButton;
    private final JButton editButton;
    private final JButton matchButton;
    private final JButton logoutButton;

    private final JLabel nameLabel = new JLabel("Name");

    private final JTextArea homeScreenTextArea = new JTextArea(10, 40);
    private final JScrollPane scrollPane = new JScrollPane(homeScreenTextArea);

    private final JButton likeButton;
    private final JButton dislikeButton;

    private HomeScreenController homeScreenController = null;

    public HomeScreenView(HomeScreenViewModel homeScreenViewModel) {
        this.homeScreenViewModel = homeScreenViewModel;
        this.homeScreenViewModel.addPropertyChangeListener(this);

        this.homeScreenTextArea.setEditable(false);
        this.homeScreenTextArea.setLineWrap(true);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.scrollPane.setBounds(new Rectangle(20,20));
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


        nameLabel.setFont(new Font("Arial", Font.BOLD, 22));
        homeScreenTextArea.setFont(new Font("Arial", Font.PLAIN, 18));
        homeScreenTextArea.setMargin(new Insets(10, 10, 10, 10));
        homeScreenTextArea.setLineWrap(true);
        homeScreenTextArea.setWrapStyleWord(true);

        final JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));
        namePanel.add(nameLabel);

        final JPanel resumeInfo = new JPanel();
        resumeInfo.setLayout(new BoxLayout(resumeInfo,BoxLayout.Y_AXIS));
        JLabel summaryLabel = new JLabel("Summary");
        summaryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        summaryLabel.setFont(new Font("Arial", Font.PLAIN, 17));
        resumeInfo.add(summaryLabel);
        resumeInfo.add(scrollPane);


        final JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
        this.dislikeButton = new JButton("\uD83D\uDC4E Dislike");
        this.likeButton = new JButton("Like \uD83D\uDC4D");
        buttons.add(dislikeButton);
        buttons.add(likeButton);

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
        if (state.getOtherId() == null) {
            // get initial recommendation
            homeScreenController.execute(state.getCurrentId());
        } else {
            // to make the userProfile class public
            nameLabel.setText(state.getOtherProfileName());
            homeScreenTextArea.setText(state.getOtherProfileSummary());
        }
    }



    public String getViewName() {
        return viewName;
    }

    public void setHomeScreenController(HomeScreenController homeScreenController) {
        this.homeScreenController = homeScreenController;
    }
}
