package com.s_giken.training.webapp.model.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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

	@NotBlank
	private String name;

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
