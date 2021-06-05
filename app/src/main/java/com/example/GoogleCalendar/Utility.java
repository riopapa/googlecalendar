package com.example.GoogleCalendar;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CalendarContract;
import android.util.Log;

import org.joda.time.DateTimeZone;
import org.joda.time.Instant;
import org.joda.time.LocalDate;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;

import androidx.core.app.ActivityCompat;



public class Utility {


    public static HashMap<LocalDate, EventInfo> localDateHashMap = new HashMap<>();

    public static HashMap<LocalDate, EventInfo> readCalendarEvent(Context context, LocalDate minTime, LocalDate maxTime) {

        int f=1;
//        String selection = "(( " + CalendarContract.Events.SYNC_EVENTS + " = " + f +" ) AND ( " + CalendarContract.Events.DTSTART + " >= " + mintime.toDateTimeAtStartOfDay().getMillis() + " ) AND ( " + CalendarContract.Events.DTSTART + " <= " + maxtime.toDateTimeAtStartOfDay().getMillis() + " ))";
        String selection = "(( " + CalendarContract.Events.DTSTART + " >= " + minTime.toDateTimeAtStartOfDay().getMillis() + " ) AND ( " + CalendarContract.Events.DTSTART + " <= " + maxTime.toDateTimeAtStartOfDay().getMillis() + " ))";
        Log.e("selection","----- selection ----");
        Log.e("selection",selection);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        Cursor cursor = context.getContentResolver().query(
                CalendarContract.Events.CONTENT_URI,
                new String[]{"_id",     // 0
                         "title",       // 1
                        "description",  // 2
                        "dtstart",      // 3
                        "dtend",        // 4
                        "eventLocation", // 5
                        CalendarContract.Events.ALL_DAY,        // 6    1, 0
                        CalendarContract.Events.CALENDAR_DISPLAY_NAME, // 7 rio papa
                        CalendarContract.Events.CALENDAR_TIME_ZONE, // 8    Asia/Seoul
                        CalendarContract.Events.SYNC_DATA9, // 9
                        CalendarContract.Events.RRULE   // 10
//                        CalendarContract.Events.ACCOUNT_TYPE, //   com.google
                        },
                        selection,
                        null,
                        null);


        cursor.moveToFirst();
        // fetching calendars name

        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss ");
        String syncacc = null;
        int count = cursor.getCount();
        Log.e("Total","Cursor count="+count);
        while (cursor.moveToNext()) {
            Integer eID = cursor.getInt(0);
            String eTitle = cursor.getString(1);
            String eDesc = cursor.getString(2);
            LocalDate eDate = getDate(Long.parseLong(cursor.getString(3)));
            Long eStart = cursor.getLong(3);
            Long eFinish = cursor.getLong(4);
            String eLocation = cursor.getString(5);
            boolean eAllay = (cursor.getInt(6) == 1);
            String eCalName = cursor.getString(7);
            String eZone = cursor.getString(8);
            String eSync9 = cursor.getString(9);
            String eRule = cursor.getString(10);
            Log.w("id="+eID,"title="+eTitle+", desc="+eDesc+", date="+eDate
                    +", time="+sdf.format(eStart)+"~"+sdf.format(eFinish)
                    +", loc="+eLocation+", all="+eAllay
                    +", zone="+eZone +", disp="+eCalName
                    +", sync9="+eSync9+", rule="+eRule
            );

            if (eTitle == null)
                continue;

            syncacc = cursor.getString(6);

            if (true) {
                LocalDate localDate = getDate(Long.parseLong(cursor.getString(3)));
//                Log.e("Acc",cursor.getString(1)+","+Integer.toHexString(cursor.getInt(8))+","+cursor.getString(9)+","+localDate);
                if (!localDateHashMap.containsKey(localDate)) {
                    EventInfo eventInfo=new EventInfo();
                    eventInfo.id=eID;
                    eventInfo.startTime =eStart;
                    eventInfo.finishTime =eFinish;
                    eventInfo.isAllDay =eAllay;
                    eventInfo.titles = new String[]{eTitle};
                    eventInfo.title=eTitle;
                    eventInfo.timeZone =eZone;
                    eventInfo.calName = eCalName;
                    eventInfo.eventcolor = setColorValue(eCalName);
                    localDateHashMap.put(localDate, eventInfo);

                } else {
                    EventInfo eventInfo= localDateHashMap.get(localDate);
                    EventInfo prev=eventInfo;
                    String[] s =eventInfo.titles;
                    boolean isneed = true;
                    for (int i = 0; i < s.length; i++) {
                        if (s[i].equals(eTitle)) {
                            isneed = false;
                            break;
                        }
                        if (i + 1 < s.length) prev = prev.nextNode;
                    }
                    if (isneed) {
                        String tt[] = Arrays.copyOf(s, s.length + 1);
                        tt[tt.length - 1] = eTitle;
                        eventInfo.titles =tt;
                        EventInfo nextNode=new EventInfo();
                        nextNode.id=eID;
                        nextNode.startTime =eStart;
                        nextNode.finishTime = eFinish;
                        nextNode.isAllDay = eAllay;
                        nextNode.title=eTitle;
                        nextNode.timeZone =eZone;
                        nextNode.calName = eCalName;
                        nextNode.eventcolor = setColorValue(eCalName);
                        prev.nextNode =nextNode;
                        localDateHashMap.put(localDate, eventInfo);
                    }

                }
            }


        }

        return localDateHashMap;
    }

    private static int setColorValue(String eCalName) {
        switch (eCalName) {
            case "Rio Papa":
                return (0xffffccdd);
            case "Rio Mama":
                return (0xffccdd44);
            case "events":
                return (0xffdd44ff);
        }
        return(0xff00ff00);
    }

    public static LocalDate getDate(long milliSeconds) {
        Instant instantFromEpochMilli
                = Instant.ofEpochMilli(milliSeconds);
        return instantFromEpochMilli.toDateTime(DateTimeZone.getDefault()).toLocalDate();

    }
}