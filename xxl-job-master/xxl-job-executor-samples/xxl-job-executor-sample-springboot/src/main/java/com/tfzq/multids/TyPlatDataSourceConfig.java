package com.tfzq.multids;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.github.pagehelper.PageHelper;

/**
 * Created by summer on 2016/11/25.
 */
@Configuration
@MapperScan(basePackages = "com.tfzq.typlat.mapper", sqlSessionTemplateRef  = "tyPlatSqlSessionTemplate")
public class TyPlatDataSourceConfig {
	@Bean(name = "tyPlatPageHelper")
	public PageHelper pageHelper() {
		PageHelper pageHelper = new PageHelper();
		Properties properties = new Properties();
		properties.setProperty("offsetAsPageNum", "true");
		properties.setProperty("rowBoundsWithCount", "true");
		properties.setProperty("reasonable", "true");
		properties.setProperty("dialect", "oracle"); // 配置mysql数据库的方言
		pageHelper.setProperties(properties);
		return pageHelper;
	}

    @Bean(name = "tyPlatDataSource")
    @ConfigurationProperties(prefix = "tydb.spring.datasource")
    public DataSource testDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "tyPlatSqlSessionFactory")
    public SqlSessionFactory testSqlSessionFactory(@Qualifier("tyPlatDataSource") DataSource dataSource,@Qualifier("tyPlatPageHelper") PageHelper pageHelper) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        Interceptor[] plugins =  new Interceptor[]{pageHelper};
        bean.setPlugins(plugins);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/tymapper/*.xml"));
        return bean.getObject();
    }

    @Bean(name = "tyPlatTransactionManager")
    public DataSourceTransactionManager testTransactionManager(@Qualifier("tyPlatDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "tyPlatSqlSessionTemplate")
    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("tyPlatSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
