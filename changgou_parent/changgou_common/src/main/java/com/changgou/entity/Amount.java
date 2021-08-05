package com.changgou.entity;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
public class Amount {

    public Amount(Integer total, String currency) {
        this.total = total;
        this.currency = currency;
    }

    private Integer total;
    private String currency;
    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }


}
