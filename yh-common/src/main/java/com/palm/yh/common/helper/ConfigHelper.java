package com.palm.yh.common.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 统一系统配置路径
 * Created by fengzt on 2016/5/23.
 */
@Component
public class ConfigHelper {

    /**
     * 系统路径
     */
    @Value("${host.path}")
    private String hostPath;


    public String getHostPath() {
        return hostPath;
    }

    public void setHostPath(String hostPath) {
        this.hostPath = hostPath;
    }
}
