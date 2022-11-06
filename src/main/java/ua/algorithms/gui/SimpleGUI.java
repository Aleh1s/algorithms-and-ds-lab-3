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
    private JTextArea outputArea;
    private JTextField valueInputField;
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

        outputArea = new JTextArea();
        outputArea.setToolTipText("Output area");

        valueInputField = new JTextField("Value");
        valueInputField.setToolTipText("Value");

        pkInputField = new JTextField("Primary key");
        pkInputField.setToolTipText("Primary key");

        actionButton = new JButton(SELECT.getActionName());
        actionButton.setToolTipText("Action button");

        valueInputField.setEditable(false);

        Panel actionPanel = new Panel();
        actionPanel.add(pkInputField);
        actionPanel.add(valueInputField);
        actionPanel.add(actionButton);
        actionPanel.setLayout(new GridLayout(3, 1, 10, 10));

        Container contentPane = getContentPane();
        contentPane.setLayout(new GridLayout(1, 3, 10, 10));
        contentPane.add(radioPanel);
        contentPane.add(actionPanel);
        contentPane.add(outputArea);

        selectRadio.addActionListener(e -> {
            actionButton.setText(SELECT.getActionName());
            valueInputField.setEditable(false);
        });

        insertRadio.addActionListener(e -> {
            actionButton.setText(INSERT.getActionName());
            valueInputField.setEditable(true);
        });

        updateRadio.addActionListener(e -> {
            actionButton.setText(UPDATE.getActionName());
            valueInputField.setEditable(true);
        });

        deleteRadio.addActionListener(e -> {
            actionButton.setText(DELETE.getActionName());
            valueInputField.setEditable(false);
        });

        actionButton.addActionListener(e -> {
            String pk = pkInputField.getText();
            if (selectRadio.isSelected()) {
                String message = controller.select(pk);
                outputArea.setText(message);
            } else if (insertRadio.isSelected()) {
                String value = valueInputField.getText();
                String message = controller.insert(pk, value);
                outputArea.setText(message);
            } else if (updateRadio.isSelected()) {
                String value = valueInputField.getText();
                String message = controller.update(pk, value);
                outputArea.setText(message);
            } else {
                String message = controller.delete(pk);
                outputArea.setText(message);
            }
        });
        
        setVisible(true);
    }
}
