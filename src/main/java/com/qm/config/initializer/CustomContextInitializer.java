package com.qm.config.initializer;

import cn.hutool.setting.dialect.Props;
import com.qm.config.utils.CuratorUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

/**
 * @program: curator-test
 * @description:
 * @author: guoqingming
 * @create: 2018-08-02 23:43
 **/
@Slf4j
public class CustomContextInitializer implements ApplicationContextInitializer {

    private static final String ROOT_PATH = "/config";

    private CuratorFramework client;

    {
        Props props = new Props("application.properties");
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.newClient(props.getStr("zk.url"), retryPolicy);
        client.start();
    }
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment env = applicationContext.getEnvironment();
        String appName = env.getProperty("spring.application.name");
        String profile = env.getProperty("spring.profiles.active");
        String appZKPath = ROOT_PATH + "/" + appName +"/" + profile;
        System.setProperty("spring.application.name",appName);

        if(!StringUtils.isEmpty(appName)){
            try{
//
                MapPropertySource appZKProperties = CuratorUtils.getFromPath(client,appZKPath);
                if(appZKProperties != null) {
                    env.getPropertySources().addLast(appZKProperties);
                }

//                MapPropertySource commonZKProperties = CuratorUtils.getFromPath(client,COMMONPATH);
//                if(commonZKProperties != null) {
//                    env.getPropertySources().addLast(commonZKProperties);
//                }
                log.debug("初始化项目[{}]环境变量完毕",appName);
            }catch (Exception e){
                log.error("初始化项目["+ appName+ "]环境变量失败.",e);
            }
        }else{
            log.warn("请为在application.yml或者application.properties配置参数spring.application.name");
        }
    }

}
