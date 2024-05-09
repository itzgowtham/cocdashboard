package com.coc.dashboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.coc.dashboard.dto.PMPMDTO;
import com.coc.dashboard.entity.PMPM_PCP;

@Repository
public interface PMPMPcpRepository extends JpaRepository<PMPM_PCP, Long> {

	@Query("SELECT NEW com.coc.dashboard.dto.PMPMDTO(p.months, p.providerName, sum(p.pricePM), SUM(p.memberCount), "
			+ "p.ipIndicator) from PMPM_PCP p where p.lob = ?1 and p.state = ?2 and p.pricePM Not IN(0) "
			+ "group by p.months,p.providerName,p.ipIndicator order by p.months desc")
	List<PMPMDTO> findPcpGroupByLobAndState(String lob, String state);

	@Query("SELECT NEW com.coc.dashboard.dto.PMPMDTO(p.months, p.providerName, sum(p.pricePM), SUM(p.memberCount), "
			+ "p.ipIndicator) from PMPM_PCP p where p.lob = ?1 and p.pricePM Not IN(0) "
			+ "group by p.months,p.providerName,p.ipIndicator order by p.months desc")
	List<PMPMDTO> findPcpGroupByLob(String lob);

	@Query("SELECT NEW com.coc.dashboard.dto.PMPMDTO(p.months, p.providerName, sum(p.pricePM), SUM(p.memberCount), "
			+ "p.ipIndicator) from PMPM_PCP p where p.state = ?1 and p.pricePM Not IN(0) "
			+ "group by p.months,p.providerName,p.ipIndicator order by p.months desc")
	List<PMPMDTO> findPcpGroupByState(String state);

	@Query("SELECT NEW com.coc.dashboard.dto.PMPMDTO(p.months, p.providerName, sum(p.pricePM), SUM(p.memberCount), "
			+ "p.ipIndicator) from PMPM_PCP p where p.pricePM Not IN(0) "
			+ "group by p.months,p.providerName,p.ipIndicator order by p.months desc")
	List<PMPMDTO> findAllPcpGroup();
}
