package com.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.data.controller.TranslationController;
import com.data.services.StorageService;
import com.data.services.TranslationService;

@RunWith(SpringRunner.class)
@WebMvcTest(TranslationController.class)
public class TranslationTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private StorageService storageService;

	@MockBean
	private TranslationService translationService;
	
	@Autowired
	private TranslationService tService;

	String exampleReq = "{\"fileName\":\"text1.txt\",\"toLanguage\":\"tr\"}";
	
	@Test
	public void translationTest() throws Exception {
		
		String result = tService.translate("gepubliceerd", "tr");
		
		Assert.assertEquals(result, "yayınlayan");
		
	}

	@Test
	public void postMethodTest() throws Exception {

		Mockito.when(storageService.readFile(Mockito.anyString())).thenReturn("gepubliceerd");
		
		Mockito.when(translationService.translate(Mockito.anyString(),Mockito.anyString())).thenReturn("yayınlayan");
		

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/translate/translateFile")
				.accept(MediaType.APPLICATION_JSON).content(exampleReq).contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.CREATED.value(), response.getStatus());
	}

}
