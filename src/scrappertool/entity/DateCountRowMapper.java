/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scrappertool.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Mendon Ashwini
 * It maps the sql resultset data to the DateCount class
 */
public class DateCountRowMapper implements RowMapper{

    @Override
    public DateCount mapRow(ResultSet rs, int rowNum) throws SQLException {
                DateCount objDateCount = new DateCount();
                objDateCount.setCount(rs.getInt(1));
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                Date date=null;
        try {
            date = format.parse(rs.getString(2));
        } catch (ParseException ex) {
            Logger.getLogger(DateCountRowMapper.class.getName()).log(Level.SEVERE, null, ex);
        }
                objDateCount.setLaunchDate(date);
                return objDateCount;
            }
    
}
