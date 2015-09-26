/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scrappertool;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import scrappertool.dao.LaunchDataDao;
import scrappertool.entity.LaunchData;
import scrappertool.utility.FetchSource;

/**
 *
 * @author Mendon Ashwini
 */
public class ScrapeFromUrl {

    FetchSource objFetchSource = new FetchSource();
    List<String> getMuncheyeLaunchURL = new ArrayList<>();

    public void dataScrapping(LaunchDataDao objLaunchDataDao) {

        String urlMuncheye = "http://muncheye.com/";
        String urlJvnotifypro = "http://v3.jvnotifypro.com/account/";
        String urlLaunchsuite = "http://www.launchsuite.net/";
        try {

//            getMuncheyeLaunch(urlMuncheye, objLaunchDataDao);
//            getJvnotifyproLaunch(urlJvnotifypro, objLaunchDataDao);
            getLaunchsuiteLaunch(urlLaunchsuite, objLaunchDataDao);

        } catch (Exception ex) {
            Logger.getLogger(ScrapeFromUrl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void getMuncheyeLaunch(String urlPage, LaunchDataDao objLaunchDataDao) {
        System.out.println("========Getting  Launch List From Muncheye========");

        String urlResponse = null;
        Document objDocument = null;
        try {

            urlResponse = objFetchSource.fetchPageSourceWithProxy(urlPage);
            objDocument = Jsoup.parse(urlResponse);

            Elements e = objDocument.select("div[id=right-column] div[class=item_info] a");
            System.out.println("" + e.size());

            for (Element e1 : e) {
                getMuncheyeLaunchURL.add("http://muncheye.com" + e1.attr("href"));
            }
            List<Future<String>> list = new ArrayList<Future<String>>();
            ExecutorService executor = Executors.newFixedThreadPool(10);
            for (String url : getMuncheyeLaunchURL) {
                try {
                    Callable worker = new MuncheyeThread(url, objLaunchDataDao);
                    Future<String> future = executor.submit(worker);
                    list.add(future);
                } catch (Exception exx) {
                    System.out.println(exx);
                }

            }
            for (Future<String> fut : list) {
                try {
                    //print the return value of Future, notice the output delay in console
                    // because Future.get() waits for task to get completed
                    System.out.println(new Date() + "::" + fut.get());
                } catch (InterruptedException | ExecutionException ep) {
                    ep.printStackTrace();
                }
            }
            //shut down the executor service now
            executor.shutdown();

        } catch (Exception ex) {
            Logger.getLogger(ScrapeFromUrl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void getJvnotifyproLaunch(String urlPage, LaunchDataDao objLaunchDataDao) {
        
        List<LaunchData> listLaunchData = new ArrayList();
        LaunchData objLaunchData = null;

        System.out.println("========Getting  Launch List From Jvnotifypro========");

        String urlResponse = null;
        Document objDocument = null;
        try {

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
            String site = "http://v3.jvnotifypro.com/account/";

            Date preLaunchDate = null;
            String description = "NA";
            String ticket = "NA";
            String clicks = "NA";

            urlResponse = objFetchSource.fetchsourceWithoutProxy(urlPage);
            objDocument = Jsoup.parse(urlResponse);
//            System.out.println(""+objDocument);

            Elements e = objDocument.select("div[class=panes] div[class=listing] div[class=section-content grey summary listings] div[class=buzz_builder_slide]");
            System.out.println("=================\n" + e.size() + "=================\n");

            for (Element e1 : e) {
                objLaunchData = new LaunchData();
                try {
                    String vendorProduct = e1.select("div").select("b").first().text();

                    String vendorProductData[] = vendorProduct.split("-");
                    vendor = vendorProductData[0];
                    product = vendorProductData[1];
                    System.out.println("vendor::" + vendor);
                    System.out.println("product::" + product);
                } catch (Exception vp) {
                }
                try {
                    String dateUnformated = e1.select("div").select("b").get(2).text();
                    System.out.println("dateUnformated::" + dateUnformated);
                    String alldata[] = dateUnformated.split(" ");
                    alldata[2] = alldata[2].replace("nd", "").replace("th", "").replace("st", "").replace("rd", "");
                    dateUnformated = "";
                    for (String alldata1 : alldata) {
                        dateUnformated = dateUnformated + alldata1 + " ";
                    }

                    DateFormat format = new SimpleDateFormat("EEE, MMMM dd yyyy", Locale.ENGLISH);
                    launchDate = format.parse(dateUnformated);

                    System.out.println("launchDate::" + launchDate);
                } catch (Exception vp) {

                    try {
                        String dateUnformated = e1.select("div").select("b").get(3).text();
                        System.out.println("dateUnformated::" + dateUnformated);
                        String alldata[] = dateUnformated.split(" ");
                        alldata[2] = alldata[2].replace("nd", "").replace("th", "").replace("st", "").replace("rd", "");
                        dateUnformated = "";
                        for (String alldata1 : alldata) {
                            dateUnformated = dateUnformated + alldata1 + " ";
                        }

                        DateFormat format = new SimpleDateFormat("EEE, MMMM dd yyyy", Locale.ENGLISH);
                        launchDate = format.parse(dateUnformated);

                        System.out.println("launchDate::" + launchDate);
                    } catch (ParseException parseException) {
                    }

                }

                try {
                    String fullDescription = e1.select("div").text();
                    String bDescription = e1.select("div b").text();
                    description = fullDescription.replace(bDescription, "");
                    System.out.println("description::" + description);
                } catch (Exception vp) {
                }
                
                try {
                    ticket = e1.select("span").get(0).text()+ "==>" + e1.select("span").get(1).text();
                    
                    System.out.println("tempTicket::" + ticket);
                    
                } catch (Exception c) {
                }
                
                try {
                    clicks = e1.select("span").get(2).text()+ "/" + e1.select("span").get(3).text();
                    
                    System.out.println("clicks::" + clicks);
                    
                } catch (Exception c) {
                }
                
                
                try {
                    String tbody = e1.select("tbody").text();
                    System.out.println("tbody::" + tbody);
                    try {
                        String tempLaunch[]=tbody.split("Launch");
                        promotionType=tempLaunch[0]+"Launch";
                        System.out.println("promotionType::" + promotionType);
                        
                        String tempMarkSoftProc[]=tempLaunch[1].split(" ");
                        
                        affiliateNetwork=tempMarkSoftProc[1];
                        System.out.println("affiliateNetwork::" + affiliateNetwork);
                        
                        
                    } catch (Exception l) {
                    }
                    try {
                        String temp1Niche[]=tbody.split("[)]");
                        String temp2Niche[]=temp1Niche[1].split("[(]");
                        niche=temp2Niche[0];
                        System.out.println("niche::" + niche);
                        
                    } catch (Exception l) {
                        l.printStackTrace();
                    }
                    
                } catch (Exception vp) {
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
           listLaunchData.add(objLaunchData);
}
            
            for (LaunchData obj : listLaunchData) {
                objLaunchDataDao.insertLaunchData(obj);
            }
            

        } catch (Exception ex) {
            Logger.getLogger(ScrapeFromUrl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public void getLaunchsuiteLaunch(String urlLaunchsuite, LaunchDataDao objLaunchDataDao) {
        System.out.println("========Getting  Launch List From Launchsuite========");
        //"http://www.launchsuite.net/getmelistingdetails.php"

        String urlResponse = null;
        Document objDocument = null;
        List<String> allLaunchsuiteID = new ArrayList<>();
        try {

            urlResponse = objFetchSource.fetchPageSourceWithProxy(urlLaunchsuite);
            objDocument = Jsoup.parse(urlResponse);

            Elements e = objDocument.select("div[class=launches-container] div[class=singelisting full]");

            for (Element e1 : e) {
                String tempId=e1.attr("id");
                tempId = tempId.replace("launch", "");
                allLaunchsuiteID.add(tempId);
                System.out.println(""+tempId);
            }
            for (String para : allLaunchsuiteID) {
                try {
                    
                     new Launchsuite().dataExtraction(para, objLaunchDataDao);
                   
                } catch (Exception exx) {
                    System.out.println(exx);
                }

            }


        } catch (Exception ex) {
            Logger.getLogger(ScrapeFromUrl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
