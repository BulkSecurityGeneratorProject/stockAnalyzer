package com.ichoice.stockanalyzer.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A StockPriceWeekly.
 */
@Entity
@Table(name = "STOCK_PRICE_WEEKLY")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StockPriceWeekly implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull        
    @Column(name = "day", nullable = false)
    private Long day;

    @NotNull        
    @Column(name = "week", nullable = false)
    private Integer week;

    @NotNull        
    @Column(name = "year", nullable = false)
    private Integer year;
    
    @Column(name = "open")
    private Float open;
    
    @Column(name = "close")
    private Float close;
    
    @Column(name = "high")
    private Float high;
    
    @Column(name = "low")
    private Float low;
    
    @Column(name = "total_volume")
    private Long totalVolume;
    
    @Column(name = "adjusted_close")
    private Float adjustedClose;
    
    @Column(name = "price_change")
    private Float priceChange;

    @ManyToOne
    private StockInfo stockInfo;

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

    public StockInfo getStockInfo() {
        return stockInfo;
    }

    public void setStockInfo(StockInfo stockInfo) {
        this.stockInfo = stockInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StockPriceWeekly stockPriceWeekly = (StockPriceWeekly) o;

        if ( ! Objects.equals(id, stockPriceWeekly.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StockPriceWeekly{" +
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
