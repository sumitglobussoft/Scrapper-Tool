/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scrappertool.entity;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author GLB-130
 */
public class DateCount{

    private int count;
    
    private Date launchDate;

    public Date getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(Date launchDate) {
        this.launchDate = launchDate;
    }

   

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
