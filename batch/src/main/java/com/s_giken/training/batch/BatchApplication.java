package com.s_giken.training.batch;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class BatchApplication implements CommandLineRunner {
	private final Logger logger = LoggerFactory.getLogger(BatchApplication.class);
	private final JdbcTemplate jdbcTemplate;

	/**
	 * SpringBoot エントリポイント
	 * 
	 * @param args コマンドライン引数
	 */
	public static void main(String[] args) {
		SpringApplication.run(BatchApplication.class, args);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param jdbcTemplate SpringBootから注入される JdbcTemplate オブジェクト
	 */
	private final BatchService batchService;
	
	public BatchApplication(JdbcTemplate jdbcTemplate, BatchService batchService) {
		this.jdbcTemplate = jdbcTemplate;
		this.batchService = batchService;
	}

	/**
	 * コマンドラインプログラムのエントリ―ポイント
	 * 
	 * @param args コマンドライン引数
	 */
	@Override
	public void run(String... args) throws RuntimeException {
		logger.info("-".repeat(40));

		// TODO: ここにバッチ処理のコードを記述する
		// - データベースからデータを取得する
		if (args.length == 0) {
			logger.error("年月（yyyyMM）を指定してください。");
			return;
		}

		String input = args[0];
		try {
			DateTimeFormatter formatter = new DateTimeFormatterBuilder()
					.appendPattern("yyyyMM")
					.parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
					.toFormatter();

			LocalDate targetDate = LocalDate.parse(input, formatter);
			logger.info("指定された年月：{}", targetDate);

			//ここに対象年月データの抽出処理を記述する。

		} catch (DateTimeParseException e) {
			logger.error("年月の形式が正しくありません。yyyyMM", e);
			throw e;
		}

		logger.info("-".repeat(40));

	}
	}
	// - データを加工する
	// - 加工したデータをデータベースに登録する

	// ダミーコード
	// 削除してください。

