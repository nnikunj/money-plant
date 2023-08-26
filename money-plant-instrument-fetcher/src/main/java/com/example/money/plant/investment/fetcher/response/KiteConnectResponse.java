package com.example.money.plant.investment.fetcher.response;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KiteConnectResponse {
    private String userId;
    private String apiKey;
    private String apiSecret;
    private String accessToken;
}
