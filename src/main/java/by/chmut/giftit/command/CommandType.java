package by.chmut.giftit.command;

import by.chmut.giftit.command.impl.*;

public enum CommandType {

    HOME(new DefaultCommand()),
    ERROR(new ErrorCommand()),
    SIGNIN(new SignInCommand()),
    SIGNUP(new SignUpCommand()),
    LOGIN(new LoginCommand()),
    LOGOUT(new LogoutCommand()),
    CREATE_ITEM(new CreateItemCommand()),
    ADD_ITEM(new AddItemCommand()),
    ACCOUNT(new AccountCommand()),
    PREVIEW_ITEM(new PreviewItemCommand()),
    CART(new CartCommand()),
    EMPTY_CART(new ResetCartCommand()),
    ADMINISTRATION(new AdminCommand()),
    SEARCH_USER(new SearchUserCommand()),
    USER_PROCESSING(new UserProcessingCommand()),
    GIVE_ANSWER(new GiveAnswerCommand()),
    ABOUT(new AboutCommand()),
    MODERATOR( new ModeratorCommand()),
    MODERATE( new ModerateCommand()),
    REGISTRATION(new RegistrationCommand());

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
