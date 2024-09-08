package com.coc.dashboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.coc.dashboard.dto.PMPMDTO;
import com.coc.dashboard.dto.TopProvider;
import com.coc.dashboard.dto.TopSpeciality;
import com.coc.dashboard.entity.PMPM;

@Repository
public interface PMPMRepository extends JpaRepository<PMPM, Long> {

	@Query("SELECT DISTINCT p.months FROM PMPM p order by p.months desc")
	List<String> findDistinctMonths();

	@Query("SELECT DISTINCT p.lob FROM PMPM p order by p.lob")
	List<String> findDistinctLob();

	@Query("SELECT DISTINCT p.state FROM PMPM p order by p.state")
	List<String> findDistinctState();

	@Query("SELECT distinct(speciality) from PMPM where speciality is not null")
	List<String> findDistinctSpecialities();
	
	@Query("SELECT distinct(p.providerName) from PMPM p where p.providerName is not null "
			+ "AND (:lob IS NULL OR p.lob = :lob) " + "AND (:state IS NULL OR p.state = :state) "
			+ "AND p.months=:endMonth order by p.providerName limit 100")
	List<String> findDistinctProviders(String lob, String state, String endMonth);

	@Query("SELECT NEW com.coc.dashboard.dto.PMPMDTO(SUM(p.pricePM), p.months) FROM PMPM p "
			+ "where (:lob IS NULL OR p.lob = :lob) " + "AND (:state IS NULL OR p.state = :state) "
			+ "group by p.months order by p.months desc")
	List<PMPMDTO> findSummaryRecords(String lob, String state);

	@Query("SELECT NEW com.coc.dashboard.dto.PMPMDTO(SUM(p.pricePM), SUM(p.memberCount), p.months, p.speciality, "
			+ "p.ipIndicator) FROM PMPM p " + "where (:lob IS NULL OR p.lob = :lob) "
			+ "AND (:state IS NULL OR p.state = :state) "
			+ "group by p.months,p.speciality,p.ipIndicator order by p.months desc")
	List<PMPMDTO> findProviderSpeciality(String lob, String state);

	@Query("SELECT NEW com.coc.dashboard.dto.PMPMDTO(p.months, sum(p.pricePM), SUM(p.memberCount), p.state, "
			+ "p.ipIndicator) from PMPM p " + "where (:lob IS NULL OR p.lob = :lob) "
			+ "AND (:state IS NULL OR p.state = :state) " + "group by p.months,p.state,p.ipIndicator")
	List<PMPMDTO> findServiceRegion(String lob, String state);

	@Query("SELECT NEW com.coc.dashboard.dto.PMPMDTO(p.months, p.providerName, sum(p.pricePM), SUM(p.memberCount), "
			+ "p.ipIndicator) from PMPM p " + "where (:lob IS NULL OR p.lob = :lob) "
			+ "AND (:state IS NULL OR p.state = :state) " + "and p.pricePM Not IN(0) "
			+ "group by p.months,p.providerName,p.ipIndicator order by p.months desc")
	List<PMPMDTO> findCareProviders(String lob, String state);

	@Query("SELECT NEW com.coc.dashboard.dto.PMPMDTO(p.state, count(Distinct(p.providerName))) from PMPM p "
			+ "where (:lob IS NULL OR p.lob = :lob) " + "AND (:state IS NULL OR p.state = :state) "
			+ "AND ((:startMonth is NULL AND :endMonth is NULL) "
			+ "OR (:startMonth IS NOT NULL AND :endMonth IS NOT NULL AND p.months between :startMonth AND :endMonth) "
			+ "OR (:startMonth IS NULL AND p.months = :endMonth)) " + "group by p.state")
	List<PMPMDTO> findServiceRegionDetails(String lob, String state, String startMonth, String endMonth);

	@Query("SELECT NEW com.coc.dashboard.dto.TopSpeciality(p.speciality, sum(p.pricePM)) from PMPM p "
			+ "where (:lob IS NULL OR p.lob = :lob) " + "AND (:state IS NULL OR p.state = :state) "
			+ "AND ((:startMonth is NULL AND :endMonth is NULL) "
			+ "OR (:startMonth IS NOT NULL AND :endMonth IS NOT NULL AND p.months between :startMonth AND :endMonth) "
			+ "OR (:startMonth IS NULL AND p.months = :endMonth)) " + "AND p.pricePM > 0 "
			+ "AND p.speciality is not null " + "group by p.speciality " + "order by sum(p.pricePM) desc limit 10")
	List<TopSpeciality> findTopSpecialities(String lob, String state, String startMonth, String endMonth);

	@Query("SELECT NEW com.coc.dashboard.dto.TopProvider(p.providerName, sum(p.pricePM)) from PMPM p "
			+ "where (:lob IS NULL OR p.lob = :lob) " + "AND (:state IS NULL OR p.state = :state) "
			+ "AND ((:startMonth is NULL AND :endMonth is NULL) "
			+ "OR (:startMonth IS NOT NULL AND :endMonth IS NOT NULL AND p.months between :startMonth AND :endMonth) "
			+ "OR (:startMonth IS NULL AND p.months = :endMonth)) " + "AND p.pricePM > 0 " + "group by p.providerName "
			+ "order by sum(p.pricePM) desc limit 10")
	List<TopProvider> findTopProviders(String lob, String state, String startMonth, String endMonth);

	@Query("SELECT NEW com.coc.dashboard.dto.TopProvider(p.providerName, sum(p.pricePM)) from PMPM p "
			+ "where (:lob IS NULL OR p.lob = :lob) " + "AND (:state IS NULL OR p.state = :state) "
			+ "AND ((:startMonth is NULL AND :endMonth is NULL) "
			+ "OR (:startMonth IS NOT NULL AND :endMonth IS NOT NULL AND p.months between :startMonth AND :endMonth) "
			+ "OR (:startMonth IS NULL AND p.months = :endMonth)) " + "AND p.pricePM > 0 "
			+ "AND (:speciality IS NULL OR p.speciality = :speciality) " + "group by p.providerName "
			+ "order by sum(p.pricePM) desc limit 10")
	List<TopProvider> findTopProvidersBySpeciality(String lob, String state, String startMonth, String endMonth,
			String speciality);

}
