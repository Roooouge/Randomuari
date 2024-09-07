package it.randomuari.commands;

import lombok.Getter;

@Getter
public class CommandResult {

    private final String outcome;
    private final String message;

    private CommandResult(String outcome, String message) {
        this.outcome = outcome;
        this.message = message;
    }

    /*
     *
     */

    public boolean success() {
        return outcome.equals(CommandManager.OK_COMMAND);
    }

    @Override
    public String toString() {
        return outcome + (message.isEmpty() ? "" : " - " + message);
    }

    /*
     *
     */

    public static CommandResult ok() {
        return ok("");
    }

    public static CommandResult ok(String message) {
        return new CommandResult(CommandManager.OK_COMMAND, message);
    }

    public static CommandResult ko(String message) {
        return new CommandResult(CommandManager.KO_COMMAND, message);
    }

    public static CommandResult invalidArgsCount(int args, int expected) {
        return invalidArgsCount(args, String.valueOf(expected));
    }

    public static CommandResult invalidArgsCount(int args, String expected) {
        return ko("Invalid number of argument(s): " + args + " arg(s), expected " + expected);
    }

    public static CommandResult argNotFound(String argName) {
        return ko("Argument not found: " + argName);
    }

    public static CommandResult integerNotFound(String argName, String argValue) {
        return ko("Argument " + (argName == null? "" : " " + argName) + "is not an int value: " + argValue);
    }

}
