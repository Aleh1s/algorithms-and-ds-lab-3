package ua.algorithms.gui;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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



    public SimpleGUI() {
        super("DBMS");
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
            pkInputField.setEditable(true);
            valueInputField.setEditable(false);
        });

        insertRadio.addActionListener(e -> {
            actionButton.setText(INSERT.getActionName());
            pkInputField.setEditable(true);
            valueInputField.setEditable(true);
        });

        updateRadio.addActionListener(e -> {
            actionButton.setText(UPDATE.getActionName());
            pkInputField.setEditable(true);
            valueInputField.setEditable(true);
        });

        deleteRadio.addActionListener(e -> {
            actionButton.setText(DELETE.getActionName());
            pkInputField.setEditable(true);
            valueInputField.setEditable(false);
        });

        actionButton.addActionListener(e -> {
            if (selectRadio.isSelected()) {

            } else if (insertRadio.isSelected()) {

            } else if (updateRadio.isSelected()) {

            } else {

            }
        });
        
        setVisible(true);
    }
}
