package ru.panfio.telescreen.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.model.CallRecord;
import ru.panfio.telescreen.repository.CallRecordRepository;
import ru.panfio.telescreen.util.CustomSQL;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CallService {

    //todo save telegram/skype calls

    private final CallRecordRepository callRecordRepository;

    private final DbManager dbManager;

    /**
     * Constructor.
     *
     * @param callRecordRepository repo
     * @param dbManager            dbManager
     */
    public CallService(CallRecordRepository callRecordRepository,
                       DbManager dbManager) {
        this.callRecordRepository = callRecordRepository;
        this.dbManager = dbManager;
    }

    /**
     * Finds and returns call records for the period.
     *
     * @param from time
     * @param to   time
     * @return records
     */
    public Iterable<CallRecord> getCallHistoryBetweenDates(
            LocalDateTime from, LocalDateTime to) {
        return callRecordRepository.findByDateBetween(from, to);
    }

    /**
     * Saves call records in the database.
     *
     * @param records list of records
     */
    public void saveCallRecords(List<CallRecord> records) {
        callRecordRepository.saveAll(records); //todo
    }


    /**
     * Processing Call history from android phone.
     */
    public void processCallHistory() {
        List<CallRecord> callRecords = new ArrayList<>();
        try (Connection conn = dbManager.connectSQLite("call/calllog.db");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(CustomSQL.CALL_HISTORY_SQL)) {
            while (rs.next()) {
                CallRecord cr = new CallRecord();
                cr.setDate(rs.getTimestamp("date").toLocalDateTime());
                cr.setDuration(rs.getInt("duration"));
                cr.setNumber(rs.getString("number"));
                String name = rs.getString("name");
                cr.setName(name.equals("") ? "Unknown" : name);
                cr.setType(rs.getInt("type"));

                callRecords.add(cr);
            }
            saveCallRecords(callRecords);
        } catch (SQLException | FileNotFoundException e) {
            log.info("Failed processing call history");
        }
    }
}
