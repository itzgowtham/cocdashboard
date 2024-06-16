package com.coc.dashboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coc.dashboard.entity.Forecast_ActiveMembership;

@Repository
public interface ForecastMemberRepository extends JpaRepository<Forecast_ActiveMembership, Long> {

	List<Forecast_ActiveMembership> findByLobAndStatecode(String lob, String statecode);
}
