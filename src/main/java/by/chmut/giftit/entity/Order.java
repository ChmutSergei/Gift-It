package by.chmut.giftit.entity;

import java.time.LocalDate;

/**
 * The Order class ensures the preservation
 * and transmission of orders from users in the system.
 *
 * @author Sergei Chmut.
 */
public class Order extends Entity {

    /**
     * The Order id.
     */
    private long orderId;
    /**
     * The User id.
     */
    private long userId;
    /**
     * The Details of the order.
     */
    private String details;
    /**
     * The Order status.
     */
    private OrderStatus orderStatus;
    /**
     * The Init date - date when order created.
     */
    private LocalDate initDate;
    /**
     * The Issue date - date when order is done.
     */
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

    /**
     * Compares this order to the specified object.
     * The result is {@code true} if and only if the argument is not {@code null} and is a
     * {@code Order} object that represents the same fields as this
     * object.
     *
     * @param object the object to compare
     * @return {@code true} if the given object represents a {@code Order}
     * equivalent to this order, {@code false} otherwise
     */
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

    /**
     * Returns a hash code for this order.
     *
     * @return a hash code value for this object.
     */
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

    /**
     * Returns order representation as a string
     *
     * @return the string which contains values of order's fields
     */
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

    /**
     * The enum Order status sets possible status for the order.
     *
     * @author Sergei Chmut.
     */
    public enum OrderStatus {
        /**
         * New order status when order created.
         */
        NEW,
        /**
         * Paid order status when order paid.
         */
        PAID,
        /**
         * Done order status when order is done.
         */
        DONE
    }
}