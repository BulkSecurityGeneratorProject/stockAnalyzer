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
 * A StockDividend.
 */
@Entity
@Table(name = "STOCK_DIVIDEND")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StockDividend implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull        
    @Column(name = "day", nullable = false)
    private Long day;

    @NotNull
    @Min(value = 0)        
    @Column(name = "amount", nullable = false)
    private Float amount;

    @ManyToOne
    private StockInfo stockInfo;

    @OneToOne
    private StockPriceDaily stockPrice;

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

    public StockInfo getStockInfo() {
        return stockInfo;
    }

    public void setStockInfo(StockInfo stockInfo) {
        this.stockInfo = stockInfo;
    }

    public StockPriceDaily getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(StockPriceDaily stockPriceDaily) {
        this.stockPrice = stockPriceDaily;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StockDividend stockDividend = (StockDividend) o;

        if ( ! Objects.equals(id, stockDividend.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StockDividend{" +
                "id=" + id +
                ", day='" + day + "'" +
                ", amount='" + amount + "'" +
                '}';
    }
}
