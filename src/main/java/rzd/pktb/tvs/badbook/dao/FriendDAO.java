package rzd.pktb.tvs.badbook.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import rzd.pktb.tvs.badbook.models.Friend;

import java.sql.Types;
import java.util.List;

@Service
public class FriendDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FriendDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Friend> get(int id) {
        List<Friend> list = jdbcTemplate.query("SELECT * FROM badbook.friends WHERE id=?", new Object[]{id},new int[]{Types.INTEGER}, new BeanPropertyRowMapper<>(Friend.class));
        return list;
    }

    public boolean save(Friend friend) {
        if (existsFriend(friend))
            return false;
        jdbcTemplate.update("INSERT INTO badbook.friends (id, friendid) VALUES (?, ?)", friend.getId(), friend.getFriendId());
        return true;
    }

    public void delete(Friend friend) {
        jdbcTemplate.update("DELETE FROM badbook.friends WHERE id=? and friendid=?", friend.getId(), friend.getFriendId());
    }

    public boolean existsFriend(Friend friend) {
        List<Friend> list = jdbcTemplate.query("SELECT * FROM badbook.friends WHERE id=? and friendid=?", new Object[]{friend.getId(), friend.getFriendId()},new int[]{Types.INTEGER, Types.INTEGER}, new BeanPropertyRowMapper<>(Friend.class));
        if (list.isEmpty())
            return false;
        else
            return true;
    }

}
