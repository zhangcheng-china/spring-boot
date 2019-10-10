package com.xkcoding.cache.redis.util;

import cn.hutool.json.JSONUtil;
import com.xkcoding.cache.redis.SpringBootDemoCacheRedisApplicationTests;
import com.xkcoding.cache.redis.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.junit.Assert.*;

/**
 * @author ：zhangcheng
 * @date ：Created in 2019/10/10 10:03
 * @description：
 * @modified By：
 * @version: $
 */
@Slf4j
public class RedisCacheUtilTest extends SpringBootDemoCacheRedisApplicationTests {

    @Resource
    private RedisCacheUtil redisCacheUtil;


    @Test
    public void setCacheObject() {
        List<User> userList = new ArrayList<>();
        LongStream.range(0,10).forEach(f->{
            userList.add(new User(f,"zhang"));
        });
        redisCacheUtil.setCacheObject("zhang::cheng",userList);
    }

    @Test
    public void setCacheObject1() {

    }

    @Test
    public void getCacheObject() {
        LinkedHashMap maps =  (LinkedHashMap)redisCacheUtil.getCacheObject("zhang::cheng");
        User user = JSONUtil.toBean(JSONUtil.toJsonStr(maps),User.class);
        log.info("userId:{} userName:{}", user.getId(),user.getName());
    }

    @Test
    public void deleteObject() {
    }

    @Test
    public void deleteObject1() {
    }

    @Test
    public void setCacheList() {
    }

    @Test
    public void getCacheList() {
    }

    @Test
    public void setCacheSet() {
    }

    @Test
    public void getCacheSet() {
    }

    @Test
    public void setCacheMap() {
    }

    @Test
    public void getCacheMap() {
    }

    @Test
    public void setCacheIntegerMap() {
    }

    @Test
    public void getCacheIntegerMap() {
    }
}
