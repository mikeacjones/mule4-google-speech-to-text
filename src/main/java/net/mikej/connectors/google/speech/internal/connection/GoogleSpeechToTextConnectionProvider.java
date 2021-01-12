package net.mikej.connectors.google.speech.internal.connection;

import static org.mule.runtime.api.meta.model.display.PathModel.Type.FILE;
import static org.mule.runtime.api.meta.model.display.PathModel.Location.EXTERNAL;

import java.io.FileInputStream;
import java.io.IOException;

import org.mule.runtime.api.connection.CachedConnectionProvider;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.connection.ConnectionValidationResult;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Path;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechSettings;

public class GoogleSpeechToTextConnectionProvider implements CachedConnectionProvider<SpeechClient> {
	
	@Parameter
	@Path(type = FILE, location = EXTERNAL)
	@DisplayName("Google JSON Token File")
	private String jsonTokenFile;

	@Override
	public SpeechClient connect() throws ConnectionException {
		
		try {
			GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(jsonTokenFile));
			FixedCredentialsProvider credentialsProvider = FixedCredentialsProvider.create(credentials);
			SpeechSettings speechSettings = SpeechSettings.newBuilder().setCredentialsProvider(credentialsProvider).build();
			SpeechClient speechClient = SpeechClient.create(speechSettings);
			return speechClient;
		} catch (IOException e) {
			throw new ConnectionException(e);
		}
	}

	@Override
	public void disconnect(SpeechClient arg0) {
		// TODO Auto-generated method stub
		arg0.close();
	}

	@Override
	public ConnectionValidationResult validate(SpeechClient arg0) {
		// TODO Auto-generated method stub
		return arg0.isTerminated() ? ConnectionValidationResult.failure("Connection failed", null) : ConnectionValidationResult.success();
	}
	
}