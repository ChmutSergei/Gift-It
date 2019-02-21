package by.chmut.giftit.entity;

import java.util.Arrays;

public class Bitmap extends Entity{

    private String name;
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

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Bitmap bitmap = (Bitmap) object;

        if (name != null ? !name.equals(bitmap.name) : bitmap.name != null) return false;
        return Arrays.equals(data, bitmap.data);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }
}
