package com.ichoice.stockanalyzer.web.rest.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the StockDividend entity.
 */
public class StockDividendDTO implements Serializable {

    private Long id;

    @NotNull
    private Long day;

    @NotNull
    @Min(value = 0)
    private Float amount;

    private Long stockInfoId;

    private String stockInfoTicker;

    private Long stockPriceId;

    private String stockPriceDay;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDay() {
        return day;
    }

    public void setDay(Long day) {
        this.day = day;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public Long getStockInfoId() {
        return stockInfoId;
    }

    public void setStockInfoId(Long stockInfoId) {
        this.stockInfoId = stockInfoId;
    }

    public String getStockInfoTicker() {
        return stockInfoTicker;
    }

    public void setStockInfoTicker(String stockInfoTicker) {
        this.stockInfoTicker = stockInfoTicker;
    }

    public Long getStockPriceId() {
        return stockPriceId;
    }

    public void setStockPriceId(Long stockPriceDailyId) {
        this.stockPriceId = stockPriceDailyId;
    }

    public String getStockPriceDay() {
        return stockPriceDay;
    }

    public void setStockPriceDay(String stockPriceDailyDay) {
        this.stockPriceDay = stockPriceDailyDay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StockDividendDTO stockDividendDTO = (StockDividendDTO) o;

        if ( ! Objects.equals(id, stockDividendDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StockDividendDTO{" +
                "id=" + id +
                ", day='" + day + "'" +
                ", amount='" + amount + "'" +
                '}';
    }
}
