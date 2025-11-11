package view;

import interface_adapter.Homescreen.*;
import interface_adapter.login.LoginState;


import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class HomeScreenView extends JPanel implements ActionListener, PropertyChangeListener {
    private final String viewName = "Home Screen";
    private final HomeScreenViewModel homeScreenViewModel;

    private final JLabel nameLabel = new JLabel("Name:");
    private final JLabel usernameLabel = new JLabel();

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

                            HomeScreenController.execute(
                                    currentState.getCurrentId(),
                                    currentState.getOtherId(),true);
                        }
                    }
                }
        );

        dislikeButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        if (evt.getSource().equals(dislikeButton)) {
                            final HomeScreenState currentState = homeScreenViewModel.getState();

                            HomeScreenController.execute(currentState.getCurrentId(),
                                    currentState.getOtherId(),false);
                        }
                    }
                }
        );

        this.add();
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
        profile = findObject(state.getOtherId());
        homeScreenTextArea.setText(profile.summary);
    }

    public String getViewName() {
        return viewName;
    }

    public void setLoginController(HomeScreenController homeScreenController) {
        this.homeScreenController = homeScreenController;
    }
}
