package com.qm.config.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;
import org.springframework.core.env.MapPropertySource;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: curator-test
 * @description:
 * @author: guoqingming
 * @create: 2018-08-02 23:48
 **/
@Slf4j
public class CuratorUtils {

    public static CuratorFramework getClient(String zkAddress) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(zkAddress)
                .retryPolicy(retryPolicy)
                .sessionTimeoutMs(6000)
                .connectionTimeoutMs(3000)
                .build();
        client.start();
        return client;
    }

    public static MapPropertySource getFromPath(CuratorFramework client, String path){
        if(client != null && !StringUtils.isEmpty(path)){
            try{
                Stat stat = client.checkExists().forPath(path);
                if(stat != null){
                    Map<String, Object> propMap  = client
                            .getChildren()
                            .forPath(path)
                            .stream()
                            .collect(Collectors.toMap(string->string, string-> {
                                try{
                                    String jsonStr = new String(client.getData().forPath(path + "/" + string), "utf-8");
//                                    Prop prop = gson.fromJson(jsonStr,Prop.class);
//                                    prop.setName(string);
//                                    log.trace("zookeeper属性[{}]-[{}]值[{}]被加载到spring环境变量中",prop.getName(),prop.getDesc(),prop.getValue());
                                    return jsonStr;
                                }catch (Exception e){
                                    log.error("",e);
                                    return null;
                                }
                            }));
                    MapPropertySource mapPropertySource = new MapPropertySource(path,propMap);
                    return mapPropertySource;
                }
            }catch (Exception e){
                log.error("read zookeeper's properties error:", e);
            }
        }
        return null;
    }
}
