package com.palm.yh.client.web.handler;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.palm.vertx.core.application.PalmVert;
import com.palm.vertx.web.support.HttpSupport;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;

/**
 * 获取excel信息
 */
@Component
public class ExcelHandler implements Handler<RoutingContext> {
	private static Logger logger = LoggerFactory.getLogger(ExcelHandler.class);

    @Autowired
    private HttpSupport httpSupport;

    @Autowired
    private PalmVert palmVert;
    
    static String EXCEL = "application/vnd.ms-excel";

    @Override
    public void handle(RoutingContext routingContext) {
    	  Future<JsonObject> excelFuture = Future.future();
   	      Set<FileUpload> fileUploadSet = routingContext.fileUploads();
          Iterator<FileUpload> fileUploadIterator = fileUploadSet.iterator();
          if(!fileUploadIterator.hasNext()){
        	  logger.debug("获取文件失败");
	       	  excelFuture.complete(new JsonObject().put("code", "-1").put("msg", "获取文件失败！"));
	       	  excelFuture.setHandler(handler ->{
	       	  httpSupport.sendJson(routingContext,handler.result());
	       	  });
	          return;
          }
          FileUpload fileUpload = fileUploadIterator.next();
          logger.debug("文件类型：{}", fileUpload.contentType());
          logger.debug("文件临时路径：{}",  fileUpload.uploadedFileName());
          logger.debug("文件大小：{}",  fileUpload.size());
          logger.debug("文件名:{}",fileUpload.fileName());
          File directory = new File("src/main/resources/excel");
		  logger.debug("excel存放路径:{}",directory.getAbsolutePath());
          // 获取excel内容
          String path =new File("src/main/resources/excel").getAbsolutePath()+"/"+fileUpload.fileName();
          Buffer uploadedFile = Vertx.vertx().fileSystem().readFileBlocking(fileUpload.uploadedFileName());
          palmVert.getVertx().fileSystem().writeFile(path,uploadedFile, result -> {
        	    if(result.succeeded()) {
        	    	 File file = new File(fileUpload.uploadedFileName());
        	  		 file.delete();
	        	    logger.debug("读取文件成功！");
	        	  	 try {
	        	  		Workbook workbook= WorkbookFactory.create(new File(path));
	        	  		excelFuture.complete(new JsonObject().put("code", "0").put("result",Rwriter(workbook)));
					} catch (Exception e) {
						  e.printStackTrace();
						  logger.debug("导入文件，不是EXCEL格式");
			        	  excelFuture.complete(new JsonObject().put("code", "-1").put("msg", "导入文件，不是EXCEL格式"));
			        	  excelFuture.setHandler(handler ->{
			        	  httpSupport.sendJson(routingContext,handler.result());
			        	  });
					} 	
        	    }else {
        	    	logger.debug("读文件失败");
        	    	excelFuture.complete(new JsonObject().put("code", "-1").put("msg", "读取文件失败，请重新导入"));
        	    }
          });
        excelFuture.setHandler(handler ->{
            logger.debug("handler:{}, result:{}", handler.succeeded(), handler.result());
            httpSupport.sendJson(routingContext, handler.result());
        });
      };
      
     public static Sheet getSheet(Workbook wb, int sheetIndex) {  
          if (wb == null) {  
              throw new RuntimeException("工作簿对象为空");  
          }  
          int sheetSize = wb.getNumberOfSheets();  
          if (sheetIndex < 0 || sheetIndex > sheetSize - 1) {  
              throw new RuntimeException("工作表获取错误");  
          }  
          return wb.getSheetAt(sheetIndex);  
      } 
     
    public static JsonArray Rwriter(Workbook wbs){
	        List<List<String>> list = getExcelRows(getSheet(wbs, 0), -1, -1);   //得到上传文件内容
	    
	        JsonArray result = new JsonArray();
	        int buffer[] = new int[5];
	        //获取字段对应列的index
	        for (int i = 0; i < list.size(); i++) {  
	            List<String> row = list.get(i);  
	            for (int j = 0; j < row.size(); j++) {
	            	if(row.get(j).contains("序号"))buffer[0]=j;
	            	else if(row.get(j).contains("产品"))buffer[1]=j;
	            	else if(row.get(j).contains("胸径"))buffer[2]=j;
	            	else if(row.get(j).contains("高度"))buffer[3]=j;
	            	else if(row.get(j).contains("冠幅"))buffer[4]=j;
	            } 
	    	}
	        for (int i = 0; i < list.size(); i++) { 
	            List<String> row = list.get(i);  
	            if(row.size()<4) continue;
	            JsonObject excel = new JsonObject();
	            if(row.get(buffer[1]) == null || row.get(buffer[1]) == "") continue;
	            excel.put("productName", row.get(buffer[1]));
	            excel.put("midiaMeter", row.get(buffer[2]));
	            excel.put("height", row.get(buffer[3]));
	            excel.put("crown", row.get(buffer[4])); 
	            result.add(excel);
	        }
	        return result;
	}
    
    public static List<List<String>> getExcelRows(Sheet sheet, int startLine, int endLine) {  
        List<List<String>> list = new ArrayList<List<String>>();  
        // 如果开始行号和结束行号都是-1的话，则全表读取  
        if (startLine == 0)  
            startLine = 0;  
        if (endLine == -1) {  
            endLine = sheet.getLastRowNum() + 1;  
        } else {  
            endLine += 1;  
        }  
        for (int i = startLine; i < endLine; i++) {  
            Row row = sheet.getRow(i);  
            if (row == null) {  
            	//该行为空，直接跳过;  
                continue;  
            }  
            int rowSize = row.getLastCellNum();  
            List<String> rowList = new ArrayList<String>();  
            for (int j = 0; j < rowSize; j++) {  
                Cell cell = row.getCell(j);  
                String temp = "";  
                if (cell == null) {  
                   //该列为空，赋值双引号  
                    temp = "";  
                } else {  
                    int cellType = cell.getCellType();  
                    switch (cellType) {  
                    case Cell.CELL_TYPE_STRING:  
                        temp = cell.getStringCellValue().trim();  
                        temp = StringUtils.isEmpty(temp) ? "" : temp;  
                        break;  
                    case Cell.CELL_TYPE_BOOLEAN:  
                        temp = String.valueOf(cell.getBooleanCellValue());  
                        break;  
                    case Cell.CELL_TYPE_FORMULA:  
                        temp = String.valueOf(cell.getCellFormula().trim());  
                        break;  
                    case Cell.CELL_TYPE_NUMERIC:  
                        if (cell.getCellStyle().getDataFormat()==58) {  //对日期格式进行出里
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
                            double value = cell.getNumericCellValue();  
                            Date date = org.apache.poi.ss.usermodel.DateUtil  
                                    .getJavaDate(value);  
                            temp = sdf.format(date);  
                            
                        } else {  
                            temp = new DecimalFormat("#.######").format(cell.getNumericCellValue());  
                        }  
                        break;  
                    case Cell.CELL_TYPE_BLANK:  
                        temp = "";  
                        break;  
                    case Cell.CELL_TYPE_ERROR:  
                        temp = "ERROR";  
                        break;  
                    default:  
                        temp = cell.toString().trim();  
                        break;  
                    }  
                }  
                rowList.add(temp);  
            }  
            list.add(rowList);  
        }  
        return list;  
   }  
      
  /*  public static Workbook getWb(String path) {
  		try {
  			return WorkbookFactory.create(new File(path));
  		} catch (Exception e) {
  			throw new RuntimeException("读取EXCEL文件出错", e);
  		}
  	}*/

}