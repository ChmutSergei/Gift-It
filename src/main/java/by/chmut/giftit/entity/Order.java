package by.chmut.giftit.entity;

import java.time.LocalDate;

public class Order extends Entity {

    private long orderId;
    private long userId;
    private long cardId;
    private String details;
    private String status;
    private LocalDate initDate;
    private LocalDate issueDate;

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getCardId() {
        return cardId;
    }

    public void setCardId(long cardId) {
        this.cardId = cardId;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getInitDate() {
        return initDate;
    }

    public void setInitDate(LocalDate initDate) {
        this.initDate = initDate;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        Order order = (Order) object;
        if (orderId != order.orderId || userId != order.userId || cardId != order.cardId) return false;
        if (details == null) {
            if (order.details != null) {
                return false;
            }
        } else if (!details.equals(order.details)) {
            return false;
        }
        if (status == null) {
            if (order.status != null) {
                return false;
            }
        } else if (!status.equals(order.status)) {
            return false;
        }
        if (initDate == null) {
            if (order.initDate != null) {
                return false;
            }
        } else if (!initDate.equals(order.initDate)) {
            return false;
        }
        if (issueDate == null) {
            if (order.issueDate != null) {
                return false;
            }
        } else if (!issueDate.equals(order.issueDate)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 31;
        result = (int) (orderId ^ (orderId >>> 32)) * result;
        result = 31 * result + (int) (userId ^ (userId >>> 32));
        result = 31 * result + (int) (cardId ^ (cardId >>> 32));
        result = 31 * result + (details != null ? details.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (initDate != null ? initDate.hashCode() : 0);
        result = 31 * result + (issueDate != null ? issueDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", userId=" + userId +
                ", cardId=" + cardId +
                ", details='" + details + '\'' +
                ", status='" + status + '\'' +
                ", initDate=" + initDate +
                ", issueDate=" + issueDate +
                '}';
    }
}