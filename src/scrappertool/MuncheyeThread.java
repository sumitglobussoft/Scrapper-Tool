/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scrappertool;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import scrappertool.dao.LaunchDataDao;
import scrappertool.entity.LaunchData;
import scrappertool.utility.FetchSource;

/**
 *
 * @author Mendon Ashwini
 */
public class MuncheyeThread implements Callable<String> {

    FetchSource objFetchSource = new FetchSource();
    LaunchDataDao objLaunchDataDao = null;
    public String url;

    MuncheyeThread(String url, LaunchDataDao objLaunchDataDao) {
        this.url = url;
        this.objLaunchDataDao=objLaunchDataDao;
    }

    @Override
    public String call() throws Exception {
        dataExtraction(url, objLaunchDataDao);
        return "done";
    }

    public void dataExtraction(String urlPage, LaunchDataDao objLaunchDataDao) {

        Document objDocument = null;
        String urlResponse = null;
        try {
            
            LaunchData objLaunchData= new LaunchData();
            System.out.println("======================================");
           String promotionType = "NA";
           String vendor = "NA";
           String product = "NA";
           Date launchDate = null;
           String launchTime = "NA";
           String frontEndPrice = "NA";
           String commission = "NA";
           String jvPage = "NA";
           String affiliateNetwork = "NA";
           String niche = "NA";
           String site = "http://muncheye.com/";
           
           Date preLaunchDate = null;
           String description = "NA";
           String ticket = "NA";
           String clicks = "NA";
           

//            objDocument = Jsoup.parse(urlPage);
            System.out.println("" + urlPage);
            urlResponse = objFetchSource.fetchPageSourceWithProxy(urlPage);
            objDocument = Jsoup.parse(urlResponse);
//            System.out.println("objDocument"+objDocument);
            
            try {
                promotionType = objDocument.select("div[class=col_heading]").text();
                System.out.println("promotionType:" + promotionType);
            } catch (Exception e) {
            }
            
            Elements e=objDocument.select("div[class=product_info] table tbody tr");
      
                try {
                    vendor = e.get(0).select("td a").text();
                    System.out.println("vendor:" + vendor);
                } catch (Exception v1) {
                }
                
                try {
                    product = e.get(1).select("td").last().text();
                    System.out.println("product:" + product);
                } catch (Exception v1) {
                }
                
                try {
                    String launchDateString;
                    launchDateString = e.get(2).select("td").last().text();
                    System.out.println("launchDate:" + launchDateString);
                    System.out.println("coverting to data form");
                    DateFormat format = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH);
                    launchDate = format.parse(launchDateString);
                    
                } catch (Exception v1) {
                }
                try {
                    launchTime = e.get(3).select("td").last().text();
                    System.out.println("launchTime:" + launchTime);
                } catch (Exception v1) {
                }
                try {
                    frontEndPrice = e.get(4).select("td").last().text();
                    System.out.println("frontEndPrice:" + frontEndPrice);
                } catch (Exception v1) {
                }
                try {
                    commission = e.get(5).select("td").last().text();
                    System.out.println("commission:" + commission);
                } catch (Exception v1) {
                }
                try {
                    jvPage = e.get(6).select("td a").text();
                    System.out.println("jvPage:" + jvPage);
                } catch (Exception v1) {
                }
                try {
                    affiliateNetwork = e.get(7).select("td a").text();
                    System.out.println("affiliateNetwork:" + affiliateNetwork);
                } catch (Exception v1) {
                }
                try {
                    niche = e.get(8).select("td a").text();
                    System.out.println("niche:" + niche);
                } catch (Exception v1) {
                }
           
           objLaunchData.setPromotionType(promotionType);
           objLaunchData.setVendor(vendor);
           objLaunchData.setProduct(product);
           objLaunchData.setLaunchDate(launchDate);
           objLaunchData.setLaunchTime(launchTime);
           objLaunchData.setFrontendPrice(frontEndPrice);
           objLaunchData.setCommission(commission);
           objLaunchData.setJvPage(jvPage);
           objLaunchData.setAffiliateNetwork(affiliateNetwork);
           objLaunchData.setNiche(niche);
           objLaunchData.setSite(site);
           objLaunchData.setPreLaunchDate(preLaunchDate);
           objLaunchData.setDescription(description);
           objLaunchData.setTicket(ticket);
           objLaunchData.setClicks(clicks);
           objLaunchDataDao.insertLaunchData(objLaunchData);
             System.out.println("======================================");

        } catch (Exception ex) {
            Logger.getLogger(ScrapeFromUrl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
