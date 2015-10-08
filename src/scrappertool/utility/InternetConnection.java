/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scrappertool.utility;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author GLB-130
 */
public class InternetConnection {
    public static int testConnection(){
		try {
			try {
				URL url = new URL("http://www.google.com");
				System.out.println(url.getHost());
				HttpURLConnection con = (HttpURLConnection) url
						.openConnection();
				con.connect();
				if (con.getResponseCode() == 200){
					System.out.println("Connection established!!");
                    return 200;
				}
			} catch (Exception exception) {
				System.out.println("No Connection");
                return 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
            return 0;
		}
        return 0;
	}
}
