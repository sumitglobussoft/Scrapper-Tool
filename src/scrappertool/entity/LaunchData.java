/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scrappertool.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author GLB-130
 */
@Entity
@Table(name = "launch_data", catalog = "scrappertool", schema = "")
public class LaunchData implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "SITE")
    private String site;
    @Column(name = "VENDOR")
    private String vendor;
    @Column(name = "PRODUCT")
    private String product;
    @Column(name = "LAUNCH_DATE")
    private String launchDate;
    @Column(name = "LAUNCH_TIME")
    private String launchTime;
    @Column(name = "FRONTEND_PRICE")
    private String frontendPrice;
    @Column(name = "COMMISSION")
    private String commission;
    @Column(name = "JV_PAGE")
    private String jvPage;
    @Column(name = "AFFILIATE_NETWORK")
    private String affiliateNetwork;
    @Column(name = "NICHE")
    private String niche;
    @Column(name = "PRE_LAUNCH_DATE")
    private String preLaunchDate;
    @Lob
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "PROMOTION_TYPE")
    private String promotionType;
    @Column(name = "TICKET")
    private String ticket;
    @Column(name = "CLICKS")
    private String clicks;

    public LaunchData() {
    }

    public LaunchData(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }


    public String getLaunchTime() {
        return launchTime;
    }

    public void setLaunchTime(String launchTime) {
        this.launchTime = launchTime;
    }

    public String getFrontendPrice() {
        return frontendPrice;
    }

    public void setFrontendPrice(String frontendPrice) {
        this.frontendPrice = frontendPrice;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getJvPage() {
        return jvPage;
    }

    public void setJvPage(String jvPage) {
        this.jvPage = jvPage;
    }

    public String getAffiliateNetwork() {
        return affiliateNetwork;
    }

    public void setAffiliateNetwork(String affiliateNetwork) {
        this.affiliateNetwork = affiliateNetwork;
    }

    public String getNiche() {
        return niche;
    }

    public void setNiche(String niche) {
        this.niche = niche;
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(String promotionType) {
        this.promotionType = promotionType;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getClicks() {
        return clicks;
    }

    public void setClicks(String clicks) {
        this.clicks = clicks;
    }

    public String getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(String launchDate) {
        this.launchDate = launchDate;
    }

    public String getPreLaunchDate() {
        return preLaunchDate;
    }

    public void setPreLaunchDate(String preLaunchDate) {
        this.preLaunchDate = preLaunchDate;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LaunchData)) {
            return false;
        }
        LaunchData other = (LaunchData) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "scrappertool.entity.LaunchData[ id=" + id + " ]";
    }
    
}
