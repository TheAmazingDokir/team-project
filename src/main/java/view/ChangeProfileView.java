package view;

import interface_adapter.create_profile.ChangeProfileController;
import interface_adapter.create_profile.ChangeProfileState;
import interface_adapter.create_profile.ChangeProfileViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ChangeProfileView extends JPanel implements ActionListener, PropertyChangeListener {

    private final String viewName = "change profile";
    private final ChangeProfileViewModel changeProfileViewModel;

    private ChangeProfileController changeProfileController = null;

    private final JTextField emailField;
    private final JTextField phoneNumberField;
    private final JComboBox<String> roleComboBox; // "Employer" / "Job Seeker"
    private final JTextArea summaryTextArea;
    private final JScrollPane summaryScrollPane;
    private final JTextArea profileTextArea;
    private final JScrollPane profileScrollPane;

    private final JButton saveButton;
    private final JButton cancelButton;

    public ChangeProfileView(ChangeProfileViewModel changeProfileViewModel) {
        this.changeProfileViewModel = changeProfileViewModel;
        this.changeProfileViewModel.addPropertyChangeListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Title
        final JLabel title = new JLabel("Change Profile");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Arial", Font.BOLD, 22));

        // Email
        final JPanel emailPanel = new JPanel();
        emailPanel.setLayout(new BoxLayout(emailPanel, BoxLayout.X_AXIS));
        final JLabel emailLabel = new JLabel("Email");
        emailField = new JTextField(25);
        emailPanel.add(emailLabel);
        emailPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        emailPanel.add(emailField);

        // Phone Number
        final JPanel phonePanel = new JPanel();
        phonePanel.setLayout(new BoxLayout(phonePanel, BoxLayout.X_AXIS));
        final JLabel phoneLabel = new JLabel("Phone Number");
        phoneNumberField = new JTextField(20);
        phonePanel.add(phoneLabel);
        phonePanel.add(Box.createRigidArea(new Dimension(10, 0)));
        phonePanel.add(phoneNumberField);

        // Role selector
        final JPanel rolePanel = new JPanel();
        rolePanel.setLayout(new BoxLayout(rolePanel, BoxLayout.X_AXIS));
        final JLabel roleLabel = new JLabel("I am a...");
        roleComboBox = new JComboBox<>(new String[]{"Employer", "Job Seeker"});
        rolePanel.add(roleLabel);
        rolePanel.add(Box.createRigidArea(new Dimension(10, 0)));
        rolePanel.add(roleComboBox);

        // Summary
        final JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
        final JLabel summaryLabel = new JLabel("Summary");
        summaryLabel.setFont(new Font("Arial", Font.PLAIN, 17));
        summaryTextArea = new JTextArea(5, 40);
        summaryTextArea.setLineWrap(true);
        summaryTextArea.setWrapStyleWord(true);
        summaryTextArea.setMargin(new Insets(5, 5, 5, 5));
        summaryScrollPane = new JScrollPane(summaryTextArea);
        summaryScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        summaryPanel.add(summaryLabel);
        summaryPanel.add(summaryScrollPane);

        // Profile
        final JPanel profilePanel = new JPanel();
        profilePanel.setLayout(new BoxLayout(profilePanel, BoxLayout.Y_AXIS));
        final JLabel profileLabel = new JLabel("Profile (Resume / Job Description)");
        profileLabel.setFont(new Font("Arial", Font.PLAIN, 17));
        profileTextArea = new JTextArea(8, 40);
        profileTextArea.setLineWrap(true);
        profileTextArea.setWrapStyleWord(true);
        profileTextArea.setMargin(new Insets(5, 5, 5, 5));
        profileScrollPane = new JScrollPane(profileTextArea);
        profileScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        profilePanel.add(profileLabel);
        profilePanel.add(profileScrollPane);

        // Buttons
        final JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");
        buttonPanel.add(saveButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(cancelButton);

        // Save button action -> calls controller
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (evt.getSource().equals(saveButton) && changeProfileController != null) {
                    // Map dropdown selection to boolean isEmployer
                    final String role = (String) roleComboBox.getSelectedItem();
                    final boolean isEmployer = "Employer".equals(role);

                    changeProfileController.execute(
                            emailField.getText(),
                            phoneNumberField.getText(),
                            summaryTextArea.getText(),
                            profileTextArea.getText(),
                            isEmployer
                    );
                }
            }
        });

        cancelButton.addActionListener(this);

        add(Box.createRigidArea(new Dimension(0, 10)));
        add(title);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(emailPanel);
        add(phonePanel);
        add(rolePanel);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(summaryPanel);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(profilePanel);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(buttonPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Click " + e.getActionCommand());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final ChangeProfileState state = (ChangeProfileState) evt.getNewValue();
        setFields(state);
    }

    private void setFields(ChangeProfileState state) {
        if (state == null) {
            return;
        }
        if (state.getEmail() != null) {
            emailField.setText(state.getEmail());
        }
        if (state.getPhoneNumber() != null) {
            phoneNumberField.setText(state.getPhoneNumber());
        }
        if (state.getIsEmployer() != null) {
            roleComboBox.setSelectedItem(state.getIsEmployer() ? "Employer" : "Job Seeker");
        }
        if (state.getSummary() != null) {
            summaryTextArea.setText(state.getSummary());
        }
        if (state.getProfile() != null) {
            profileTextArea.setText(state.getProfile());
        }
    }

    public String getViewName() {
        return viewName;
    }

    public void setChangeProfileController(ChangeProfileController changeProfileController) {
        this.changeProfileController = changeProfileController;
    }
}
