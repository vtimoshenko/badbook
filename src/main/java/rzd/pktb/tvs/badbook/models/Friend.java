package rzd.pktb.tvs.badbook.models;

public class Friend {

    private int id;
    private int friendId;

    public Friend() {
    }

    public Friend(int id, int friendId) {
        this.id = id;
        this.friendId = friendId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFriendId() {
        return friendId;
    }

    public void setFriendId(int friendId) {
        this.friendId = friendId;
    }
}
