package com.coc.dashboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coc.dashboard.entity.Forecast_PMPM;

@Repository
public interface ForecastPMPMRepository extends JpaRepository<Forecast_PMPM, Double> {

	List<Forecast_PMPM> findByLobAndStatecode(String lob, String statecode);
}
