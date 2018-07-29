package codeu.model.data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class Utils {

    /** Returns the Instant into a String time format to display to users. */
    public static String getTime(Instant creation) {
        LocalDateTime localDate = LocalDateTime.ofInstant(creation, ZoneId.systemDefault());
        int hour = localDate.getHour();
        String timeAMPM = "";
        if (hour == 0) {
            hour = 12;
            timeAMPM = "AM";
        } else if (hour >= 12) {
            hour = hour % 12;
            timeAMPM = "PM";
        } else {
            timeAMPM = "AM";
        }
        int minute = localDate.getMinute();
        String strMinute = "" + minute;
        if (minute < 10) {
            strMinute = "0" + minute;
        }
        String date = localDate.getMonth().toString() + " " + localDate.getDayOfMonth() + ", " + localDate.getYear() + " - " + hour + ":" + strMinute + " " + timeAMPM;
        return date;
    }

    /** Returns the conversation URL between 2 users in alphabetical order. */
    public static String getPrivateConversationURL(String firstUser, String secondUser) {
        String conversationName = "";
        if (firstUser.compareTo(secondUser) > 0) {
            String temp = firstUser;
            firstUser = secondUser;
            secondUser = temp;
        }

        /* Creates conversation link by concatenating the two user's names in alphabetical order. */
        conversationName = firstUser + "-" + secondUser;
        return conversationName;
    }
}
