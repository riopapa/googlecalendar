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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

import androidx.core.app.ActivityCompat;



public class Utility {


    public static HashMap<LocalDate, EventInfo> localDateHashMap = new HashMap<>();

    public static HashMap<LocalDate, EventInfo> readCalendarEvent(Context context, LocalDate mintime, LocalDate maxtime) {

        int f=1;
        String selection = "(( " + CalendarContract.Events.SYNC_EVENTS + " = " + f +" ) AND ( " + CalendarContract.Events.DTSTART + " >= " + mintime.toDateTimeAtStartOfDay().getMillis() + " ) AND ( " + CalendarContract.Events.DTSTART + " <= " + maxtime.toDateTimeAtStartOfDay().getMillis() + " ))";
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
                        CalendarContract.Events.CALENDAR_TIME_ZONE // 8    Asia/Seoul
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
            String eID = cursor.getString(0);
            String eTitle = cursor.getString(1);
            String eDesc = cursor.getString(2);
            LocalDate eDate = getDate(Long.parseLong(cursor.getString(3)));
            Long eStart = cursor.getLong(3);
            Long eFinish = cursor.getLong(4);
            String eLocation = cursor.getString(5);
            boolean eAllay = (cursor.getInt(6) == 1);
            String eDispName = cursor.getString(7);
            String eZone = cursor.getString(8);
            Log.w("id="+eID,"title="+eTitle+", desc="+eDesc+", date="+eDate
                    +", time="+sdf.format(eStart)+"~"+sdf.format(eFinish)
                    +", loc="+eLocation+", all="+eAllay
                    +", zone="+eZone +", disp="+eDispName
            );
            if (eTitle == null)
                continue;

            syncacc = cursor.getString(6);

            if (true) {
                LocalDate localDate = getDate(Long.parseLong(cursor.getString(3)));
//                Log.e("Acc",cursor.getString(1)+","+Integer.toHexString(cursor.getInt(8))+","+cursor.getString(9)+","+localDate);
                if (!localDateHashMap.containsKey(localDate)) {
                    EventInfo eventInfo=new EventInfo();
                    eventInfo.id=cursor.getInt(0);
                    eventInfo.starttime=cursor.getLong(3);
                    eventInfo.endtime=cursor.getLong(4);
                    eventInfo.isallday=cursor.getInt(7)==1;
                    String title = cursor.getString(1);
                    eventInfo.eventtitles = new String[]{(title == null) ? "blank":title};
                    eventInfo.title=cursor.getString(1);
                    eventInfo.timezone=cursor.getString(8);
                    eventInfo.eventcolor= 12345; //cursor.getInt(8);
                    localDateHashMap.put(localDate, eventInfo);

                } else {
                    EventInfo eventInfo= localDateHashMap.get(localDate);
                    EventInfo prev=eventInfo;
                    String[] s =eventInfo.eventtitles;
                    Log.e("s[] "+localDate, "len="+s.length);
                    String nTitle = cursor.getString(1);
//                    if (nTitle == null) {
//                        nTitle = "NULL";
//                        Log.e("ntitle ","is "+nTitle);
//                    }
                    boolean isneed = true;
                    for (int i = 0; i < s.length; i++) {
                        String s1;
//                        try {
                            s1 = s[i];
//                            if (s1 ==  null) {
//                                Log.w("S1 is ","NULL");
//                                s1 = "no value ^";
//                            }
//                        } catch (Exception e) {
//                            s1 = "nothing "+i;
//                        }
//                        Log.e("event title "+i,s1 + localDate);
                        if (s1.equals(nTitle)) {

                            isneed = false;
                            break;
                        }
                        if (i + 1 < s.length) prev = prev.nextnode;

                    }
                    if (isneed) {
                        ArrayList<String> ar = new ArrayList<String>();
                        for (int i = 0; i < s.length; i++)
                            ar.add(s[i]);
                        ar.add(cursor.getString(1));
                        String []ss = ar.toArray(new String[0]);

                        eventInfo.eventtitles=ss;

                        EventInfo nextnode=new EventInfo();
                        nextnode.id=cursor.getInt(0);
                        nextnode.starttime=Long.parseLong(cursor.getString(3));
                        try {
                            nextnode.endtime = Long.parseLong(cursor.getString(4));
                        } catch (Exception e) {
                            Log.e("endtime","exception "+cursor.getString(4));
                            nextnode.endtime = nextnode.starttime + 60000;
                        }
//                        try {
                            nextnode.isallday = cursor.getInt(7) == 1;
//                        } catch (Exception e) {
//                            nextnode.isallday = false;
//                        }
                        nextnode.title=cursor.getString(1);
                        nextnode.timezone=cursor.getString(8);
                        nextnode.eventcolor= 56789; // cursor.getInt(8);
                        prev.nextnode=nextnode;


                        localDateHashMap.put(localDate, eventInfo);
                    }

                }
            }


        }

        return localDateHashMap;
    }

    public static LocalDate getDate(long milliSeconds) {
        Instant instantFromEpochMilli
                = Instant.ofEpochMilli(milliSeconds);
        return instantFromEpochMilli.toDateTime(DateTimeZone.getDefault()).toLocalDate();

    }
}