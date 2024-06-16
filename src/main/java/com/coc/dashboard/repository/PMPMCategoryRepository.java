package com.coc.dashboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.coc.dashboard.dto.PMPMDTO;
import com.coc.dashboard.dto.TopProvider;
import com.coc.dashboard.entity.PMPM_Category;

@Repository
public interface PMPMCategoryRepository extends JpaRepository<PMPM_Category, Long> {

	@Query("SELECT NEW com.coc.dashboard.dto.PMPMDTO(SUM(p.pricePM), SUM(p.memberCount), p.months, p.category, "
			+ "p.ipIndicator) FROM PMPM_Category p " + "where (:lob IS NULL OR p.lob = :lob) "
			+ "AND (:state IS NULL OR p.state = :state) "
			+ "group by p.months,p.category,p.ipIndicator order by p.months desc")
	List<PMPMDTO> findCareCategory(String lob, String state);

	@Query("SELECT NEW com.coc.dashboard.dto.TopProvider(p.providerName, sum(p.pricePM)) from PMPM_Category p "
			+ "where (:lob IS NULL OR p.lob = :lob) " + "AND (:state IS NULL OR p.state = :state) "
			+ "AND ((:startMonth is NULL AND :endMonth is NULL) "
			+ "OR (:startMonth IS NOT NULL AND :endMonth IS NOT NULL AND p.months between :startMonth AND :endMonth) "
			+ "OR (:startMonth IS NULL AND p.months = :endMonth)) " + "AND p.pricePM > 0 "
			+ "AND (:category IS NULL OR p.category = :category) " + "group by p.providerName "
			+ "order by sum(p.pricePM) desc limit 10")
	List<TopProvider> findTopProvidersByCategory(String lob, String state, String startMonth, String endMonth,
			String category);

	@Query("SELECT distinct(category) from PMPM_Category where category is not null")
	List<String> findDistinctCategories();
	
	@Query("SELECT count(p.providerName) from PMPM_Category p "
			+ "where (:lob IS NULL OR p.lob = :lob) " + "AND (:state IS NULL OR p.state = :state) "
			+ "AND ((:startMonth is NULL AND :endMonth is NULL) "
			+ "OR (:startMonth IS NOT NULL AND :endMonth IS NOT NULL AND p.months between :startMonth AND :endMonth) "
			+ "OR (:startMonth IS NULL AND p.months = :endMonth)) " + "AND p.pricePM > 0 "
			+ "AND (:category IS NULL OR p.category = :category) ")
	Long findProvidersCount(String lob, String state, String startMonth, String endMonth, String category);
}
