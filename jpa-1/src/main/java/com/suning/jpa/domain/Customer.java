package com.suning.jpa.domain;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author yqx
 * @Company https://www.suning.com
 * @date 2020/11/21 9:32
 * @desc
 */
@NamedQuery(name="testNamedQuery", query="FROM Customer c WHERE c.id = ?")
@Cacheable
@Table(name = "JPA_CUSTOMERS")
@Entity
public class Customer {
    private Integer id;
    private String lastName;
    private String email;
    private int age;
    private Date createdTime;
    private Date birth;

    private Set<Order> orders = new HashSet<Order>();

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "LAST_NAME")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    @Temporal(TemporalType.DATE)
    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    /**
     * 映射单向 1-n 的关联关系
     * 使用 @OneToMany 来映射 1-n 的关联关系
     * 使用 @JoinColumn 来映射外键列的名称
     * 可以使用 @OneToMany 的 fetch 属性来修改默认的加载策略
     * 可以通过 @OneToMany 的 cascade 属性来修改默认的删除策略.
     * 注意: 若在 1 的一端的 @OneToMany 中使用 mappedBy 属性, 则 @OneToMany 端就不能再使用 @JoinColumn 属性了.
     * @return
     */
//    @JoinColumn(name = "CUSTOMER_ID")
    @OneToMany(fetch = FetchType.LAZY,cascade = {CascadeType.REMOVE},mappedBy = "customer")
    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", createdTime=" + createdTime +
                ", birth=" + birth +
                '}';
    }
}
