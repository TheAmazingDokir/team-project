package view;

import entity.UserProfile;
import interface_adapter.home_screen.HomeScreenController;
import interface_adapter.home_screen.HomeScreenState;
import interface_adapter.match_page.*;

import Entities.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class MatchPageView extends JPanel implements ActionListener, PropertyChangeListener {
    private final String viewName = "Match Page";
    private final MatchPageViewModel matchPageViewModel;

    private final JButton homeButton;
    private final JButton editButton;
    private final JButton matchButton;
    private final JButton logoutButton;

    private final JLabel nameLabel= new JLabel("Name:");
    private final JLabel userNameLabel;
    private final JLabel emailLabel= new JLabel("Email:");
    private final JLabel userEmailLabel;
    private final JLabel phoneLabel= new JLabel("Phone:");
    private final JLabel userPhoneLabel;

    private MatchPageController matchPageController = null;

    private final JScrollPane matchUserScrollPane = new JScrollPane();

    private final JTextArea resumeTextArea = new JTextArea(10, 20);
    private final JScrollPane resumeScrollPane = new JScrollPane(resumeTextArea);


    private final JButton refreshButton = new JButton("Refresh");

    public MatchPageView(MatchPageViewModel matchPageViewModel) {
        this.matchPageViewModel = matchPageViewModel;
        this.matchPageViewModel.addPropertyChangeListener(this);


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


        final JPanel resumePanel = new JPanel();

        this.userNameLabel = new JLabel("");
        this.userEmailLabel = new JLabel("");
        this.userPhoneLabel = new JLabel("");
        final JPanel namePanel = new JPanel();
        final JPanel emailPanel = new JPanel();
        final JPanel phonePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));
        emailPanel.setLayout(new BoxLayout(emailPanel, BoxLayout.X_AXIS));
        phonePanel.setLayout(new BoxLayout(phonePanel, BoxLayout.X_AXIS));
        namePanel.add(nameLabel);
        namePanel.add(userNameLabel);
        emailPanel.add(emailLabel);
        emailPanel.add(userEmailLabel);
        phonePanel.add(phoneLabel);
        phonePanel.add(userPhoneLabel);

        this.resumeTextArea.setEditable(false);
        this.resumeTextArea.setLineWrap(true);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        resumePanel.setLayout(new BoxLayout(resumePanel, BoxLayout.Y_AXIS));
        resumePanel.add(namePanel);
        resumePanel.add(emailPanel);
        resumePanel.add(phonePanel);
        resumePanel.add(resumeScrollPane);

        final JPanel componentPanel = new JPanel();
        componentPanel.setLayout(new BoxLayout(componentPanel, BoxLayout.X_AXIS));
        componentPanel.add(matchUserScrollPane);
        componentPanel.add(resumePanel);



        nameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        emailLabel.setFont(new Font("Arial", Font.BOLD, 20));
        phoneLabel.setFont(new Font("Arial", Font.BOLD, 20));
        userNameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        userEmailLabel.setFont(new Font("Arial", Font.BOLD, 20));
        userPhoneLabel.setFont(new Font("Arial", Font.BOLD, 20));
        resumeTextArea.setFont(new Font("Arial", Font.PLAIN, 18));
        resumeTextArea.setMargin(new Insets(10, 10, 10, 10));
        resumeTextArea.setLineWrap(true);
        resumeTextArea.setWrapStyleWord(true);


        this.add(upperbuttonsPanel);
        this.add(componentPanel);
        this.add(this.refreshButton);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Click " + e.getActionCommand());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final MatchPageState state = (MatchPageState) evt.getNewValue();
        setFields(state);
    }

    private void setFields(MatchPageState state) {
        matchUserScrollPane.removeAll();
        // to make the userProfile class public
        for (int i = 0; i < state.getUserMatches().size(); i++) {
            JButton userButton = new JButton(state.getUserMatchesName().get(i));
            Integer userId = state.getUserMatches().get(i).getUserId();
            userButton.addActionListener(
                    new ActionListener(){
                        public void actionPerformed(ActionEvent evt) {
                            if (evt.getSource().equals(userButton)) {
                                matchPageController.execute(userId);
                            }
                        }
                    }
            );
            matchUserScrollPane.add(userButton);
        }
        userNameLabel.setText(state.getName());
        userEmailLabel.setText(state.getEmail());
        userPhoneLabel.setText(state.getPhone());
        resumeTextArea.setText(state.getResume());
    }



    public String getViewName() {
        return viewName;
    }

    public void setMatchPageController(MatchPageController matchPageController) {
        this.matchPageController = matchPageController;
    }

}
