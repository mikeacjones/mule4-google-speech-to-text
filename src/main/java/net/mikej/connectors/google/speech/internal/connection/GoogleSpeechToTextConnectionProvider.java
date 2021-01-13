package net.mikej.connectors.google.speech.internal.connection;

import static org.mule.runtime.api.meta.model.display.PathModel.Type.FILE;
import static org.mule.runtime.api.meta.model.display.PathModel.Location.EXTERNAL;

import java.io.IOException;

import org.mule.runtime.api.connection.CachedConnectionProvider;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.connection.ConnectionValidationResult;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Path;

import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;

public class GoogleSpeechToTextConnectionProvider implements CachedConnectionProvider<GoogleSpeechToTextClient> {

	@Parameter
	@Path(type = FILE, location = EXTERNAL)
	@DisplayName("Google JSON Token File")
	private String jsonTokenFile;

	@Parameter
	@DisplayName("Session ID")
	private String sessionID;

	@Parameter
	@DisplayName("Sample Rate (HZ)")
	private int sampleRate;

	@Parameter
	@DisplayName("Encoding")
	private AudioEncoding audioEncoding;

	@Parameter
	@DisplayName("Language Code")
	private String languageCode = "en-US";

	@Override
	public GoogleSpeechToTextClient connect() throws ConnectionException {
		GoogleSpeechToTextClient client = new GoogleSpeechToTextClient(jsonTokenFile, sessionID, sampleRate,
				audioEncoding, languageCode);
		return client;
	}

	@Override
	public void disconnect(GoogleSpeechToTextClient arg0) {
		// TODO Auto-generated method stub
		arg0.close();
	}

	@Override
	public ConnectionValidationResult validate(GoogleSpeechToTextClient arg0) {
		// TODO Auto-generated method stub
		try {
			arg0.connect();
			boolean res = arg0.validate();
			arg0.close();
			return res ? ConnectionValidationResult.failure("Connection failed", null)
					: ConnectionValidationResult.success();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return ConnectionValidationResult.failure("Connection failed", e);
		}
	}

}