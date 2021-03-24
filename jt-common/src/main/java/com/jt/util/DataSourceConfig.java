package com.jt.util;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import javax.sql.DataSource;
import java.sql.SQLException;


/**
 * @Author zhou
 * @create 2021-03-14 11:17
 */


/**  * Created by wk on 2018/1/10.  */
@Configuration
@ConfigurationProperties(prefix="spring.datasources")
public class DataSourceConfig {
    private Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);
    private String type;
    private String url;
    private String driverClassName;
    private String username;
    private String password;
    private Integer initialSize;
    private Integer minIdle;
    private Integer maxActive;
    private Integer maxWait;
    private Integer timeBetweenEvictionRunsMillis;
    private Integer minEvictableIdleTimeMillis;
    private String validationQuery;
    private Boolean testWhileIdle;
    private Boolean testOnBorrow;
    private Boolean testOnReturn;
    private Boolean poolPreparedStatements;
    private Integer maxPoolPreparedStatementPerConnectionSize;
    private String filters;
    private String connectionProperties;
    public String getDriverClassName() {
        return driverClassName;
    }
    public void setDriverClassName(String driverClassName) {
            this.driverClassName = driverClassName;
    } public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Integer getInitialSize() { return initialSize; }
    public void setInitialSize(Integer initialSize) { this.initialSize = initialSize; }
    public Integer getMinIdle() { return minIdle; }
    public void setMinIdle(Integer minIdle) { this.minIdle = minIdle; }
    public Integer getMaxActive() { return maxActive; }
    public void setMaxActive(Integer maxActive) { this.maxActive = maxActive; }
    public Integer getMaxWait() { return maxWait; }
    public void setMaxWait(Integer maxWait) { this.maxWait = maxWait; }
    public Integer getTimeBetweenEvictionRunsMillis() { return timeBetweenEvictionRunsMillis; }
    public void setTimeBetweenEvictionRunsMillis(Integer timeBetweenEvictionRunsMillis) { this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis; }
    public Integer getMinEvictableIdleTimeMillis() { return minEvictableIdleTimeMillis; }
    public void setMinEvictableIdleTimeMillis(Integer minEvictableIdleTimeMillis) { this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis; }
    public String getValidationQuery() { return validationQuery; }
    public void setValidationQuery(String validationQuery) { this.validationQuery = validationQuery; } public Boolean getTestWhileIdle() { return testWhileIdle; }
    public void setTestWhileIdle(Boolean testWhileIdle) { this.testWhileIdle = testWhileIdle; } public Boolean getTestOnBorrow() { return testOnBorrow; }
    public void setTestOnBorrow(Boolean testOnBorrow) { this.testOnBorrow = testOnBorrow; } public Boolean getTestOnReturn() { return testOnReturn; }
    public void setTestOnReturn(Boolean testOnReturn) { this.testOnReturn = testOnReturn; } public Boolean getPoolPreparedStatements() { return poolPreparedStatements; }
    public void setPoolPreparedStatements(Boolean poolPreparedStatements) { this.poolPreparedStatements = poolPreparedStatements; }
    public Integer getMaxPoolPreparedStatementPerConnectionSize() { return maxPoolPreparedStatementPerConnectionSize; }
    public void setMaxPoolPreparedStatementPerConnectionSize(Integer maxPoolPreparedStatementPerConnectionSize) { this.maxPoolPreparedStatementPerConnectionSize = maxPoolPreparedStatementPerConnectionSize; }
    public String getFilters() { return filters; }
    public void setFilters(String filters) { this.filters = filters; } public String getConnectionProperties() { return connectionProperties; }
    public void setConnectionProperties(String connectionProperties) { this.connectionProperties = connectionProperties; }

    @Bean
    public ServletRegistrationBean druidServlet() {
        ServletRegistrationBean reg = new ServletRegistrationBean();
        reg.setServlet(new StatViewServlet()); reg.addUrlMappings("/druid/*");
        reg.addInitParameter("loginUsername", username);
        reg.addInitParameter("loginPassword", password);
        return reg;
    }
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        filterRegistrationBean.addInitParameter("profileEnable", "true"); return filterRegistrationBean;
    }
    @Bean
    @Primary
    public DataSource druidDataSource() {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(url); datasource.setUsername(username);
        datasource.setPassword(password);
        datasource.setDriverClassName(driverClassName);
        datasource.setInitialSize(initialSize);
        datasource.setMinIdle(minIdle);
        datasource.setMaxActive(maxActive);
        datasource.setMaxWait(maxWait);
        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        datasource.setValidationQuery(validationQuery);
        datasource.setTestWhileIdle(testWhileIdle);
        datasource.setTestOnBorrow(testOnBorrow);
        datasource.setTestOnReturn(testOnReturn);
        String paramArray[] = connectionProperties.split(";");
        datasource.setConnectionProperties(connectionProperties);
        try {
            datasource.setFilters(filters);
        } catch (SQLException e) {
            logger.error("druid configuration initialization filter", e);
        }
        return datasource;
    }
}
