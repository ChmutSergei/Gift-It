package by.chmut.giftit.command;

import by.chmut.giftit.controller.Router;
import by.chmut.giftit.entity.Comment;

import javax.servlet.http.HttpServletRequest;

public interface Command {

    Router execute(HttpServletRequest req);

    public static void main(String[] args) {
        Comment.Status status = Comment.Status.valueOf("ACTIVE");
        System.out.println(status);
    }
}
