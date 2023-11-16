package com.artx.artx.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.UUID;

@Getter
public class ReadUserDto {

	@Schema(description = "유저 고유 식별 번호", nullable = false, example = "fafe2100-e770-4cfc-aef7-960837b777df")
	private UUID userId;
	@Schema(description = "아이디", nullable = false, example = "artxlover")
	private String username;

	public ReadUserDto(UUID userId, String username) {
		this.userId = userId;
		this.username = username;
	}

}
