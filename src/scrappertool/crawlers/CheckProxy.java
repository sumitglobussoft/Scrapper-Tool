/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scrappertool.crawlers;

import java.net.InetAddress;
import java.util.concurrent.Callable;
import static scrappertool.ui.MainPage.calendarButton;
import static scrappertool.ui.MainPage.loggerArea;
import static scrappertool.ui.MainPage.proxyList;
import static scrappertool.ui.MainPage.refreshDB;
import static scrappertool.ui.MainPage.withProxyRadio;
import static scrappertool.ui.MainPage.withoutProxyRadio;

/**
 *
 * @author Mendon Ashwini
 * CheckProxy is a thread which checks if the proxies in the file are valid r not
 */
public class CheckProxy implements Callable<String> {
    @Override
    public String call() throws Exception {
        
        for(int i=proxyList.size()-1;i>=0;i--){
                    System.out.println(proxyList.get(i).proxyPort+"::check:::"+proxyList.get(i).proxyIP);
                    int test=0;

                    
                    try {
                        InetAddress inet = InetAddress.getByName(proxyList.get(i).proxyIP);
                        if (inet.isReachable(5000)) {
                            System.out.println(proxyList.get(i).proxyIP + " is reachable");
                        } else {
                            test = 1;
                            System.out.println(proxyList.get(i).proxyIP + " is not reachable");
                            loggerArea.append("\n\n" + proxyList.get(i).proxyIP + " is not reachable");
                        }
                    } catch (Exception iOException) {
                        loggerArea.append("\n\n" + proxyList.get(i).proxyIP + " is not valid IP");
                    }
                    if(test==0){
                    try {
                        int port= Integer.parseInt(proxyList.get(i).proxyPort);
                        if((port>65535)||(port<0)){
                            test=1;
                            loggerArea.append("\n\n"+proxyList.get(i).proxyPort + " is not valid");
                            
                        }
                    } catch (Exception e) {
                        test=1;
                    }
                    }
                    if(test==1){
                        try {
                            loggerArea.append("\n\n" + proxyList.get(i).proxyIP + ":" + proxyList.get(i).proxyPort + " is removed from list");
                            proxyList.remove(proxyList.get(i));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    

                }
                if(proxyList.size()>0){
                loggerArea.append("\n\nDone Loading the Proxy!!");
                loggerArea.append("\nData will be refreshed with Proxy!!");
                }
                else{
                    loggerArea.append("\n\nThere is not Data in the proxy list!!");
                    withoutProxyRadio.setSelected(true);
            withProxyRadio.setSelected(false);
                }
        calendarButton.setEnabled(true);
                    refreshDB.setEnabled(true);
        return "done";
    }
    
}
