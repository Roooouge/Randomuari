package it.randomuari.commands;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class CommandHistory extends ArrayList<String> implements KeyListener, DocumentListener {

    private final JTextField commandField;
    private int index;

    public CommandHistory(JTextField commandField) {
        this.commandField = commandField;
        index = 0;
    }

    public void addCommand(String command) {
        add(0, command);
    }

    /*
     *
     */

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP && index < size()) {
            commandField.setText(get(index));

            if (index != size()-1)
                index ++;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN && index >= 0) {
            if (index == 0)
                commandField.setText("");
            else {
                index --;
                commandField.setText(get(index));
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Empty
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Empty
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        // Empty
    }


    @Override
    public void removeUpdate(DocumentEvent e) {
        // Empty
    }


    @Override
    public void changedUpdate(DocumentEvent e) {
        index = 0;
    }
}
