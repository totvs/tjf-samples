package com.tjf.sample.github.messaging.infrastructure.messaging;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Set;

import io.cloudevents.CloudEvent;
import io.cloudevents.CloudEventData;
import io.cloudevents.SpecVersion;

public class TOTVSCloudEvent implements CloudEvent {

	public TOTVSCloudEvent() {}
	
	private String id;
	
	@Override
	public SpecVersion getSpecVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public URI getSource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDataContentType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public URI getDataSchema() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSubject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OffsetDateTime getTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getAttribute(String attributeName) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getExtension(String extensionName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getExtensionNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CloudEventData getData() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setData(CloudEventData data) {
		System.out.println(data);		
	}
}
