package com.hnmtx;

import java.io.IOException;

import javax.sql.DataSource;
 
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.hnmtx.system.codeEngine.imp.BeetlFileGenerator;
import com.hnmtx.system.common.util.SpringUtil;
import com.hnmtx.system.metadata.entity.MdProject;
import com.hnmtx.system.metadata.repository.MdProjectRepository;

import ognl.OgnlException;

@EnableCaching
@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan
//@MapperScan("com.hnmtx.system.metadata.mapper")
public class CodeGeneratorApplication {

	private static Logger logger = Logger.getLogger(CodeGeneratorApplication.class);
	
/*    @Bean
    @ConfigurationProperties(prefix="spring.datasource")
    public DataSource dataSource() {
        return new org.apache.tomcat.jdbc.pool.DataSource();
    }
	
    //提供SqlSeesion
    @Bean
    public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
 
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());
 
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
 
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:/mybatis/*.xml"));
 
        return sqlSessionFactoryBean.getObject();
    }
    
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }*/
    
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, OgnlException, IOException {
		//SpringApplication.run(ImplApplication.class, "--debug");
		ApplicationContext  ctx =  SpringApplication.run(CodeGeneratorApplication.class, args);
		
		logger.info("=============ready for generate code... =============");
		
		MdProjectRepository mdProjectRepository=SpringUtil.getBean(MdProjectRepository.class);
		//获取mdProject对象
		MdProject mdProject=mdProjectRepository.selectStrongByPrimaryKey("00000000000000000000010000000000");  //基础模块
		
		BeetlFileGenerator g = new BeetlFileGenerator();
		g.setTemplatePath("D:\\template");
		g.setRootObject(mdProject);
		g.setTargetPath("D:\\codeGenPath");
		g.generate();
		
		//g.setTemplatePath(CodeGeneratorApplication.class.getClassLoader().getSystemResource("template").getPath());
		
		logger.info("=============generate code  finished. =============");
		
		
	}

}
