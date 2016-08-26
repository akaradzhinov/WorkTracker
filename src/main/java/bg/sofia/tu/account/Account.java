package bg.sofia.tu.account;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 * author: Aleksandar Karadzhinov
 * email: aleksandar.karadjinov@gmail.com
 * <p/>
 * created on 15/08/2016 @ 20:28.
 */
@Entity
@Table(name = "account")
public class Account implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String username;

    @JsonIgnore
    private String password;

    private String role = "ROLE_USER";

    private Timestamp created;

    private int enabled;

    protected Account() {

    }

    public Account(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.created = new Timestamp(new Date().getTime());
        enabled = 1;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Timestamp getCreated() {
        return created;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }
}
