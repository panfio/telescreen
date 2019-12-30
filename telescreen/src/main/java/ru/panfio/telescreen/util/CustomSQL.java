package ru.panfio.telescreen.util;


public final class CustomSQL {
    //CHECKSTYLE:OFF
    public static final String PLAY_HISTORY_SQL = "SELECT * FROM playhistory";
    public static final String CALL_HISTORY_SQL = "SELECT "
            + "_id, number, date, duration, name, type "
            + "FROM calls";
    public static final String APP_ACTIVITY_SQL = "SELECT\n" +
            "    e._id, e.package_id,\n" +
            "    e.timestamp, e.type,\n" +
            "    e.instance_id, p.package_name\n" +
            "FROM events e LEFT JOIN packages p \n" +
            "ON e.package_id = p._id\n" +
            "ORDER BY e._id ASC";
    public static final String SOUND_INFO_SQL = "SELECT s._id AS id, " +
            "u.username AS username, " +
            "s.title AS title, " +
            "s.permalink_url AS permalink_url " +
            "FROM Sounds s LEFT JOIN Users u ON s.user_id == u._id " +
            "WHERE s._id == ?";
    //CHECKSTYLE:ON
    private CustomSQL() {

    }

}
