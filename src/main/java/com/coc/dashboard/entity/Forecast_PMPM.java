package com.coc.dashboard.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Table(name = "FORECAST_PMPM_REPORTING")
@Data
public class Forecast_PMPM {

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
    
    @Column(name = "pmpm")
    private Double pmpm;

    @Column(name = "pmpm_forecast")
    private Double pmpm_forecast;
    
    @Column(name = "pmpm_forecast_lower")
    private Double pmpm_forecast_lower;
    
    @Column(name = "pmpm_forecast_upper")
    private Double pmpm_forecast_upper;
    
    @Column(name = "pmpm_forecast_mae")
    private Double pmpm_forecast_mae;

    @Column(name = "model")
    private String model;
    
    @Transient
    private Double confidenceInterval;
    
}