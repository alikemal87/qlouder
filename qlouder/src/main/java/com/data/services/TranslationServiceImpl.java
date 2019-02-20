package com.data.services;

import java.io.FileInputStream;

import com.data.constant.GoogleCloudConstants;
import com.google.appengine.api.utils.SystemProperty;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.google.cloud.translate.Translate.TranslateOption;
import org.springframework.stereotype.Service;

@Service
public class TranslationServiceImpl implements TranslationService{
	
	private Translate translate ;
	public TranslationServiceImpl() {
		init();
	}

	private void init() {
		try {
			if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) {
				TranslateOptions tOptions = (TranslateOptions) TranslateOptions.getDefaultInstance().getService();
				this.translate = tOptions.getService();
				 }
			else
			{
			TranslateOptions tOptions = TranslateOptions.newBuilder().
					setProjectId(GoogleCloudConstants.PROJECT_ID).setCredentials(GoogleCredentials.fromStream(
			                    new FileInputStream(GoogleCloudConstants.PATH_TO_JSON_KEY))).build();
			
			 this.translate = tOptions.getService();
			}
		} catch (Exception ee) {

		}

	}

	
	@Override
	public String translate(String content,String toLanguage)
	{
		 Translation translation =
			        translate.translate(
			        		content,
			            TranslateOption.sourceLanguage(GoogleCloudConstants.FROM_LANGUAGE),
			            TranslateOption.targetLanguage(toLanguage));
		 
		 return translation.getTranslatedText();
	}
}
