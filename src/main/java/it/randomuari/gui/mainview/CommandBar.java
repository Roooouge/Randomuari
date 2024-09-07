package it.randomuari.gui.mainview;

import it.randomuari.commands.CommandHistory;
import it.randomuari.commands.CommandManager;
import it.randomuari.commands.CommandResult;
import it.randomuari.gui.GUIUtils;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommandBar extends JPanel implements ActionListener {

    private final JTextField commandField;
    private final CommandHistory history;

    public CommandBar() {
        setLayout(new BorderLayout());
        JTextArea slashArea = new JTextArea("/");
        slashArea.setBorder(new EmptyBorder(0, 2, 1, 1));
        slashArea.setFont(GUIUtils.FONT);
        slashArea.setBackground(GUIUtils.BACKGROUND);
        slashArea.setForeground(GUIUtils.FOREGROUND);
        slashArea.setEditable(false);
        slashArea.setFocusable(false);

        commandField = new JTextField();
        history = new CommandHistory(commandField);
        commandField.setFont(GUIUtils.FONT);
        commandField.setBackground(GUIUtils.BACKGROUND);
        commandField.setForeground(GUIUtils.FOREGROUND);
        commandField.setBorder(null);
        commandField.addActionListener(this);
        commandField.addKeyListener(history);
        commandField.getDocument().addDocumentListener(history);

        add(slashArea, BorderLayout.WEST);
        add(commandField, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = commandField.getText().trim();
        if (command.isEmpty())
            return;

        log.debug("Command request: /{}", command);

        history.addCommand(command);
        commandField.setText("");

        CommandResult commandResult = CommandManager.execCommand(command);
        if (commandResult.success())
            log.debug("Command result: {}", commandResult);
        else
            log.error("Command result: {}", commandResult);

    }

    /*
     *
     */

    public void commandFocus() {
        commandField.requestFocus();
    }

}
