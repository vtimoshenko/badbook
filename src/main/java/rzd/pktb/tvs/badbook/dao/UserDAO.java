package rzd.pktb.tvs.badbook.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import rzd.pktb.tvs.badbook.models.Friend;
import rzd.pktb.tvs.badbook.models.User;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class UserDAO implements UserDetailsService {

    private final JdbcTemplate jdbcTemplate;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserDAO(JdbcTemplate jdbcTemplate, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.jdbcTemplate = jdbcTemplate;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public List<User> list() {
        List<User> list = jdbcTemplate.query("SELECT * FROM badbook.users", new Object[]{},new int[]{}, new BeanPropertyRowMapper<>(User.class));
        return list;
    }

    public List<User> list(List<Friend> flist) {
        if (flist.isEmpty())
            return new ArrayList<User>();

        String inSQL = String.join(",", Collections.nCopies(flist.size(), "?"));
        Object[] ids = new Object[flist.size()];
        for (int i=0;i<flist.size();i++) ids[i] = flist.get(i).getFriendId();
        int[] types = new int[flist.size()];
        Arrays.fill(types, Types.INTEGER);

        List<User> list = jdbcTemplate.query(String.format("SELECT * FROM badbook.users where id in (%s)", inSQL),
                ids,
                types,
                new BeanPropertyRowMapper<>(User.class));

        return list;
    }

    public User get(int id) {
        List<User> list = jdbcTemplate.query("SELECT * FROM badbook.users WHERE id=?", new Object[]{id},new int[]{Types.INTEGER}, new BeanPropertyRowMapper<>(User.class));
        return list.get(0);
    }

    public boolean save(User user) {
        if (existsUsername(user.getUsername()))
            return false;
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        jdbcTemplate.update("INSERT INTO badbook.users (" +
                "username, " +
                "password," +
                "name," +
                "surname," +
                "age," +
                "sex," +
                "interests," +
                "city" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                user.getUsername(),
                user.getPassword(),
                user.getName(),
                user.getSurname(),
                user.getAge(),
                user.getSex(),
                user.getInterests(),
                user.getCity());
        return true;
    }

    public void update(int id, User user) {
        jdbcTemplate.update("UPDATE badbook.users SET " +
                "name=?, " +
                "surname=?, " +
                "age=?, " +
                "sex=?, " +
                "interests=?, " +
                "city=? " +
                " WHERE id=?",
                user.getName(),
                user.getSurname(),
                user.getAge(),
                user.getSex(),
                user.getInterests(),
                user.getCity(),
                id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM badbook.users WHERE id=?", id);
    }

    public boolean existsUsername(String username) {
        List<User> list = jdbcTemplate.query("SELECT * FROM badbook.users WHERE username=?", new Object[]{username},new int[]{Types.VARCHAR}, new BeanPropertyRowMapper<>(User.class));
        if (list.isEmpty())
            return false;
        else
            return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<User> list = jdbcTemplate.query("SELECT * FROM badbook.users WHERE username=?", new Object[]{username},new int[]{Types.VARCHAR}, new BeanPropertyRowMapper<>(User.class));
        if (list.isEmpty())
            throw new UsernameNotFoundException(username);
        else
            return list.get(0);
    }
}
