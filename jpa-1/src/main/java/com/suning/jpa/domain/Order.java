package com.suning.jpa.domain;

import javax.persistence.*;

/**
 * @author yqx
 * @Company https://www.suning.com
 * @date 2020/11/22 14:06
 * @desc
 */
@Table(name = "JPA_ORDERS")
@Entity
public class Order {
    private Integer id;
    private String orderName;

    private Customer customer;

    @GeneratedValue
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "ORDER_NAME")
    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    @JoinColumn(name = "CUSTOMER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
