package bg.sofia.tu.enums;

/**
 * author: Aleksandar Karadzhinov
 * email: aleksandar.karadjinov@gmail.com
 * <p/>
 * created on 15/06/2016 @ 20:39.
 */
public enum Priority {
    HIGHEST("Highest", "red"),
    HIGH("High", "orange"),
    MEDIUM("Medium", "gold"),
    LOW("Low", "green"),
    LOWEST("Lowest", "blue");

    private String value;

    private String color;

    Priority(String value, String color) {
        this.value = value;
        this.color = color;
    }

    public String getValue() {
        return value;
    }

    public String getColor() {
        return color;
    }
}
