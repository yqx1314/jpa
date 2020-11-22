## 1.入门案例

### 第一步：创建JPA工程

![image-20201122091848293](C:\Users\yqx\Desktop\找工作\java学习\JPA\JPA\image-20201122091848293.png)

### 第二步：添加maven依赖

```xml
<!--  hibernate 依赖 -->
<!-- https://mvnrepository.com/artifact/org.hibernate/hibernate-entitymanager -->
<dependency>
    <groupId>org.hibernate</groupId>
    <artifactId>hibernate-entitymanager</artifactId>
    <version>4.2.4.Final</version>
</dependency>

<!-- mysql 驱动 -->
<!-- https://mvnrepository.com/artifact/mysql/mysql-connector-java -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>5.1.8</version>
</dependency>
```

### 第三步：编写主配置文件persistence.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence xmlns="http://java.sun.com/xml/ns/persistence"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://java.sun.com/xml/ns/persistence
    http://java.sun.com/xml/ns/persistence/persistence_2_0.xsd"
             version="2.0">
    <!--配置持久化单元
        name：持久化单元名称
        transaction-type：事务类型
             RESOURCE_LOCAL：本地事务管理
             JTA：分布式事务管理 -->
    <persistence-unit name="jpa-1" transaction-type="RESOURCE_LOCAL">
        <!--配置JPA规范的服务提供商 -->
        <provider>org.hibernate.ejb.HibernatePersistence</provider>
<!--        添加持久化类-->
        <class>com.suning.jpa.domain.Customer</class>
        <properties>
            <!-- 数据库驱动 -->
            <property name="javax.persistence.jdbc.driver" value="com.mysql.jdbc.Driver" />
            <!-- 数据库地址 -->
            <property name="javax.persistence.jdbc.url" value="jdbc:mysql://localhost:3306/jpa" />
            <!-- 数据库用户名 -->
            <property name="javax.persistence.jdbc.user" value="root" />
            <!-- 数据库密码 -->
            <property name="javax.persistence.jdbc.password" value="mysql" />
            <!--jpa提供者的可选配置：我们的JPA规范的提供者为hibernate，所以jpa的核心配置中兼容hibernate的配 -->
            <property name="hibernate.show_sql" value="true" />
            <property name="hibernate.format_sql" value="true" />
            <property name="hibernate.hbm2ddl.auto" value="create" />
        </properties>
    </persistence-unit>
</persistence>
```



### 第四步：创建实体类，指定实体类与数据库表的对应关系

```java
import javax.persistence.*;

/**
 * @author yqx
 * @Company https://www.suning.com
 * @date 2020/11/21 9:32
 * @desc
 */
@Table(name = "JPA_CUSTOMERS")
@Entity
public class Customer {
    private Integer id;
    private String lastName;
    private String email;
    private int age;

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
}
```



### 第五步：执行操作

```java
//1. 创建 EntitymanagerFactory
String persistenceUnitName = "jpa-1";
EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
//2. 创建 EntityManager. 类似于 Hibernate 的 SessionFactory
EntityManager entityManager = entityManagerFactory.createEntityManager();
//3. 开启事务
EntityTransaction transaction = entityManager.getTransaction();
transaction.begin();
//4. 进行持久化操作
Customer customer = new Customer();
customer.setAge(24);
customer.setEmail("yqx_test@163.com");
customer.setLastName("yuan");
entityManager.persist(customer);
//5. 提交事务
transaction.commit();
//6. 关闭 EntityManager
entityManager.close();
//7. 关闭 EntityManagerFactory
entityManagerFactory.close();
```

## 2.基本注解

### 2.1@Entity

```text
@Entity 标注用于实体类声明语句之前，指出该Java 类为实体类，将映射到指定的数据库表。
```

### 2.2@Table

```text
当实体类与其映射的数据库表名不同名时需要使用 @Table 标注说明，该标注与 @Entity 标注并列使用，置于实体类声明语句之前，可写于单独语句行，也可与声明语句同行。
@Table 标注的常用选项是 name，用于指明数据库的表名
@Table标注还有一个两个选项 catalog 和 schema 用于设置表所属的数据库目录或模式，通常为数据库名
```

### 2.3@Id

```text
@Id 标注用于声明一个实体类的属性映射为数据库的主键列。该属性通常置于属性声明语句之前，可与声明语句同行，也可写在单独行上。
@Id标注也可置于属性的getter方法之前
```

### 2.4@GeneratedValue

```text
@GeneratedValue  用于标注主键的生成策略，通过 strategy 属性指定。默认情况下，JPA 自动选择一个最适合底层数据库的主键生成策略：SqlServer 对应 identity，MySQL 对应 auto increment。
在 javax.persistence.GenerationType 中定义了以下几种可供选择的策略：
	IDENTITY：采用数据库 ID自增长的方式来自增主键字段，Oracle 不支持这种方式；
	AUTO： JPA自动选择合适的策略，是默认选项；
	SEQUENCE：通过序列产生主键，通过 @SequenceGenerator 注解指定序列名，MySql 不支持这种方式
	TABLE：通过表产生主键，框架借由表模拟序列产生主键，使用该策略可以使应用更易于数据库移植
```

### 2.5@Basic

```text
@Basic 表示一个简单的属性到数据库表的字段的映射,对于没有任何标注的 getXxxx() 方法,默认即为@Basic
fetch: 表示该属性的读取策略,有 EAGER 和 LAZY 两种,分别表示主支抓取和延迟加载,默认为 EAGER.
optional:表示该属性是否允许为null, 默认为true
```

### 2.6@Column

```text
当实体的属性与其映射的数据库表的列不同名时需要使用@Column 标注说明，该属性通常置于实体的属性声明语句之前，还可与 @Id 标注一起使用。
@Column 标注的常用属性是 name，用于设置映射数据库表的列名。此外，该标注还包含其它多个属性，如：unique 、nullable、length 等。
@Column 标注的 columnDefinition 属性: 表示该字段在数据库中的实际类型.通常 ORM 框架可以根据属性类型自动判断数据库中字段的类型,但是对于Date类型仍无法确定数据库中字段类型究竟是DATE,TIME还是TIMESTAMP.此外,String的默认映射类型为VARCHAR, 如果要将 String 类型映射到特定数据库的 BLOB 或TEXT 字段类型.
@Column标注也可置于属性的getter方法之前
```

### 2.7@Transient

```text
表示该属性并非一个到数据库表的字段的映射,ORM框架将忽略该属性.
如果一个属性并非数据库表的字段映射,就务必将其标示为@Transient,否则,ORM框架默认其注解为@Basic
```

### 2.8@Temporal

```text
在核心的 Java API 中并没有定义 Date 类型的精度(temporal precision).  而在数据库中,表示 Date 类型的数据有 DATE, TIME, 和 TIMESTAMP 三种精度(即单纯的日期,时间,或者两者 兼备). 在进行属性映射时可使用@Temporal注解来调整精度.
```

### 2.9用 table 来生成主键详解

```text
将当前主键的值单独保存到一个数据库的表中，主键的值每次都是从指定的表中查询来获得
这种方法生成主键的策略可以适用于任何数据库，不必担心不同数据库不兼容造成的问题。
```

![image-20201122095701976](C:\Users\yqx\Desktop\找工作\java学习\JPA\JPA\image-20201122095701976.png)

## 3.JPA API

### 3.1Persistence

```text
Persistence  类是用于获取 EntityManagerFactory 实例。该类包含一个名为 createEntityManagerFactory 的 静态方法 。
createEntityManagerFactory 方法有如下两个重载版本。
	带有一个参数的方法以 JPA 配置文件 persistence.xml 中的持久化单元名为参数
	带有两个参数的方法：前一个参数含义相同，后一个参数 Map类型，用于设置 JPA 的相关属性，这时将忽略其它地方设置的属性。Map 对象的属性名必须是 JPA 实现库提供商的名字空间约定的属性名。
```

### 3.2 EntityManagerFactory

```text
EntityManagerFactory 接口主要用来创建 EntityManager 实例。该接口约定了如下4个方法：
	createEntityManager()：用于创建实体管理器对象实例。
	createEntityManager(Map map)：用于创建实体管理器对象实例的重载方法，Map 参数用于提供 EntityManager 的属性。
	isOpen()：检查 EntityManagerFactory 是否处于打开状态。实体管理器工厂创建后一直处于打开状态，除非调用close()方法将其关闭。
	close()：关闭 EntityManagerFactory 。 EntityManagerFactory 关闭后将释放所有资源，isOpen()方法测试将返回 false，其它方法将不能调用，否则将导致IllegalStateException异常。
```

### 3.3EntityManager

```text
在 JPA 规范中, EntityManager 是完成持久化操作的核心对象。实体作为普通 Java 对象，只有在调用 EntityManager 将其持久化后才会变成持久化对象。EntityManager 对象在一组实体类与底层数据源之间进行 O/R 映射的管理。它可以用来管理和更新 Entity Bean, 根椐主键查找 Entity Bean, 还可以通过JPQL语句查询实体。
1.find (Class<T> entityClass,Object primaryKey)：返回指定的 OID 对应的实体类对象，如果这个实体存在于当前的持久化环境，则返回一个被缓存的对象；否则会创建一个新的 Entity, 并加载数据库中相关信息；若 OID 不存在于数据库中，则返回一个 null。第一个参数为被查询的实体类类型，第二个参数为待查找实体的主键值。
2.getReference (Class<T> entityClass,Object primaryKey)：与find()方法类似，不同的是：如果缓存中不存在指定的 Entity, EntityManager 会创建一个 Entity 类的代理，但是不会立即加载数据库中的信息，只有第一次真正使用此 Entity 的属性才加载，所以如果此 OID 在数据库不存在，getReference() 不会返回 null 值, 而是抛出EntityNotFoundException
3.persist (Object entity)：用于将新创建的 Entity 纳入到 EntityManager 的管理。该方法执行后，传入 persist() 方法的 Entity 对象转换成持久化状态。
	如果传入 persist() 方法的 Entity 对象已经处于持久化状态，则 persist() 方法什么都不做。
	如果对删除状态的 Entity 进行 persist() 操作，会转换为持久化状态。
	如果对游离状态的实体执行 persist() 操作，可能会在 persist() 方法抛出 EntityExistException(也有可能是在flush或事务提交后抛出)。
4.remove (Object entity)：删除实例。如果实例是被管理的，即与数据库实体记录关联，则同时会删除关联的数据库记录,只可以删除持久化状态的实例
5.merge (T entity)：merge() 用于处理 Entity 的同步。即数据库的插入和更新操作
6.flush ()：同步持久上下文环境，即将持久上下文环境的所有未保存实体的状态信息保存到数据库中。
7.setFlushMode (FlushModeType flushMode)：设置持久上下文环境的Flush模式。参数可以取2个枚举
	FlushModeType.AUTO 为自动更新数据库实体，
	FlushModeType.COMMIT 为直到提交事务时才更新数据库记录。
8.getFlushMode ()：获取持久上下文环境的Flush模式。返回FlushModeType类的枚举值
9.refresh (Object entity)：用数据库实体记录的值更新实体对象的状态，即更新实例的属性值。
10.clear ()：清除持久上下文环境，断开所有关联的实体。如果这时还有未提交的更新则会被撤消。
11.contains (Object entity)：判断一个实例是否属于当前持久上下文环境管理的实体。
12.isOpen ()：判断当前的实体管理器是否是打开状态。
13.getTransaction ()：返回资源层的事务对象。EntityTransaction实例可以用于开始和提交多个事务。
14.close ()：关闭实体管理器。之后若调用实体管理器实例的方法或其派生的查询对象的方法都将抛出 IllegalstateException 异常，除了getTransaction 和 isOpen方法(返回 false)。不过，当与实体管理器关联的事务处于活动状态时，调用 close 方法后持久上下文将仍处于被管理状态，直到事务完成。
15.createQuery (String qlString)：创建一个查询对象。
16.createNamedQuery (String name)：根据命名的查询语句块创建查询对象。参数为命名的查询语句。
17.createNativeQuery (String sqlString)：使用标准 SQL语句创建查询对象。参数为标准SQL语句字符串。
18.createNativeQuery (String sqls, String resultSetMapping)：使用标准SQL语句创建查询对象，并指定返回结果集 Map的 名称。
```

![image-20201122103423993](C:\Users\yqx\Desktop\找工作\java学习\JPA\JPA\image-20201122103423993.png)

### 3.4 EntityTransaction

```text
EntityTransaction 接口用来管理资源层实体管理器的事务操作。通过调用实体管理器的getTransaction方法 获得其实例.
1.begin ()用于启动一个事务，此后的多个数据库操作将作为整体被提交或撤消。若这时事务已启动则会抛出 IllegalStateException 异常。
2.commit ()用于提交当前事务。即将事务启动以后的所有数据库更新操作持久化至数据库中。
3.rollback ()撤消(回滚)当前事务。即撤消事务启动后的所有数据库更新操作，从而不对数据库产生影响。
4.setRollbackOnly ()使当前事务只能被撤消。
5.getRollbackOnly ()查看当前事务是否设置了只能撤消标志。
6.isActive ()查看当前事务是否是活动的。如果返回true则不能调用begin方法，否则将抛出 IllegalStateException 异常；如果返回 false 则不能调用 commit、rollback、setRollbackOnly 及 getRollbackOnly 方法，否则将抛出 IllegalStateException 异常
```

## 4.映射关联关系

### 4.1单向多对一

```java
//映射单向 n-1 的关联关系
//使用 @ManyToOne 来映射多对一的关联关系
//使用 @JoinColumn 来映射外键. 
//可使用 @ManyToOne 的 fetch 属性来修改默认的关联属性的加载策略
@JoinColumn(name = "CUSTOMER_ID")
@ManyToOne(fetch = FetchType.LAZY)
public Customer getCustomer() {
    return customer;
}
```

### 4.2单项一对多

```java
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
```

### 4.3 双向一对一

```java
/**
	 * //使用 @OneToOne 来映射 1-1 关联关系。
	 * 	//若需要在当前数据表中添加主键则需要使用 @JoinColumn 来进行映射. 注意, 1-1 关联关系, 所以需要添加 unique=true
	 * @return
	 */
@JoinColumn(name="MGR_ID", unique=true)
@OneToOne(fetch=FetchType.LAZY)
public Manager getMgr() {
    return mgr;
}
/**
	 * 对于不维护关联关系, 没有外键的一方, 使用 @OneToOne 来进行映射, 建议设置 mappedBy=true
	 * @return
	 */
@OneToOne(mappedBy="mgr")
public Department getDept() {
    return dept;
}
```

### 4.4双向多对多

```java
/**
	 * //使用 @ManyToMany 注解来映射多对多关联关系
	 * 使用 @JoinTable 来映射中间表
	 * 	1. name 指向中间表的名字
	 * 	2. joinColumns 映射当前类所在的表在中间表中的外键
	 * 	2.1 name 指定外键列的列名
	 * 	2.2 referencedColumnName 指定外键列关联当前表的哪一列
	 * 	3. inverseJoinColumns 映射关联的类所在中间表的外键
	 * @return
	 */
@JoinTable(name="ITEM_CATEGORY",
           joinColumns={@JoinColumn(name="ITEM_ID", referencedColumnName="ID")},
           inverseJoinColumns={@JoinColumn(name="CATEGORY_ID", referencedColumnName="ID")})
@ManyToMany
public Set<Category> getCategories() {
    return categories;
}
@ManyToMany(mappedBy="categories")
public Set<Item> getItems() {
    return items;
}
```

## 5.使用二级缓存

### 5.1二级缓存相关配置

```xml
<!-- 二级缓存相关 -->
<property name="hibernate.cache.use_second_level_cache" value="true"/>
<property name="hibernate.cache.region.factory_class" value="org.hibernate.cache.ehcache.EhCacheRegionFactory"/>
<property name="hibernate.cache.use_query_cache" value="true"/>
```

### 5.2导入相关jar包

```xml
<dependency>
    <groupId>org.hibernate</groupId>
    <artifactId>hibernate-ehcache</artifactId>
    <version>4.2.4.Final</version>
</dependency>
<dependency>
    <groupId>org.ehcache</groupId>
    <artifactId>ehcache</artifactId>
    <version>3.6.3</version>
</dependency>
```

### 5.3配置二级缓存策略

```xml
<!--
  配置二级缓存的策略
  ALL：所有的实体类都被缓存
  NONE：所有的实体类都不被缓存.
  ENABLE_SELECTIVE：标识 @Cacheable(true) 注解的实体类将被缓存
  DISABLE_SELECTIVE：缓存除标识 @Cacheable(false) 以外的所有实体类
  UNSPECIFIED：默认值，JPA 产品默认值将被使用
  -->
<shared-cache-mode>ENABLE_SELECTIVE</shared-cache-mode>
```

### 5.4修改实体类，添加注解@Cacheable

```java
@Cacheable
@Table(name = "JPA_CUSTOMERS")
@Entity
public class Customer {
    private Integer id;
    private String lastName;
```

## 6.spring整合JPA(jpa的jar包版本为2.1)

### 6.1导入所需的jar包

```xml
<properties>
    <spring.version>5.0.2.RELEASE</spring.version>
    <hibernate.version>5.0.7.Final</hibernate.version>
    <slf4j.version>1.6.6</slf4j.version>
    <log4j.version>1.2.12</log4j.version>
    <c3p0.version>0.9.1.2</c3p0.version>
    <mysql.version>5.1.6</mysql.version>
</properties>

<dependencies>
    <!-- spring beg -->
    <dependency>
        <groupId>org.aspectj</groupId>
        <artifactId>aspectjweaver</artifactId>
        <version>1.6.8</version>
    </dependency>

    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
        <version>${spring.version}</version>
    </dependency>

    <!-- spring对orm框架的支持包-->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-orm</artifactId>
        <version>${spring.version}</version>
    </dependency>
    <!-- spring end -->

    <!-- hibernate beg -->
    <dependency>
        <groupId>org.hibernate</groupId>
        <artifactId>hibernate-entitymanager</artifactId>
        <version>${hibernate.version}</version>
    </dependency>
    <!-- hibernate end -->

    <!-- c3p0 beg -->
    <dependency>
        <groupId>c3p0</groupId>
        <artifactId>c3p0</artifactId>
        <version>${c3p0.version}</version>
    </dependency>
    <!-- c3p0 end -->

    <!-- log end -->
    <dependency>
        <groupId>log4j</groupId>
        <artifactId>log4j</artifactId>
        <version>${log4j.version}</version>
    </dependency>

    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-api</artifactId>
        <version>${slf4j.version}</version>
    </dependency>

    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-log4j12</artifactId>
        <version>${slf4j.version}</version>
    </dependency>
    <!-- log end -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>${mysql.version}</version>
    </dependency>
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.12</version>
        <scope>compile</scope>
    </dependency>
</dependencies>
```

### 6.2 编写spring配置文件applicationContext.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:context="http://www.springframework.org/schema/context"
	xmlns:tx="http://www.springframework.org/schema/tx"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
		http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-4.0.xsd
		http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-4.0.xsd">

	<!-- 配置自动扫描的包 -->
	<context:component-scan base-package="com.suning.jpa"></context:component-scan>

	<!-- 配置 C3P0 数据源 -->
	<context:property-placeholder location="classpath:db.properties"/>
	<bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
		<property name="user" value="${jdbc.user}"></property>
		<property name="password" value="${jdbc.password}"></property>
		<property name="driverClass" value="${jdbc.driverClass}"></property>
		<property name="jdbcUrl" value="${jdbc.jdbcUrl}"></property>
		<!-- 配置其他属性 -->
	</bean>
	
	<!-- 配置 EntityManagerFactory -->
	<bean id="entityManagerFactory"
		class="org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean">
		<property name="dataSource" ref="dataSource"></property>
		<!-- 配置 JPA 提供商的适配器. 可以通过内部 bean 的方式来配置 -->
		<property name="jpaVendorAdapter">
			<bean class="org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter"></bean>
		</property>	
		<!-- 配置实体类所在的包 -->
		<property name="packagesToScan" value="com.suning.jpa.spring.entities"></property>
		<!-- 配置 JPA 的基本属性. 例如 JPA 实现产品的属性 -->
		<property name="jpaProperties">
			<props>
				<prop key="hibernate.show_sql">true</prop>
				<prop key="hibernate.format_sql">true</prop>
				<prop key="hibernate.hbm2ddl.auto">update</prop>
			</props>
		</property>
	</bean>
	
	<!-- 配置 JPA 使用的事务管理器 -->
	<bean id="transactionManager"
		class="org.springframework.orm.jpa.JpaTransactionManager">
		<property name="entityManagerFactory" ref="entityManagerFactory"></property>	
	</bean>
	
	<!-- 配置支持基于注解是事务配置 -->
	<tx:annotation-driven transaction-manager="transactionManager"/>

</beans>
```

### 6.3编写实体类和dao层，service层

```java
package com.suning.jpa.spring.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="JPA_PERSONS")
@Entity
public class Person {

	private Integer id;
	private String lastName;

	private String email;
	private int age;

	@GeneratedValue
	@Id
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="LAST_NAME")
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
}
```

```java
package com.suning.jpa.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.suning.jpa.spring.entities.Person;
import org.springframework.stereotype.Repository;



/**
 * @author yqx
 */
@Repository
public class PersonDao {

   //获得和当前事务绑定的EntityManager对象
   @PersistenceContext
   private EntityManager entityManager;
   
   public void save(Person person){
      entityManager.persist(person);
   }
   
}
```

