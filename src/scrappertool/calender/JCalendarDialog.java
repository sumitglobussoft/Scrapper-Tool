/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scrappertool.calender;

/**
 *
 * @author Mendon Ashwini
 * Its the layout where the calender is implemented
 */

 
import java.awt.BorderLayout;
import java.awt.Dialog.ModalityType;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
 
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import scrappertool.dao.LaunchDataDao;
 
/**
 * <p>
 * This class creates a <code>JDialog</code> using the JCalendar
 * <code>JPanel</code>. This is the most common usage for a JCalendar.
 * </p>
 * 
 * <p>
 * Generally, the JCalendarDialog would be executed as a part of a
 * <code>JButton</code> action listener. Something like this:
 * </p>
 * 
 * <pre>
 *  <code>private class CalendarActionListener implements ActionListener {
 * 
 *      private JFrame frame;
 * 
 *      private JTextField textField;
 * 
 *      public CalendarActionListener(JFrame frame, JTextField textField) {
 *          this.frame = frame;
 *          this.textField = textField;
 *      }
 * 
 *      &#64Override
 *      public void actionPerformed(ActionEvent event) {
 *          JCalendarDialog dialog = new JCalendarDialog(frame);
 *          dialog.setLocale(Locale.ENGLISH);
 *          dialog.createDialog();
 *          if (dialog.getReturnCode() == JCalendarDialog.OK_PRESSED) {
 *              textField.setText(dialog.getDateString());
 *          }
 *      }
 * 
 *  }
 *  </code>
 * </pre>
 * 
 * @author Gilbert G. Le Blanc
 * @version 1.0 - 13 February 2015
 * 
 * @see java.awt.event.ActionListener
 * @see javax.swing.JButton
 * @see javax.swing.JDialog
 * 
 */
public class JCalendarDialog {
 
    public static final int OK_PRESSED = 1;
    public static final int CANCEL_PRESSED = 2;
 
    private int returnCode;
    private int startOfWeek;
    private int[] exclusionDaysOfWeek;
 
    private Calendar calendar;
    private Calendar selectedDate;
 
    private JCalendar jcalendar;
 
    private JDialog dialog;
 
    private JFrame frame;
 
    private List<ExclusionDateRange> exclusionDateRanges;
 
    private Locale locale;
 
    private String dateString;
    private String dialogTitle;
    private String simpleDateFormat;
    private LaunchDataDao objLaunchDataDao=null;
 
    /**
     * This creates an instance of the JCalendarDialog for the
     * <code>JFrame</code> with the calendar selection button, and sets the
     * default values.
     * 
     * @param frame
     *            - A <code>JFrame</code> instance.
     * @param objLaunchDataDao
     */
    public JCalendarDialog(JFrame frame, LaunchDataDao objLaunchDataDao) {
        this.frame = frame;
        this.locale = Locale.getDefault();
        this.calendar = Calendar.getInstance();
        this.dialogTitle = "Date Selector";
        this.simpleDateFormat = "MMM d, yyyy";
        this.startOfWeek = Calendar.SUNDAY;
        this.exclusionDaysOfWeek = new int[0];
        this.exclusionDateRanges = new ArrayList<ExclusionDateRange>();
        this.objLaunchDataDao = objLaunchDataDao;
    }
 
    /**
     * This optional method sets the day that starts the week. The default is
     * <code>Calendar.SUNDAY</code>.
     * 
     * @param startOfWeek
     *            - Calendar day of week value indicating the start of the week.
     *            For example, <code>Calendar.FRIDAY</code>.
     */
    public void setStartOfWeek(int startOfWeek) {
        this.startOfWeek = startOfWeek;
    }
 
    /**
     * This optional method sets the display month of the JCalendarDialog using
     * a <code>Calendar</code>. The default is the current month.
     * 
     * @param calendar
     *            - <code>Calendar</code> instance that sets the display month
     *            of the JCalendarDialog.
     */
    public void setCalendar(Calendar calendar) {
        this.calendar = (Calendar) calendar.clone();
    }
 
    /**
     * This optional method sets the title of the JCalendarDialog to correspond
     * to the <code>JLabel</code> text on the selected date field.
     * 
     * @param dialogTitle
     *            - A <code>String</code> with the JCalendar dialog title.
     */
    public void setDialogTitle(String dialogTitle) {
        this.dialogTitle = dialogTitle;
    }
 
    /**
     * This optional method sets the <code>Locale</code> for the
     * JCalendarDialog. This allows for localized month and day of week names.
     * The default is your default <code>Locale</code>.
     * 
     * @param locale
     *            - The <code>Locale</code> for the JCalendarDialog.
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }
 
    /**
     * This optional method sets the <code>SimpleDateFormat</code> for the
     * selected date String returned by the JCalendarDialog. The default is
     * <code>"MMM d, yyyy"</code>.
     * 
     * @param simpleDateFormat
     *            - A <code>String</code> with the desired date format of the
     *            selected date.
     * 
     * @see java.text.SimpleDateFormat
     */
    public void setSimpleDateFormat(String simpleDateFormat) {
        this.simpleDateFormat = simpleDateFormat;
    }
 
    /**
     * This optional method allows you to add date ranges to exclude from the
     * JCalendar selection.
     * 
     * @param exclusionDateRange
     *            - An <code>ExclusionDateRange</code> to exclude the range of
     *            dates from the JCalendar selection.
     */
    public void addExclusionDateRange(ExclusionDateRange exclusionDateRange) {
        this.exclusionDateRanges.add(exclusionDateRange);
    }
 
    /**
     * This optional method allows you to add a <code>List</code> of date ranges
     * to exclude from the JCalendarDialog selection.
     * 
     * @param exclusionDateRanges
     *            - A <code>List</code> of <code>ExclusiojnDateRange</code> to
     *            exclude the ranges of dates from the JCalendarDialog
     *            selection.
     */
    public void addExclusionDateRanges(
            List<ExclusionDateRange> exclusionDateRanges) {
        this.exclusionDateRanges.addAll(exclusionDateRanges);
    }
 
    /**
     * This optional method allows you to select one or more days of the week to
     * exclude from the JCalendarDialog selection.
     * 
     * @param dayOfWeek
     *            - One or more <code>Calendar</code> days of the week to
     *            exclude from the JCalendarDialog selection. For example,
     *            <code>Calendar.SATURDAY, Calendar.SUNDAY</code>.
     */
    public void setExclusionDaysOfWeek(int... dayOfWeek) {
        exclusionDaysOfWeek = new int[dayOfWeek.length];
        for (int i = 0; i < dayOfWeek.length; i++) {
            exclusionDaysOfWeek[i] = dayOfWeek[i];
        }
    }
 
    /**
     * This mandatory method creates the JCalendarDialog <code>JDialog</code>
     * after all of the JCalendarDialog values are set or defaulted. Failure to
     * execute this method will result in a <code>NullPointerException</code> .
     */
    public void createDialog() {
        dialog = new JDialog(frame);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setTitle(dialogTitle);
 
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
 
        jcalendar = new JCalendar(objLaunchDataDao);
        jcalendar.setExclusionDaysOfWeek(exclusionDaysOfWeek);
        jcalendar.addExclusionDateRanges(exclusionDateRanges);
        jcalendar.setCalendar(calendar);
        jcalendar.setDateFormat(simpleDateFormat);
        jcalendar.setLocale(locale);
        jcalendar.setStartOfWeek(startOfWeek);
        jcalendar.createPanel();
 
        mainPanel.add(jcalendar.getPanel(), BorderLayout.CENTER);
 
               JPanel colorPanel = new JPanel();
        



        JLabel jLabel1 = new javax.swing.JLabel();
        JLabel jLabel2 = new javax.swing.JLabel();
        JLabel jLabel3 = new javax.swing.JLabel();
        JLabel jLabel4 = new javax.swing.JLabel();
        JLabel jLabel5 = new javax.swing.JLabel();
        JLabel jLabel6 = new javax.swing.JLabel();
        JTextField jTextField1 = new javax.swing.JTextField();
        JTextField jTextField2 = new javax.swing.JTextField();
        JTextField jTextField3 = new javax.swing.JTextField();
        JTextField jTextField4 = new javax.swing.JTextField();
        JTextField jTextField5 = new javax.swing.JTextField();
        
     jLabel1.setText("<5 Launches");

        jLabel2.setText(">5 Launches");

        jLabel3.setText(">10 Launches");

        jLabel4.setText("Today's Day");

        jLabel5.setText("Selected Date");

        jLabel6.setText("COLOR CODE");
        
        jTextField1.setEditable(false);
        jTextField1.setBackground(new java.awt.Color(51, 255, 51));
        
        jTextField2.setEditable(false);
        jTextField2.setBackground(new java.awt.Color(0, 255, 255));
        
        jTextField3.setEditable(false);
        jTextField3.setBackground(new java.awt.Color(204, 0, 0));
        
        jTextField4.setEditable(false);
        jTextField4.setBackground(new java.awt.Color(255, 255, 0));
        
        jTextField5.setEditable(false);
        jTextField5.setBackground(new java.awt.Color(102, 102, 102));
        
         javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(colorPanel);
        colorPanel.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextField3)
                            .addComponent(jTextField4)
                            .addComponent(jTextField5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        
//        colorPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
//        JLabel title = new JLabel();
//        title.setText("COLOR CODE FOR LAUNCH");
//        JLabel colour1 = new JLabel();
//        colour1.setText("GREEN");
//   
//        colorPanel.add(title, JPanel.LEFT_ALIGNMENT);
//        colorPanel.add(colour1,JPanel.RIGHT_ALIGNMENT);
//        mainPanel.add(colorPanel,BorderLayout.LINE_END);
        
    
        mainPanel.add(colorPanel,BorderLayout.LINE_END);
 
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(0,30,30));
 
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new OKButtonActionListener());
        buttonPanel.add(okButton);
 
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new CancelButtonActionListener());
        buttonPanel.add(cancelButton);
 
        okButton.setPreferredSize(cancelButton.getPreferredSize());
 
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
 
        dialog.add(mainPanel);
 
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setModalityType(ModalityType.APPLICATION_MODAL);
        dialog.setVisible(true);
    }
 
    /**
     * This method returns the return code of the JDialog. This allows the
     * creator of the JCalendarDialog to determine whether or not a date was
     * selected.
     * 
     * @return OK_PRESSED or CANCEL_PRESSED, depending on which button was
     *         clicked.
     */
    public int getReturnCode() {
        return returnCode;
    }
 
    /**
     * This method returns the selected date in the
     * <code>SimpleDateFormat</code> of the <code>String</code> passed as input
     * through the constructor.
     * 
     * @return The selected date formatted using the
     *         <code>SimpleDateFormat</code> input string.
     */
    public String getFormattedSelectedDate() {
        return dateString;
    }
 
    /**
     * This method returns the <code>Calendar</code> instance of the selected
     * date.
     * 
     * @return The <code>Calendar</code> instance of the selected date
     */
    public Calendar getSelectedDate() {
        return selectedDate;
    }
 
    private class CancelButtonActionListener implements ActionListener {
 
        @Override
        public void actionPerformed(ActionEvent event) {
            returnCode = CANCEL_PRESSED;
            dialog.setVisible(false);
        }
 
    }
 
    private class OKButtonActionListener implements ActionListener {
 
        @Override
        public void actionPerformed(ActionEvent event) {
            String s = jcalendar.getFormattedSelectedDate();
            if (!s.equals("")) {
                dateString = s;
                selectedDate = jcalendar.getSelectedDate();
                returnCode = OK_PRESSED;
                dialog.setVisible(false);
            }
        }
 
    }
 
}
