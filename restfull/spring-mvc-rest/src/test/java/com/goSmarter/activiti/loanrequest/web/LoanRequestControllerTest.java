/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.goSmarter.activiti.loanrequest.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;

import com.goSmarter.activiti.loanrequest.domain.LoanRequest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = WebContextLoader.class, value = {
		"classpath:META-INF/spring/applicationContext.xml",
		"classpath:META-INF/spring/test-datasource-config.xml"})
public class LoanRequestControllerTest {

	private static Log logger = LogFactory
			.getLog(LoanRequestControllerTest.class);

	MockHttpServletRequest requestMock;
	MockHttpServletResponse responseMock;
	AnnotationMethodHandlerAdapter handlerAdapter;
	ObjectMapper mapper;
	LoanRequest loanRequest;

	@Autowired
	LoanRequestController loanRequestController;

	@Autowired
	SqlMapClientTemplate ibatisTemplate;

	@Before
	public void setUp() {
		requestMock = new MockHttpServletRequest();
		requestMock.setContentType(MediaType.APPLICATION_JSON_VALUE);
		requestMock.setAttribute(HandlerMapping.INTROSPECT_TYPE_LEVEL_MAPPING, Boolean.FALSE);
		
		responseMock = new MockHttpServletResponse();

		handlerAdapter = new AnnotationMethodHandlerAdapter();
		HttpMessageConverter[] messageConverters = {new MappingJacksonHttpMessageConverter()};
		handlerAdapter.setMessageConverters(messageConverters);

		mapper = new ObjectMapper();
		loanRequest = new LoanRequest();
	}
	
	@After
	public void tearDown(){
		ibatisTemplate.delete("GoSmarter.loanRequestDeleteAll");
	}

	@Test
	public void testGetLoanRequest() throws Exception {
		LoanRequest loanRequest1 = new LoanRequest();
		loanRequest1.setId(2);
		ibatisTemplate.insert("GoSmarter.loanRequestInsert", loanRequest1);

		requestMock.setMethod("GET");
	    requestMock.setRequestURI("/loanrequests/2");

	    handlerAdapter.handle(requestMock, responseMock, loanRequestController);
		logger.debug(responseMock.getContentAsString());
	    
	    LoanRequest loanRequest = mapper.readValue(responseMock.getContentAsString(), LoanRequest.class);
	    assertNotNull(loanRequest);
	    assertEquals(loanRequest.getId(), Integer.valueOf(2));
	}

	@Test
	public void testListLoanRequest() throws Exception {
		LoanRequest loanRequest1 = new LoanRequest();
		ibatisTemplate.insert("GoSmarter.loanRequestInsert", loanRequest1);
		loanRequest1 = new LoanRequest();
		loanRequest1.setId(2);
		ibatisTemplate.insert("GoSmarter.loanRequestInsert", loanRequest1);

		requestMock.setMethod("GET");
	    requestMock.setRequestURI("/loanrequests");

	    handlerAdapter.handle(requestMock, responseMock, loanRequestController);
	    List<LoanRequest> loanRequests = mapper.readValue(responseMock.getContentAsString(), List.class);
	    assertNotNull(loanRequests);
	    assertEquals(loanRequests.size(), 2);
	}

	@Test
	public void testCreateLoanRequest() throws Exception {
		requestMock.setMethod("POST");

		String jsonPcUser = mapper.writeValueAsString(loanRequest);
		logger.debug(jsonPcUser);
		requestMock.setRequestURI("/loanrequests/create/" + jsonPcUser );

		handlerAdapter.handle(requestMock, responseMock, loanRequestController);
		
		logger.debug(responseMock.getContentAsString());
	    assertEquals(responseMock.getContentAsString(), "true");
	}

	@Test
	public void testListLoanUpdate() throws Exception {
		requestMock.setMethod("POST");

		LoanRequest loanRequest1 = new LoanRequest();
		ibatisTemplate.insert("GoSmarter.loanRequestInsert", loanRequest1);

		loanRequest1.setCustomerName("krishna prasad");
		String jsonPcUser = mapper.writeValueAsString(loanRequest1);
		logger.debug(jsonPcUser);
		requestMock.setRequestURI("/loanrequests/update/" + jsonPcUser );

		handlerAdapter.handle(requestMock, responseMock, loanRequestController);
		logger.debug(responseMock.getContentAsString());
	    assertEquals(responseMock.getContentAsString(), "true");
	    
		LoanRequest loanRequest2 = (LoanRequest) ibatisTemplate.queryForObject(
				"GoSmarter.loanRequestDetails", 1);
		assertEquals(loanRequest2.getCustomerName(), "krishna prasad");
	}

	@Test
	public void testListLoanDelete() throws Exception {
		LoanRequest loanRequest1 = new LoanRequest();
		ibatisTemplate.insert("GoSmarter.loanRequestInsert", loanRequest1);

		requestMock.setMethod("POST");

		requestMock.setRequestURI("/loanrequests/delete/1" );

		handlerAdapter.handle(requestMock, responseMock, loanRequestController);
		
		List<LoanRequest> loanRequests = (List<LoanRequest>) ibatisTemplate
				.queryForList("GoSmarter.loanRequestList");

		assertEquals(loanRequests.size(), 0);
	}
}
