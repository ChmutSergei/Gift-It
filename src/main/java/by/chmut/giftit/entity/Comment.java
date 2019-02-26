package by.chmut.giftit.entity;

import java.time.LocalDate;

public class Comment extends Entity {

    private long commentId;
    private long userId;
    private long itemId;
    private String message;
    private LocalDate date;
    private Status status;

    public long getCommentId() {
        return commentId;
    }

    public void setCommentId(long commentId) {
        this.commentId = commentId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        Comment comment = (Comment) object;
        if (commentId != comment.commentId || userId != comment.userId || itemId != comment.itemId) {
            return false;
        }
        if (message == null) {
            if (comment.message != null) {
                return false;
            }
        } else if (!message.equals(comment.message)) {
            return false;
        }
        if (date == null) {
            if (comment.date != null) {
                return false;
            }
        } else if (!date.equals(comment.date)) {
            return false;
        }
        return status == comment.status;
    }

    @Override
    public int hashCode() {
        int result = 31;
        result = (int) (commentId ^ (commentId >>> 32)) * result;
        result = 31 * result + (int) (userId ^ (userId >>> 32));
        result = 31 * result + (int) (itemId ^ (itemId >>> 32));
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + commentId +
                ", userId=" + userId +
                ", itemId=" + itemId +
                ", message='" + message + '\'' +
                ", date=" + date +
                ", status=" + status +
                '}';
    }

    public enum Status {
        NEW, ACTIVE, BLOCKED
    }
}
