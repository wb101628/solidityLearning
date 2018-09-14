package com.poi.testpoi.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.poi.testpoi.service.BatchService;

@Controller
public class IndexController {

	@Autowired
	private BatchService batchService;
	
	@RequestMapping("/index")
	public String index () {
		return "index";
		
	}
	
	@RequestMapping(value = "/import", method = RequestMethod.POST)
	@ResponseBody
	public Object exImport( @RequestParam("file")MultipartFile uploadfile) {
		Map<String,Object> map = new HashMap<String,Object>();

		String fileName = uploadfile.getOriginalFilename();

		try {
			map = batchService.batchImport(fileName, uploadfile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}


}
