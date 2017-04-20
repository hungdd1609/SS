package com.goSmarter.activiti.loanrequest.web;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.io.StringReader;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.goSmarter.activiti.loanrequest.domain.LoanRequest;

public class LoanRequestEditor extends PropertyEditorSupport {

	ObjectMapper mapper = new ObjectMapper();

	@Override
	public void setAsText(String text) throws IllegalArgumentException {

		LoanRequest obj;
		try {
			obj = mapper.readValue(text, LoanRequest.class);
			setValue(obj);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String getAsText() {
		return getValue().toString();
	}

}
