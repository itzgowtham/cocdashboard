package com.coc.dashboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coc.dashboard.entity.TargetPMPM;

@Repository
public interface TargetPMPMRepository extends JpaRepository<TargetPMPM, String>   {

}
