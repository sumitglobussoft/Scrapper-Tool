/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scrappertool.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import static scrappertool.ui.MainPage.loggerArea;

/**
 *
 * @author GLB-130
 */
public class GenerateExcelFile {

    public static void excel() throws FileNotFoundException, IOException {

        HSSFWorkbook hwb = new HSSFWorkbook();
        HSSFSheet sheet = hwb.createSheet("Scrapper Tool Details");
        HSSFRow rowhead = sheet.createRow((int) 0);
        rowhead.createCell((int) 1).setCellValue("Product");
        rowhead.createCell((int) 2).setCellValue("Vendor");
        rowhead.createCell((int) 3).setCellValue("Launch Date");
        rowhead.createCell((int) 4).setCellValue("Niche");
        rowhead.createCell((int) 5).setCellValue("Affilate Network");
        rowhead.createCell((int) 6).setCellValue("Decription");
        rowhead.createCell((int) 7).setCellValue("Promotion Type");

        try {
            Class.forName("org.sqlite.JDBC");
            Connection con = DriverManager.getConnection("jdbc:sqlite:scrappertool.db", "root", "");
            String sql = "select *from launch_data";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            int k = 0;
            while (rs.next()) {

                HSSFRow row = sheet.createRow((int) k + 2);
                try {
                    row.createCell((int) 0).setCellValue(k + 1);
                } catch (Exception sd) {
                }
                try {
                    row.createCell((int) 1).setCellValue(rs.getString("PRODUCT") + "");
                } catch (Exception sd) {
                }

                try {
                    row.createCell((int) 2).setCellValue(rs.getString("VENDOR") + "");
                } catch (Exception sd) {
                }

                try {
                    row.createCell((int) 3).setCellValue(rs.getString("LAUNCH_DATE") + "");
                } catch (Exception sd) {
                }

                try {
                    row.createCell((int) 4).setCellValue(rs.getString("NICHE") + "");
                } catch (Exception sd) {
                }

                try {
                    row.createCell((int) 5).setCellValue(rs.getString("AFFILIATE_NETWORK") + "");
                } catch (Exception sd) {
                }

                try {
                    row.createCell((int) 6).setCellValue(rs.getString("DESCRIPTION") + "");
                } catch (Exception sd) {
                }

                try {
                    row.createCell((int) 7).setCellValue(rs.getString("PROMOTION_TYPE") + "");
                } catch (Exception sd) {
                }

                k++;
            }

        } catch (Exception aaa) {
        }

        JFileChooser fileChooser = new JFileChooser();
        String filename = "";
        int returnStatus = fileChooser.showSaveDialog(null);
        if (returnStatus == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            System.out.println(selectedFile);
            filename = selectedFile.toString();
        }
        FileOutputStream fileOut = new FileOutputStream(filename);
        hwb.write(fileOut);
        fileOut.close();
        System.out.println("Your excel file has been generated!");
        JOptionPane.showMessageDialog(null, "Excel File regenerated successfully\n" + filename);
        loggerArea.append(String.valueOf("Excel File regenerated successfully " + "\n"));
    }
}
