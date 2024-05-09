package com.coc.dashboard.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "PMPM_CATEGORY")
@Data
public class PMPM_Category {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
	
    @Column(name = "months")
    private String months;

    @Column(name = "providername")
    private String providerName;
    
    @Column(name = "category")
    private String category;
    
    @Column(name = "member_count")
    private Integer memberCount;
    
    @Column(name = "state")
    private String state;

    @Column(name = "ip_indicator")
    private String ipIndicator;
    
    @Column(name = "lob")
    private String lob;

    @Column(name = "pricepm")
    private Double pricePM;
    
}