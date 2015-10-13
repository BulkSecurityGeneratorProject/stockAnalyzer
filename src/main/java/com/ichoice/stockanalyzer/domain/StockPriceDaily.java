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
 * A StockPriceDaily.
 */
@Entity
@Table(name = "STOCK_PRICE_DAILY")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StockPriceDaily implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull        
    @Column(name = "day", nullable = false)
    private Long day;
    
    @Column(name = "open")
    private Float open;
    
    @Column(name = "close")
    private Float close;
    
    @Column(name = "high")
    private Float high;
    
    @Column(name = "low")
    private Float low;
    
    @Column(name = "volume")
    private Long volume;
    
    @Column(name = "adjusted_close")
    private Float adjustedClose;

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

        StockPriceDaily stockPriceDaily = (StockPriceDaily) o;

        if ( ! Objects.equals(id, stockPriceDaily.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StockPriceDaily{" +
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
