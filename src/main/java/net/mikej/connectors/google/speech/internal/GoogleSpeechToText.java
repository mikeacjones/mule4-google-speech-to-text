package net.mikej.connectors.google.speech.internal;

import org.mule.runtime.extension.api.annotation.Extension;
import org.mule.runtime.extension.api.annotation.connectivity.ConnectionProviders;
import org.mule.runtime.extension.api.annotation.dsl.xml.Xml;

import net.mikej.connectors.google.speech.internal.connection.GoogleSpeechToTextConnectionProvider;

@Xml(prefix = "google-speech-to-text")
@Extension(name = "Google Speech to Text")
@ConnectionProviders({ GoogleSpeechToTextConnectionProvider.class })
public class GoogleSpeechToText {
	
}