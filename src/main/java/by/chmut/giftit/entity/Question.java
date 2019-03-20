package by.chmut.giftit.entity;

import java.time.LocalDate;

/**
 * The Question class ensures the preservation
 * and transmission of questions from users in the system.
 *
 * @author Sergei Chmut.
 */
public class Question extends Entity {

    /**
     * The Question id.
     */
    private long questionId;
    /**
     * The User id.
     */
    private long userId;
    /**
     * The Request contains user's question.
     */
    private String request;
    /**
     * The Response contains admin's answer.
     */
    private String response;
    /**
     * The Request date - date when user made request.
     */
    private LocalDate requestDate;
    /**
     * The Response date - date when admin return response.
     */
    private LocalDate responseDate;

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

    public LocalDate getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(LocalDate responseDate) {
        this.responseDate = responseDate;
    }

    /**
     * Compares this question to the specified object.
     * The result is {@code true} if and only if the argument is not {@code null} and is a
     * {@code Question} object that represents the same fields as this
     * object.
     *
     * @param object the object to compare
     * @return {@code true} if the given object represents a {@code Question}
     * equivalent to this question, {@code false} otherwise
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        Question question = (Question) object;
        if (questionId != question.questionId || userId != question.userId) return false;
        if (request == null) {
            if (question.request != null) {
                return false;
            }
        } else if (!request.equals(question.request)) {
            return false;
        }
        if (response == null) {
            if (question.response != null) {
                return false;
            }
        } else if (!response.equals(question.response)) {
            return false;
        }
        if (requestDate == null) {
            if (question.requestDate != null) {
                return false;
            }
        } else if (!requestDate.equals(question.requestDate)) {
            return false;
        }
        if (responseDate == null) {
            if (question.responseDate != null) {
                return false;
            }
        } else if (!responseDate.equals(question.responseDate)) {
            return false;
        }
        return true;
    }

    /**
     * Returns a hash code for this question.
     *
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        int result = (int) (questionId ^ (questionId >>> 32));
        result = 31 * result + (int) (userId ^ (userId >>> 32));
        result = 31 * result + (request != null ? request.hashCode() : 0);
        result = 31 * result + (response != null ? response.hashCode() : 0);
        result = 31 * result + (requestDate != null ? requestDate.hashCode() : 0);
        result = 31 * result + (responseDate != null ? responseDate.hashCode() : 0);
        return result;
    }

    /**
     * Returns question representation as a string
     *
     * @return the string which contains values of question's fields
     */
    @Override
    public String toString() {
        return "Question{" +
                "questionId=" + questionId +
                ", userId=" + userId +
                ", request='" + request + '\'' +
                ", response='" + response + '\'' +
                ", requestDate=" + requestDate +
                ", responseDate=" + responseDate +
                '}';
    }
}
