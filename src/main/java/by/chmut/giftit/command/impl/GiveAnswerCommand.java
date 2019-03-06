package by.chmut.giftit.command.impl;

import by.chmut.giftit.command.Command;
import by.chmut.giftit.controller.Router;
import by.chmut.giftit.service.QuestionService;
import by.chmut.giftit.service.ServiceException;
import by.chmut.giftit.service.ServiceFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

import static by.chmut.giftit.command.CommandType.ADMINISTRATION;
import static by.chmut.giftit.constant.AttributeName.*;
import static by.chmut.giftit.constant.PathPage.ERROR_PATH;

public class GiveAnswerCommand implements Command {

    private static final Logger logger = LogManager.getLogger();

    private QuestionService service = ServiceFactory.getInstance().getQuestionService();

    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router();
        router.setRedirectPath(ADMINISTRATION.name().toLowerCase());
        String answer = request.getParameter(ANSWER_PARAMETER_NAME);
        String optionalId = request.getParameter(QUESTION_ID_PARAMETER_NAME);
        long questionId = optionalId != null ? Long.parseLong(optionalId) : 0;
        if (answer.length() <= MAX_LENGTH_COMMENT || questionId == 0) {
            try {
                service.setAnswer(questionId, answer);
            } catch (ServiceException exception) {
                logger.error("Error when try to save answer for question", exception);
                request.getSession().setAttribute(EXCEPTION_PARAMETER_NAME, exception);
                router.setRedirectPath(ERROR_PATH);
            }
        } else {
            logger.error("Error when try to save answer for question");
            request.getSession().setAttribute(EXCEPTION_PARAMETER_NAME, "Incorrect data for set answer");
            router.setRedirectPath(ERROR_PATH);
        }
        return router;
    }
}
