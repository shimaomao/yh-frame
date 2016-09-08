package com.palm.yh.common.support;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.palm.vertx.util.PropertiesUtil;
import com.palm.vertx.web.support.TemplateMessageResolver;

import io.vertx.core.buffer.Buffer;

/**
 * <h3>资源模板替换文件提供类</h3>
 * <p>注意若同一个文件既有JS也有html替换标签，则先替换js的标签，后替换html的标签。</p>
 * @author zhongdh
 *
 */
public class MLHTemplateMessageResolver implements TemplateMessageResolver {

	@Override
	public List<String> getHTMLPaths() {
		return getPathsByKey("mlh.tpl.html.files");
	}
	
	@Override
	public List<String> getJSPaths() {
		return getPathsByKey("mlh.tpl.js.files");
	}

	@Override
	public Map<String, String> getMessageMap() {
		Map<String,String> messageMap=new HashMap<String,String>();
		this.builderJsMap(messageMap);
		this.builderHeaderMap(messageMap);
		this.builderFooterMap(messageMap);
		return messageMap;
	}
	
	/**
	 * <h3>组装JAVASCRIPT资源替换数据MAP集合</h3>
	 * @param messageMap
	 */
	private void builderJsMap(Map<String,String> messageMap){
		//是否开启用户行为记录开关：true为开启
	    //messageMap.put("userlog_switch", Boolean.toString(true));
		//是否开启开发模式，true为开发模式即为静态数据，false为生产模式即为动态数据
	    //messageMap.put("mlh.js.isDev", Boolean.toString(false));
		//js接口请求，normal为动态数据接口，DEV为开发的模拟数据
	    //messageMap.put("mlh.js.version", "NORMAL");
	    Properties mlhJSProperties=PropertiesUtil.getProperties("properties/common/mlhJSConfig.properties");
	    mlhJSProperties.keySet().forEach(key->{
	    	messageMap.put(key.toString(), mlhJSProperties.getProperty(key.toString()));
	    });
	}
	
	/**
	 * <h3>组装HTML头部替换资源数据MAP集合</h3>
	 * @param messageMap
	 */
	private void builderHeaderMap(Map<String,String> messageMap){
		//一般页面的导航
		// 创建读写缓冲
		byte[] bytes = new byte[65536];
		Buffer buffer = Buffer.buffer();
		int sizelen = 0;
		// try-with-resources
		// JDK1.7特性，获取并关闭
		try (InputStream inStream = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("properties/header.tpl")) {
			while (-1 != (sizelen = inStream.read(bytes))) {
				buffer.appendBytes(bytes, 0, sizelen);
			}
			messageMap.put("header_tpl", buffer.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		//首页的导航
		//创建读写缓冲
		byte[] index_bytes = new byte[65536];
		Buffer index_buffer = Buffer.buffer();
		int index_sizelen = 0;
		try (InputStream inStream = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("properties/index_header.tpl")) {
			while (-1 != (index_sizelen = inStream.read(index_bytes))) {
				index_buffer.appendBytes(index_bytes, 0, index_sizelen);
			}
			messageMap.put("index_header_tpl", index_buffer.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * <h3>组装HTML头部替换资源数据MAP集合</h3>
	 * @param messageMap
	 */
	private void builderFooterMap(Map<String,String> messageMap){
		// 创建读写缓冲
		byte[] bytes = new byte[65536];
		Buffer buffer = Buffer.buffer();
		int sizelen = 0;
		// try-with-resources
		// JDK1.7特性，获取并关闭
		try (InputStream inStream = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("properties/footer.tpl")) {
			while (-1 != (sizelen = inStream.read(bytes))) {
				buffer.appendBytes(bytes, 0, sizelen);
			}
			messageMap.put("footer_tpl", buffer.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
				
	}
	
	/**
	 * <h3>通过key获取资源文件配置</h3>
	 * @param key 配置文件里的key，具体查看：{@link yh-common/properties/mlhTPLFileConfig.properties}
	 * @return List<String>
	 */
	private List<String> getPathsByKey(String key){
		Properties mlhTPLFileProperties = PropertiesUtil.getProperties("properties/mlhTPLFileConfig.properties");
		String path = mlhTPLFileProperties.getProperty(key);
		if (StringUtils.isNotBlank(path)) {
			List<String> files = new ArrayList<String>();
			if (path.contains(",")) {
				String[] paths = path.split(",");
				if (null != paths && paths.length > 0) {
					for (int i = 0; i < paths.length; i++) {
						files.add(paths[i]);
					}
				}
			} else {
				files.add(path);
			}
			return files;
		}
		return new ArrayList<String>();
	}

}
