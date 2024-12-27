package com.enit.pricing.dto;

import java.math.BigDecimal;

public class CartResponse {
    private BigDecimal totalBeforeDiscount;
    private BigDecimal totalAfterDiscount;

    public BigDecimal getTotalBeforeDiscount() {
        return totalBeforeDiscount;
    }
    public void setTotalBeforeDiscount(BigDecimal totalBeforeDiscount) {
        this.totalBeforeDiscount = totalBeforeDiscount;
    }
    public CartResponse(BigDecimal totalBeforeDiscount, BigDecimal totalAfterDiscount) {
        this.totalBeforeDiscount = totalBeforeDiscount;
        this.totalAfterDiscount = totalAfterDiscount;
    }
    public BigDecimal getTotalAfterDiscount() {
        return totalAfterDiscount;
    }
    public void setTotalAfterDiscount(BigDecimal totalAfterDiscount) {
        this.totalAfterDiscount = totalAfterDiscount;
    }
}
