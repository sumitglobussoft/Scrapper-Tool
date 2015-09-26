/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scrappertool;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import scrappertool.dao.LaunchDataDao;
import scrappertool.dao.LaunchDataDaoImpl;

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

        ScrapeFromUrl obj = new ScrapeFromUrl();
        obj.dataScrapping(objLaunchDataDao);

    }

}
