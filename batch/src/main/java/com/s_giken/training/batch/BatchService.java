package com.s_giken.training.batch;

import java.math.BigDecimal;
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
	    	int deletedData = jdbcTemplate.update("DELETE FROM T_BILLING_DATA WHERE billing_ym = ?", targetDate);
	    	int deletedStatus = jdbcTemplate.update("DELETE FROM T_BILLING_STATUS WHERE billing_ym = ?", targetDate);
	    	
	    	logger.info("未確定請求情報を削除しました(明細{}件, データ{}件, 状態{}件)", deletedDetails,deletedData,deletedStatus);
	    	
	    	int insertedStatus = jdbcTemplate.update("INSERT INTO T_BILLING_STATUS(billing_ym, is_commit)VALUES(?,FALSE)", targetDate);
	    	logger.info("請求ステータス情報を追加しました:{}件",insertedStatus);
	    	
	    	String memberSql =  """ 
	    			SELECT*FROM T_MEMBER
	    			WHERE start_date <= ?
	    			AND(end_date IS NULL OR end_date >= ?)
	    			
	    			""";
	    	
	    	List<Map<String,Object>>members = jdbcTemplate.queryForList(memberSql, targetDate.withDayOfMonth(targetDate.lengthOfMonth()),
	    			targetDate.withDayOfMonth(1));
	    	logger.info("有効な加入者情報:) {}件",members.size());
	    	
	    	String chargeSql = """
	    			SELECT*FROM T_CHARGE
	    			WHERE start_date <= ?
	    			AND(end_date IS NULL OR end_date >= ?)
	    			
	    			""";
	    			
	    	List<Map<String,Object>>charges = jdbcTemplate.queryForList(chargeSql, targetDate.withDayOfMonth(targetDate.lengthOfMonth()),targetDate.withDayOfMonth(1));//（月末の日付と月初の日付を指定する）
	    	
	    	logger.info("有効な料金情報:{}件",charges.size());//取得件数のログを出力する
	    	
	    	int billingDataCount = 0;
	    	int billingDetailCount = 0;
	    	BigDecimal taxRate = new BigDecimal("0.1");
	    	
	    	//加入者一覧（members）から一人ずつ取り出して、memberという変数に格納するループ処理
	    	for(Map<String,Object> member : members) {
	    		Long memberId = (Long)member.get("member_id");

	    		
	    		//該当する料金情報を抽出
	    		List<Map<String,Object>>memberCharges = charges.stream()
	    				.filter(c->memberId.equals(c.get("member_id")))
	    				.toList();
	    		/*for文で書く場合
	    		 * for(Map<String,Object>c:charges) {
	    		 * if(member_id.equals(c.get(memberid)))
	    		*/
	    		
	    		BigDecimal totalAmount = memberCharges.stream()
	    				.map(c ->(BigDecimal)c.get("amount"))
						.reduce(BigDecimal.ZERO, BigDecimal::add));
						
				BigDecimal total = totalAmount
						.multiply(BigDecimal.ONE.add(taxRate))
						.setScale(0,RoundingMode.DOWN);
				
				jdbcTemplate.update("""
						INSERT INTO T_BILLING_DATA(
						billing_ym,member_id, mail, name, address, start_date, end_date, payment_method,
						amount,tax_ratio, total
						)VALUES(?,?,?,?,?,?,?,?,?,?,?)
						""",
						
						targetDate,
						memberId,
						member.get("mail"),
						member.get("name"),
						member.get("address"),
						member.get("start_date"),
						member.get("end_date"),
						member.get("payment_method"),
						totalAmount,
						taxRate,
						total
						);
				billingDataCount ++;
						
			for(Map<String,Object>charge:memberCharges) {
				jdbcTemplate.update("""
						INSERT INTO T_BILLING_DETAIL_DATA(
						billing_ym,member_id,charge_id,name,
						amount,start_date,end_date
						)VALUES(?,?,?,?,?,?,?)
						""",
						targetDate,
						memberId,
						charge.get("charge_id"),
						charge.get("name"),
						charge.get("amount"),
						charge.get("start_date"),
						charge.get("end_date")
						);
				billingDetailCount ++;
			}
	    	}
	    	
	    	logger.info("請求データ情報を追加しました:{}件",billingDataCount);
	    	logger.info("請求明細データ情報を追加しました:{}件",billingDetailCount);
	    }
}
