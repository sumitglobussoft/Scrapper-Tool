/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scrappertool.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author GLB-130
 */
public class DateCountRowMapper implements RowMapper{

    @Override
    public DateCount mapRow(ResultSet rs, int rowNum) throws SQLException {
                DateCount objDateCount = new DateCount();
                objDateCount.setCount(rs.getInt(1));
                objDateCount.setLaunchDate(rs.getDate(2));
                return objDateCount;
            }
    
}
