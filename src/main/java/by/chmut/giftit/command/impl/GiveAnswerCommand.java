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

/**
 * The Give answer command class provides to save the answer to the question.
 *
 * @author Sergei Chmut.
 */
public class GiveAnswerCommand implements Command {

    /**
     * The logger for logging possible errors.
     */
    private static final Logger logger = LogManager.getLogger();

    /**
     * The Question service to take advantage of business logic capabilities.
     */
    private QuestionService service = ServiceFactory.getInstance().getQuestionService();

    /**
     * The method receives the answer from the request
     * and sends it to the service level for saving to the database.
     * If an error occurs while trying to save,
     * return Router with Error page path.
     *
     * @param request the request object that is passed to the servlet
     * @return the router object that contains page path for forward or redirect
     */
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
