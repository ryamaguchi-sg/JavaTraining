package com.s_giken.training.webapp.model.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Charge {
	@Nullable
	private Long chargeId;

	@Size(min=4, max=16, message="1文字から64文字で指定してください。")
	@NotBlank
	private String name;

	@Min(value=1, message="正の整数を入力してください。")
	@Digits(integer = 10, fraction = 0)
	@NotNull
	private BigDecimal amount;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@NotNull
	private LocalDate startDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Nullable
	private LocalDate endDate;

	@Nullable
	private Timestamp createdAt;

	@Nullable
	private Timestamp modifiedAt;
}
