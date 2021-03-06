package net.mikej.connectors.google.speech.internal;

import org.mule.runtime.extension.api.annotation.Extension;
import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.Sources;
import org.mule.runtime.extension.api.annotation.connectivity.ConnectionProviders;
import org.mule.runtime.extension.api.annotation.dsl.xml.Xml;

import net.mikej.connectors.google.speech.internal.connection.GoogleSpeechToTextConnectionProvider;
import net.mikej.connectors.google.speech.internal.operations.SpeechToTextOperations;
import net.mikej.connectors.google.speech.internal.source.SpeechResponseSource;

@Xml(prefix = "google-speech-to-text")
@Extension(name = "Google Speech to Text")
@ConnectionProviders({ GoogleSpeechToTextConnectionProvider.class })
@Sources({ SpeechResponseSource.class })
@Operations({ SpeechToTextOperations.class })
public class GoogleSpeechToText {

}