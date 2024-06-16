package com.coc.dashboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.coc.dashboard.dto.PMPMDTO;
import com.coc.dashboard.dto.TopProvider;
import com.coc.dashboard.dto.TopSpeciality;
import com.coc.dashboard.entity.PMPM_PCP;

@Repository
public interface PMPMPcpRepository extends JpaRepository<PMPM_PCP, Long> {

	@Query("SELECT NEW com.coc.dashboard.dto.PMPMDTO(p.months, p.providerName, sum(p.pricePM), SUM(p.memberCount), "
			+ "p.ipIndicator) from PMPM_PCP p " + "where (:lob IS NULL OR p.lob = :lob) "
			+ "AND (:state IS NULL OR p.state = :state) " + "and p.pricePM Not IN(0) "
			+ "group by p.months,p.providerName,p.ipIndicator order by p.months desc")
	List<PMPMDTO> findPcpGroup(String lob, String state);

	@Query("SELECT NEW com.coc.dashboard.dto.TopSpeciality(p.speciality, sum(p.pricePM)) from PMPM_PCP p "
			+ "where (:lob IS NULL OR p.lob = :lob) " + "AND (:state IS NULL OR p.state = :state) "
			+ "AND ((:startMonth is NULL AND :endMonth is NULL) "
			+ "OR (:startMonth IS NOT NULL AND :endMonth IS NOT NULL AND p.months between :startMonth AND :endMonth) "
			+ "OR (:startMonth IS NULL AND p.months = :endMonth)) " + "AND p.pricePM > 0 " + "group by p.speciality "
			+ "order by sum(p.pricePM) desc limit 10")
	List<TopSpeciality> findTopSpeciality(String lob, String state, String startMonth, String endMonth);

	@Query("SELECT NEW com.coc.dashboard.dto.TopProvider(p.providerName, sum(p.pricePM)) from PMPM_PCP p "
			+ "where (:lob IS NULL OR p.lob = :lob) " + "AND (:state IS NULL OR p.state = :state) "
			+ "AND ((:startMonth is NULL AND :endMonth is NULL) "
			+ "OR (:startMonth IS NOT NULL AND :endMonth IS NOT NULL AND p.months between :startMonth AND :endMonth) "
			+ "OR (:startMonth IS NULL AND p.months = :endMonth)) " + "AND p.pricePM > 0 "
			+ "AND (:speciality IS NULL OR p.speciality = :speciality) " + "group by p.providerName "
			+ "order by sum(p.pricePM) desc limit 10")
	List<TopProvider> findTopProvidersBySpeciality(String lob, String state, String startMonth, String endMonth,
			String speciality);
	
	@Query("SELECT distinct(speciality) from PMPM_PCP where speciality is not null")
	List<String> findDistinctSpecialities();
}
