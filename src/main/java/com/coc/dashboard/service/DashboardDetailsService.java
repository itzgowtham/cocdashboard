package com.coc.dashboard.service;

import java.util.Map;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.coc.dashboard.exception.MyCustomException;
import com.coc.dashboard.model.PMPMObject;
import com.coc.dashboard.util.DateFormat;

import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class DashboardDetailsService {

	private DateFormat dateFormat;
	private DataAccessService dataAccessService;

	private void validatePMPMObject(PMPMObject pmpmObject) {
		pmpmObject.setLob(StringUtils.trimToNull(pmpmObject.getLob()));
		pmpmObject.setState(StringUtils.trimToNull(pmpmObject.getState()));
		pmpmObject.setStartMonth(StringUtils.isNotBlank(pmpmObject.getStartMonth())
				? dateFormat.convertStringtoIntegerDateFormat(pmpmObject.getStartMonth())
				: null);
		pmpmObject.setEndMonth(StringUtils.isNotBlank(pmpmObject.getEndMonth())
				? dateFormat.convertStringtoIntegerDateFormat(pmpmObject.getEndMonth())
				: null);
		pmpmObject.setSpeciality(StringUtils.trimToNull(pmpmObject.getSpeciality()));
		pmpmObject.setProviderName(StringUtils.trimToNull(pmpmObject.getProviderName()));
		pmpmObject.setCareCategory(StringUtils.trimToNull(pmpmObject.getCareCategory()));
	}

	public Map<String, Object> careCategoryDetails(PMPMObject pmpmObject) throws MyCustomException {
		log.info("Inside DashboardDetailsService.careCategoryDetails() method");
		validatePMPMObject(pmpmObject);
		Map<String, Object> mapData = dataAccessService.careCategoryDetails(pmpmObject.getLob(), pmpmObject.getState(),
				pmpmObject.getStartMonth(), pmpmObject.getEndMonth(), pmpmObject.getCareCategory());
		log.info("Exiting DashboardService.careCategoryDetails() method");
		return mapData;
	}

	public Map<String, Object> serviceRegionDetails(PMPMObject pmpmObject) throws MyCustomException {
		log.info("Inside DashboardDetailsService.serviceRegionDetails() method");
		validatePMPMObject(pmpmObject);
		Map<String, Object> mapData = dataAccessService.serviceRegionDetails(pmpmObject.getLob(), pmpmObject.getState(),
				pmpmObject.getStartMonth(), pmpmObject.getEndMonth());
		log.info("Exiting DashboardService.serviceRegionDetails() method");
		return mapData;
	}

	public Map<String, Object> providerSpecialityDetails(PMPMObject pmpmObject) throws MyCustomException {
		log.info("Inside DashboardDetailsService.providerSpecialityDetails() method");
		validatePMPMObject(pmpmObject);
		Map<String, Object> mapData = dataAccessService.providerSpecialityDetails(pmpmObject.getLob(),
				pmpmObject.getState(), pmpmObject.getStartMonth(), pmpmObject.getEndMonth(),
				pmpmObject.getSpeciality());
		log.info("Exiting DashboardService.providerSpecialityDetails() method");
		return mapData;
	}

	public Map<String, Object> careProviderDetails(PMPMObject pmpmObject) throws MyCustomException {
		log.info("Inside DashboardDetailsService.careProviderDetails() method");
		validatePMPMObject(pmpmObject);
		Map<String, Object> mapData = dataAccessService.careProviderDetails(pmpmObject.getLob(), pmpmObject.getState(),
				pmpmObject.getStartMonth(), pmpmObject.getEndMonth(), pmpmObject.getSpeciality(),
				pmpmObject.getProviderName());
		log.info("Exiting DashboardService.careProviderDetails() method");
		return mapData;
	}

	public Map<String, Object> pcpGroupDetails(PMPMObject pmpmObject) throws MyCustomException {
		log.info("Inside DashboardDetailsService.pcpGroupDetails() method");
		validatePMPMObject(pmpmObject);
		Map<String, Object> mapData = dataAccessService.pcpGroupDetails(pmpmObject.getLob(), pmpmObject.getState(),
				pmpmObject.getStartMonth(), pmpmObject.getEndMonth(), pmpmObject.getSpeciality(),
				pmpmObject.getProviderName());
		log.info("Exiting DashboardService.pcpGroupDetails() method");
		return mapData;
	}

}
