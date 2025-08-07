package net.remgant.weather.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@IdClass(AuthorityPK.class)
@Entity
@Table(name = "authorities")
public class Authority {
    @Id
    String username;
    @Id
    String authority;

    public Authority() {
    }

    public Authority(String username, String authority) {
        this.username = username;
        this.authority = authority;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String toString() {
        return "Authority{" +
                "username='" + username + '\'' +
                ", authority='" + authority + '\'' +
                '}';
    }
}
