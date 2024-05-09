package com.coc.dashboard.service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coc.dashboard.dto.MemberViewDTO;
import com.coc.dashboard.dto.PMPMDTO;
import com.coc.dashboard.dto.ResultData;
import com.coc.dashboard.entity.TargetPMPM;
import com.coc.dashboard.exception.MyCustomException;
import com.coc.dashboard.model.DataPair;
import com.coc.dashboard.util.DateFormat;

@Service
public class DataModificationService {

	@Autowired
	private DateFormat dateFormat;

	public Map<String, List<String>> distinctLobStateMonths(CompletableFuture<List<String>> lobFuture,
			CompletableFuture<List<String>> stateFuture, CompletableFuture<List<String>> monthFuture)
			throws MyCustomException {
		List<String> lobs = null;
		List<String> states = null;
		List<String> months = null;
		try {
			lobs = lobFuture.get();
			states = stateFuture.get();
			months = monthFuture.get();
		} catch (Exception e) {
			throw new MyCustomException("Exception occured while reading the data from db");
		}
		Map<String, List<String>> data = new HashMap<>();
		months = months.stream().map(val -> dateFormat.convertIntegertoStringDateFormat(val))
				.collect(Collectors.toList());
		data.put("lob", lobs);
		data.put("state", states);
		data.put("months", months);
		return data;
	}

	public DataPair<List<ResultData>, String, Object> summaryPage(CompletableFuture<List<PMPMDTO>> pmpmListFuture,
			CompletableFuture<List<MemberViewDTO>> memberViewListFuture) throws MyCustomException {
		try {
			List<PMPMDTO> pmpmList = pmpmListFuture.get();
			List<MemberViewDTO> memberViewList = memberViewListFuture.get();

			Map<String, Double> pricePmMap = pmpmList.stream().collect(Collectors.toMap(PMPMDTO::getMonths,
					pm -> pm.getTotalPricepm() != null ? pm.getTotalPricepm() : 0.0));
			List<ResultData> objs = memberViewList.stream().map(mv -> {
				return new ResultData((mv.getTotalActiveMembers() != null) ? mv.getTotalActiveMembers() : 0L,
						pricePmMap.getOrDefault(mv.getMonths(), 0.0), mv.getMonths());
			}).collect(Collectors.toList());
			String endMonth = pmpmList.stream().map(PMPMDTO::getMonths).max(Comparator.naturalOrder()).orElse(null);
			return new DataPair<>(objs, endMonth, null);
		} catch (InterruptedException | ExecutionException e) {
			throw new MyCustomException("Exception occured while reading the data from db");
		}
	}

	public Map<String, Long> convertToTargetPercentageMap(List<TargetPMPM> targetPMPM) {
		return targetPMPM.stream().collect(Collectors.toMap(
				t -> dateFormat.convertIntegertoStringDateFormat(t.getMonths()), TargetPMPM::getTargetPercentage));
	}

}
