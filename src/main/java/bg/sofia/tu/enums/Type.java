package bg.sofia.tu.enums;

/**
 * author: Aleksandar Karadzhinov
 * email: aleksandar.karadjinov@gmail.com
 * <p/>
 * created on 15/06/2016 @ 20:41.
 */
public enum Type {
    STORY("Story", "darkblue"),
    TASK("Task", "DarkMagenta "),
    BUG("Bug", "darkred"),
    TECHNICAL_STORY("Technical Story", "steelblue"),
    TEST("Test", "teal");

    private String value;

    private String color;

    Type(String value, String color) {
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
