package com.example.zerodelta.dto;


import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class PortfolioSummaryResponse {
    private BigDecimal fiatBalance;
    private BigDecimal totalInvestedValue;
    private List<HoldingDetail> holdings;

    @Data
    @Builder
    public static class HoldingDetail{
        private String ticker;
        private String assetName;
        private String assetClass;
        private BigDecimal units;
        private BigDecimal averageBuyPrice;
        private BigDecimal currentMarketPrice;
        private BigDecimal totalValue;
        private BigDecimal profitLossPercentage;
    }
}
