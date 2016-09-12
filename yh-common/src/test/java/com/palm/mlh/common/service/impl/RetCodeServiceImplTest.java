package com.palm.mlh.common.service.impl;

import com.palm.yh.common.service.RetCodeService;
import com.palm.vertx.core.enums.UnitConsumerType;
import com.palm.vertx.test.annotation.TestConsumer;
import com.palm.vertx.test.run.PalmTest;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * retCode测试用例
 * Created by fengzt on 2016/5/24.
 */
@TestConsumer(runType= UnitConsumerType.All)
public class RetCodeServiceImplTest extends PalmTest {
    private Logger logger = LoggerFactory.getLogger(RetCodeServiceImplTest.class);

    @Autowired
    private RetCodeService retCodeService;

    @Test
    public void addRetCode(TestContext testContext ) throws Exception {

        JsonArray array = new JsonArray();
        array.add( new JsonObject().put("code", "10012").put("key", "PHONE_NUM_EMPTY").put("msg_zh-CN", "手机号码空"));

        array.add( new JsonObject().put("code", "10013").put("key", "PHONE_CODE_EXIST").put("msg_zh-CN", "验证码没过期"));

        array.add( new JsonObject().put("code", "10014").put("key", "PHONE_CODE_INCORRECT").put("msg_zh-CN", "手机号码格式错误"));

        array.add( new JsonObject().put("code", "10015").put("key", "CONTAIN_SENSITIVE_WORD").put("msg_zh-CN", "内容有敏感词汇"));

        array.add( new JsonObject().put("code", "10016").put("key", "USER_NO_LOGIN").put("msg_zh-CN", "用户未登录"));

        for( int i = 0; i < array.size(); i ++) {
            Async async = testContext.async();
            retCodeService.addRetCode(array.getJsonObject(i), resultHander -> {
                logger.debug("[RetCodeServiceImplTest#addRetCode] 返回结果{} : {}", resultHander.succeeded(), resultHander.result());
                if (resultHander.succeeded()) {
                    logger.debug("[RetCodeServiceImplTest#addRetCode] 增加成功！");
                    assertTrue(null != resultHander.result());
                } else {
                    logger.debug("[RetCodeServiceImplTest#addRetCode] 增加成功！");
                }
                async.complete();
            });
        }

    }

    @Test
    public void findOneRetCode(TestContext testContext) throws Exception {
        Async async = testContext.async();
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("key", "SUCCESS");
        retCodeService.findOneRetCode(jsonObject, result ->{
            logger.debug("findOneRetCode结果 {}", result);
            assertTrue(null != result);
            async.complete();
        });
    }

    @Test
    public void findRetCode(TestContext testContext) throws Exception {
        Async async = testContext.async();
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("code", "0");
        retCodeService.findRetCode(jsonObject, handler ->{
            logger.debug("测试findRetCode结果 {}：{}", handler.succeeded(), handler.result());
            assertTrue(null != handler.result());
            async.complete();
        });
    }

    @Test
    public void delRetCode(TestContext testContext) throws Exception {
        Async async = testContext.async();
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("code", "0");
        retCodeService.delRetCode(jsonObject, handler ->{
            logger.debug("删除结果 {}：{}", handler.succeeded(), handler.result());
            if (handler.succeeded()) {
                assertTrue(null != handler.result());
            }
            async.complete();
        });

    }
}