package com.ichoice.stockanalyzer.web.rest.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the StockInfo entity.
 */
public class StockInfoDTO implements Serializable {

    private Long id;

    private String ticker;

    private String companyName;

    private Long stockExchangeId;

    private String stockExchangeName;

    private Long stockCategoryId;

    private String stockCategoryName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Long getStockExchangeId() {
        return stockExchangeId;
    }

    public void setStockExchangeId(Long stockExchangeId) {
        this.stockExchangeId = stockExchangeId;
    }

    public String getStockExchangeName() {
        return stockExchangeName;
    }

    public void setStockExchangeName(String stockExchangeName) {
        this.stockExchangeName = stockExchangeName;
    }

    public Long getStockCategoryId() {
        return stockCategoryId;
    }

    public void setStockCategoryId(Long stockCategoryId) {
        this.stockCategoryId = stockCategoryId;
    }

    public String getStockCategoryName() {
        return stockCategoryName;
    }

    public void setStockCategoryName(String stockCategoryName) {
        this.stockCategoryName = stockCategoryName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StockInfoDTO stockInfoDTO = (StockInfoDTO) o;

        if ( ! Objects.equals(id, stockInfoDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StockInfoDTO{" +
                "id=" + id +
                ", ticker='" + ticker + "'" +
                ", companyName='" + companyName + "'" +
                '}';
    }
}
