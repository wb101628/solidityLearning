package com.poi.testpoi.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface BatchService {


	Map<String,Object> batchImport(String fileName, MultipartFile file) throws Exception;
}
