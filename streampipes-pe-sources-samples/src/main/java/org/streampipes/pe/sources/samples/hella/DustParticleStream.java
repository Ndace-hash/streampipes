package org.streampipes.pe.sources.samples.hella;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.streampipes.commons.Utils;
import org.streampipes.messaging.EventProducer;
import org.streampipes.messaging.kafka.StreamPipesKafkaProducer;
import org.streampipes.sdk.helpers.EpProperties;
import org.streampipes.model.impl.EventSchema;
import org.streampipes.model.impl.EventStream;
import org.streampipes.model.impl.eventproperty.EventProperty;
import org.streampipes.model.impl.eventproperty.EventPropertyPrimitive;
import org.streampipes.model.impl.graph.SepDescription;
import org.streampipes.model.vocabulary.SO;
import org.streampipes.model.vocabulary.XSD;
import org.streampipes.pe.sources.samples.config.SourcesConfig;
import org.streampipes.pe.sources.samples.adapter.CsvPublisher;
import org.streampipes.pe.sources.samples.adapter.CsvReadingTask;
import org.streampipes.pe.sources.samples.adapter.FolderReadingTask;
import org.streampipes.pe.sources.samples.adapter.LineParser;
import org.streampipes.pe.sources.samples.adapter.SimulationSettings;
import org.streampipes.pe.sources.samples.hella.parser.DustLineParser;

public class DustParticleStream extends AbstractHellaStream {

	public static final String dustFolder =System.getProperty("user.home") + File.separator +".streampipes" +File.separator +"sources" +File.separator +"data" +File.separator +"dust" +File.separator;

	@Override
	public EventStream declareModel(SepDescription sep) {
		
		EventStream stream = prepareStream(HellaVariables.Dust.topic());
		
		EventSchema schema = new EventSchema();
		List<EventProperty> eventProperties = new ArrayList<EventProperty>();
		eventProperties.add(EpProperties.stringEp("variable_type", SO.Text));
		eventProperties.add(new EventPropertyPrimitive(XSD._long.toString(), "variable_timestamp", "", Utils.createURI("http://schema.org/DateTime")));
		eventProperties.add(new EventPropertyPrimitive(XSD._string.toString(), "id", "", Utils.createURI(SO.Text)));
		eventProperties.add(new EventPropertyPrimitive(XSD._integer.toString(), "bin0", "", Utils.createURI(SO.Number)));
		eventProperties.add(new EventPropertyPrimitive(XSD._integer.toString(), "bin1", "", Utils.createURI(SO.Number)));
		eventProperties.add(new EventPropertyPrimitive(XSD._integer.toString(), "bin2", "", Utils.createURI(SO.Number)));
		eventProperties.add(new EventPropertyPrimitive(XSD._integer.toString(), "bin3", "", Utils.createURI(SO.Number)));
		eventProperties.add(new EventPropertyPrimitive(XSD._integer.toString(), "bin4", "", Utils.createURI(SO.Number)));
		eventProperties.add(new EventPropertyPrimitive(XSD._integer.toString(), "bin5", "", Utils.createURI(SO.Number)));
		eventProperties.add(new EventPropertyPrimitive(XSD._integer.toString(), "bin6", "", Utils.createURI(SO.Number)));
		eventProperties.add(new EventPropertyPrimitive(XSD._integer.toString(), "bin7", "", Utils.createURI(SO.Number)));
		eventProperties.add(new EventPropertyPrimitive(XSD._integer.toString(), "bin8", "", Utils.createURI(SO.Number)));
		eventProperties.add(new EventPropertyPrimitive(XSD._integer.toString(), "bin9", "", Utils.createURI(SO.Number)));
		eventProperties.add(new EventPropertyPrimitive(XSD._integer.toString(), "bin10", "", Utils.createURI(SO.Number)));
		eventProperties.add(new EventPropertyPrimitive(XSD._integer.toString(), "bin11", "", Utils.createURI(SO.Number)));
		eventProperties.add(new EventPropertyPrimitive(XSD._integer.toString(), "bin12", "", Utils.createURI(SO.Number)));
		eventProperties.add(new EventPropertyPrimitive(XSD._integer.toString(), "bin13", "", Utils.createURI(SO.Number)));
		eventProperties.add(new EventPropertyPrimitive(XSD._integer.toString(), "bin14", "", Utils.createURI(SO.Number)));
		eventProperties.add(new EventPropertyPrimitive(XSD._integer.toString(), "bin15", "", Utils.createURI(SO.Number)));
			
	
		
		schema.setEventProperties(eventProperties);
		stream.setEventSchema(schema);
		stream.setName(HellaVariables.Dust.eventName());
		stream.setDescription(HellaVariables.Dust.description());
		stream.setUri(sep.getUri() + "/dust");
		stream.setIconUrl(SourcesConfig.iconBaseUrl + "/icon-dust" +".png");
		
		return stream;
	}

	@Override
	public void executeStream() {
		
		System.out.println("Execute Dust replay");
		EventProducer publisher = new StreamPipesKafkaProducer(SourcesConfig.INSTANCE.getKafkaUrl(), HellaVariables.Dust.topic());
		
		//IMessagePublisher publisher = new ConsoleLoggingPublisher();
		
		LineParser dustLineParser = new DustLineParser();
		CsvReadingTask csvReadingTask = new CsvReadingTask(makeFolderReadingTasks(), ",", "variable_timestamp", dustLineParser, true);
				
		Thread mouldingReplayThread = new Thread(new CsvPublisher(publisher, csvReadingTask, SimulationSettings.PERFORMANCE_TEST));
		mouldingReplayThread.start();
	}

	private List<FolderReadingTask> makeFolderReadingTasks() {
	
		FolderReadingTask task = new FolderReadingTask(dustFolder, "hella-bin-all-ids-sorted-", "csv", 0, 0);
		
		return Arrays.asList(task);
	}

	@Override
	public boolean isExecutable() {
		return true;
	}

}