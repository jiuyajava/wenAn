
package utils;

import clover.org.apache.commons.collections.CollectionUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;

/**
 *
 * @Title : ImportUtils.java
 * @Package : com.odianyun.finance.business.common.utils
 * @Description :
 * @version : V1.0
 */
public class ExcelExportUtils {

	/**
	 * 获得导出的Excel对象
	 * @Description :
	 * @param : headTitle 列表头 | outputList 查询出的List
	 * @return : SXSSFWorkbook
	 * @throws :
	 */
	public static <T> SXSSFWorkbook getWorkbook(String[] headTitleArr, List<T> outputList,
			List<String> topInfoList,SXSSFWorkbook workbook,String sheetName) throws Exception {
		if(workbook==null) {
			workbook = new SXSSFWorkbook();
		}
		if(headTitleArr == null || headTitleArr.length == 0){
			return workbook;
		}
		Sheet sheet = null;
		if(sheetName==null){
			sheet=workbook.createSheet();
		}else{
			sheetName = sheetName.replaceAll("/", "").replaceAll("\\\\", "").replaceAll("\\?", "").
				replaceAll("\\*", "").replaceAll("\\[", "").replaceAll("\\]", "");
			sheet=workbook.createSheet(sheetName);
		}
		int rowNum = 0;
		if(CollectionUtils.isNotEmpty(topInfoList)){
			for(String str: topInfoList){
				Row row = sheet.createRow(rowNum);// 创建一行
				Cell cell=row.createCell(0);
				cell.setCellValue(str);
				rowNum++;
			}
		}
		List<ExcelVo> heads = new ArrayList<>();
		for(int i=0; i<headTitleArr.length; i++){//根据顺序
			heads.add(new ExcelVo(headTitleArr[i], HSSFCell.CELL_TYPE_STRING,""));
		}
		generateNoHeaderStyleExcelHeader(sheet, heads, workbook,rowNum++);
		//解决poi自动将0.00转换成0的问题
		DataFormat df = workbook.createDataFormat();  //此处设置数据格式
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setDataFormat(df.getFormat("#,#0.00")); //小数点后保留两位，可以写contentStyle.setDataFormat(df.getFormat("#,#0.00"));
		if(CollectionUtils.isNotEmpty(outputList)){
			for(Object obj: outputList){
				Row row = sheet.createRow(rowNum++);// 创建一行
				//generRowByStrArr(row, heads, obj,cellStyle);
			}
		}
		return workbook;
	}

	/**
	 * 获得导出的Excel对象
	 * @Description :
	 * @param : headMap 列表头 | outputList 查询出的List
	 * @return : SXSSFWorkbook
	 * @throws :
	 */
	public static <T> SXSSFWorkbook getWorkbook(Map<String, String> headMap, List<T> outputList, ExportRowHandle exportRowHandle) throws Exception {
		return ExcelExportUtils.getWorkbook(headMap,outputList,null,null,null, exportRowHandle);
	}

	/**
	 * 获得导出的Excel对象
	 * @Description :
	 * @param : headMap 列表头 | outputList 查询出的List
	 * @param workbook
	 * @param 秒杀
	 * @return : SXSSFWorkbook
	 * @throws :
	 */
	public static <T> SXSSFWorkbook getWorkbook(Map<String, String> headMap, List<T> outputList, SXSSFWorkbook workbook, String name) throws Exception {
		return ExcelExportUtils.getWorkbook(headMap,outputList,null,workbook,name, null);
	}

	/**
	 *  获得导出的Excel对象
	 * @param headMap headMap 列表头
	 * @param outputList 查询出的List
	 */
	public static <T> SXSSFWorkbook getWorkbook(Map<String, String> headMap, List<T> outputList,
												List<String> topInfoList,SXSSFWorkbook workbook,String sheetName) throws Exception {
		return getWorkbook(headMap, outputList, topInfoList, workbook, sheetName, null);
	}

	/**
	 *  获得导出的Excel对象
	 * @param headMap headMap 列表头
	 * @param outputList 查询出的List
	 * @param topInfoList
	 * @param workbook
	 * @param sheetName
	 * @param <T>
	 * @return
	 * @throws Exception
	 */
	public static <T> SXSSFWorkbook getWorkbook(Map<String, String> headMap, List<T> outputList,
			List<String> topInfoList,SXSSFWorkbook workbook,String sheetName, ExportRowHandle exportRowHandle) throws Exception {
		if(workbook==null) {
			workbook = new SXSSFWorkbook();
		}
		if(headMap == null || headMap.isEmpty()){
			return workbook;
		}
		Sheet sheet = null;
		if(sheetName==null){
			sheet=workbook.createSheet();
		}else{
			sheetName = sheetName.replaceAll("/", "").replaceAll("\\\\", "").replaceAll("\\?", "").
				replaceAll("\\*", "").replaceAll("\\[", "").replaceAll("\\]", "");
			sheet=workbook.createSheet(sheetName);
		}
		int rowNum = 0;
		if(CollectionUtils.isNotEmpty(topInfoList)){
			for(String str: topInfoList){
				Row row = sheet.createRow(rowNum);// 创建一行
				Cell cell=row.createCell(0);
				cell.setCellValue(str);
				rowNum++;
			}
		}

		List<ExcelVo> heads = new ArrayList<>();
		Map<String,String> formats = new HashMap<>();
		if(headMap instanceof LinkedHashMap){
		    for (String key : headMap.keySet()) {
		    	String field=key;
				if(key.indexOf('|')!=-1){
					field=key.substring(0, key.indexOf("|"));
					formats.put(field,key.substring(key.lastIndexOf("|")+1));  //时间格式
				}
				heads.add(new ExcelVo(String.valueOf(headMap.get(key)), HSSFCell.CELL_TYPE_STRING, field));
		     }
		}else{
			for(int i=0; i<headMap.size(); i++){//根据顺序添加
				Iterator it = headMap.keySet().iterator();
				while (it.hasNext()) {
					String key = it.next().toString();
					String index = key.substring(0, key.indexOf("|"));
					String field=key.substring(key.indexOf("|")+1);
					if(index.equals(String.valueOf(i))){
						if(field.indexOf('|')!=-1){
							field=field.substring(0, field.indexOf("|"));  //域名
							formats.put(field,key.substring(key.lastIndexOf("|")+1));  //格式
						}
						heads.add(new ExcelVo(String.valueOf(headMap.get(key)), HSSFCell.CELL_TYPE_STRING, field));
						//break;//只要填写了index就可以匹配到
					}
				}
			}
		}
		generateExcelHeader(sheet, heads, workbook,rowNum++);
		//解决poi自动将0.00转换成0的问题
		DataFormat df = workbook.createDataFormat();  //此处设置数据格式
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setDataFormat(df.getFormat("#,#0.00")); //小数点后保留两位，可以写contentStyle.setDataFormat(df.getFormat("#,#0.00"));
		if(CollectionUtils.isNotEmpty(outputList)){
			for(Object obj: outputList){
				Row row = sheet.createRow(rowNum++);// 创建一行
				generRow(row, heads, obj,cellStyle,formats, exportRowHandle);
			}
		}
		return workbook;
	}

	public static <T> SXSSFWorkbook getWorkbook1(Map<String, String> headMap, List<T> outputList) throws Exception {
		return ExcelExportUtils.getWorkbook1(headMap,outputList,null,null,null);
	}
	public static <T> SXSSFWorkbook getWorkbook1(Map<String, String> headMap, List<T> outputList,
												List<String> topInfoList,SXSSFWorkbook workbook,String sheetName) throws Exception {
		if(workbook==null) {
			workbook = new SXSSFWorkbook();
		}
		if(headMap == null || headMap.isEmpty()){
			return workbook;
		}
		Sheet sheet = null;
		if(sheetName==null){
			sheet=workbook.createSheet();
		}else{
			sheetName = sheetName.replaceAll("/", "").replaceAll("\\\\", "").replaceAll("\\?", "").
					replaceAll("\\*", "").replaceAll("\\[", "").replaceAll("\\]", "");
			sheet=workbook.createSheet(sheetName);
		}
		int rowNum = 0;
		if(CollectionUtils.isNotEmpty(topInfoList)){
			for(String str: topInfoList){
				Row row = sheet.createRow(rowNum);// 创建一行
				Cell cell=row.createCell(0);
				cell.setCellValue(str);
				rowNum++;
			}
		}

		List<ExcelVo> heads = new ArrayList<>();
		Map<String,String> formats = new HashMap<>();
		if(headMap instanceof LinkedHashMap){
			for (String key : headMap.keySet()) {
				String field=key;
				if(key.indexOf('|')!=-1){
					field=key.substring(0, key.indexOf("|"));
					formats.put(field,key.substring(key.lastIndexOf("|")+1));  //时间格式
				}
				heads.add(new ExcelVo(String.valueOf(headMap.get(key)), HSSFCell.CELL_TYPE_STRING, field));
			}
		}else{
			for(int i=0; i<headMap.size(); i++){//根据顺序添加
				Iterator it = headMap.keySet().iterator();
				while (it.hasNext()) {
					String key = it.next().toString();
					String index = key.substring(0, key.indexOf("|"));
					String field=key.substring(key.indexOf("|")+1);
					if(index.equals(String.valueOf(i))){
						if(field.indexOf('|')!=-1){
							field=field.substring(0, field.indexOf("|"));  //域名
							formats.put(field,key.substring(key.lastIndexOf("|")+1));  //格式
						}
						heads.add(new ExcelVo(String.valueOf(headMap.get(key)), HSSFCell.CELL_TYPE_STRING, field));
						//break;//只要填写了index就可以匹配到
					}
				}
			}
		}
		generateExcelHeader(sheet, heads, workbook,rowNum++);
		//解决poi自动将0.00转换成0的问题
		//DataFormat df = workbook.createDataFormat();  //此处设置数据格式
		CellStyle cellStyle = workbook.createCellStyle();
		//cellStyle.setDataFormat(df.getFormat("#,#0.000000")); //小数点后保留六位，可以写contentStyle.setDataFormat(df.getFormat("#,#0.00"));
		if(CollectionUtils.isNotEmpty(outputList)){
			for(Object obj: outputList){
				Row row = sheet.createRow(rowNum++);// 创建一行
				//generRow1(row, heads, obj,cellStyle,formats);
			}
		}
		return workbook;
	}

	private static void generRow(Row row, List<ExcelVo> header, Object obj, CellStyle cellStyle, Map<String, String> formats, ExportRowHandle rowHandle) throws Exception{
		for (int j = 0; j < header.size(); j++) {
			int cellType = header.get(j).getRowType();
			Cell cell = row.createCell(j);// 创建一列
			cell.setCellType(header.get(j).getRowType());
			String field=header.get(j).getRowNameEg();
			Method  method  = obj.getClass().getDeclaredMethod("get"+FinNumUtils.toUpperCaseFirstOne(field) );
			method.setAccessible(true);
			Object value = method.invoke(obj);
			if(null==value){
				value="";
			}
			header.get(j).getRowType();
			if (rowHandle != null && rowHandle.rowDataHandle(field, value, cell)) {
			} else if(value instanceof String){
				cell.setCellValue(String.valueOf(value));
			}else if(value instanceof Integer){
				cell.setCellValue((Integer)value);
			}else if(value instanceof Double){
				cell.setCellStyle(cellStyle);
				cell.setCellValue((Double)value);
			}else if(value instanceof Long){
				cell.setCellValue(String.valueOf((Long)value));
			}else if(value instanceof Boolean){
				cell.setCellValue((Boolean)value);
			}else if(value instanceof BigDecimal){
				cell.setCellStyle(cellStyle);
				cell.setCellValue(FinNumUtils.transMoneyForExport((BigDecimal)value));
			}else if(value instanceof Date){
				if(formats!=null&&!formats.isEmpty()&&formats.get(field)!=null){  //判断是否指定了时间格式
					//cell.setCellValue(FinDateUtils.getStrByDateAndFormat((Date)value, formats.get(field)) );
				}else{
					//cell.setCellValue(DateUtils.convertDate2String((Date)value ));
				}
			}
		}
	}


	public static class ExcelVo {
		public ExcelVo(String rowNameCn, int rowType, String rowNameEg) {
			this.rowNameCn = rowNameCn;
			this.rowNameEg = rowNameEg;
			this.rowType = rowType;
		}
		/**字段中文描述*/
		private String rowNameCn;
		/**字段类型*/
		private int rowType;
		/**字段名*/
		private String rowNameEg;
		/**列宽*/
		private int cellWith = 25*256;

		public String getRowNameCn() {
			return rowNameCn;
		}

		public void setRowNameCn(String rowNameCn) {
			this.rowNameCn = rowNameCn;
		}

		public int getRowType() {
			return rowType;
		}

		public void setRowType(int rowType) {
			this.rowType = rowType;
		}

		public String getRowNameEg() {
			return rowNameEg;
		}

		public void setRowNameEg(String rowNameEg) {
			this.rowNameEg = rowNameEg;
		}

		public int getCellWith() {
			return cellWith;
		}

		public void setCellWith(int cellWith) {
			this.cellWith = cellWith;
		}
	}



	private static void generateNoHeaderStyleExcelHeader(Sheet sheet, List<ExcelVo> header,
			SXSSFWorkbook workbook,int rowNum) {
		CellStyle headerStyle = workbook.createCellStyle();// 单元格样式
		Font font = workbook.createFont();// 字体样式
		font.setFontHeightInPoints((short) 12);
		headerStyle.setFont(font);
		if (header != null && header.size() > 0) {// 生成头行
			Row row = sheet.createRow(rowNum);// 创建一行
			for (int i = 0; i < header.size(); i++) {
				Cell cell = row.createCell(i);// 创建一列
				cell.setCellType(header.get(i).getRowType());
				cell.setCellValue(header.get(i).getRowNameCn());
				cell.setCellStyle(headerStyle);
				sheet.setColumnWidth(i, header.get(i).getCellWith());
			}
		}
	}

	private static void generateExcelHeader(Sheet sheet, List<ExcelVo> header,
			SXSSFWorkbook workbook,int rowNum) {
		CellStyle headerStyle = workbook.createCellStyle();// 单元格样式
		Font font = workbook.createFont();// 字体样式
		font.setFontHeightInPoints((short) 12);
		//font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		headerStyle.setFont(font);
		if (header != null && header.size() > 0) {// 生成头行
			Row row = sheet.createRow(rowNum);// 创建一行
			for (int i = 0; i < header.size(); i++) {
				Cell cell = row.createCell(i);// 创建一列
				cell.setCellType(header.get(i).getRowType());
				cell.setCellValue(header.get(i).getRowNameCn());
				cell.setCellStyle(headerStyle);
				sheet.setColumnWidth(i, header.get(i).getCellWith());
			}
		}
	}


	private static void generateExcelHeaderForReport(Sheet sheet, List<ExcelVo> header,
			SXSSFWorkbook workbook,int rowNum) {
		CellStyle headerStyle = workbook.createCellStyle();// 单元格样式
		Font font = workbook.createFont();// 字体样式
		font.setFontHeightInPoints((short) 12);
		//font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		headerStyle.setFont(font);
		headerStyle.setAlignment(HorizontalAlignment.CENTER);//左右居中
		headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);//上下居中
		headerStyle.setBorderBottom(BorderStyle.THIN); //下边框
		headerStyle.setBorderLeft(BorderStyle.THIN);//左边框
		headerStyle.setBorderTop(BorderStyle.THIN);//上边框
		headerStyle.setBorderRight(BorderStyle.THIN);//右边框
		if (header != null && header.size() > 0) {// 生成头行
			Row row = sheet.createRow(rowNum);// 创建一行
			for (int i = 0; i < header.size(); i++) {
				Cell cell = row.createCell(i);// 创建一列
				cell.setCellType(header.get(i).getRowType());
				cell.setCellValue(header.get(i).getRowNameCn());
				cell.setCellStyle(headerStyle);
				sheet.setColumnWidth(i, header.get(i).getCellWith());
			}
		}
	}





}
