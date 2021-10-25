package com.data.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.data.services.StorageService;
import com.data.services.TranslationService;

@Controller
public class TranslationController {

	@Autowired
	private StorageService storageService;

	@Autowired
	private TranslationService translationService;

	@RequestMapping(method = RequestMethod.POST, value = "/translate/translateFile")
	@ResponseBody
	public ResponseEntity<Void> getParam(@RequestParam(required = true) String fileName,
			@RequestParam(required = true) String toLanguage) throws Exception {
		String[] newFileName;
		if (!fileName.endsWith(".txt"))
			throw new Exception("Wrong file name");

		try {
			String content = storageService.readFile(fileName);
			String translatedContent = translationService.translate(content, toLanguage);
			newFileName = fileName.split(".txt");
			storageService.writeToFile(translatedContent, newFileName[0] + "_" + toLanguage + ".txt");
		} catch (Exception ee) {
			throw ee;
		}

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("").buildAndExpand(newFileName).toUri();

		return ResponseEntity.created(location).build();

	}

}
