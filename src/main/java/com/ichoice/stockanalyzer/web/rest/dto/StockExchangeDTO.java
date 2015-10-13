package com.ichoice.stockanalyzer.web.rest.dto;

import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the StockExchange entity.
 */
public class StockExchangeDTO implements Serializable {

    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StockExchangeDTO stockExchangeDTO = (StockExchangeDTO) o;

        if ( ! Objects.equals(id, stockExchangeDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StockExchangeDTO{" +
                "id=" + id +
                ", name='" + name + "'" +
                '}';
    }
}
