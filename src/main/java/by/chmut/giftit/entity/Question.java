package by.chmut.giftit.entity;

import java.time.LocalDate;

public class Question extends Entity {

    private long questionId;
    private long userId;
    private String request;
    private String response;
    private LocalDate requestDate;
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
