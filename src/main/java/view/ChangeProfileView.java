package view;

import interface_adapter.create_profile.ChangeProfileController;
import interface_adapter.create_profile.ChangeProfileState;
import interface_adapter.create_profile.ChangeProfileViewModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
    private final JComboBox<String> roleComboBox;
    private final JTextArea summaryTextArea;
    private final JScrollPane summaryScrollPane;
    private final JTextArea profileTextArea;
    private final JScrollPane profileScrollPane;

    private final JButton saveButton;
    private final JButton cancelButton;

    // Consistent width for all input components
    private static final int FIELD_WIDTH = 400;
    private static final int LARGE_FIELD_WIDTH = 542;
    private static final int FIELD_HEIGHT = 28;
    private static final int TEXTAREA_HEIGHT_SMALL = 100;
    private static final int TEXTAREA_HEIGHT_LARGE = 150;

    public ChangeProfileView(ChangeProfileViewModel changeProfileViewModel) {
        this.changeProfileViewModel = changeProfileViewModel;
        this.changeProfileViewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Title
        final JLabel title = new JLabel("Change Profile");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Arial", Font.BOLD, 22));

        mainPanel.add(title);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Email
        emailField = new JTextField();
        configureTextField(emailField);
        JPanel emailPanel = createLabeledRow("Email", emailField);

        // Phone Number
        phoneNumberField = new JTextField();
        configureTextField(phoneNumberField);
        JPanel phonePanel = createLabeledRow("Phone Number", phoneNumberField);

        // Role
        roleComboBox = new JComboBox<>(new String[]{"Employer", "Job Seeker"});
        configureComboBox(roleComboBox);
        JPanel rolePanel = createLabeledRow("I am a...", roleComboBox);

        // Summary
        summaryTextArea = new JTextArea();
        configureTextArea(summaryTextArea);
        summaryScrollPane = wrapInScrollPane(summaryTextArea, TEXTAREA_HEIGHT_SMALL);
        JPanel summaryPanel = createLabeledBlock("Summary", summaryScrollPane);

        // Profile
        profileTextArea = new JTextArea();
        configureTextArea(profileTextArea);
        profileScrollPane = wrapInScrollPane(profileTextArea, TEXTAREA_HEIGHT_LARGE);
        JPanel profilePanel = createLabeledBlock("Profile (Resume / Job Description)", profileScrollPane);

        // Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");

        Dimension buttonSize = new Dimension(120, 32);
        saveButton.setPreferredSize(buttonSize);
        saveButton.setMaximumSize(buttonSize);
        cancelButton.setPreferredSize(buttonSize);
        cancelButton.setMaximumSize(buttonSize);

        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(saveButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        buttonPanel.add(cancelButton);
        buttonPanel.add(Box.createHorizontalGlue());

        // Save button action listerner
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (evt.getSource().equals(saveButton) && changeProfileController != null) {
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

        // Add everything to panel
        mainPanel.add(emailPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(phonePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(rolePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(summaryPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(profilePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(buttonPanel);

        // Center the panel
        JPanel centerWrapper = new JPanel();
        centerWrapper.setLayout(new GridBagLayout());
        centerWrapper.add(mainPanel, new GridBagConstraints());

        add(centerWrapper, BorderLayout.CENTER);
    }

    // MY HELPERS --------------------------------------------------

    private void configureTextField(JTextField field) {
        Dimension size = new Dimension(FIELD_WIDTH, FIELD_HEIGHT);
        field.setPreferredSize(size);
        field.setMaximumSize(size);
        field.setMinimumSize(size);
    }

    private void configureComboBox(JComboBox<?> comboBox) {
        Dimension size = new Dimension(FIELD_WIDTH, FIELD_HEIGHT);
        comboBox.setPreferredSize(size);
        comboBox.setMaximumSize(size);
        comboBox.setMinimumSize(size);
    }

    private void configureTextArea(JTextArea textArea) {
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setMargin(new Insets(8, 8, 8, 8));
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
    }

    private JScrollPane wrapInScrollPane(JTextArea textArea, int height) {
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        Dimension size = new Dimension(LARGE_FIELD_WIDTH, height);
        scrollPane.setPreferredSize(size);
        scrollPane.setMaximumSize(size);
        scrollPane.setMinimumSize(size);

        return scrollPane;
    }

    private JPanel createLabeledRow(String labelText, JComponent field) {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.setAlignmentX(Component.CENTER_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, FIELD_HEIGHT + 10));
        row.setBorder(new EmptyBorder(2, 0, 2, 0));

        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(130, FIELD_HEIGHT));
        label.setMinimumSize(new Dimension(130, FIELD_HEIGHT));
        label.setMaximumSize(new Dimension(130, FIELD_HEIGHT));

        row.add(Box.createHorizontalGlue());
        row.add(label);
        row.add(Box.createRigidArea(new Dimension(10, 0)));
        row.add(field);
        row.add(Box.createHorizontalGlue());

        return row;
    }

    private JPanel createLabeledBlock(String labelText, JComponent block) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setBorder(new EmptyBorder(2, 0, 2, 0));

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        block.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(block);

        return panel;
    }

    // --------------------------------------------------------------

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
