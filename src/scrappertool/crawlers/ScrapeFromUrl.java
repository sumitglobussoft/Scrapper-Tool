/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scrappertool.crawlers;

import java.io.IOException;
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
import scrappertool.entity.ProxyImport;
import scrappertool.ui.MainPage;
import static scrappertool.ui.MainPage.loggerArea;
import scrappertool.utility.FetchSource;

/**
 *
 * @author Mendon Ashwini
 */
public class ScrapeFromUrl {

    FetchSource objFetchSource = new FetchSource();

    public void dataScrapping(LaunchDataDao objLaunchDataDao, List<ProxyImport> proxyList) {

        String urlMuncheye = "http://muncheye.com/";
        String urlJvnotifypro = "http://v3.jvnotifypro.com/account/";
        String urlLaunchsuite = "http://www.launchsuite.net/";
        try {
            List<Future<String>> list = new ArrayList<Future<String>>();
            
            ///to call  MuncheyeMainThread 
            ExecutorService executor1 = Executors.newFixedThreadPool(1);
            Callable worker1 = new MuncheyeMainThread(urlMuncheye, objLaunchDataDao, proxyList, objFetchSource);

            try {
                Future<String> future1 = executor1.submit(worker1);
                list.add(future1);
            } catch (Exception ex) {
                Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
            }

            /// to call JvnotifyproMainThread
            ExecutorService executor2 = Executors.newFixedThreadPool(1);
            Callable worker2 = new JvnotifyproMainThread(urlJvnotifypro, objLaunchDataDao, proxyList, objFetchSource);

            try {
                Future<String> future2 = executor2.submit(worker2);
                list.add(future2);
            } catch (Exception ex) {
                Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
            }

            //to call LaunchsuitMainThread
            
            ExecutorService executor3 = Executors.newFixedThreadPool(1);
            Callable worker3 = new LaunchsuitMainThread(urlLaunchsuite, objLaunchDataDao, proxyList, objFetchSource);

            try {
                Future<String> future3 = executor3.submit(worker3);
                list.add(future3);
            } catch (Exception ex) {
                Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
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
             executor1.shutdown();
             executor2.shutdown();
             executor3.shutdown();


//            getMuncheyeLaunch(urlMuncheye, objLaunchDataDao, proxyList);
//            getJvnotifyproLaunch(urlJvnotifypro, objLaunchDataDao, proxyList);
//            getLaunchsuiteLaunch(urlLaunchsuite, objLaunchDataDao, proxyList);
        } catch (Exception ex) {
            Logger.getLogger(ScrapeFromUrl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}

class LaunchIDAN {

    String id;
    String an;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAn() {
        return an;
    }

    public void setAn(String an) {
        this.an = an;
    }

}

class MuncheyeMainThread implements Callable<String> {

    List<String> getMuncheyeLaunchURL = new ArrayList<>();
    String urlMuncheye;
    LaunchDataDao objLaunchDataDao = null;
    List<ProxyImport> proxyList = null;
    FetchSource objFetchSource = null;

    MuncheyeMainThread(String urlMuncheye, LaunchDataDao objLaunchDataDao, List<ProxyImport> proxyList, FetchSource objFetchSource) {
        this.urlMuncheye = urlMuncheye;
        this.objLaunchDataDao = objLaunchDataDao;
        this.proxyList = proxyList;
        this.objFetchSource = objFetchSource;
    }

    @Override
    public String call() throws Exception {
        getMuncheyeLaunch(urlMuncheye, objLaunchDataDao, proxyList);
        return "done";
    }

    public void getMuncheyeLaunch(String urlPage, LaunchDataDao objLaunchDataDao, List<ProxyImport> proxyList) {
        System.out.println("========Getting  Launch List From Muncheye========");

        String urlResponse = null;
        Document objDocument = null;
        try {

            if ((proxyList != null) && (proxyList.size() > 0)) {
                System.out.println("with prox");
                urlResponse = objFetchSource.fetchPageSourceWithProxy(urlPage, proxyList);
            } else {
                System.out.println("without prox");
                try {
                    urlResponse = objFetchSource.fetchsourceWithoutProxy(urlPage);
                } catch (IOException iOException) {
                    try {
                        System.out.println("2nd time");
                        urlResponse = objFetchSource.fetchsourceWithoutProxy(urlPage);
                    } catch (IOException iOException1) {
                    }
                }
            }
            objDocument = Jsoup.parse(urlResponse);

            Elements e = objDocument.select("div[id=right-column] div[class*=item_info] a");
            System.out.println("" + e.size());

            for (Element e1 : e) {
                getMuncheyeLaunchURL.add("http://muncheye.com" + e1.attr("href"));
            }
            List<Future<String>> list = new ArrayList<Future<String>>();
            ExecutorService executor = Executors.newFixedThreadPool(10);
            for (String url : getMuncheyeLaunchURL) {
                try {
                    Callable worker = new MuncheyeThread(url, objLaunchDataDao, proxyList);
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

}

class JvnotifyproMainThread implements Callable<String> {

    List<String> getMuncheyeLaunchURL = new ArrayList<>();
    String urlJvnotifypro;
    LaunchDataDao objLaunchDataDao = null;
    List<ProxyImport> proxyList = null;
    FetchSource objFetchSource = null;

    JvnotifyproMainThread(String urlJvnotifypro, LaunchDataDao objLaunchDataDao, List<ProxyImport> proxyList, FetchSource objFetchSource) {
        this.urlJvnotifypro = urlJvnotifypro;
        this.objLaunchDataDao = objLaunchDataDao;
        this.proxyList = proxyList;
        this.objFetchSource = objFetchSource;
    }

    @Override
    public String call() throws Exception {
        getJvnotifyproLaunch(urlJvnotifypro, objLaunchDataDao, proxyList);
        return "done";
    }

    public void getJvnotifyproLaunch(String urlPage, LaunchDataDao objLaunchDataDao, List<ProxyImport> proxyList) {

        List<LaunchData> listLaunchData = new ArrayList();
        LaunchData objLaunchData = null;

        System.out.println("========Getting  Launch List From Jvnotifypro========");

        String urlResponse = null;
        Document objDocument = null;
        try {

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
            String site = "http://v3.jvnotifypro.com/account/";

            String preLaunchDate = null;
            String description = "NA";
            String ticket = "NA";
            String clicks = "NA";

            if ((proxyList != null) && (proxyList.size() > 0)) {
                System.out.println("with prox");
                urlResponse = objFetchSource.fetchPageSourceWithProxy(urlPage, proxyList);
            } else {
                System.out.println("without prox");
                try {
                    urlResponse = objFetchSource.fetchsourceWithoutProxy(urlPage);
                } catch (IOException iOException) {
                    try {
                        System.out.println("2nd time");
                        urlResponse = objFetchSource.fetchsourceWithoutProxy(urlPage);
                    } catch (IOException iOException1) {
                    }
                }
            }
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
                    launchDate = new SimpleDateFormat("yyyy-MM-dd").format(format.parse(dateUnformated));

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
                        launchDate = new SimpleDateFormat("yyyy-MM-dd").format(format.parse(dateUnformated));

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
                    ticket = e1.select("span").get(0).text() + "==>" + e1.select("span").get(1).text();

                    System.out.println("tempTicket::" + ticket);

                } catch (Exception c) {
                }

                try {
                    clicks = e1.select("span").get(2).text() + "/" + e1.select("span").get(3).text();

                    System.out.println("clicks::" + clicks);

                } catch (Exception c) {
                }

                try {
                    String tbody = e1.select("tbody").text();
                    System.out.println("tbody::" + tbody);
                    try {
                        String tempLaunch[] = tbody.split("Launch");
                        promotionType = tempLaunch[0] + "Launch";
                        System.out.println("promotionType::" + promotionType);

                        String tempMarkSoftProc[] = tempLaunch[1].split(" ");

                        affiliateNetwork = tempMarkSoftProc[1];
                        System.out.println("affiliateNetwork::" + affiliateNetwork);

                    } catch (Exception l) {
                    }
                    try {
                        String temp1Niche[] = tbody.split("[)]");
                        String temp2Niche[] = temp1Niche[1].split("[(]");
                        niche = temp2Niche[0];
                        System.out.println("niche::" + niche);

                    } catch (Exception l) {
                        l.printStackTrace();
                    }

                } catch (Exception vp) {
                }

                objLaunchData.setPromotionType(promotionType);
                objLaunchData.setVendor(vendor.trim());
                objLaunchData.setProduct(product.trim());
                objLaunchData.setLaunchDate(launchDate);
                objLaunchData.setLaunchTime(launchTime);
                objLaunchData.setFrontendPrice(frontEndPrice);
                objLaunchData.setCommission(commission);
                objLaunchData.setJvPage(jvPage);
                objLaunchData.setAffiliateNetwork(affiliateNetwork);
                objLaunchData.setNiche(niche);
                objLaunchData.setSite(site);
//                objLaunchData.setPreLaunchDate(preLaunchDate);
                objLaunchData.setDescription(description);
                objLaunchData.setTicket(ticket);
                objLaunchData.setClicks(clicks);
                listLaunchData.add(objLaunchData);

//                System.out.println("\nLaunchDate        ----------> : " + objLaunchData.getLaunchDate());
                loggerArea.append("\nProduct\t\t----------> :\t" + objLaunchData.getProduct().trim());
                loggerArea.append("\nVendor\t\t----------> :\t" + objLaunchData.getVendor().trim());
                loggerArea.append("\nLaunchDate\t----------> :\t" + objLaunchData.getLaunchDate());
                loggerArea.append("\nNiche\t\t----------> :\t" + objLaunchData.getNiche().trim());
                loggerArea.append("\nAffiliateNetwork\t----------> :\t" + objLaunchData.getAffiliateNetwork().trim());
                loggerArea.append("\nDescription\t----------> :\t" + objLaunchData.getDescription().trim());
                loggerArea.append("\nPromotionType\t----------> :\t" + objLaunchData.getPromotionType().trim());
                loggerArea.append("\n");
            }

            for (LaunchData obj : listLaunchData) {
                objLaunchDataDao.insertLaunchData(obj);
            }

        } catch (Exception ex) {
            Logger.getLogger(ScrapeFromUrl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}

class LaunchsuitMainThread implements Callable<String> {

    List<String> getMuncheyeLaunchURL = new ArrayList<>();
    String urlLaunchsuite;
    LaunchDataDao objLaunchDataDao = null;
    List<ProxyImport> proxyList = null;
    FetchSource objFetchSource = null;

    LaunchsuitMainThread(String urlLaunchsuite, LaunchDataDao objLaunchDataDao, List<ProxyImport> proxyList, FetchSource objFetchSource) {
        this.urlLaunchsuite = urlLaunchsuite;
        this.objLaunchDataDao = objLaunchDataDao;
        this.proxyList = proxyList;
        this.objFetchSource = objFetchSource;
    }

    @Override
    public String call() throws Exception {
        getLaunchsuiteLaunch(urlLaunchsuite, objLaunchDataDao, proxyList);
        return "done";
    }

    public void getLaunchsuiteLaunch(String urlLaunchsuite, LaunchDataDao objLaunchDataDao, List<ProxyImport> proxyList) {
        System.out.println("========Getting  Launch List From Launchsuite========");
        //"http://www.launchsuite.net/getmelistingdetails.php"

        String urlResponse = null;
        Document objDocument = null;
        List<LaunchIDAN> allLaunchsuiteID = new ArrayList<>();
        try {
            if ((proxyList != null) && (proxyList.size() > 0)) {
                urlResponse = objFetchSource.fetchPageSourceWithProxy(urlLaunchsuite, proxyList);
            } else {
                try {
                    urlResponse = objFetchSource.fetchsourceWithoutProxy(urlLaunchsuite);
                } catch (IOException iOException) {
                    try {
                        System.out.println("2nd time");
                    urlResponse = objFetchSource.fetchsourceWithoutProxy(urlLaunchsuite);
                    } catch (Exception e) {
                    }
                }
            }

            objDocument = Jsoup.parse(urlResponse);

            Elements e = objDocument.select("div[class=launches-container] div[class=singelisting full]");

            for (Element e1 : e) {
                LaunchIDAN obj = new LaunchIDAN();
                String tempId = e1.attr("id");
                String aff = null;
                try {
                    aff = e1.select("div[class*=affiliatenetwork]").attr("class").replace("Listing affiliatenetwork", "");
                    if (aff.equals("zoo")) {
                        obj.setAn("JVZoo");
                    } else if (aff.equals("wp")) {
                        obj.setAn("WarriorPlus");
                    } else {
                        obj.setAn(aff);
                    }
                } catch (Exception an) {
                }
                System.out.println("affN::::" + aff);

                tempId = tempId.replace("launch", "");
                obj.setId(tempId);
                allLaunchsuiteID.add(obj);
                System.out.println("" + tempId);
            }
            for (LaunchIDAN objLaunchIDAN : allLaunchsuiteID) {
                try {

                    new Launchsuite().dataExtraction(objLaunchIDAN.getId(), objLaunchIDAN.getAn(), objLaunchDataDao, proxyList);

                } catch (Exception exx) {
                    System.out.println(exx);
                }

            }

        } catch (Exception ex) {
            Logger.getLogger(ScrapeFromUrl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
