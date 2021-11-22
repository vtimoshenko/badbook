package rzd.pktb.tvs.badbook.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import rzd.pktb.tvs.badbook.models.Friend;

import java.sql.Types;
import java.util.List;

@Service
public class FriendDAO {

    private String table;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FriendDAO(JdbcTemplate jdbcTemplate, @Value("${db.tables.friends}") String table) {
        this.jdbcTemplate = jdbcTemplate;
        this.table = table;
    }

    public List<Friend> get(int id) {
        List<Friend> list = jdbcTemplate.query("SELECT * FROM " + table + " WHERE id=?", new Object[]{id},new int[]{Types.INTEGER}, new BeanPropertyRowMapper<>(Friend.class));
        return list;
    }

    public boolean save(Friend friend) {
        if (existsFriend(friend))
            return false;
        jdbcTemplate.update("INSERT INTO " + table + " (id, friendid) VALUES (?, ?)", friend.getId(), friend.getFriendId());
        return true;
    }

    public void delete(Friend friend) {
        jdbcTemplate.update("DELETE FROM " + table + " WHERE id=? and friendid=?", friend.getId(), friend.getFriendId());
    }

    public boolean existsFriend(Friend friend) {
        List<Friend> list = jdbcTemplate.query("SELECT * FROM " + table + " WHERE id=? and friendid=?", new Object[]{friend.getId(), friend.getFriendId()},new int[]{Types.INTEGER, Types.INTEGER}, new BeanPropertyRowMapper<>(Friend.class));
        if (list.isEmpty())
            return false;
        else
            return true;
    }

}
