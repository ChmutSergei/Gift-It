package by.chmut.giftit.entity;

import java.time.LocalDate;

public class Order extends Entity {

    private long orderId;
    private long userId;
    private String details;
    private OrderStatus orderStatus;
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

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
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
        if (orderId != order.orderId || userId != order.userId ) return false;
        if (details == null) {
            if (order.details != null) {
                return false;
            }
        } else if (!details.equals(order.details)) {
            return false;
        }
        if (orderStatus != order.orderStatus) {
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
        result = 31 * result + (details != null ? details.hashCode() : 0);
        result = 31 * result + (orderStatus != null ? orderStatus.hashCode() : 0);
        result = 31 * result + (initDate != null ? initDate.hashCode() : 0);
        result = 31 * result + (issueDate != null ? issueDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", userId=" + userId +
                ", details='" + details + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", initDate=" + initDate +
                ", issueDate=" + issueDate +
                '}';
    }

    public enum OrderStatus {
        PAID, DONE
    }
}