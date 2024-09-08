package com.coc.dashboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.coc.dashboard.dto.TopMember;
import com.coc.dashboard.dto.TopMemberProvider;
import com.coc.dashboard.dto.TopMemberSpeciality;
import com.coc.dashboard.dto.TopProvider;
import com.coc.dashboard.dto.TopSpeciality;
import com.coc.dashboard.entity.ProviderSpecialityDetail;

public interface ProviderSpecialityDetailsRepository extends JpaRepository<ProviderSpecialityDetail, Long> {

	@Query("SELECT NEW com.coc.dashboard.dto.TopMember(p.memberUid, sum(p.pricePM)) from ProviderSpecialityDetail p "
			+ "where (:lob IS NULL OR p.lob = :lob) " + "AND (:state IS NULL OR p.state = :state) "
			+ "AND ((:startMonth is NULL AND :endMonth is NULL) "
			+ "OR (:startMonth IS NOT NULL AND :endMonth IS NOT NULL AND p.months between :startMonth AND :endMonth) "
			+ "OR (:startMonth IS NULL AND p.months = :endMonth)) " + "AND p.pricePM > 0 " + "group by p.memberUid "
			+ "order by sum(p.pricePM) desc limit 10")
	List<TopMember> findTopMembers(String lob, String state, String startMonth, String endMonth);

	@Query("SELECT NEW com.coc.dashboard.dto.TopMemberSpeciality(p.speciality, count(p.memberUid)) from "
			+ "ProviderSpecialityDetail p " + "where (:lob IS NULL OR p.lob = :lob) "
			+ "AND (:state IS NULL OR p.state = :state) " + "AND ((:startMonth is NULL AND :endMonth is NULL) "
			+ "OR (:startMonth IS NOT NULL AND :endMonth IS NOT NULL AND p.months between :startMonth AND :endMonth) "
			+ "OR (:startMonth IS NULL AND p.months = :endMonth)) " + "AND p.pricePM > 0 "
			+ "AND p.speciality is not null " + "group by p.speciality " + "order by count(p.memberUid) desc limit 10")
	List<TopMemberSpeciality> findTopProviderMembers(String lob, String state, String startMonth, String endMonth);

	@Query("SELECT NEW com.coc.dashboard.dto.TopMember(p.memberUid, sum(p.pricePM)) from ProviderSpecialityDetail p "
			+ "where (:lob IS NULL OR p.lob = :lob) " + "AND (:state IS NULL OR p.state = :state) "
			+ "AND ((:startMonth is NULL AND :endMonth is NULL) "
			+ "OR (:startMonth IS NOT NULL AND :endMonth IS NOT NULL AND p.months between :startMonth AND :endMonth) "
			+ "OR (:startMonth IS NULL AND p.months = :endMonth)) "
			+ "AND (:speciality IS NULL OR p.speciality = :speciality) " + "AND p.pricePM > 0 "
			+ "group by p.memberUid " + "order by sum(p.pricePM) desc limit 10")
	List<TopMember> findTopMembersBySpeciality(String lob, String state, String startMonth, String endMonth,
			String speciality);

	@Query("SELECT NEW com.coc.dashboard.dto.TopMemberProvider(p.providerName, count(p.memberUid)) from "
			+ "ProviderSpecialityDetail p " + "where (:lob IS NULL OR p.lob = :lob) "
			+ "AND (:state IS NULL OR p.state = :state) " + "AND ((:startMonth is NULL AND :endMonth is NULL) "
			+ "OR (:startMonth IS NOT NULL AND :endMonth IS NOT NULL AND p.months between :startMonth AND :endMonth) "
			+ "OR (:startMonth IS NULL AND p.months = :endMonth)) " + "AND p.pricePM > 0 " + "group by p.providerName "
			+ "order by count(p.memberUid) desc limit 10")
	List<TopMemberProvider> findTopMembersByProvider(String lob, String state, String startMonth, String endMonth);

	@Query("SELECT NEW com.coc.dashboard.dto.TopMember(p.memberUid, sum(p.pricePM)) from ProviderSpecialityDetail p "
			+ "where (:lob IS NULL OR p.lob = :lob) " + "AND (:state IS NULL OR p.state = :state) "
			+ "AND ((:startMonth is NULL AND :endMonth is NULL) "
			+ "OR (:startMonth IS NOT NULL AND :endMonth IS NOT NULL AND p.months between :startMonth AND :endMonth) "
			+ "OR (:startMonth IS NULL AND p.months = :endMonth)) "
			+ "AND (:providerName IS NULL OR p.providerName = :providerName) " + "AND p.pricePM > 0 "
			+ "group by p.memberUid " + "order by sum(p.pricePM) desc limit 10")
	List<TopMember> findTopMembersByProvider(String lob, String state, String startMonth, String endMonth,
			String providerName);

	@Query("SELECT count(p.providerName) from ProviderSpecialityDetail p " + "where (:lob IS NULL OR p.lob = :lob) "
			+ "AND (:state IS NULL OR p.state = :state) " + "AND ((:startMonth is NULL AND :endMonth is NULL) "
			+ "OR (:startMonth IS NOT NULL AND :endMonth IS NOT NULL AND p.months between :startMonth AND :endMonth) "
			+ "OR (:startMonth IS NULL AND p.months = :endMonth)) " + "AND p.pricePM > 0 "
			+ "AND (:speciality IS NULL OR p.speciality = :speciality) ")
	Long findMembersCount(String lob, String state, String startMonth, String endMonth, String speciality);
}
