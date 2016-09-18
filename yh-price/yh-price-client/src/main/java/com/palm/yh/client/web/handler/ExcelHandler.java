package com.palm.yh.client.web.handler;

import com.palm.vertx.auth.util.service.UserAuthService;
import com.palm.vertx.core.application.PalmVert;
import com.palm.vertx.web.support.HttpSupport;
import com.palm.yh.common.util.YhConsumerAddressUtil;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;

import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 更新用户
 * Created by fengzt on 2016/9/7.
 */
@Component
public class ExcelHandler implements Handler<RoutingContext> {
    private Logger logger = LoggerFactory.getLogger(ExcelHandler.class);

    @Autowired
    private HttpSupport httpSupport;

    @Autowired
    private PalmVert palmVert;

    @Autowired
    private UserAuthService userAuthService;

    @Override
    public void handle(RoutingContext routingContext) {
    	 Set<FileUpload> fileUploadSet = routingContext.fileUploads();
         Iterator<FileUpload> fileUploadIterator = fileUploadSet.iterator();
         while (fileUploadIterator.hasNext()){
           FileUpload fileUpload = fileUploadIterator.next();

           // To get the uploaded file do
           Buffer uploadedFile = vertx.fileSystem().readFileBlocking(fileUpload.uploadedFileName());

           // Uploaded File Name
           try {
             String fileName = URLDecoder.decode(fileUpload.fileName(), "UTF-8");
           } catch (UnsupportedEncodingException e) {
             e.printStackTrace();
           }

           // Use the Event Bus to dispatch the file now
           // Since Event Bus does not support POJOs by default so we need to create a MessageCodec implementation
           // and provide methods for encode and decode the bytes
         }


       });

    }
}