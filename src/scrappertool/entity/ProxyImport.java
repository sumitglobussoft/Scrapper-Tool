/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scrappertool.entity;

/**
 *
 * @author Mendon Ashwini
 * In this class all the proxies are set either with username and password or without
 * It is private proxy or public proxy can be noted with the proxyLen
 */
public class ProxyImport {

    public String proxyIP;
    public String proxyPort;
    public String proxyUserName;
    public String proxyPassword;
    public int proxyLen;

    public ProxyImport(String line) {
        String[] divideEachProperty = line.split(":");
        proxyIP = divideEachProperty[0];
        proxyPort = divideEachProperty[1];
        proxyLen=divideEachProperty.length;

        if (proxyLen == 4) {
            proxyUserName = divideEachProperty[2];
            proxyPassword = divideEachProperty[3];
        }

    }

}
