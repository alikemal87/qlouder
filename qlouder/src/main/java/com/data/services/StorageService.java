package com.data.services;

public interface StorageService {

	 String readFile(String fileName);
	 
	 
	 void writeToFile(String content, String fileName);
}
