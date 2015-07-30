package com.baodian.service.util.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.stereotype.Service;

import com.baodian.service.util.Export2ExcelManager;

@Service("export2ExcelManager")
public class Export2EcxelManagerImpl implements Export2ExcelManager {

	private InputStream excelStream;
	
	private String[] columnNames;
	private String[] columnMethods;
	
	public InputStream export2Excel(String[] columnNames,
			String[] columnMethods, List list) throws Exception{
		if(list == null){
			throw new Exception("Exception in export2Excel(columnNames,columnMethods,list),the list is null");
		}
		HSSFWorkbook workbook = getWorkBook(columnNames,columnMethods,list);
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		workbook.write(output);
		
		byte[] ba = output.toByteArray();
		setExcelStream(new ByteArrayInputStream(ba));
		output.flush();
		output.close();
		return excelStream;
	}
	
	public HSSFWorkbook getWorkBook(String[] columnNames,String[] columnMethods,List list) throws Exception{
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		
		//创建第1行，也就是输出表头
		HSSFRow row = sheet.createRow(0);  
		HSSFCell cell;
		HSSFCellStyle cellstyle = workbook.createCellStyle();
		HSSFFont font1 = workbook.createFont();
		for(int i=0;i < columnNames.length;i++){
			sheet.setColumnWidth(i, 5000);   //设置列宽
			row.setHeight((short) 1000);
			cell = row.createCell(i);    //创建第i列
			cell.setCellValue(new HSSFRichTextString(columnNames[i]));
			font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);   //粗体显示
			cellstyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);   //填充颜色
			cellstyle.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
			cellstyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);//下边框
			cellstyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
			cellstyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
			cellstyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
			cellstyle.setFont(font1);
			cellstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//左右居中      
			cellstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//上下居中 
			cell.setCellStyle(cellstyle);
		}
		
		//输出各行数据
		Object list_object;
		Object value;
		String cellvalue;
		cellstyle = workbook.createCellStyle();
		for(int i=0;i < list.size();i++){
			list_object = list.get(i);
			row = sheet.createRow(i+1);   //创建第i+1行
			for(int j = 0;j < columnMethods.length;j++){
				cell = row.createCell(j); //创建第j列
				Method method = list_object.getClass().getMethod(columnMethods[j]);//这里用到了反射机制，通过方法名来取得对应方法返回的结果对象
				value = method.invoke(list_object);
				if(value != null){
					cellvalue = value.toString();
				}else{
					cellvalue = "";
				}
				//System.out.println("----------method="+method+",cellvalue="+cellvalue);
				cellvalue = cellvalue.replace("\\r\\n", "\r\n");
				cellvalue = cellvalue.replace("\\\\", "\\");
				cellvalue = cellvalue.replace("\\/", "/");
				cellvalue = cellvalue.trim();
				cell.setCellValue(new HSSFRichTextString(cellvalue));
				cellstyle.setWrapText(true);    //设置自动换行
				cellstyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);//左对齐
				cellstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//上下居中 
				cell.setCellStyle(cellstyle);
			}
		}
		return workbook;
	}

	public String[] getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}

	public String[] getColumnMethods() {
		return columnMethods;
	}

	public void setColumnMethods(String[] columnMethods) {
		this.columnMethods = columnMethods;
	}

	public InputStream getExcelStream() {
		return excelStream;
	}

	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}

}
