/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scrappertool.crawlers;

import java.util.List;
import java.util.concurrent.Callable;
import scrappertool.dao.LaunchDataDao;
import scrappertool.entity.ProxyImport;
import static scrappertool.ui.MainPage.calendarButton;
import static scrappertool.ui.MainPage.loggerArea;
import static scrappertool.ui.MainPage.refreshDB;
import static scrappertool.ui.MainPage.withProxyRadio;
import static scrappertool.ui.MainPage.withoutProxyRadio;

/**
 *
 * @author GLB-130
 */
public class DeleteDBStartCrawl implements Callable<String> {

    LaunchDataDao objLaunchDataDao = null;
    List<ProxyImport> proxyList = null;

    public DeleteDBStartCrawl(LaunchDataDao objLaunchDataDao, List<ProxyImport> proxyList) {
        this.objLaunchDataDao = objLaunchDataDao;
        this.proxyList = proxyList;
    }

    @Override
    public String call() throws Exception {

        objLaunchDataDao.createTable();
        objLaunchDataDao.deleteAll();
        ScrapeFromUrl obj = new ScrapeFromUrl();
        obj.dataScrapping(objLaunchDataDao, proxyList);

        loggerArea.append("\n================================Done===========================");
        withoutProxyRadio.setEnabled(true);
        withProxyRadio.setEnabled(true);
        calendarButton.setEnabled(true);
        refreshDB.setEnabled(true);

        return "done";
    }

}
