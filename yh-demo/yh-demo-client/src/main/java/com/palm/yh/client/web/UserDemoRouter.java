package com.palm.yh.client.web;

import com.palm.vertx.web.bean.annotation.Routing;
import com.palm.vertx.web.interfaces.RouterWare;
import com.palm.yh.client.web.handler.FindHandler;
import com.palm.yh.client.web.handler.AddHandler;
import com.palm.yh.client.web.handler.OpenPageHandler;
import com.palm.yh.client.web.handler.UpdateHandler;
import io.vertx.ext.web.Router;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 个人信息http接口
 * Created by fengzt on 2016/9/7.
 */
@Routing(rootPath = "/demo")
public class UserDemoRouter extends RouterWare {

    @Autowired
    private AddHandler addHandler;

    @Autowired
    private UpdateHandler updateHandler;

    @Autowired
    private FindHandler findHandler;

    @Autowired
    private OpenPageHandler openPageHandler;


    @Override
    public void setRoute(Router router) {
        //用户注册
        router.post("/add").handler(addHandler);

        //更新用户信息
        router.post("/update").handler(updateHandler);

        //查询个人信息
        router.get("/find/:userId").handler(findHandler);


        router.get("/openPage").handler(openPageHandler);

        //列表查询
//        router.route("/findList").handler(findListHandler);

    }
}
