package by.chmut.giftit.command.impl;

import by.chmut.giftit.command.Command;
import by.chmut.giftit.controller.Router;

import javax.servlet.http.HttpServletRequest;

public class ErrorCommand implements Command {
    @Override
    public Router execute(HttpServletRequest req) {
        return null;
    }
}
