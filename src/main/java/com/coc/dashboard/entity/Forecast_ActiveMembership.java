package com.coc.dashboard.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Table(name = "FORECAST_ACTIVEMEMBERSHIP_REPORTING")
@Data
public class Forecast_ActiveMembership {
	
    @Column(name = "payergroupcode")
    private String lob;

    @Column(name = "monthuid")
    private String months;
    
    @Column(name = "statecode")
    private String statecode;
    
    @Column(name = "activemembership")
    private Long activemembership;
    
    @Id
    @Column(name = "activemembership_forecast")
    private Long activemembership_forecast;
    
    @Column(name = "activemembership_forecast_lower")
    private Long activemembership_forecast_lower;
    
    @Column(name = "activemembership_forecast_upper")
    private Long activemembership_forecast_upper;
    
    @Column(name = "activemembership_forecast_mae")
    private Long activemembership_forecast_mae;

    @Column(name = "model")
    private String model;
    
    @Transient
    private Double confidenceInterval;
}