/**
 * =========================================================================
 * 					Bench4Q_Script version 1.3.1
 * =========================================================================
 * 
 * Bench4Q is available on the Internet at  
 * http://www.trustie.net/projects/project/show/Bench4Q
 * You can find latest version there. 
 * Bench4Q_Script adds a script module for Internet application to Bench4Q
 * http://www.trustie.com/projects/project/show/Bench4Q_Script
 * 
 * Distributed according to the GNU Lesser General Public Licence. 
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by   
 * the Free Software Foundation; either version 2.1 of the License, or any
 * later version.
 * 
 * SEE Copyright.txt FOR FULL COPYRIGHT INFORMATION.
 * 
 * This source code is distributed "as is" in the hope that it will be
 * useful.  It comes with no warranty, and no author or distributor
 * accepts any responsibility for the consequences of its use.
 *
 *
 * This version is a based on the implementation of TPC-W from University of Wisconsin. 
 * This version used some source code of The Grinder.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
 *  * Initial developer(s): Wangsa , Tianfei , WUYulong , Zhufeng
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
 * 
 */
package scriptbq.monitor;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * This class is just a tool class to convert time into specified forms.
 */
public class FormatTime {

	/**
	 * Method to get the time in the form of string
	 * @param milisecond  The time in form of long
	 * @return
	 */
    public static String getTime(long milisecond) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        cal.clear();        
        cal.setTime(new Date(milisecond));
        String hour =
        	(cal.get(Calendar.HOUR) == 0)
        	? ""
        	: cal.get(Calendar.HOUR) + ":";
        if(hour.length() == 2)
        {
            hour = "0" + hour;
        }
        String minute =
        	(cal.get(Calendar.MINUTE) == 0 && hour.equals(""))
        	? ""
        	: cal.get(Calendar.MINUTE) + "'";
        if(minute.length() == 2)
        {
            minute = "0" + minute;
        }
        String second = cal.get(Calendar.SECOND) + "\"";
        if(second.length() == 2)
        {
            second = "0" + second;
        }
        return hour + minute + second;    }
    
    /**
     * Method to get the date
     * @return
     */
    public static String getDate(){
        Calendar cal = Calendar.getInstance(TimeZone.getDefault(),Locale.US); // default TimeZONE
        cal.get(Calendar.DAY_OF_YEAR);
        return format(cal);
    }
    
    /**
     * Method to get the Date according to the millis
     * @param millis
     * @return
     */
    public static String getDate(long millis){
        Calendar cal = Calendar.getInstance(TimeZone.getDefault(),Locale.US); // default TimeZONE
        cal.clear();
        cal.setTime(new Date(millis));
        return format(cal); 
    }

    /**
     * Method to format the Calendar
     * @param cal
     * @return
     */
    private static String format(Calendar cal) {
        String day = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault()).format(cal.getTime());
        String time = DateFormat.getTimeInstance(DateFormat.MEDIUM, Locale.getDefault()).format(cal.getTime());
        return day + " " + time;
    }
    
}
