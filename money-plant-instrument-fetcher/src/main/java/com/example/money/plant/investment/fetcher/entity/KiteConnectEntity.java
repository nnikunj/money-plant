package com.example.money.plant.investment.fetcher.entity;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.gemfire.mapping.annotation.Region;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.Serializable;
import java.util.Objects;

@Region(value = "LoginToken")
public class KiteConnectEntity implements Serializable {

	@Id
	private String userId;

	private String apiKey;

	private String apiSecret;

	private String accessToken;

	@PersistenceCreator
	public KiteConnectEntity(String userId, String apiKey, String apiSecret, String accessToken) {
		this.userId = userId;
		this.apiKey = apiKey;
		this.apiSecret = apiSecret;
		this.accessToken = accessToken;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getApiSecret() {
		return apiSecret;
	}

	public void setApiSecret(String apiSecret) {
		this.apiSecret = apiSecret;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		KiteConnectEntity that = (KiteConnectEntity) o;
		return Objects.equals(userId, that.userId) && Objects.equals(apiKey, that.apiKey)
				&& Objects.equals(apiSecret, that.apiSecret) && Objects.equals(accessToken, that.accessToken);
	}

	@Override
	public int hashCode() {
		return Objects.hash(userId, apiKey, apiSecret, accessToken);
	}

	@Override
	public String toString() {
		return "Token {" + "userId='" + userId + '\'' + ", apiKey='" + apiKey + '\'' + ", apiSecret='" + apiSecret
				+ '\'' + ", accessToken='" + accessToken + '\'' + '}';
	}

}
