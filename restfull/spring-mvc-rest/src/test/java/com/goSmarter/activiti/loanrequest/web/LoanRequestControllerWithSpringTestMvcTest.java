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

import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.server.MockMvc;
import org.springframework.test.web.server.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.context.support.XmlWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;

import com.goSmarter.activiti.loanrequest.domain.LoanRequest;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(loader = XmlWebContextLoader.class , value = {
		"classpath:META-INF/spring/applicationContext.xml",
		"classpath:META-INF/spring/test-datasource-config.xml"})
public class LoanRequestControllerWithSpringTestMvcTest {

	private static Log logger = LogFactory
			.getLog(LoanRequestControllerWithSpringTestMvcTest.class);

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Autowired
	LoanRequestController loanRequestController;

	ObjectMapper mapper;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webApplicationContextSetup(this.wac)
				.build();

		mapper = new ObjectMapper();
	}

	@Test
	public void testList() throws Exception {
		mockMvc.perform(
				get("/loanrequests/list").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void testCreate() throws Exception {

		LoanRequest loanRequest = new LoanRequest();
		String jsonPcUser = mapper.writeValueAsString(loanRequest);

		logger.debug(jsonPcUser);
/*		mockMvc.perform(
				post("/loanrequests/create/" + jsonPcUser)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)).andExpect(
				status().isOk());
*/
		mockMvc.perform(
				post("loanrequests")
				.body(jsonPcUser.getBytes())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(
				status().isCreated());
	}
}
