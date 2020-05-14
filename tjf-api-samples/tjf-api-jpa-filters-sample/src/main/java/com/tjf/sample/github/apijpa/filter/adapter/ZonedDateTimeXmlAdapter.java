package com.tjf.sample.github.apijpa.filter.adapter;

import java.time.ZonedDateTime;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class ZonedDateTimeXmlAdapter extends XmlAdapter<String, ZonedDateTime> {

	@Override
	public ZonedDateTime unmarshal(String v) throws Exception {
		return ZonedDateTime.parse(v);
	}

	@Override
	public String marshal(ZonedDateTime v) throws Exception {
		return v.toString();
	}

}
