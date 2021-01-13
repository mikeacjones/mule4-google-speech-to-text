package net.mikej.connectors.google.speech.internal.connection;

import net.mikej.connectors.google.speech.api.GoogleTextToSpeechResult;

public interface SpeechResultListener {
	void onResponse(GoogleTextToSpeechResult recognitionResult);
}