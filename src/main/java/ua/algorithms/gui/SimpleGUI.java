package ua.algorithms.gui;

import ua.algorithms.mvc.Controller;

import javax.swing.*;
import java.awt.*;

import static ua.algorithms.gui.BasicAction.*;

public class SimpleGUI extends JFrame {

    private JButton actionButton;
    private JRadioButton selectRadio;
    private JRadioButton insertRadio;
    private JRadioButton deleteRadio;
    private JRadioButton updateRadio;
    private JTextPane outputArea;
    private JTextField firstNameInputField;
    private JTextField lastNameInputField;
    private JTextField emailInputField;
    private JTextField pkInputField;
    private final Controller controller;

    public SimpleGUI(Controller controller) {
        super("DBMS");
        this.controller = controller;
    }

    public void init() {
        setBounds(270, 50, 1000, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        selectRadio = new JRadioButton(SELECT.getActionName());
        insertRadio = new JRadioButton(INSERT.getActionName());
        deleteRadio = new JRadioButton(DELETE.getActionName());
        updateRadio = new JRadioButton(UPDATE.getActionName());
        selectRadio.setSelected(true);

        ButtonGroup radioGroup = new ButtonGroup();
        radioGroup.add(selectRadio);
        radioGroup.add(insertRadio);
        radioGroup.add(deleteRadio);
        radioGroup.add(updateRadio);

        Panel radioPanel = new Panel();
        radioPanel.add(selectRadio);
        radioPanel.add(insertRadio);
        radioPanel.add(deleteRadio);
        radioPanel.add(updateRadio);
        radioPanel.setLayout(new GridLayout(4, 1, 10, 10));

        outputArea = new JTextPane();
        outputArea.setToolTipText("Output area");


        String primaryKeyText = "Primary key";
        pkInputField = new JTextField(primaryKeyText);
        pkInputField.setToolTipText(primaryKeyText);

        String firstNameText = "First name";
        firstNameInputField = new JTextField(firstNameText);
        firstNameInputField.setToolTipText(firstNameText);

        String lastNameText = "Last name";
        lastNameInputField = new JTextField(lastNameText);
        lastNameInputField.setToolTipText(lastNameText);

        String emailText = "Email";
        emailInputField = new JTextField(emailText);
        emailInputField.setToolTipText(emailText);

        actionButton = new JButton(SELECT.getActionName());
        actionButton.setToolTipText("Action button");

        firstNameInputField.setEditable(false);
        lastNameInputField.setEditable(false);
        emailInputField.setEditable(false);

        Panel actionPanel = new Panel();
        actionPanel.add(pkInputField);
        actionPanel.add(firstNameInputField);
        actionPanel.add(lastNameInputField);
        actionPanel.add(emailInputField);
        actionPanel.add(actionButton);
        actionPanel.setLayout(new GridLayout(5, 1, 10, 10));

        Container contentPane = getContentPane();
        contentPane.setLayout(new GridLayout(1, 3, 10, 10));
        contentPane.add(radioPanel);
        contentPane.add(actionPanel);
        contentPane.add(outputArea);

        selectRadio.addActionListener(e -> {
            actionButton.setText(SELECT.getActionName());
            firstNameInputField.setEditable(false);
            lastNameInputField.setEditable(false);
            emailInputField.setEditable(false);
        });

        insertRadio.addActionListener(e -> {
            actionButton.setText(INSERT.getActionName());
            firstNameInputField.setEditable(true);
            lastNameInputField.setEditable(true);
            emailInputField.setEditable(true);
        });

        updateRadio.addActionListener(e -> {
            actionButton.setText(UPDATE.getActionName());
            firstNameInputField.setEditable(true);
            lastNameInputField.setEditable(true);
            emailInputField.setEditable(true);
        });

        deleteRadio.addActionListener(e -> {
            actionButton.setText(DELETE.getActionName());
            firstNameInputField.setEditable(false);
            lastNameInputField.setEditable(false);
            emailInputField.setEditable(false);
        });

        actionButton.addActionListener(e -> {
            String pk = pkInputField.getText();
            if (selectRadio.isSelected()) {
                String message = controller.select(pk);
                outputArea.setText(message);
            } else if (insertRadio.isSelected()) {
                String firstName = firstNameInputField.getText();
                String lastName = lastNameInputField.getText();
                String email = emailInputField.getText();
                String message = controller.insert(pk, firstName, lastName, email);
                outputArea.setText(message);
            } else if (updateRadio.isSelected()) {
                String firstName = firstNameInputField.getText();
                String lastName = lastNameInputField.getText();
                String email = emailInputField.getText();
                String message = controller.update(pk, firstName, lastName, email);
                outputArea.setText(message);
            } else {
                String message = controller.delete(pk);
                outputArea.setText(message);
            }
        });
        
        setVisible(true);
    }
}
