package by.chmut.giftit.command;

import by.chmut.giftit.command.impl.DefaultCommand;
import by.chmut.giftit.command.impl.ErrorCommand;
import by.chmut.giftit.command.impl.ResultsCommand;

public enum CommandType {

    HOME(new DefaultCommand()),
    ERROR(new ErrorCommand()),
    RESULTS(new ResultsCommand());

    private Command command;

    CommandType(Command command) {
        this.command = command;
    }

    public Command getCommand() {
       return command;
    }

    public static CommandType chooseType(String commandParameter) {
        if (!(commandParameter == null || commandParameter.isEmpty())) {
            for (CommandType type : CommandType.values()) {
                String commandName = type.name();
                if (commandName.equalsIgnoreCase(commandParameter)) {
                    return type;
                }
            }
        }
        return ERROR;
    }
}
