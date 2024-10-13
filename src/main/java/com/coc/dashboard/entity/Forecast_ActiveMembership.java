package com.coc.dashboard.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "FORECAST_ACTIVEMEMBERSHIP_REPORTING")
@Data
public class Forecast_ActiveMembership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
	
    @Column(name = "payergroupcode")
    private String lob;

    @Column(name = "monthuid")
    private String months;
    
    @Column(name = "statecode")
    private String statecode;
    
    @Column(name = "activemembership")
    private Long activemembership;

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