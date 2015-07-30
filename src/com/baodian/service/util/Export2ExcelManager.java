package com.baodian.service.util;

import java.io.InputStream;
import java.util.List;

public interface Export2ExcelManager {
	public InputStream export2Excel(String[] columnNames,String[] columnMethods,List list) throws Exception;
}
