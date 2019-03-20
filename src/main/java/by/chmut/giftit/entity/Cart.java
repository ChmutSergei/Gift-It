package by.chmut.giftit.entity;

import java.math.BigDecimal;

/**
 * The Cart class ensures the preservation
 * and transmission of user's cart in the system.
 *
 * @author Sergei Chmut.
 */
public class Cart extends Entity {

    /**
     * The Cart id.
     */
    private long cartId;
    /**
     * The User id who owns the cart.
     */
    private long userId;
    /**
     * The Item id contained in the cart.
     */
    private long itemId;
    /**
     * The Order id to which the cart is attached.
     */
    private long orderId;
    /**
     * The Count of this item in the cart.
     */
    private BigDecimal count;

    public long getCartId() {
        return cartId;
    }

    public void setCartId(long cartId) {
        this.cartId = cartId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getCount() {
        return count;
    }

    public void setCount(BigDecimal count) {
        this.count = count;
    }

    /**
     * Compares this cart to the specified object.
     * The result is {@code true} if and only if the argument is not {@code null} and is a
     * {@code Cart} object that represents the same fields as this
     * object.
     *
     * @param object the object to compare
     * @return {@code true} if the given object represents a {@code Cart}
     * equivalent to this cart, {@code false} otherwise
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        Cart cart = (Cart) object;
        if (cartId != cart.cartId || userId != cart.userId || itemId != cart.itemId || orderId != cart.orderId) {
            return false;
        }
        if (count == null) {
            if (cart.count != null) {
                return false;
            }
        } else if (!count.equals(cart.count)) {
            return false;
        }
        return true;
    }

    /**
     * Returns a hash code for this cart.
     *
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        int result = 31;
        result = (int) (cartId ^ (cartId >>> 32)) * result;
        result = 31 * result + (int) (userId ^ (userId >>> 32));
        result = 31 * result + (int) (itemId ^ (itemId >>> 32));
        result = 31 * result + (int) (orderId ^ (orderId >>> 32));
        result = 31 * result + (count != null ? count.hashCode() * result : 0);
        return result;
    }

    /**
     * Returns cart representation as a string
     *
     * @return the string which contains values of cart's fields
     */
    @Override
    public String toString() {
        return "Cart{" +
                "cartId=" + cartId +
                ", userId=" + userId +
                ", itemId=" + itemId +
                ", orderId=" + orderId +
                ", count=" + count +
                '}';
    }
}
