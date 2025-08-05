package com.tusuapp.coreapi.services.bbb;

import com.tusuapp.coreapi.models.BookingSession;
import com.tusuapp.coreapi.repositories.BookingSessionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Formatter;


@Service
public class BBBService {

    private static String BBB_BASE_URL = "https://app.bbbserver.com/bbb-integration-v2/e3451170-d465-451c-81ee-9d553d881d5a/api";

    @Value("${BBB_SECRET}")
    String BBB_SECRET;

    @Autowired
    private BookingSessionRepo bookingSessionRepo;

    public String generateCreateUrl(BookingSession session) throws Exception {
        String meetingName = session.getBooking().getTutor().getFullName() + " Class";
        String query = "name=" + URLEncoder.encode(meetingName, StandardCharsets.UTF_8) +
                "&meetingID=" + session.getMeetingId() +
                "&attendeePW=" + session.getStudentPass() +
                "&moderatorPW=" + session.getTutorPass() +
                "&autoStartRecording=true" +
                "&endCallbackUrl=https://tusuapp.com" +
                "&logoutURL=https://tusuapp.com";
        String checksumSource = "create" + query + BBB_SECRET;
        String checksum = sha1Hex(checksumSource);
        return BBB_BASE_URL + "/create?" + query + "&checksum=" + checksum;
    }

    public String generateJoinUrl(String userName, String meetingID, String password) throws Exception {
        String encodedFullName = URLEncoder.encode(userName, StandardCharsets.UTF_8);
        String queryString = "fullName=" + encodedFullName + "&meetingID=" + meetingID
                + "&password=" + password
                + "&autoStartRecording=true"
                + "&endCallbackUrl=https://tusuapp.com"
                + "&logoutURL=https://tusuapp.com";
        String toHash = "join" + queryString + BBB_SECRET;
        String checksum = sha1Hex(toHash);
        return BBB_BASE_URL + "/join?" + queryString + "&checksum=" + checksum;
    }


    public String getRecordingsUrl(String meetingID) throws Exception {
        String query = "meetingID=" + meetingID;
        String checksumSource = "getRecordings" + query + BBB_SECRET;
        String checksum = sha1Hex(checksumSource);
        return BBB_BASE_URL + "/getRecordings?" + query + "&checksum=" + checksum;
    }

    private String sha1Hex(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] hashBytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
        Formatter formatter = new Formatter();
        for (byte b : hashBytes) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }


}
