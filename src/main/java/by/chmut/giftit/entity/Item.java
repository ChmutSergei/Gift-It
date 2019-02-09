package by.chmut.giftit.entity;

import java.math.BigDecimal;

public class Item extends Entity {

    private long itemId;
    private String itemName;
    private String type;
    private String description;
    private BigDecimal cost;

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

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

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
        if (cost == null) {
            if (item.cost != null) {
                return false;
            }
        } else if (!cost.equals(item.cost)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 31;
        result = (int) (itemId ^ (itemId >>> 32)) * result;
        result = 31 * result + (itemName != null ? itemName.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (cost != null ? cost.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", cost=" + cost +
                '}';
    }
}
