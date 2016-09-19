package com.palm.yh.client.web.handler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.palm.vertx.auth.util.service.UserAuthService;
import com.palm.vertx.core.application.PalmVert;
import com.palm.vertx.web.support.HttpSupport;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
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

    @Override
    public void handle(RoutingContext routingContext) {
    	 logger.debug("进入excel",routingContext.fileUploads());
    	 Future<JsonObject> excelFuture = Future.future();
    	 try {
    		 excelFuture.complete(Rwriter());
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
    	 excelFuture.setHandler(handler ->{
	            logger.debug("handler:{}, result:{}", handler.succeeded(), handler.result());
	            httpSupport.sendJson(routingContext, handler.result());
	    });
   	 /*Set<FileUpload> fileUploadSet = routingContext.fileUploads();
        Iterator<FileUpload> fileUploadIterator = fileUploadSet.iterator();
        while (fileUploadIterator.hasNext()){
          FileUpload fileUpload = fileUploadIterator.next();

          // To get the uploaded file do
          Buffer uploadedFile = Vertx.vertx().fileSystem().readFileBlocking(fileUpload.uploadedFileName());
           logger.debug("uploadedFile:{}",uploadedFile);
          // Uploaded File Name
          try {
            String fileName = URLDecoder.decode(fileUpload.fileName(), "UTF-8");
            logger.debug("fileName:{}",fileName);
           
          } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
          }

          // Use the Event Bus to dispatch the file now
          // Since Event Bus does not support POJOs by default so we need to create a MessageCodec implementation
          // and provide methods for encode and decode the bytes
        }*/
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
     
    public static JsonObject Rwriter() throws FileNotFoundException {
		 	String path = "E:\\华东项目采购清单.xls";  
		 	//读取excel
		 	Workbook wbs = getWb(path);  
	        List<List<String>> list = getExcelRows(getSheet(wbs, 0), -1, -1);   //得到上传文件内容
	        
	        JsonObject result = new JsonObject();
	        int buffer[] = new int[5];
	        //第一行序号 
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
	            logger.debug("excel:{}",excel);
	            result.put(""+i+"", excel);
	        }
	        logger.debug("result:{}",result);
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
      
    public static Workbook getWb(String path) {
  		try {
  			return WorkbookFactory.create(new File(path));
  		} catch (Exception e) {
  			throw new RuntimeException("读取EXCEL文件出错", e);
  		}
  	}

}