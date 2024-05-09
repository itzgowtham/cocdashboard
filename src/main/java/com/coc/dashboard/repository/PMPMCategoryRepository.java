package com.coc.dashboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.coc.dashboard.dto.PMPMDTO;
import com.coc.dashboard.entity.PMPM;
import com.coc.dashboard.entity.PMPM_Category;

@Repository
public interface PMPMCategoryRepository extends JpaRepository<PMPM_Category, Long> {

	@Query("SELECT NEW com.coc.dashboard.dto.PMPMDTO(SUM(p.pricePM), SUM(p.memberCount), p.months, p.category, "
			+ "p.ipIndicator) FROM PMPM_Category p WHERE p.lob = ?1 AND p.state = ?2 "
			+ "group by p.months,p.category,p.ipIndicator order by p.months desc")
	List<PMPMDTO> findCareCategoryByLobAndState(String lob, String state);

	@Query("SELECT NEW com.coc.dashboard.dto.PMPMDTO(SUM(p.pricePM), SUM(p.memberCount), p.months, p.category, "
			+ "p.ipIndicator) FROM PMPM_Category p WHERE p.lob = ?1 "
			+ "group by p.months,p.category,p.ipIndicator order by p.months desc")
	List<PMPMDTO> findCareCategoryByLob(String lob);

	@Query("SELECT NEW com.coc.dashboard.dto.PMPMDTO(SUM(p.pricePM), SUM(p.memberCount), p.months, p.category, "
			+ "p.ipIndicator) FROM PMPM_Category p WHERE p.state = ?1 "
			+ "group by p.months,p.category,p.ipIndicator order by p.months desc")
	List<PMPMDTO> findCareCategoryByState(String state);

	@Query("SELECT NEW com.coc.dashboard.dto.PMPMDTO(SUM(p.pricePM), SUM(p.memberCount), p.months, p.category, "
			+ "p.ipIndicator) FROM PMPM_Category p group by p.months,p.category,p.ipIndicator order by p.months desc")
	List<PMPMDTO> findAllCareCategory();
}
