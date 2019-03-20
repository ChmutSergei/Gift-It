package by.chmut.giftit.entity;

import java.util.Arrays;

/**
 * The Bitmap class provides storage
 * and transmission of bitmaps for all items in the system.
 *
 * @author Sergei Chmut.
 */
public class Bitmap extends Entity{

    /**
     * The Name of criteria.
     */
    private String name;
    /**
     * The Data - array of the bits.
     */
    private int[] data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int[] getData() {
        return data;
    }

    public void setData(int[] data) {
        this.data = data;
    }

    /**
     * Compares this bitmap to the specified object.
     * The result is {@code true} if and only if the argument is not {@code null} and is a
     * {@code Bitmap} object that represents the same fields as this
     * object.
     *
     * @param object the object to compare
     * @return {@code true} if the given object represents a {@code Bitmap}
     * equivalent to this bitmap, {@code false} otherwise
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Bitmap bitmap = (Bitmap) object;

        if (name != null ? !name.equals(bitmap.name) : bitmap.name != null) return false;
        return Arrays.equals(data, bitmap.data);
    }

    /**
     * Returns a hash code for this bitmap.
     *
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }
}