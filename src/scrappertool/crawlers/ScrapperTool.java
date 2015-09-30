/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scrappertool.crawlers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import scrappertool.dao.LaunchDataDao;
import scrappertool.dao.LaunchDataDaoImpl;
import scrappertool.entity.DateCount;

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
        

         List<Integer> list = new ArrayList<Integer>();

        for (int i=0;i<31;i++) {
            list.add(i);
        }
        
       List<DateCount> listLaunchDatas = objLaunchDataDao.listLaunchDatas();
        System.out.println("listLaunchDatas.size()::"+listLaunchDatas.size());
        
        Calendar today = Calendar.getInstance();


         List<DateCount> newList = new ArrayList<>();
        for (DateCount listLaunchData : listLaunchDatas) {
            
            System.out.println("LaunchData::"+listLaunchData.getLaunchDate()+"  Count::"+listLaunchData.getCount());

            Calendar cal = Calendar.getInstance();
            
            cal.setTime(listLaunchData.getLaunchDate());
            
             
 
            if((cal.get(Calendar.YEAR)==today.get(Calendar.YEAR))&&(cal.get(Calendar.MONTH)==today.get(Calendar.MONTH))){
                newList.add(listLaunchData);
            }
        }
        
        System.out.println("\n\n\nnewList::"+newList.size());
        
        for (DateCount newList1 : newList) {
            System.out.println(newList1.getLaunchDate()+"========"+newList1.getCount());
            
        }
        

    }

}
