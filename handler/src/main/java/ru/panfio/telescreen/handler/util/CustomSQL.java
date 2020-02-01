package ru.panfio.telescreen.handler.util;

//CHECKSTYLE:OFF
public final class CustomSQL {

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
            "FROM Sounds s LEFT JOIN Users u ON s.user_id == u._id ";
    public static final String MIBAND_DAILY_SQL = "select Date||\" \" as Date,LightSleepMin+DeepSleepMin as InBedMin,DeepSleepMin,LightSleepMin,SleepStart,SleepEnd,AwakeMin\n" +
            ",DailyDistanceMeter,DailySteps,DailyBurnCalories\n" +
            ",DailyDistanceMeter-RunDistanceMeter as WalkDistance,WalkTimeMin,DailyBurnCalories-RunBurnCalories as WalkBurnCalories\n" +
            ",RunDistanceMeter,RunTimeMin,RunBurnCalories from (\n" +
            "select date,summary\n" +
            ",cast(rtrim(substr(summary,instr(summary,'\"lt\":')+5,7),', \"st\":') as Integer) as LightSleepMin\n" +
            ",cast(rtrim(substr(summary,instr(summary,'\"dp\":')+5,7),', \"ed\":') as Integer) as DeepSleepMin\n" +
            ",cast(rtrim(substr(summary,instr(summary,'\"wk\":')+5,7),', \"dp\":') as Integer) as AwakeMin\n" +
            "\n" +
            ",cast(rtrim(substr(summary,instr(summary,'\"st\":')+5,10),', \"wk\":') as INTEGER) * 1000 as SleepStart\n" +
            ",cast(rtrim(substr(summary,instr(summary,'\"ed\":')+5,10),', \"v\":') as Integer) * 1000 as SleepEnd\n" +
            "\n" +
            ",cast(rtrim(substr(summary,instr(summary,'\"rn\":')+5,7),', \"cal\"') as Integer) as RunTimeMin\n" +
            ",cast(rtrim(substr(summary,instr(summary,'\"runDist\":')+10,7),', \"wk\":') as Integer) as RunDistanceMeter\n" +
            ",cast(rtrim(substr(summary,instr(summary,'\"runCal\":')+9,7),', \"dis\"') as Integer) as RunBurnCalories\n" +
            "\n" +
            ",cast(rtrim(substr(substr(summary,instr(summary,'\"wk\":')+5),instr(substr(summary,instr(summary,'\"wk\":')+5),'\"wk\":')+5,7),', \"ttl\"') as Integer) as WalkTimeMin\n" +
            ",cast(rtrim(substr(summary,instr(summary,'\"ttl\":')+6,7),', \"runC') as Integer) as DailySteps\n" +
            ",cast(rtrim(substr(summary,instr(summary,'\"dis\":')+6,7),'}}') as Integer) as DailyDistanceMeter\n" +
            ",cast(rtrim(substr(summary,instr(summary,'\"cal\":')+6,4),', \"runD') as Integer) as DailyBurnCalories\n" +
            " from date_data where type = 0) order by date";
    private CustomSQL() {

    }
}
//CHECKSTYLE:ON
