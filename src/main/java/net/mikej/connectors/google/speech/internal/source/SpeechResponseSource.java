package net.mikej.connectors.google.speech.internal.source;

import org.mule.runtime.api.connection.ConnectionProvider;
import org.mule.runtime.api.exception.MuleException;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.runtime.operation.Result;
import org.mule.runtime.extension.api.runtime.source.Source;
import org.mule.runtime.extension.api.runtime.source.SourceCallback;

import net.mikej.connectors.google.speech.api.GoogleTextToSpeechResult;
import net.mikej.connectors.google.speech.internal.connection.GoogleSpeechToTextClient;
import net.mikej.connectors.google.speech.internal.connection.SpeechResultListener;

public class SpeechResponseSource extends Source<GoogleTextToSpeechResult, Void> {

	@Connection
	private ConnectionProvider<GoogleSpeechToTextClient> googleConnectionProvider;
	private GoogleSpeechToTextClient googleSpeechtoTextClient;

	@Override
	public void onStart(SourceCallback<GoogleTextToSpeechResult, Void> arg0) throws MuleException {
		this.googleSpeechtoTextClient = googleConnectionProvider.connect();
		this.googleSpeechtoTextClient.setSpeechResultListener(new SpeechResultListener() {

			@Override
			public void onResponse(GoogleTextToSpeechResult recognitionResult) {
				arg0.handle(Result.<GoogleTextToSpeechResult, Void>builder().output(recognitionResult).build());
			}

		});
	}

	@Override
	public void onStop() {
		googleSpeechtoTextClient.close();
		googleSpeechtoTextClient = null;
	}

}