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
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import scrappertool.dao.LaunchDataDao;
import scrappertool.entity.DateCount;

/**
 * <p>
 * This class creates a Swing <code>JPanel</code> containing one month of a calendar. The <code>JPanel</code> can be
 * used in a Swing application, or in the <code>JCalendarDialog</code> that's included in this package.
 * </p>
 *
 * <p>
 * There are four arrow buttons on top of the calendar. The first button on the left moves the calendar back one year.
 * The second button on the left moves the calendar back one month. The third button moves the calendar forward one
 * month. The fourth button moves the calendar forward one year.
 * </p>
 *
 * <p>
 * The current date has a yellow background. The user left clicks on a date to select the date. A date must be selected
 * before the user presses the OK button. Once the user selects a date, the selected date has a green background.
 * </p>
 *
 * <p>
 * Exclusions may be added to the JCalendar so that the user may not select certain dates. There are two methods to add
 * exclusions to the JCalendar. The first method, <code>setExclusionDaysOfWeek</code>, allows you to exclude certain
 * days of the week. The second method, <code>addExclusionDateRange</code> allows you to add one or more date ranges to
 * exclude. The date range can be one day, or several days.
 * </p>
 *
 * <p>
 * Here's an example of how to use a <code>JCalendar</code>:
 *
 * <pre>
 * <code>     JPanel mainPanel = new JPanel();
 *      mainPanel.setLayout(new BorderLayout());
 *
 *      jcalendar = new JCalendar();
 *      jcalendar.setExclusionDaysOfWeek(Calendar.SATURDAY, Calendar.SUNDAY);
 *      jcalendar.addExclusionDateRange(new ExclusionDateRange("M/d/yy",
 *              "2/16/15", "2/16/15"));
 *      jcalendar.setCalendar(calendar);
 *      jcalendar.setDateFormat(simpleDateFormat);
 *      jcalendar.setLocale(locale);
 *      jcalendar.setStartOfWeek(Calendar.SUNDAY);
 *      jcalendar.createPanel();
 *
 *      mainPanel.add(jcalendar.getPanel(), BorderLayout.CENTER);
 * </code>
 * </pre>
 *
 * @author Gilbert G. Le Blanc
 *
 * @version 1.0 - 13 February 2015
 *
 * @see javax.swing.JPanel
 */
public class JCalendar {

    private static final int DAYS_IN_WEEK = 7;

    private int startOfWeek;
    private int[] exclusionDaysOfWeek;

    private Border blackLineBorder;

    private Calendar calendar;
    private Calendar selectedDate;

    private DayPanel[] dayPanel;

    private JLabel monthLabel;

    private JPanel panel;

    private List<ExclusionDateRange> exclusionDateRanges;

    private Locale locale;

    private SimpleDateFormat dateFormat;

    private String dateString;

    private LaunchDataDao objLaunchDataDao = null;

    /**
     * This creates an instance of JCalendar with default values.
     *
     * @param objLaunchDataDao
     */
    public JCalendar(LaunchDataDao objLaunchDataDao) {
        this.calendar = Calendar.getInstance();
        this.locale = Locale.getDefault();
        this.startOfWeek = Calendar.SUNDAY;
        this.dateFormat = new SimpleDateFormat("MMM d, yyyy");
        this.exclusionDateRanges = new ArrayList<ExclusionDateRange>();
        this.exclusionDaysOfWeek = new int[0];

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        this.dateString = "";
        this.selectedDate = null;
        this.objLaunchDataDao = objLaunchDataDao;
    }

    /**
     * This optional method sets the day that starts the week. The default is <code>Calendar.SUNDAY</code>.
     *
     * @param startOfWeek - Calendar day of week value indicating the start of the week. For example,
     * <code>Calendar.FRIDAY</code>.
     */
    public void setStartOfWeek(int startOfWeek) {
        this.startOfWeek = startOfWeek;
    }

    /**
     * This optional method sets the display month of the JCalendar using a <code>Calendar</code>. The default is the
     * current month.
     *
     * @param calendar - <code>Calendar</code> instance that sets the display month of the JCalendar.
     */
    public void setCalendar(Calendar calendar) {
        this.calendar = (Calendar) calendar.clone();

        this.calendar.set(Calendar.HOUR_OF_DAY, 0);
        this.calendar.set(Calendar.MINUTE, 0);
        this.calendar.set(Calendar.SECOND, 0);
        this.calendar.set(Calendar.MILLISECOND, 0);
    }

    /**
     * This optional method sets the <code>Locale</code> for the JCalendar. This allows for localized month and day of
     * week names. The default is your default <code>Locale</code>.
     *
     * @param locale - The <code>Locale</code> for the JCalendar.
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    /**
     * This optional method sets the <code>SimpleDateFormat</code> for the selected date String returned by the
     * JCalendar. The default is <code>"MMM d, yyyy"</code>.
     *
     * @param simpleDateFormat - A <code>String</code> with the desired date format of the selected date.
     *
     * @see java.text.SimpleDateFormat
     */
    public void setDateFormat(String simpleDateFormat) {
        this.dateFormat = new SimpleDateFormat(simpleDateFormat);
    }

    /**
     * This optional method allows you to add date ranges to exclude from the JCalendar selection.
     *
     * @param exclusionDateRange - An <code>ExclusionDateRange</code> to exclude the range of dates from the JCalendar
     * selection.
     */
    public void addExclusionDateRange(ExclusionDateRange exclusionDateRange) {
        this.exclusionDateRanges.add(exclusionDateRange);
    }

    /**
     * This optional method allows you to add a <code>List</code> of date ranges to exclude from the JCalendar
     * selection.
     *
     * @param exclusionDateRanges - A <code>List</code> of <code>ExclusiojnDateRange</code> to exclude the ranges of
     * dates from the JCalendar selection.
     */
    public void addExclusionDateRanges(
            List<ExclusionDateRange> exclusionDateRanges) {
        this.exclusionDateRanges.addAll(exclusionDateRanges);
    }

    /**
     * This optional method allows you to select one or more days of the week to exclude from the JCalendar selection.
     *
     * @param dayOfWeek - One or more <code>Calendar</code> days of the week to exclude from the JCalendar selection.
     * For example, <code>Calendar.SATURDAY, Calendar.SUNDAY</code>.
     */
    public void setExclusionDaysOfWeek(int... dayOfWeek) {
        exclusionDaysOfWeek = new int[dayOfWeek.length];
        for (int i = 0; i < dayOfWeek.length; i++) {
            exclusionDaysOfWeek[i] = dayOfWeek[i];
        }
    }

    /**
     * This mandatory method creates the JCalendar <code>JPanel</code> after all of the JCalendar values are set or
     * defaulted. Failure to execute this method will result in a <code>NullPointerException</code> .
     */
    public void createPanel() {
        blackLineBorder = BorderFactory.createLineBorder(Color.BLACK);

        panel = new JPanel();
        panel.setBorder(blackLineBorder);
        panel.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setAlignmentX(50);
        topPanel.setAlignmentY(50);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(blackLineBorder);

        Font smallFont = buttonPanel.getFont().deriveFont(10.0F);

        JButton yearBackButton = new JButton("<<");
        yearBackButton.setFont(smallFont);
        yearBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                calendar.add(Calendar.YEAR, -1);
                updatePartControl();
            }
        });
        buttonPanel.add(yearBackButton);

        JButton monthBackButton = new JButton("<");
        monthBackButton.setFont(smallFont);
        monthBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                calendar.add(Calendar.MONTH, -1);
                updatePartControl();
            }
        });
        buttonPanel.add(monthBackButton);

        JButton monthForwardButton = new JButton(">");
        monthForwardButton.setFont(smallFont);
        monthForwardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                calendar.add(Calendar.MONTH, 1);
                updatePartControl();
            }
        });
        buttonPanel.add(monthForwardButton);

        JButton yearForwardButton = new JButton(">>");
        yearForwardButton.setFont(smallFont);
        yearForwardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                calendar.add(Calendar.YEAR, 1);
                updatePartControl();
            }
        });
        buttonPanel.add(yearForwardButton);

        monthBackButton.setPreferredSize(yearBackButton.getPreferredSize());
        monthForwardButton.setPreferredSize(yearForwardButton
                .getPreferredSize());

        topPanel.add(buttonPanel, BorderLayout.NORTH);

        JPanel monthPanel = new JPanel();
        monthPanel.setBackground(Color.WHITE);
        monthPanel.setBorder(blackLineBorder);

        monthLabel = new JLabel(getDisplayMonthYear());
        monthPanel.add(monthLabel);

        topPanel.add(monthPanel, BorderLayout.SOUTH);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(createDayGrid(), BorderLayout.CENTER);
    }

    private JPanel createDayGrid() {
        JPanel panel = new JPanel();
        panel.setBorder(blackLineBorder);
        panel.setLayout(new GridLayout(0, DAYS_IN_WEEK));

        String[] daysOfWeek = getDaysOfWeek();
        for (int i = 0; i < daysOfWeek.length; i++) {
            JPanel weekdayPanel = new JPanel();
            weekdayPanel.setBackground(Color.WHITE);
            weekdayPanel.setBorder(blackLineBorder);

            String s = daysOfWeek[i].substring(0, 1);
            JLabel weekdayLabel = new JLabel(s);
            weekdayPanel.add(weekdayLabel);

            panel.add(weekdayPanel);
        }

        JCalendarDay[] days = getDays();
        dayPanel = new DayPanel[days.length];
        for (int i = 0; i < days.length; i++) {
            dayPanel[i] = new DayPanel();
            dayPanel[i].setJCalendarDay(days[i]);
            dayPanel[i].createPartControl();
            panel.add(dayPanel[i]);
        }

        return panel;
    }

    private void updatePartControl() {
        monthLabel.setText(getDisplayMonthYear());

        JCalendarDay[] days = getDays();
        for (int i = 0; i < days.length; i++) {
            dayPanel[i].setJCalendarDay(days[i]);
            dayPanel[i].updatePartControl();
        }
    }

    private String[] getDaysOfWeek() {
        String[] daysOfWeek = new String[DAYS_IN_WEEK];
        Calendar temp = (Calendar) calendar.clone();

        for (int i = 0; i < daysOfWeek.length; i++) {
            temp.set(Calendar.DAY_OF_WEEK, (i + startOfWeek) % DAYS_IN_WEEK);
            String s = temp.getDisplayName(Calendar.DAY_OF_WEEK,
                    Calendar.SHORT_FORMAT, locale);
            daysOfWeek[i] = s;
        }

        return daysOfWeek;
    }

    private JCalendarDay[] getDays() {
        JCalendarDay[] days = new JCalendarDay[DAYS_IN_WEEK * 6];
        Calendar today = Calendar.getInstance(locale);
        Calendar temp = (Calendar) calendar.clone();
        temp.set(Calendar.DAY_OF_MONTH, 1);
        getFirstDate(temp);
        System.out.println("inside=================================");
        int currentMonth = calendar.get(Calendar.MONTH);
        System.out.println("currentMonth::" + currentMonth);

        for (int i = 0; i < days.length; i++) {
            int month = temp.get(Calendar.MONTH);
            if (month == currentMonth) {
                boolean isExcluded = isExcludedDayOfWeek(temp)
                        || isExcludedDateRange(temp);
                Color color = Color.WHITE;
                if (isToday(temp, today)) {
                    color = Color.YELLOW;
                }
                if (isToday(temp, selectedDate)) {
                    color = Color.GREEN;
                }
                int day = temp.get(Calendar.DAY_OF_MONTH);
                days[i] = new JCalendarDay(day, color, isExcluded);
            } else {
                days[i] = new JCalendarDay(0, Color.WHITE, false);
            }

            temp.add(Calendar.DAY_OF_MONTH, 1);
        }

        List<DateCount> listLaunchDatas = objLaunchDataDao.listLaunchDatas();
        List<DateCount> newList = new ArrayList<>();
        Calendar temp2 = (Calendar) calendar.clone();
        for (DateCount listLaunchData : listLaunchDatas) {

            Calendar cal = Calendar.getInstance();

            cal.setTime(listLaunchData.getLaunchDate());

            if ((cal.get(Calendar.YEAR) == temp2.get(Calendar.YEAR)) && (cal.get(Calendar.MONTH) == temp2.get(Calendar.MONTH))) {
                newList.add(listLaunchData);
            }
        }

        System.out.println("\n\nnewList::" + newList.size());

        for (JCalendarDay day : days) {

            for (DateCount newList1 : newList) {

                Calendar cal2 = Calendar.getInstance();

                cal2.setTime(newList1.getLaunchDate());

                if ((day.getDay() == cal2.get(Calendar.DAY_OF_MONTH))&&(day.getColor()!=Color.GREEN)){

                    if (newList1.getCount() > 10) {
                        Color color = Color.RED;
                        day.setColor(color);
                        break;
                    }

                    if (newList1.getCount() > 5) {
                        Color color = Color.CYAN;
                        day.setColor(color);
                        break;
                    }

                    if (newList1.getCount() > 2) {
                        Color color = Color.GRAY;
                        day.setColor(color);
                        break;
                    }
                    if (newList1.getCount() > 0) {
                        Color color = Color.LIGHT_GRAY;
                        day.setColor(color);
                        break;
                    }

                }

            }

        }

        return days;
    }

    private boolean isExcludedDayOfWeek(Calendar c) {
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        for (int i = 0; i < exclusionDaysOfWeek.length; i++) {
            if (dayOfWeek == exclusionDaysOfWeek[i]) {
                return true;
            }
        }
        return false;
    }

    private boolean isExcludedDateRange(Calendar c) {
        for (ExclusionDateRange range : exclusionDateRanges) {
            if (c.after(range.getFromCalendar())
                    && c.before(range.getToCalendar())) {
                return true;
            }
        }
        return false;
    }

    private boolean isToday(Calendar c1, Calendar c2) {
        if ((c1 == null) || (c2 == null)) {
            return false;
        }

        int year1 = c1.get(Calendar.YEAR);
        int month1 = c1.get(Calendar.MONTH);
        int day1 = c1.get(Calendar.DAY_OF_MONTH);

        int year2 = c2.get(Calendar.YEAR);
        int month2 = c2.get(Calendar.MONTH);
        int day2 = c2.get(Calendar.DAY_OF_MONTH);

        return (day1 == day2) && (month1 == month2) && (year1 == year2);
    }

    /**
     * This method gets the date of the first day of the calendar week. It could be the first day of the month, but more
     * likely, it's a day in the previous month.
     *
     * @param calendar - Working <code>Calendar</code> instance that this method can manipulate to set the first day of
     * the calendar week.
     */
    private void getFirstDate(Calendar calendar) {
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) % DAYS_IN_WEEK;
        int amount = 0;
        for (int i = 0; i < DAYS_IN_WEEK; i++) {
            int j = (i + startOfWeek) % DAYS_IN_WEEK;
            if (j == dayOfWeek) {
                break;
            }
            amount--;
        }
        calendar.add(Calendar.DAY_OF_MONTH, amount);
    }

    private String getDisplayMonthYear() {
        int year = calendar.get(Calendar.YEAR);
        String s = calendar.getDisplayName(Calendar.MONTH,
                Calendar.LONG_FORMAT, locale);
        return s + " " + year;
    }

    /**
     * This method returns the <code>JPanel</code> with the current month calendar.
     *
     * @return The current month calendar <code>JPanel</code>.
     */
    public JPanel getPanel() {
        return panel;
    }

    /**
     * This method returns the selected date in the <code>SimpleDateFormat</code> of the <code>String</code> passed as
     * input through the constructor.
     *
     * @return The selected date formatted using the <code>SimpleDateFormat</code> input string.
     */
    public String getFormattedSelectedDate() {
        return dateString;
    }

    /**
     * This method returns the <code>Calendar</code> instance of the selected date.
     *
     * @return The <code>Calendar</code> instance of the selected date
     */
    public Calendar getSelectedDate() {
        return selectedDate;
    }

    private class DayMouseListener extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent event) {
            DayPanel panel = (DayPanel) event.getSource();
            if (!panel.isExcluded()) {
                JLabel label = panel.getDayLabel();
                String s = label.getText().trim();

                if (!s.equals("")) {
                    getSelectedDate(s);
                }
            }
        }

        private void getSelectedDate(String s) {
            selectedDate = Calendar.getInstance();
            selectedDate.set(calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH), Integer.valueOf(s), 0, 0, 0);
            dateString = dateFormat.format(selectedDate.getTime());
            updatePartControl();
        }

    }

    private class DayPanel extends JPanel {

        private static final long serialVersionUID = -2980436029289287026L;

        private JCalendarDay jcalendarDay;

        private JLabel dayLabel;

        public void setJCalendarDay(JCalendarDay jcalendarDay) {
            this.jcalendarDay = jcalendarDay;
        }

        public void createPartControl() {
            addMouseListener(new DayMouseListener());
            setBackground(jcalendarDay.getColor());
            setBorder(blackLineBorder);

            dayLabel = new JLabel(getDay());
            dayLabel.setForeground(Color.BLUE);
            add(dayLabel);
        }

        public void updatePartControl() {
            setBackground(jcalendarDay.getColor());
            dayLabel.setText(getDay());
            repaint();
        }

        private String getDay() {
            String s = " ";
            if (jcalendarDay.getDay() > 0) {
                s = Integer.toString(jcalendarDay.getDay());
            }
            return s;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (jcalendarDay.isExcluded()) {
                g.setColor(Color.DARK_GRAY);
                g.drawLine(getWidth(), 0, 0, getHeight());
            }
        }

        public boolean isExcluded() {
            return jcalendarDay.isExcluded();
        }

        public JLabel getDayLabel() {
            return dayLabel;
        }

    }

    private class JCalendarDay {

        private final boolean isExcluded;

        private final int day;

        private Color color;

        public JCalendarDay(int day, Color color, boolean isExcluded) {
            this.day = day;
            this.color = color;
            this.isExcluded = isExcluded;
        }

        public int getDay() {
            return day;
        }

        public Color getColor() {
            return color;
        }

        public boolean isExcluded() {
            return isExcluded;
        }

        public void setColor(Color color) {
            this.color = color;
        }

    }

}
