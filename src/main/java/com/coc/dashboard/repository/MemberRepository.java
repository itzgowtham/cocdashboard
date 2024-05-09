package com.coc.dashboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.coc.dashboard.dto.MemberViewDTO;
import com.coc.dashboard.entity.MemberView;

@Repository
public interface MemberRepository extends JpaRepository<MemberView, Long> {

	@Query("SELECT NEW com.coc.dashboard.dto.MemberViewDTO(SUM(m.activeMembers), m.months) FROM MemberView m WHERE "
			+ "m.lob = ?1 AND m.statecode = ?2 group by m.months order by m.months desc")
	List<MemberViewDTO> findSummaryByLobAndState(String lob, String statecode);

	@Query("SELECT NEW com.coc.dashboard.dto.MemberViewDTO(SUM(m.activeMembers), m.months) FROM MemberView m WHERE "
			+ "m.lob = ?1 group by m.months order by m.months desc")
	List<MemberViewDTO> findSummaryByLob(String lob);

	@Query("SELECT NEW com.coc.dashboard.dto.MemberViewDTO(SUM(m.activeMembers), m.months) FROM MemberView m WHERE "
			+ "m.statecode = ?1 group by m.months order by m.months desc")
	List<MemberViewDTO> findSummaryByState(String statecode);

	@Query("SELECT NEW com.coc.dashboard.dto.MemberViewDTO(SUM(m.activeMembers), m.months) FROM MemberView m "
			+ "group by m.months order by m.months desc")
	List<MemberViewDTO> findSummaryAllRecords();

	@Query("SELECT NEW com.coc.dashboard.dto.MemberViewDTO(sum(m.activeMembers), m.months, m.statecode) "
			+ "from MemberView m where m.lob = ?1 AND m.statecode = ?2 group by m.months,m.statecode")
	List<MemberViewDTO> findServiceRegionByLobAndState(String lob, String state);

	@Query("SELECT NEW com.coc.dashboard.dto.MemberViewDTO(sum(m.activeMembers), m.months, m.statecode) "
			+ "from MemberView m where m.lob = ?1 group by m.months,m.statecode")
	List<MemberViewDTO> findServiceRegionByLob(String lob);

	@Query("SELECT NEW com.coc.dashboard.dto.MemberViewDTO(sum(m.activeMembers), m.months, m.statecode) "
			+ "from MemberView m where m.statecode = ?1 group by m.months,m.statecode")
	List<MemberViewDTO> findServiceRegionByState(String state);
	
	@Query("SELECT NEW com.coc.dashboard.dto.MemberViewDTO(sum(m.activeMembers), m.months, m.statecode) "
			+ "from MemberView m group by m.months,m.statecode")
	List<MemberViewDTO> findAllByServiceRegion();
}
