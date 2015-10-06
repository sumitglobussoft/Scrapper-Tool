/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scrappertool.crawlers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import scrappertool.dao.LaunchDataDao;
import scrappertool.entity.LaunchData;
import scrappertool.entity.ProxyImport;
import static scrappertool.ui.MainPage.loggerArea;
import scrappertool.utility.FetchSource;

/**
 *
 * @author Mendon Ashwini
 */
public class MuncheyeThread implements Callable<String> {

    FetchSource objFetchSource = new FetchSource();
    LaunchDataDao objLaunchDataDao = null;
    public String url;
    List<ProxyImport> proxyList = null;

    MuncheyeThread(String url, LaunchDataDao objLaunchDataDao, List<ProxyImport> proxyList) {
        this.url = url;
        this.objLaunchDataDao = objLaunchDataDao;
        this.proxyList = proxyList;
    }

    @Override
    public String call() throws Exception {
        dataExtraction(url, objLaunchDataDao, proxyList);
        return "done";
    }

    public void dataExtraction(String urlPage, LaunchDataDao objLaunchDataDao, List<ProxyImport> proxyList) {

        Document objDocument = null;
        String urlResponse = null;
        try {

            LaunchData objLaunchData = new LaunchData();
            System.out.println("======================================");
            String promotionType = "NA";
            String vendor = "NA";
            String product = "NA";
            String launchDate = null;
            String launchTime = "NA";
            String frontEndPrice = "NA";
            String commission = "NA";
            String jvPage = "NA";
            String affiliateNetwork = "NA";
            String niche = "NA";
            String site = "http://muncheye.com/";

            String preLaunchDate = null;
            String description = "NA";
            String ticket = "NA";
            String clicks = "NA";

            
            if((proxyList!=null)&&(proxyList.size()>0)){
                System.out.println("with prox");
            urlResponse = objFetchSource.fetchPageSourceWithProxy(urlPage, proxyList);
             }
            else {
                System.out.println("without prox");
                try {
                    urlResponse = objFetchSource.fetchsourceWithoutProxy(urlPage);
                } catch (Exception e) {
                    try {
                         System.out.println("2nd time");
                        urlResponse = objFetchSource.fetchsourceWithoutProxy(urlPage);
                    } catch (IOException iOException) {
                    }
                }
            
            }         
            
            objDocument = Jsoup.parse(urlResponse);

            try {
                promotionType = objDocument.select("div[class=col_heading]").text();
                System.out.println("promotionType:" + promotionType);
            } catch (Exception e) {
            }

            Elements e = objDocument.select("div[class=product_info] table tbody tr");

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
                launchDate = new SimpleDateFormat("yyyy-MM-dd").format(format.parse(launchDateString));

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
            
            loggerArea.append("\nProduct           ----------> : " + objLaunchData.getProduct().trim());
            loggerArea.append("\nVendor            ----------> : " + objLaunchData.getVendor().trim());
            loggerArea.append("\nLaunchDate        ----------> : " + objLaunchData.getLaunchDate());
            loggerArea.append("\nNiche             ----------> : " + objLaunchData.getNiche().trim());
            loggerArea.append("\nAffiliateNetwork  ----------> : " + objLaunchData.getAffiliateNetwork().trim());
            loggerArea.append("\nDescription       ----------> : " + objLaunchData.getDescription().trim());
            loggerArea.append("\nPromotionType     ----------> : " + objLaunchData.getPromotionType().trim());
            loggerArea.append("\n");
            
            System.out.println("======================================");

        } catch (Exception ex) {
            Logger.getLogger(ScrapeFromUrl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
