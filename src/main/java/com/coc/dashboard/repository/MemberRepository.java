package com.coc.dashboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.coc.dashboard.dto.MemberViewDTO;
import com.coc.dashboard.entity.MemberView;

@Repository
public interface MemberRepository extends JpaRepository<MemberView, Long> {
	
	@Query("SELECT NEW com.coc.dashboard.dto.MemberViewDTO(SUM(m.activeMembers), m.months) FROM MemberView m "
			+ "where (:lob IS NULL OR m.lob = :lob) " + "AND (:statecode IS NULL OR m.statecode = :statecode) "
			+ "group by m.months order by m.months desc")
	List<MemberViewDTO> findSummaryRecords(String lob, String statecode);
	
	@Query("SELECT NEW com.coc.dashboard.dto.MemberViewDTO(sum(m.activeMembers), m.months, m.statecode) "
			+ "from MemberView m "
			+ "where (:lob IS NULL OR m.lob = :lob) " + "AND (:statecode IS NULL OR m.statecode = :statecode) "
			+ "group by m.months,m.statecode")
	List<MemberViewDTO> findServiceRegion(String lob, String statecode);
}
