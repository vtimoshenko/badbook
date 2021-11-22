package rzd.pktb.tvs.badbook.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class User implements UserDetails {

    private int id;

    @NotEmpty(message = "Username should be not empty")
    @Size(min = 3, max = 256, message = "Size of username must be longer than 3")
    private String username;

    @NotEmpty(message = "Password should be not empty")
    @Size(min = 3, max = 256, message = "Size of password must be longer than 3")
    private String password;

    @Size(min = 1, max = 256, message = "Size of name must be shorter than 256")
    private String name;

    @Size(min = 1, max = 256, message = "Size of surname must be shorter than 256")
    private String surname;

    @Min(value = 1, message = "Age must be a positive digit")
    private int age;

    private String sex;

    @Size(min = 1, max = 2048, message = "Size of interests must be shorter than 2048 and not blank")
    private String interests;

    @Size(min = 1, max = 128, message = "Size of city must be shorter than 128 and not blank")
    private String city;

    private Set<Role> roles;

    private Set<Friend> friends;

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        roles = new HashSet<Role>();
        roles.add(new Role());
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Set<Friend> getFriends() {
        return friends;
    }

    public void setFriends(Set<Friend> friends) {
        this.friends = friends;
    }

}
