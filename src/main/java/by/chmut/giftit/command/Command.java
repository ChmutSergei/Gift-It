package by.chmut.giftit.command;

import by.chmut.giftit.controller.Router;
import by.chmut.giftit.entity.Comment;

import javax.servlet.http.HttpServletRequest;

public interface Command {

    Router execute(HttpServletRequest req);
}
