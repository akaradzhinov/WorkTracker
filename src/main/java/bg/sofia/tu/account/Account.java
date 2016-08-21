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
    private String email;

    @JsonIgnore
    private String password;

    private String role = "ROLE_USER";

    private Timestamp created;

    protected Account() {

    }

    public Account(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.created = new Timestamp(new Date().getTime());
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}
