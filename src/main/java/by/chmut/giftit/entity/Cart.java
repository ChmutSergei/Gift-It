package by.chmut.giftit.entity;

import java.math.BigDecimal;

public class Cart extends Entity {

    private long cartId;
    private long userId;
    private long itemId;
    private long orderId;
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
