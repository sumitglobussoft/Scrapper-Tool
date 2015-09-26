/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scrappertool.dao;

import java.util.List;
import javax.sql.DataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import scrappertool.entity.LaunchData;

/**
 *
 * @author GLB-130
 */
public class LaunchDataDaoImpl implements LaunchDataDao {

    private SimpleJdbcInsert launchDataInsert;
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplateObject;

    @Override
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplateObject = new JdbcTemplate(dataSource);
    }

    @Override
    public void create(String site, Integer age) {
        String SQL = "insert into launch_data (site, product) values (?, ?)";

        jdbcTemplateObject.update(SQL, site, "testing");
        System.out.println("Created Record Name = " + site + " Age = " + age);

    }

    @Override
    public LaunchData getLaunchData(Integer id) {
        String SQL = "select * from launch_data where id = ?";
        LaunchData student = (LaunchData) jdbcTemplateObject.queryForObject(SQL,
                new Object[]{id}, new BeanPropertyRowMapper(LaunchData.class));
        return student;
    }

    @Override
    public List<LaunchData> listStudents() {
        String SQL = "select * from Student";
        List<LaunchData> students = jdbcTemplateObject.query(SQL,
                new BeanPropertyRowMapper(LaunchData.class));
        return students;
    }

    @Override
    public void delete(Integer id) {
        String SQL = "delete from Student where id = ?";
        jdbcTemplateObject.update(SQL, id);
        System.out.println("Deleted Record with ID = " + id);
        return;
    }

    @Override
    public void update(Integer id, Integer age) {
        String SQL = "update Student set age = ? where id = ?";
        jdbcTemplateObject.update(SQL, age, id);
        System.out.println("Updated Record with ID = " + id);
        return;
    }

    @Override
    public void insertLaunchData(LaunchData objLaunchData) {
        String SQL = "insert into launch_data (SITE, VENDOR, PRODUCT, LAUNCH_DATE, LAUNCH_TIME, FRONTEND_PRICE, COMMISSION, JV_PAGE, AFFILIATE_NETWORK, NICHE, PRE_LAUNCH_DATE, DESCRIPTION, PROMOTION_TYPE, TICKET, CLICKS) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            jdbcTemplateObject.update(SQL, objLaunchData.getSite(), objLaunchData.getVendor(), objLaunchData.getProduct(), objLaunchData.getLaunchDate(), objLaunchData.getLaunchTime(), objLaunchData.getFrontendPrice(), objLaunchData.getCommission(), objLaunchData.getJvPage(), objLaunchData.getAffiliateNetwork(), objLaunchData.getNiche(), objLaunchData.getPreLaunchDate(), objLaunchData.getDescription(), objLaunchData.getPromotionType(), objLaunchData.getTicket(), objLaunchData.getClicks());
            System.out.println("LaunchData inserted");
        } catch (DataAccessException dataAccessException) {
            String SQL2 = "select * from launch_data where VENDOR = ?, LAUNCH_DATE =? and PRODUCT= ?";
            try {
                LaunchData launch = (LaunchData) jdbcTemplateObject.queryForObject(SQL2,
                        new Object[]{objLaunchData.getVendor(), objLaunchData.getLaunchDate(), objLaunchData.getProduct()}, new BeanPropertyRowMapper(LaunchData.class));
                String SQL3 = "update launch_data set SITE = ?, VENDOR= ?, PRODUCT= ?, LAUNCH_DATE= ?, LAUNCH_TIME= ?, FRONTEND_PRICE= ?, COMMISSION= ?, JV_PAGE= ?, AFFILIATE_NETWORK= ?, NICHE= ?, PRE_LAUNCH_DATE= ?, DESCRIPTION= ?, PROMOTION_TYPE= ?, TICKET= ?, CLICKS= ? where id = ?";
                jdbcTemplateObject.update(SQL3, objLaunchData.getSite(), objLaunchData.getVendor(), objLaunchData.getProduct(), objLaunchData.getLaunchDate(), objLaunchData.getLaunchTime(), objLaunchData.getFrontendPrice(), objLaunchData.getCommission(), objLaunchData.getJvPage(), objLaunchData.getAffiliateNetwork(), objLaunchData.getNiche(), objLaunchData.getPreLaunchDate(), objLaunchData.getDescription(), objLaunchData.getPromotionType(), objLaunchData.getTicket(), objLaunchData.getClicks(), launch.getId());
            } catch (DataAccessException dataAccessException1) {
            }
        }

    }

}
