package com.coc.dashboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.coc.dashboard.dto.TopMember;
import com.coc.dashboard.entity.CareCategoryDetail;

public interface CareCategoryDetailsRepository extends JpaRepository<CareCategoryDetail, Long> {

	@Query("SELECT NEW com.coc.dashboard.dto.TopMember(c.memberUid, sum(c.pricePM)) from CareCategoryDetail c "
			+ "where (:lob IS NULL OR c.lob = :lob) " + "AND (:state IS NULL OR c.state = :state) "
			+ "AND ((:startMonth is NULL AND :endMonth is NULL) "
			+ "OR (:startMonth IS NOT NULL AND :endMonth IS NOT NULL AND c.months between :startMonth AND :endMonth) "
			+ "OR (:startMonth IS NULL AND c.months = :endMonth)) " + "AND c.pricePM > 0 "
			+ "AND (:category IS NULL OR c.category = :category) " + "group by c.memberUid "
			+ "order by sum(c.pricePM) desc limit 10")
	List<TopMember> findTopMembersByCategory(String lob, String state, String startMonth, String endMonth,
			String category);

	@Query("SELECT count(c.category) from CareCategoryDetail c " + "where (:lob IS NULL OR c.lob = :lob) "
			+ "AND (:state IS NULL OR c.state = :state) " + "AND ((:startMonth is NULL AND :endMonth is NULL) "
			+ "OR (:startMonth IS NOT NULL AND :endMonth IS NOT NULL AND c.months between :startMonth AND :endMonth) "
			+ "OR (:startMonth IS NULL AND c.months = :endMonth)) "
			+ "AND (:category IS NULL OR c.category = :category) " + "AND c.pricePM > 0 ")
	Long findMembersCount(String lob, String state, String startMonth, String endMonth, String category);
}
