/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scrappertool.crawlers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import scrappertool.dao.LaunchDataDao;
import scrappertool.entity.LaunchData;
import scrappertool.entity.ProxyImport;
import static scrappertool.ui.MainPage.loggerArea;
import scrappertool.utility.FetchSource;

/**
 *
 * @author GLB-130
 */
public class Launchsuite {

    FetchSource objFetchSource = new FetchSource();

    public void dataExtraction(String urlParameter, String affiliatedN, LaunchDataDao objLaunchDataDao, List<ProxyImport> proxyList) {

        String urlPage = "http://www.launchsuite.net/getmelistingdetails.php";
        Document objDocument = null;
        String urlResponse = null;
        try {

            LaunchData objLaunchData = new LaunchData();
            System.out.println("====================urlParameter==================" + urlParameter);
            String promotionType = "NA";
            String vendor = "NA";
            String product = "NA";
            Date launchDate = null;
            String launchTime = "NA";
            String frontEndPrice = "NA";
            String commission = "NA";
            String jvPage = "NA";
            String affiliateNetwork = affiliatedN;
            String niche = "NA";
            String site = "http://www.launchsuite.net/";

            Date preLaunchDate = null;
            String description = "NA";
            String ticket = "NA";
            String clicks = "NA";

            urlResponse = objFetchSource.fetchPageSourceWithProxyPost(urlPage, urlParameter, proxyList);

//            System.out.println("urlResponse" + urlResponse);

            try {
                JSONObject objJSONObject = new JSONObject(urlResponse);

                product = objJSONObject.getString("productname");

                vendor = objJSONObject.getString("vendors");

                try {
                    String tempLaunchdate = objJSONObject.getString("launchdate");
                    System.out.println("launchDate:" + tempLaunchdate);
                    System.out.println("coverting to data form");
                    DateFormat format = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH);
                    launchDate = format.parse(tempLaunchdate);
                } catch (JSONException jSONException) {
                }

                commission = objJSONObject.getString("fecommisssion")+"%";

                jvPage = objJSONObject.getString("zoourl");

                niche = objJSONObject.getString("productCategory");

                try {
                    String tempdescription = objJSONObject.getString("listingDescription");
                    objDocument = Jsoup.parse(tempdescription);
                    System.out.println("---------:::objDocument :::----------> : " + objDocument);
                    description = objDocument.text();
                } catch (JSONException jSONException) {
                }

            } catch (JSONException jSONException) {
            }

            System.out.println("---------1.product     ----------> : " + product);
            System.out.println("---------2.vendor      ----------> : " + vendor);
            System.out.println("---------3.launchDate  ----------> : " + launchDate);
            System.out.println("---------4.commission  ----------> : " + commission);
            System.out.println("---------5.jvPage      ----------> : " + jvPage);
            System.out.println("---------6.niche       ----------> : " + niche);
            System.out.println("---------7.description ----------> : " + description);

           
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
