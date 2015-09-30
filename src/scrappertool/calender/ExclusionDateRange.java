/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scrappertool.calender;

/**
 *
 * @author GLB-130
 */

 
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
 
/**
 * <p>
 * This data class is used to specify a date range. Because
 * <code>Calendar</code> is such a difficult class to work with, the constructor
 * takes the date range as <code>String</code>s.
 * </p>
 * 
 * <p>
 * Each instance of this data class represents one date range. More than one
 * date range can be passed to JCalendar by calling the JCalendar
 * <code>addExclusionDateRange</code> method once for each date range.
 * </p>
 * 
 * @author Gilbert G. Le Blanc
 * 
 * @version 1.0 - 13 February 2015
 * 
 * @see java.text.SimpleDateFormat
 * @see java.util.Calendar
 */
public class ExclusionDateRange {
 
    private Calendar fromCalendar;
    private Calendar toCalendar;
 
    /**
     * This creates an ExclusionDateRange instance with a date range.
     * 
     * @param simpleDateFormat
     *            - A <code>String</code> with the simple date format of the
     *            next two date strings.
     * @param fromDateString
     *            - A <code>String</code> representing the from date of the
     *            exclusion date range.
     * @param toDateString
     *            - A <code>String</code> representing the to date of the
     *            exclusion date range.
     */
    public ExclusionDateRange(String simpleDateFormat, String fromDateString,
            String toDateString) {
        SimpleDateFormat inputDate = new SimpleDateFormat(simpleDateFormat);
 
        this.fromCalendar = createCalendarDate(inputDate, fromDateString);
        this.toCalendar = createCalendarDate(inputDate, toDateString);
 
        if ((fromCalendar == null) || (toCalendar == null)) {
            return;
        }
 
        clearTime(fromCalendar);
        clearTime(toCalendar);
 
        fromCalendar.add(Calendar.DAY_OF_MONTH, -1);
        toCalendar.add(Calendar.DAY_OF_MONTH, 1);
    }
 
    private Calendar createCalendarDate(SimpleDateFormat inputDate,
            String dateString) {
        Date date;
        try {
            date = inputDate.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
 
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }
 
    private void clearTime(Calendar c) {
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
    }
 
    /**
     * This method returns the from date as a <code>Calendar</code> instance.
     * 
     * @return A <code>Calendar</code> instance of the from date of the excluded
     *         date range.
     */
    public Calendar getFromCalendar() {
        return fromCalendar;
    }
 
    /**
     * This method returns the to date as a <code>Calendar</code> instance.
     * 
     * @return A <code>Calendar</code> instance of the to date of the excluded
     *         date range.
     */
    public Calendar getToCalendar() {
        return toCalendar;
    }
 
}
