package com.coc.dashboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.coc.dashboard.dto.TopMember;
import com.coc.dashboard.dto.TopMemberProvider;
import com.coc.dashboard.dto.TopMemberSpeciality;
import com.coc.dashboard.dto.TopSpeciality;
import com.coc.dashboard.entity.PcpDetail;

public interface PcpGroupDetailsRepository extends JpaRepository<PcpDetail, Long> {
	
	@Query("SELECT NEW com.coc.dashboard.dto.TopMemberProvider(p.providerName, count(p.memberUid)) from PcpDetail p "
			+ "where (:lob IS NULL OR p.lob = :lob) " + "AND (:state IS NULL OR p.state = :state) "
			+ "AND ((:startMonth is NULL AND :endMonth is NULL) "
			+ "OR (:startMonth IS NOT NULL AND :endMonth IS NOT NULL AND p.months between :startMonth AND :endMonth) "
			+ "OR (:startMonth IS NULL AND p.months = :endMonth)) " + "AND p.pricePM > 0 " + "group by p.providerName "
			+ "order by count(p.memberUid) desc limit 10")
	List<TopMemberProvider> findTopMembers(String lob, String state, String startMonth, String endMonth);

	@Query("SELECT NEW com.coc.dashboard.dto.TopMember(p.memberUid, sum(p.pricePM)) from PcpDetail p "
			+ "where (:lob IS NULL OR p.lob = :lob) " + "AND (:state IS NULL OR p.state = :state) "
			+ "AND ((:startMonth is NULL AND :endMonth is NULL) "
			+ "OR (:startMonth IS NOT NULL AND :endMonth IS NOT NULL AND p.months between :startMonth AND :endMonth) "
			+ "OR (:startMonth IS NULL AND p.months = :endMonth)) " + "AND p.pricePM > 0 "
			+ "AND (:providerName IS NULL OR p.providerName = :providerName) " + "group by p.memberUid "
			+ "order by sum(p.pricePM) desc limit 10")
	List<TopMember> findTopMembersBySpeciality(String lob, String state, String startMonth, String endMonth,
			String providerName);
}
