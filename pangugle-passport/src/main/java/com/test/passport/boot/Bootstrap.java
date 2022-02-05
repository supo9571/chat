package com.test.passport.boot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.pangugle.framework.spring.SpringBootManager;
import com.pangugle.framework.spring.beans.SimpleNameGenerator;

/**
 * 仅供测试部署专用,生产环境请勿使用
 * @author Administrator
 *
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = {
		"com.pangugle.framework.spring",
		"com.pangugle.passport",
		// 测试加载器,仅供参考，需要自定义
		"com.test"},
								nameGenerator=SimpleNameGenerator.class)
@EnableTransactionManagement
public class Bootstrap {

	public static void main(String[] args) throws Exception {
		// run spring
		SpringBootManager.run(Bootstrap.class, "bootstrap.global.web.server.port", "global_web", args);
	}

}
