package by.chmut.giftit.command;

import by.chmut.giftit.command.impl.*;

/**
 * The enum Command type contains possible commands to process from request.
 * Each command contains a specific class that implements the interface command.
 *
 * @author Sergei Chmut.
 */
public enum CommandType {

    MAIN(new MainCommand()),
    ERROR(new ErrorCommand()),
    SIGN_IN(new SignInCommand()),
    SIGN_UP(new SignUpCommand()),
    LOGIN(new LoginCommand()),
    LOGOUT(new LogoutCommand()),
    CREATE_ITEM(new CreateItemCommand()),
    ADD_ITEM(new AddItemCommand()),
    ACCOUNT(new AccountCommand()),
    PREVIEW_ITEM(new PreviewItemCommand()),
    CART(new CartCommand()),
    RESET_CART(new ResetCartCommand()),
    ADMINISTRATION(new AdminCommand()),
    SEARCH_USER(new SearchUserCommand()),
    USER_PROCESSING(new UserProcessingCommand()),
    GIVE_ANSWER(new GiveAnswerCommand()),
    ABOUT(new AboutCommand()),
    MODERATOR( new ModeratorCommand()),
    MODERATE( new ModerateCommand()),
    PAYMENT( new PaymentCommand()),
    MAKE_PAYMENT( new MakePaymentCommand()),
    REGISTRATION(new RegistrationCommand());

    private Command command;

    CommandType(Command command) {
        this.command = command;
    }

    public Command getCommand() {
        return command;
    }

    /**
     * Choose type command by name.
     * If missing command returns command ERROR.
     *
     * @param commandName the command parameter
     * @return the command type
     */
    public static CommandType chooseType(String commandName) {
        if (!(commandName == null || commandName.isEmpty())) {
            for (CommandType type : CommandType.values()) {
                String command = type.name();
                if (command.equalsIgnoreCase(commandName)) {
                    return type;
                }
            }
        }
        return ERROR;
    }
}
