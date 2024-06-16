package com.coc.dashboard.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "CARE_CATEGORY_DETAIL")
@Data
public class CareCategoryDetail {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
	
    @Column(name = "months")
    private String months;
    
    @Column(name = "category")
    private String category;
    
    @Column(name = "memberuid")
    private Long memberUid;
    
    @Column(name = "state")
    private String state;
    
    @Column(name = "lob")
    private String lob;

    @Column(name = "pricepm")
    private Double pricePM;
    
}