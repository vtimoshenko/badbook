package rzd.pktb.tvs.badbook.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import rzd.pktb.tvs.badbook.models.Friend;
import rzd.pktb.tvs.badbook.models.User;
import rzd.pktb.tvs.badbook.models.UserSearch;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class UserDAO implements UserDetailsService {

    private String table;
    private final JdbcTemplate jdbcTemplate;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDAO.class);

    @Autowired
    public UserDAO(JdbcTemplate jdbcTemplate, BCryptPasswordEncoder bCryptPasswordEncoder, @Value("${db.tables.users}") String table) {
        this.jdbcTemplate = jdbcTemplate;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.table = table;
    }

    public List<User> list() {
        List<User> list = jdbcTemplate.query("SELECT * FROM " + table, new Object[]{},new int[]{}, new BeanPropertyRowMapper<>(User.class));
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

        List<User> list = jdbcTemplate.query(String.format("SELECT * FROM " + table + " where id in (%s)", inSQL),
                ids,
                types,
                new BeanPropertyRowMapper<>(User.class));

        return list;
    }

    public List<User> list(UserSearch userSearch) {
        //LOGGER.info("name:" + userSearch.getName() + ", surname:" + userSearch.getSurname() + ", query:" + "SELECT * FROM " + table + " WHERE name LIKE '%" + userSearch.getName() + "' and surname LIKE '%" + userSearch.getSurname() + "'");
        List<User> list = jdbcTemplate.query("SELECT * FROM " + table + " WHERE name LIKE ? and surname LIKE ? order by id", new Object[]{userSearch.getName() + "%", userSearch.getSurname() + "%"},new int[]{Types.VARCHAR, Types.VARCHAR}, new BeanPropertyRowMapper<>(User.class));
        return list;
    }

    public User get(int id) {
        List<User> list = jdbcTemplate.query("SELECT * FROM " + table + " WHERE id=?", new Object[]{id},new int[]{Types.INTEGER}, new BeanPropertyRowMapper<>(User.class));
        return list.get(0);
    }

    public boolean save(User user) {
        if (existsUsername(user.getUsername()))
            return false;
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        jdbcTemplate.update("INSERT INTO " + table + " (" +
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
        jdbcTemplate.update("UPDATE " + table + " SET " +
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
        jdbcTemplate.update("DELETE FROM " + table + " WHERE id=?", id);
    }

    public boolean existsUsername(String username) {
        List<User> list = jdbcTemplate.query("SELECT * FROM " + table + " WHERE username=?", new Object[]{username},new int[]{Types.VARCHAR}, new BeanPropertyRowMapper<>(User.class));
        if (list.isEmpty())
            return false;
        else
            return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<User> list = jdbcTemplate.query("SELECT * FROM " + table + " WHERE username=?", new Object[]{username},new int[]{Types.VARCHAR}, new BeanPropertyRowMapper<>(User.class));
        if (list.isEmpty())
            throw new UsernameNotFoundException(username);
        else
            return list.get(0);
    }
}
