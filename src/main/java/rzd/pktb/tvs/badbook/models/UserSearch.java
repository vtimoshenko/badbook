package rzd.pktb.tvs.badbook.models;

import javax.validation.constraints.Size;

public class UserSearch {

    @Size(min = 0, max = 256, message = "Size of name must be shorter than 256")
    private String name;

    @Size(min = 0, max = 256, message = "Size of surname must be shorter than 256")
    private String surname;

    public UserSearch() {
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

}
