package net.mikej.connectors.google.speech.api;

import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.StreamingRecognitionResult;
import com.google.cloud.speech.v1.StreamingRecognizeResponse;

public class GoogleTextToSpeechResult {
	
	private float confidence;
	private String transcript;
	private int wordCount;
	private int resultsCount;
	
	public GoogleTextToSpeechResult(
			float confidence,
			String transcript,
			int wordCount,
			int resultsCount
	) {
		this.confidence = confidence;
		this.transcript = transcript;
		this.wordCount = wordCount;
		this.resultsCount = resultsCount;
	}
	
	
	public static GoogleTextToSpeechResult fromResponse(StreamingRecognizeResponse response) {
		StreamingRecognitionResult result = response.getResultsList().get(0);
		SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
		
		
		return new GoogleTextToSpeechResult(
				alternative.getConfidence(),
				alternative.getTranscript(),
				alternative.getWordsCount(),
				response.getResultsCount()
		);
	}
	
	public float getConfidence() { return confidence; }
	public String getTranscript() { return transcript; }
	public int getWordCount() { return wordCount; }
	public int getResultsCount() { return resultsCount; }
}