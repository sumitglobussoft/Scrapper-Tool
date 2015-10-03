/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scrappertool.crawlers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import scrappertool.dao.LaunchDataDao;
import scrappertool.dao.LaunchDataDaoImpl;
import scrappertool.entity.DateCount;
import scrappertool.utility.FetchSource;

/**
 *
 * @author Mendon Ashwini
 */
public class ScrapperTool {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        ApplicationContext context
                = new ClassPathXmlApplicationContext("Beans.xml");

        LaunchDataDao objLaunchDataDao
                = (LaunchDataDaoImpl) context.getBean("LaunchDataDaoImpl");
        
//objLaunchDataDao.createTable();
//        ScrapeFromUrl o=new ScrapeFromUrl();
//        o.getJvnotifyproLaunch("http://www.launchsuite.net/getmelistingdetails.php", objLaunchDataDao, null);

//        FetchSource obj=new FetchSource();
//        try {
//           String a= obj.sendPostWithoutProxy("http://www.launchsuite.net/getmelistingdetails.php", "93");
//            System.out.println("a"+a);
////       
//        } catch (Exception ex) {
//            Logger.getLogger(ScrapperTool.class.getName()).log(Level.SEVERE, null, ex);
//        }
        

    }

}
