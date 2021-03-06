package bg.sofia.tu.task.priority;

import javax.persistence.*;

/**
 * author: Aleksandar Karadzhinov
 * email: alexandar.karadzhinov@cayetanogaming.com
 * <p/>
 * created on 09/09/2016 @ 15:40.
 */
@Entity
@Table(name = "priorities")
public class Priority {

    private int id;

    private String value;

    private String description;

    private int power;

    private boolean enabled;

    public Priority() {}

    public Priority(String value, String description, int power, boolean enabled) {
        this.value = value;
        this.description = description;
        this.power = power;
        this.enabled = enabled;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(nullable = false, unique = true, length = 12)
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Column(nullable = false, length = 128)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Priority{");
        sb.append("id=").append(id);
        sb.append(", value='").append(value).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", power=").append(power);
        sb.append(", enabled=").append(enabled);
        sb.append('}');
        return sb.toString();
    }
}
