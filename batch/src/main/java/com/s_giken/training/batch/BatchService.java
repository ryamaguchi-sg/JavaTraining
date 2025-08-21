package com.s_giken.training.batch;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BatchService {
	private final Logger logger = LoggerFactory.getLogger(BatchService.class);
	)
	private final JdbcTemplate jdbcTemplate;

	public BatchService(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		}

	    @Transactional
	    public void processBillingData(LocalDate targetDate) {
	    	logger.info("対象年月:{}", targetDate);
	    	
	    	String checkSql = "SELECT COUNT(*)FROM T_BILLING_STATUS WHERE billing_ym =? AND is_commit = TRUE";
	    	Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class,targetDate);
	    	
	    	if(count != null && count > 0) {
	    		throw new IllegalStateException("請求データは確定済みです。処理を中断します。");
	    		
	    	}
	    	
	    	int deletedDetails = jdbcTemplate.update("DELETE FROM T_BILLING_DETAIL_DATA WHERE billing_ym =?", targetDate);
	    	int deletedData = jdbcTemplate.update("DELETE FROM T_BILLING_DATA WHERE billing_ym?", targetDate);
	    	int deletedStatus = jdbcTemplate.update("DELETE FROM T_BILLING_STATUS WHERE billing_ym?", targetDate);
	    	
	    	logger.info("未確定請求情報を削除しました(明細{}件, データ{}件, 状態{}件)", deletedDetails,deletedData,deletedStatus);
	    	
	    	int insertedStatus = jdbcTemplate.update()
	    }
	   
	    
	    public List<Map<String, Object>> fetchDataForMonth(LocalDate targetDate) {
	        LocalDate startDate = targetDate.withDayOfMonth(1);
	        LocalDate endDate = startDate.plusMonths(1);

	        String sql = "SELECT * FROM T_BILLING_DATA WHERE billing_ym = ?";
	        return jdbcTemplate.queryForList(sql, startDate, endDate);

	}
	}
