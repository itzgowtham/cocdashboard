package com.coc.dashboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.coc.dashboard.dto.TopMember;
import com.coc.dashboard.entity.ServiceAreaRegionDetail;

public interface ServiceAreaRegionDetailsRepository extends JpaRepository<ServiceAreaRegionDetail, Long> {

	@Query("SELECT NEW com.coc.dashboard.dto.TopMember(s.memberUid, sum(s.pricePM)) from ServiceAreaRegionDetail s "
			+ "where (:lob IS NULL OR s.lob = :lob) " + "AND (:state IS NULL OR s.state = :state) "
			+ "AND ((:startMonth is NULL AND :endMonth is NULL) "
			+ "OR (:startMonth IS NOT NULL AND :endMonth IS NOT NULL AND s.months between :startMonth AND :endMonth) "
			+ "OR (:startMonth IS NULL AND s.months = :endMonth)) " + "AND s.pricePM > 0 " + "group by s.memberUid "
			+ "order by sum(s.pricePM) desc limit 10")
	List<TopMember> findTopMembers(String lob, String state, String startMonth, String endMonth);
}
