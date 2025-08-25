package com.s_giken.training.batch;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

	private Map<String, BigDecimal> calculateTotalWithTax(List<Map<String, Object>> memberCharges,
			BigDecimal taxMultiplier) {
		//金額の合計（税抜き）
		BigDecimal totalAmount = memberCharges.stream()
				.map(c -> (BigDecimal) c.get("amount"))
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		//税込みの合計金額
		BigDecimal total = totalAmount.multiply(taxMultiplier)
				.setScale(0, RoundingMode.DOWN);

		return Map.of("totalAmount", totalAmount, "total", total);
	}

	@Transactional
	public void processBillingData(LocalDate targetDate) {
		logger.info("対象年月:{}", targetDate);

		String checkSql = "SELECT COUNT(*)FROM T_BILLING_STATUS WHERE billing_ym =? AND is_commit = TRUE";
		Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, targetDate);

		if (count != null && count > 0) {
			throw new IllegalStateException("請求データは確定済みです。処理を中断します。");
		}

		int deletedDetailsCount = jdbcTemplate.update("DELETE FROM T_BILLING_DETAIL_DATA WHERE billing_ym =?",
				targetDate);
		int deletedDataCount = jdbcTemplate.update("DELETE FROM T_BILLING_DATA WHERE billing_ym = ?", targetDate);
		int deletedStatusCount = jdbcTemplate.update("DELETE FROM T_BILLING_STATUS WHERE billing_ym = ?", targetDate);

		logger.info("未確定請求情報を削除しました(明細{}件, データ{}件, 状態{}件)", deletedDetailsCount, deletedDataCount, deletedStatusCount);

		int insertedStatus = jdbcTemplate.update("INSERT INTO T_BILLING_STATUS(billing_ym, is_commit)VALUES(?,FALSE)",
				targetDate);
		logger.info("請求ステータス情報を追加しました:{}件", insertedStatus);

		String memberSql = """
				SELECT * FROM T_MEMBER
				WHERE start_date <= ?
				AND(end_date IS NULL OR end_date >= ?)

				""";

		LocalDate firstDayOfMonth = targetDate.withDayOfMonth(1);
		LocalDate lastDayOfMonth = targetDate.withDayOfMonth(targetDate.lengthOfMonth());

		List<Map<String, Object>> members = jdbcTemplate.queryForList(memberSql,
				lastDayOfMonth,
				firstDayOfMonth);

		logger.info("有効な加入者情報:) {}件", members.size());

		String chargeSql = """
				SELECT * FROM T_CHARGE
				WHERE start_date <= ?
				AND(end_date IS NULL OR end_date >= ?)

				""";

		List<Map<String, Object>> charges = jdbcTemplate.queryForList(chargeSql,
				lastDayOfMonth, firstDayOfMonth);//（月末の日付と月初の日付を指定する）

		logger.info("有効な料金情報:{}件", charges.size());//取得件数のログを出力する

		//		String crossJoinSql = """
		//				SELECT m.*, c.*
		//				FROM T_MEMBER m
		//				CROSS JOIN T_CHARGE c
		//				WHERE m.start_date <= ?
		//				AND(m.end_date IS NULL OR m.end_date >= ?)
		//				AND(c.start_date <= ?)
		//				AND(c.end_date IS NULL OR c.end_date >= ?)
		//				""";
		//		List<Map<String, Object>> memberChargePairs = jdbcTemplate.queryForList(
		//				crossJoinSql,
		//				lastDayOfMonth, firstDayOfMonth,
		//				lastDayOfMonth, firstDayOfMonth);
		//		logger.info("有効な加入者と有効な料金の組み合わせ:{}件", memberChargePairs.size());

		//		Map<Long, List<Map<String, Object>>> groupedByMember = memberChargePairs.stream()
		//				.collect(Collectors.groupingBy(row -> (Long) row.get("member_id")));
		int billingDataCount = 0;
		int billingDetailCount = 0;
		BigDecimal taxRate = new BigDecimal("0.1");
		BigDecimal taxMultiplier = new BigDecimal("1.1");
		//加入者一覧（members）から一人ずつ取り出して、memberという変数に格納するループ処理
		for (Map<String, Object> member : members) {
			Long memberId = (Long) member.get("member_id");

			//該当する料金情報を抽出
			List<Map<String, Object>> memberCharges = charges.stream()
					.toList();

			String insertBillingDataSql = """
					INSERT INTO T_BILLING_DATA(
					billing_ym,member_id, mail, name, address, start_date, end_date, payment_method,
					amount,tax_ratio, total
					)VALUES(?,?,?,?,?,?,?,?,?,?,?)
					""";
			String insertBillingDetailSql = """
					INSERT INTO T_BILLING_DETAIL_DATA(
						billing_ym, member_id, charge_id, name,
						amount, start_date, end_date
						)VALUES(?,?,?,?,?,?,?)
						""";

			//		for (Map.Entry<Long, List<Map<String, Object>>> entry : groupedByMember.entrySet()) {
			//			Long memberId = entry.getKey();
			//			List<Map<String, Object>> memberCharges = entry.getValue();
			//
			//			Map<String, Object> member = memberCharges.get(0);
			//
			//			BigDecimal total = calculateTotalWithTax(memberCharges, taxMultiplier);

			Map<String, BigDecimal> amounts = calculateTotalWithTax(memberCharges, taxMultiplier);
			BigDecimal totalAmount = amounts.get("totalAmount");
			BigDecimal total = amounts.get("total");

			jdbcTemplate.update(insertBillingDataSql,
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
					total);
			billingDataCount++;

			for (Map<String, Object> charge : memberCharges) {
				jdbcTemplate.update(insertBillingDetailSql,

						targetDate,
						memberId,
						charge.get("charge_id"),
						charge.get("name"),
						charge.get("amount"),
						charge.get("start_date"),
						charge.get("end_date"));
				billingDetailCount++;
			}
		}

		logger.info("請求データ情報を追加しました:{}件", billingDataCount);
		logger.info("請求明細データ情報を追加しました:{}件", billingDetailCount);
	}
}
