package net.mikej.connectors.google.speech.api;

import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.StreamingRecognitionResult;
import com.google.cloud.speech.v1.StreamingRecognizeResponse;

public class GoogleTextToSpeechResult {
	
	private float confidence;
	private String transcript;
	private boolean isFinal;
	
	public GoogleTextToSpeechResult(
			float confidence,
			String transcript,
			boolean isFinal
	) {
		this.confidence = confidence;
		this.transcript = transcript;
		this.isFinal = isFinal;
	}
	
	
	public static GoogleTextToSpeechResult fromResponse(StreamingRecognizeResponse response) {
		StreamingRecognitionResult result = response.getResultsList().get(0);
		SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
		
		return new GoogleTextToSpeechResult(
				alternative.getConfidence(),
				alternative.getTranscript(),
				result.getIsFinal()
		);
	}
	
	public float getConfidence() { return confidence; }
	public String getTranscript() { return transcript; }
	public boolean getIsFinal() { return isFinal; }
}