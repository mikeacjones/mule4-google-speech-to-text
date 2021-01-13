package net.mikej.connectors.google.speech.internal.operations;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;

import net.mikej.connectors.google.speech.internal.connection.GoogleSpeechToTextClient;

public class SpeechToTextOperations {

	@DisplayName("Send Audio Data")
	public void sendAudioData(@Connection GoogleSpeechToTextClient googleSpeechToTextClient,
			@DisplayName("Base 64 Encoded Data") String data) throws FileNotFoundException, IOException {
		googleSpeechToTextClient.sendData(data);
	}
	
	@DisplayName("Close Audio Transcription")
	public void closeAudioTranscription(@Connection GoogleSpeechToTextClient googleSpeechToTextClient) {
		googleSpeechToTextClient.close();
	}
}