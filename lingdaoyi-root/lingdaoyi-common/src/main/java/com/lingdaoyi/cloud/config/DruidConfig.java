package com.lingdaoyi.cloud.config;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;

/**
 * 数据库连接池工具类
 * 
 * @author Jinsey
 *
 */
@Configuration
@PropertySource(value = { "classpath:jdbc.properties" })
public class DruidConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(DruidConfig.class);

	@Value("${db.datasource.url}")
	private String dbUrl;

	@Value("${db.datasource.username}")
	private String dbUsername;

	@Value("${db.datasource.password}")
	private String dbPassword;

	@Value("${db.datasource.driver}")
	private String driverClassName;

	@Value("${db.datasource.initialSize}")
	private int initialSize;

	@Value("${db.datasource.minIdle}")
	private int minIdle;

	@Value("${db.datasource.maxActive}")
	private int maxActive;

	@Value("${db.datasource.filters}")
	private String filters;

	// @Value("${spring.datasource.durid.maxWait}")
	// private int maxWait;
	//
	// @Value("${spring.datasource.validationQuery}")
	// private String validationQuery;
	//
	// @Value("${spring.datasource.testOnBorrow}")
	// private boolean testOnBorrow;
	//
	// @Value("${spring.datasource.testOnReturn}")
	// private boolean testOnReturn;
	//
	// @Value("${spring.datasource.testWhileIdle}")
	// private boolean testWhileIdle;
	//
	// @Value("${spring.datasource.timeBetweenEvictionRunsMillis}")
	// private int timeBetweenEvictionRunsMillis;
	//
	// @Value("${spring.datasource.minEvictableIdleTimeMillis}")
	// private int minEvictableIdleTimeMillis;
	//
	// @Value("${spring.datasource.removeAbandoned}")
	// private boolean removeAbandoned;
	//
	// @Value("${spring.datasource.removeAbandonedTimeout}")
	// private int removeAbandonedTimeout;
	//
	// @Value("${spring.datasource.logAbandoned}")
	// private boolean logAbandoned;
	//
	// @Value("${spring.datasource.poolPreparedStatements}")
	// private boolean poolPreparedStatements;
	//
	// @Value("${spring.datasource.maxPoolPreparedStatementPerConnectionSize}")
	// private int maxPoolPreparedStatementPerConnectionSize;

	@Bean // 被@Bean声明，为Spring容器所管理
	@Primary // @Primary表示这里定义的DataSource将覆盖其他来源的DataSource。
	public DataSource dataSource() {
		DruidDataSource datasource = new DruidDataSource();

		// 数据库连接基础属性
		datasource.setDriverClassName(driverClassName);
		datasource.setUrl(dbUrl);
		datasource.setUsername(dbUsername);
		datasource.setPassword(dbPassword);
		datasource.setInitialSize(initialSize);
		datasource.setMinIdle(minIdle);
		datasource.setMaxActive(maxActive);

		// 其他配置
		// datasource.setMaxWait(maxWait);
		// datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
		// datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
		// datasource.setValidationQuery(validationQuery);
		// datasource.setTestWhileIdle(testWhileIdle);
		// datasource.setTestOnBorrow(testOnBorrow);
		// datasource.setTestOnReturn(testOnReturn);
		// datasource.setPoolPreparedStatements(poolPreparedStatements);
		// datasource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
		try {
			datasource.setFilters(filters);
		} catch (SQLException e) {
			LOGGER.error("druid configuration initialization filter", e);
		}
		return datasource;
	}

	/**
	 * 注册一个StatViewServlet
	 */
	@Bean
	public ServletRegistrationBean DruidStatViewServle() {
		ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(),
				"/druid/*");

		// 添加初始化参数：initParams
		/** 白名单，如果不配置或value为空，则允许所有 */
		// servletRegistrationBean.addInitParameter("allow","127.0.0.1,192.0.0.1");
		/** 黑名单，与白名单存在相同IP时，优先于白名单 */
		// servletRegistrationBean.addInitParameter("deny","192.0.0.1");
		/** 用户名 */
		// servletRegistrationBean.addInitParameter("loginUsername","jikefriend");
		/** 密码 */
		// servletRegistrationBean.addInitParameter("loginPassword","jikefriend");
		/** 禁用页面上的“Reset All”功能 */
		// servletRegistrationBean.addInitParameter("resetEnable","false");
		return servletRegistrationBean;
	}

	/**
	 * 注册一个：WebStatFilter
	 */
	@Bean
	public FilterRegistrationBean druidStatFilter() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());

		/** 过滤规则 */
		// filterRegistrationBean.addUrlPatterns("/*");
		/** 忽略资源 */
		// filterRegistrationBean.addInitParameter("exclusions","*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
		return filterRegistrationBean;
	}

}
