package by.chmut.giftit.entity;

import java.io.File;
import java.math.BigDecimal;

/**
 * The Item class ensures the preservation
 * and transmission of items in the system.
 *
 * @author Sergei Chmut.
 */
public class Item extends Entity {

    /**
     * The Item id.
     */
    private long itemId;
    /**
     * The Item name.
     */
    private String itemName;
    /**
     * The Type.
     */
    private String type;
    /**
     * The Description.
     */
    private String description;
    /**
     * The Active - status item : active/not active.
     */
    private boolean active;
    /**
     * The Price.
     */
    private BigDecimal price;
    /**
     * The Count.
     */
    private BigDecimal count;
    /**
     * The Image of item.
     */
    private File image;

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getCount() {
        return count;
    }

    public void setCount(BigDecimal count) {
        this.count = count;
    }

    public File getImage() {
        return image;
    }

    public void setImage(File image) {
        this.image = image;
    }

    /**
     * Compares this item to the specified object.
     * The result is {@code true} if and only if the argument is not {@code null} and is a
     * {@code Item} object that represents the same fields as this
     * object.
     *
     * @param object the object to compare
     * @return {@code true} if the given object represents a {@code Item}
     * equivalent to this item, {@code false} otherwise
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        Item item = (Item) object;
        if (itemId != item.itemId) return false;
        if (active != item.active) return false;
        if (itemName == null) {
            if (item.itemName != null) {
                return false;
            }
        } else if (!itemName.equals(item.itemName)) {
            return false;
        }
        if (type == null) {
            if (item.type != null) {
                return false;
            }
        } else if (!type.equals(item.type)) {
            return false;
        }
        if (description == null) {
            if (item.description != null) {
                return false;
            }
        } else if (!description.equals(item.description)) {
            return false;
        }
        if (price == null) {
            if (item.price != null) {
                return false;
            }
        } else if (!price.equals(item.price)) {
            return false;
        }
        if (count == null) {
            if (item.count != null) {
                return false;
            }
        } else if (!count.equals(item.count)) {
            return false;
        }
        if (image == null) {
            if (item.image != null) {
                return false;
            }
        } else if (!image.equals(item.image)) {
            return false;
        }
        return true;
    }

    /**
     * Returns a hash code for this item.
     *
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        int result = 31;
        result = (int) (itemId ^ (itemId >>> 32)) * result;
        result = 31 * result + (!active ? 1 : 0)*31;
        result = 31 * result + (itemName != null ? itemName.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (count != null ? count.hashCode() : 0);
        result = 31 * result + (image != null ? image.hashCode() : 0);
        return result;
    }

    /**
     * Returns item representation as a string
     *
     * @return the string which contains values of item's fields
     */
    @Override
    public String toString() {
        return "Item{" +
                "itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", active=" + active +
                ", price=" + price +
                ", image=" + image +
                '}';
    }
}
