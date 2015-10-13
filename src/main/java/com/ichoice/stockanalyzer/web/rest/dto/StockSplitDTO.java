package com.ichoice.stockanalyzer.web.rest.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the StockSplit entity.
 */
public class StockSplitDTO implements Serializable {

    private Long id;

    @NotNull
    private Long day;

    @NotNull
    private Double splitRatio;

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

    public Double getSplitRatio() {
        return splitRatio;
    }

    public void setSplitRatio(Double splitRatio) {
        this.splitRatio = splitRatio;
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

        StockSplitDTO stockSplitDTO = (StockSplitDTO) o;

        if ( ! Objects.equals(id, stockSplitDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StockSplitDTO{" +
                "id=" + id +
                ", day='" + day + "'" +
                ", splitRatio='" + splitRatio + "'" +
                '}';
    }
}
