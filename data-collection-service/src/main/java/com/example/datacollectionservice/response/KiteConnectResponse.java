package com.example.datacollectionservice.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class KiteConnectResponse {
    private String userId;
    private String apiKey;
    private String apiSecret;
    private String accessToken;
}
