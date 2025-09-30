package com.s_giken.training.webapp.model.entity;

import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
	@NotBlank
	private String name;

	@NotBlank
	private String password;
}