package com.coc.dashboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.coc.dashboard.dto.PMPMDTO;
import com.coc.dashboard.entity.PMPM;

@Repository
public interface PMPMRepository extends JpaRepository<PMPM, Long> {

	@Query("SELECT DISTINCT p.months FROM PMPM p order by p.months desc")
	List<String> findDistinctMonths();

	@Query("SELECT DISTINCT p.lob FROM PMPM p order by p.lob")
	List<String> findDistinctLob();

	@Query("SELECT DISTINCT p.state FROM PMPM p order by p.state")
	List<String> findDistinctState();

	@Query("SELECT NEW com.coc.dashboard.dto.PMPMDTO(SUM(p.pricePM), p.months) FROM PMPM p WHERE "
			+ "p.lob = ?1 AND p.state = ?2 group by p.months order by p.months desc")
	List<PMPMDTO> findSummaryByLobAndState(String lob, String state);

	@Query("SELECT NEW com.coc.dashboard.dto.PMPMDTO(SUM(p.pricePM), p.months) FROM PMPM p WHERE "
			+ "p.lob = ?1 group by p.months order by p.months desc")
	List<PMPMDTO> findSummaryByLob(String lob);

	@Query("SELECT NEW com.coc.dashboard.dto.PMPMDTO(SUM(p.pricePM), p.months) FROM PMPM p WHERE "
			+ "p.state = ?1 group by p.months order by p.months desc")
	List<PMPMDTO> findSummaryByState(String state);

	@Query("SELECT NEW com.coc.dashboard.dto.PMPMDTO(SUM(p.pricePM), p.months) FROM PMPM p "
			+ "group by p.months order by p.months desc")
	List<PMPMDTO> findAllSummaryRecords();

	@Query("SELECT NEW com.coc.dashboard.dto.PMPMDTO(SUM(p.pricePM), SUM(p.memberCount), p.months, p.speciality, "
			+ "p.ipIndicator) FROM PMPM p WHERE p.lob = ?1 AND p.state = ?2 group by p.months,p.speciality,p.ipIndicator "
			+ "order by p.months desc")
	List<PMPMDTO> findProviderSpecialityByLobAndState(String lob, String state);

	@Query("SELECT NEW com.coc.dashboard.dto.PMPMDTO(SUM(p.pricePM), SUM(p.memberCount), p.months, p.speciality, "
			+ "p.ipIndicator) FROM PMPM p WHERE p.lob = ?1 group by p.months,p.speciality,p.ipIndicator "
			+ "order by p.months desc")
	List<PMPMDTO> findProviderSpecialityByLob(String lob);

	@Query("SELECT NEW com.coc.dashboard.dto.PMPMDTO(SUM(p.pricePM), SUM(p.memberCount), p.months, p.speciality, "
			+ "p.ipIndicator) FROM PMPM p WHERE p.state = ?1 group by p.months,p.speciality,p.ipIndicator "
			+ "order by p.months desc")
	List<PMPMDTO> findProviderSpecialityByState(String state);

	@Query("SELECT NEW com.coc.dashboard.dto.PMPMDTO(SUM(p.pricePM), SUM(p.memberCount), p.months, p.speciality, "
			+ "p.ipIndicator) FROM PMPM p group by p.months,p.speciality,p.ipIndicator order by p.months desc")
	List<PMPMDTO> findAllProviderSpeciality();

	@Query("SELECT NEW com.coc.dashboard.dto.PMPMDTO(p.months, sum(p.pricePM), SUM(p.memberCount), p.state, p.ipIndicator)"
			+ " from PMPM p where p.lob = ?1 and p.state = ?2 group by p.months,p.state,p.ipIndicator")
	List<PMPMDTO> findServiceRegionByLobAndState(String lob, String state);

	@Query("SELECT NEW com.coc.dashboard.dto.PMPMDTO(p.months, sum(p.pricePM), SUM(p.memberCount), p.state, p.ipIndicator)"
			+ " from PMPM p where p.lob = ?1 group by p.months,p.state,p.ipIndicator")
	List<PMPMDTO> findServiceRegionByLob(String lob);

	@Query("SELECT NEW com.coc.dashboard.dto.PMPMDTO(p.months, sum(p.pricePM), SUM(p.memberCount), p.state, p.ipIndicator)"
			+ " from PMPM p where p.state = ?1 group by p.months,p.state,p.ipIndicator")
	List<PMPMDTO> findServiceRegionByState(String state);

	@Query("SELECT NEW com.coc.dashboard.dto.PMPMDTO(p.months, sum(p.pricePM), SUM(p.memberCount), p.state, p.ipIndicator)"
			+ " from PMPM p group by p.months,p.state,p.ipIndicator")
	List<PMPMDTO> findAllByServiceRegion();

	@Query("SELECT NEW com.coc.dashboard.dto.PMPMDTO(p.months, p.providerName, sum(p.pricePM), SUM(p.memberCount), "
			+ "p.ipIndicator) from PMPM p where p.lob = ?1 and p.state = ?2 and p.pricePM Not IN(0) "
			+ "group by p.months,p.providerName,p.ipIndicator order by p.months desc")
	List<PMPMDTO> findCareProvider(String lob, String state);

	@Query("SELECT NEW com.coc.dashboard.dto.PMPMDTO(p.months, p.providerName, sum(p.pricePM), SUM(p.memberCount), "
			+ "p.ipIndicator) from PMPM p where p.state = ?1 and p.pricePM Not IN(0) "
			+ "group by p.months,p.providerName,p.ipIndicator order by p.months desc")
	List<PMPMDTO> findCareProvider(String state);

}
