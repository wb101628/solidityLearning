package com.poi.testpoi.service.Impl;

import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.web3j.utils.Convert;

import com.poi.testpoi.common.MyException;
import com.poi.testpoi.mapper.BTMapper;
import com.poi.testpoi.pojo.BatchTransfer;
import com.poi.testpoi.service.BatchService;

@Service
public class BatchServiceImpl implements BatchService {
	
	@Autowired
	private BTMapper bTMapper;

	@Transactional(readOnly = false,rollbackFor = Exception.class)
	@Override
	public Map<String,Object> batchImport(String fileName, MultipartFile file) throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		List<String> listAccounts = new ArrayList<String>();
		List<BigInteger> listCounts = new ArrayList<BigInteger>();
		double total = 0;
		List<BatchTransfer> btList = new ArrayList<BatchTransfer>();
		if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
			throw new MyException("上传文件格式不正确");
		}
		boolean isExcel2003 = true;
		if (fileName.matches("^.+\\.(?i)(xlsx)$")) {
			isExcel2003 = false;
		}
		InputStream is = file.getInputStream();
		Workbook wb = null;
		if (isExcel2003) {
			wb = new HSSFWorkbook(is);
		} else {
			wb = new XSSFWorkbook(is);
		}
		Sheet sheet = wb.getSheetAt(0);
		if(sheet == null){
			throw new MyException("未找到");
		}
		BatchTransfer bt;
		for (int r = 0; r <= sheet.getLastRowNum(); r++) {//r = 2 表示从第三行开始循环 如果你的第三行开始是数据
			Row row = sheet.getRow(r);//通过sheet表单对象得到 行对象
			if (row == null){
				continue;
			}

			//sheet.getLastRowNum() 的值是 10，所以Excel表中的数据至少是10条；不然报错 NullPointerException

			bt = new BatchTransfer();

			if( row.getCell(0).getCellType() !=1){//循环时，得到每一行的单元格进行判断
				throw new MyException("导入失败(第"+(r+1)+"行,用户名请设为文本格式)");
			}
			String account = row.getCell(0).getStringCellValue();//得到每一行第一个单元格的值
			if(account == null || account.isEmpty()){//判断是否为空
				throw new MyException("导入失败(第"+(r+1)+"行,用户名未填写)");
			}
			row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);//得到每一行的 第二个单元格的值
			String count = row.getCell(1).getStringCellValue();
			if(count==null || count.isEmpty()){
				throw new MyException("导入失败(第"+(r+1)+"行,密码未填写)");
			}
			
			listAccounts.add(account);
			listCounts.add(Convert.toWei(count, Convert.Unit.ETHER).toBigInteger());
			total = total + Double.valueOf(count);
			
			//完整的循环一次 就组成了一个对象
			bt.setAccount(account);
			bt.setCount(count);
			bt.setStatus(2);
			btList.add(bt);
		}
		for (BatchTransfer bTransfer : btList) {
			bTMapper.addBT(bTransfer);
		}
		map.put("accounts", listAccounts);
		map.put("counts", listCounts);
		map.put("total", Convert.toWei(String.valueOf(total), Convert.Unit.ETHER).toBigInteger());
		return map;
	}

}
