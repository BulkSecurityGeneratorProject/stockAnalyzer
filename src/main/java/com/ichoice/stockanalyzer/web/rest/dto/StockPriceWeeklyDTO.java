package com.ichoice.stockanalyzer.web.rest.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the StockPriceWeekly entity.
 */
public class StockPriceWeeklyDTO implements Serializable {

    private Long id;

    @NotNull
    private Long day;

    @NotNull
    private Integer week;

    @NotNull
    private Integer year;

    private Float open;

    private Float close;

    private Float high;

    private Float low;

    private Long totalVolume;

    private Float adjustedClose;

    private Float priceChange;

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

    public Integer getWeek() {
        return week;
    }

    public void setWeek(Integer week) {
        this.week = week;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
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

    public Long getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(Long totalVolume) {
        this.totalVolume = totalVolume;
    }

    public Float getAdjustedClose() {
        return adjustedClose;
    }

    public void setAdjustedClose(Float adjustedClose) {
        this.adjustedClose = adjustedClose;
    }

    public Float getPriceChange() {
        return priceChange;
    }

    public void setPriceChange(Float priceChange) {
        this.priceChange = priceChange;
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

        StockPriceWeeklyDTO stockPriceWeeklyDTO = (StockPriceWeeklyDTO) o;

        if ( ! Objects.equals(id, stockPriceWeeklyDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StockPriceWeeklyDTO{" +
                "id=" + id +
                ", day='" + day + "'" +
                ", week='" + week + "'" +
                ", year='" + year + "'" +
                ", open='" + open + "'" +
                ", close='" + close + "'" +
                ", high='" + high + "'" +
                ", low='" + low + "'" +
                ", totalVolume='" + totalVolume + "'" +
                ", adjustedClose='" + adjustedClose + "'" +
                ", priceChange='" + priceChange + "'" +
                '}';
    }
}
