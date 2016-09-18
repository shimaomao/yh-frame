package com.palm.yh.client.web;

import org.springframework.beans.factory.annotation.Autowired;

import com.palm.vertx.web.bean.annotation.Routing;
import com.palm.vertx.web.interfaces.RouterWare;
import com.palm.yh.client.web.handler.AddProductHandler;
import com.palm.yh.client.web.handler.ExcelHandler;
import com.palm.yh.client.web.handler.FindProductHandler;
import com.palm.yh.client.web.handler.OpenPageHandler;

import io.vertx.ext.web.Router;

/**
 * 价格查询http接口
 */
@Routing(rootPath = "/price")
public class UserPriceRouter extends RouterWare {

    @Autowired
    private OpenPageHandler openPageHandler;
    
    @Autowired
    private AddProductHandler addProductHandler;
    
    @Autowired
    private FindProductHandler findProductHandler;
    
    @Autowired
    private ExcelHandler excelHandler;
    

    @Override
    public void setRoute(Router router) {

        //查询页面
        router.get("/findPage").handler(openPageHandler);
       
        //产品新增
        router.post("/addProduct").handler(addProductHandler);
        
        //查询产品（分页）
        router.post("/findProduct").handler(findProductHandler);
        
        //导入excel表并返回数据
        router.post("/excelData").handler(excelHandler);
        
    }
}
