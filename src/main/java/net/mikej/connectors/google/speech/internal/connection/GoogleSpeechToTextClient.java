package net.mikej.connectors.google.speech.internal.connection;

import java.io.Console;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Base64;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.gax.rpc.ClientStream;
import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.StreamController;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechSettings;
import com.google.cloud.speech.v1.StreamingRecognitionConfig;
import com.google.cloud.speech.v1.StreamingRecognizeResponse;
import com.google.protobuf.ByteString;

import net.mikej.connectors.google.speech.api.GoogleTextToSpeechResult;

import com.google.cloud.speech.v1.StreamingRecognitionResult;
import com.google.cloud.speech.v1.StreamingRecognizeRequest;

public class GoogleSpeechToTextClient {
	private SpeechClient speechClient;
	private ResponseObserver<StreamingRecognizeResponse> responseObserver;
	private ClientStream<StreamingRecognizeRequest> clientStream;
	private String jsonKeyPath;
	private String sessionID;
	private int sampleRate;
	private AudioEncoding audioEncoding;
	private String languageCode;
	private boolean connectionOpen;

	public GoogleSpeechToTextClient(String jsonKeyPath, String sessionID, int sampleRate, AudioEncoding audioEncoding,
			String languageCode) {
		this.jsonKeyPath = jsonKeyPath;
		this.sessionID = sessionID;
		this.sampleRate = sampleRate;
		this.audioEncoding = audioEncoding;
		this.languageCode = languageCode;
	}

	public String getSessionID() {
		return sessionID;
	}

	public void connect() throws FileNotFoundException, IOException {
		GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(this.jsonKeyPath));
		FixedCredentialsProvider credentialsProvider = FixedCredentialsProvider.create(credentials);
		SpeechSettings speechSettings = SpeechSettings.newBuilder().setCredentialsProvider(credentialsProvider).build();
		this.speechClient = SpeechClient.create(speechSettings);
		this.responseObserver = new ResponseObserver<StreamingRecognizeResponse>() {

			@Override
			public void onComplete() {
				connectionOpen = false;
			}

			@Override
			public void onError(Throwable arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onResponse(StreamingRecognizeResponse arg0) {
				if (resultListener != null)
					resultListener.onResponse(GoogleTextToSpeechResult.fromResponse(arg0));
			}

			@Override
			public void onStart(StreamController arg0) {
				connectionOpen = true;
			}
			

		};
	}

	public void start() throws FileNotFoundException, IOException {
		if (this.speechClient == null) connect();
		
		this.clientStream = this.speechClient.streamingRecognizeCallable().splitCall(this.responseObserver);

		RecognitionConfig recognitionConfig = RecognitionConfig.newBuilder().setEncoding(this.audioEncoding)
				.setLanguageCode(this.languageCode).setSampleRateHertz(this.sampleRate).setEnableAutomaticPunctuation(true).build();
		StreamingRecognitionConfig streamingRecognitionConfig = StreamingRecognitionConfig.newBuilder()
				.setConfig(recognitionConfig).setInterimResults(true).build();
		StreamingRecognizeRequest request = StreamingRecognizeRequest.newBuilder()
				.setStreamingConfig(streamingRecognitionConfig).build();
		this.clientStream.send(request);
	}

	public void sendData(String data) throws FileNotFoundException, IOException {
		if (!connectionOpen)
			start();
		byte[] decodedData = Base64.getDecoder().decode(data);
		ByteString byteString = ByteString.copyFrom(decodedData);
		StreamingRecognizeRequest request = StreamingRecognizeRequest.newBuilder()
				.setAudioContent(byteString).build();
		this.clientStream.send(request);
	}

	public void close() {
		if (responseObserver != null)
			responseObserver.onComplete();
		if (speechClient != null)
			speechClient.close();

		responseObserver = null;
		speechClient = null;
	}

	public boolean validate() {
		return speechClient.isTerminated();
	}

	private SpeechResultListener resultListener;

	public void setSpeechResultListener(SpeechResultListener listener) {
		this.resultListener = listener;
	}
}