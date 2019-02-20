package com.data.services;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.channels.Channels;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.data.constant.GoogleCloudConstants;
import com.google.appengine.api.utils.SystemProperty;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.ReadChannel;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

@Service
public class StorageServiceImpl implements StorageService{

	private Storage storage = null;

	public StorageServiceImpl() {
		init();
	}

	private void init() {
		try {
			
			if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) {
				StorageOptions options = (StorageOptions) StorageOptions.getDefaultInstance().getService();
				this.storage = options.getService();
				 }
			else
			{
				StorageOptions options = StorageOptions.newBuilder().setProjectId(GoogleCloudConstants.PROJECT_ID)
						.setCredentials(
								GoogleCredentials.fromStream(new FileInputStream(GoogleCloudConstants.PATH_TO_JSON_KEY)))
						.build();
				this.storage = options.getService();
			}
			
		} catch (Exception ee) {

		}

	}
	
	@Override
	public String readFile(String fileName) {
		Blob blob = storage.get(GoogleCloudConstants.BUCKET_NAME, fileName);
		ReadChannel readChannel = blob.reader();
		InputStream inputStream = Channels.newInputStream(readChannel);
		String result = new BufferedReader(new InputStreamReader(inputStream)).lines()
				.collect(Collectors.joining("\n"));

		return result;
	}
	
	@Override
	public void writeToFile(String content, String fileName) {
		Bucket crateBucket = storage.get(GoogleCloudConstants.BUCKET_NAME);
		byte[] bytes = content.getBytes();
		crateBucket.create(fileName, bytes);
	}

}
