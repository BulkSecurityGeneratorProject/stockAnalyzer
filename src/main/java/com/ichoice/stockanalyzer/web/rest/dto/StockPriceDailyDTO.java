package com.ichoice.stockanalyzer.web.rest.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the StockPriceDaily entity.
 */
public class StockPriceDailyDTO implements Serializable {

    private Long id;

    @NotNull
    private Long day;

    private Float open;

    private Float close;

    private Float high;

    private Float low;

    private Long volume;

    private Float adjustedClose;

    private Long stockInfoId;

    private String stockInfoTicker;

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

    public Float getOpen() {
        return open;
    }

    public void setOpen(Float open) {
        this.open = open;
    }

    public Float getClose() {
        return close;
    }

    public void setClose(Float close) {
        this.close = close;
    }

    public Float getHigh() {
        return high;
    }

    public void setHigh(Float high) {
        this.high = high;
    }

    public Float getLow() {
        return low;
    }

    public void setLow(Float low) {
        this.low = low;
    }

    public Long getVolume() {
        return volume;
    }

    public void setVolume(Long volume) {
        this.volume = volume;
    }

    public Float getAdjustedClose() {
        return adjustedClose;
    }

    public void setAdjustedClose(Float adjustedClose) {
        this.adjustedClose = adjustedClose;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StockPriceDailyDTO stockPriceDailyDTO = (StockPriceDailyDTO) o;

        if ( ! Objects.equals(id, stockPriceDailyDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StockPriceDailyDTO{" +
                "id=" + id +
                ", day='" + day + "'" +
                ", open='" + open + "'" +
                ", close='" + close + "'" +
                ", high='" + high + "'" +
                ", low='" + low + "'" +
                ", volume='" + volume + "'" +
                ", adjustedClose='" + adjustedClose + "'" +
                '}';
    }
}
