package by.chmut.giftit.entity;

import java.time.LocalDate;

/**
 * The Comment class ensures the preservation
 * and transmission of user's comments in the system.
 *
 * @author Sergei Chmut.
 */
public class Comment extends Entity {

    /**
     * The Comment id.
     */
    private long commentId;
    /**
     * The User id who made comment.
     */
    private long userId;
    /**
     * The Item id for which the comment was made.
     */
    private long itemId;
    /**
     * The Message contains user's message.
     */
    private String message;
    /**
     * The Date - date when the comment was made .
     */
    private LocalDate date;
    /**
     * The Comment status.
     */
    private CommentStatus commentStatus;

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

    public CommentStatus getCommentStatus() {
        return commentStatus;
    }

    public void setCommentStatus(CommentStatus commentStatus) {
        this.commentStatus = commentStatus;
    }

    /**
     * Compares this comment to the specified object.
     * The result is {@code true} if and only if the argument is not {@code null} and is a
     * {@code Comment} object that represents the same fields as this
     * object.
     *
     * @param object the object to compare
     * @return {@code true} if the given object represents a {@code Comment}
     * equivalent to this comment, {@code false} otherwise
     */
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
        return commentStatus == comment.commentStatus;
    }

    /**
     * Returns a hash code for this comment.
     *
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        int result = 31;
        result = (int) (commentId ^ (commentId >>> 32)) * result;
        result = 31 * result + (int) (userId ^ (userId >>> 32));
        result = 31 * result + (int) (itemId ^ (itemId >>> 32));
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (commentStatus != null ? commentStatus.hashCode() : 0);
        return result;
    }

    /**
     * Returns comment representation as a string
     *
     * @return the string which contains values of comment's fields
     */
    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + commentId +
                ", userId=" + userId +
                ", itemId=" + itemId +
                ", message='" + message + '\'' +
                ", date=" + date +
                ", commentStatus=" + commentStatus +
                '}';
    }

    /**
     * The enum Comment status sets possible status for the comment.
     *
     * @author Sergei Chmut.
     */
    public enum CommentStatus {
        /**
         * New comment status when comment created.
         */
        NEW,
        /**
         * Active comment status for comments that have been moderated.
         */
        ACTIVE,
        /**
         * Blocked comment status for comments that moderator has blocked.
         */
        BLOCKED
    }
}
