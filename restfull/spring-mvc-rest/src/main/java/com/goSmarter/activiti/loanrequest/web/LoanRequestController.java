package com.goSmarter.activiti.loanrequest.web;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.goSmarter.activiti.loanrequest.domain.LoanRequest;
import org.springframework.http.HttpStatus;

@Controller
public class LoanRequestController {

	@Autowired
	SqlMapClientTemplate ibatisTemplate;

	@InitBinder
	public void initBinder(WebDataBinder b) {
		b.registerCustomEditor(LoanRequest.class, new LoanRequestEditor());
	}

	private static Log logger = LogFactory.getLog(LoanRequestController.class);

	public LoanRequestController() {
	}

	@RequestMapping(value = "/loanrequests", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<LoanRequest> readAll() {
		List<LoanRequest> loanRequests = (List<LoanRequest>) ibatisTemplate
				.queryForList("GoSmarter.loanRequestList");

		return loanRequests;
	}

	@RequestMapping(value = "/loanrequests/{id}", method = RequestMethod.GET, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public LoanRequest read(@PathVariable Integer id) {
		assertNotNull(ibatisTemplate);

		LoanRequest loanRequest = (LoanRequest) ibatisTemplate.queryForObject(
				"GoSmarter.loanRequestDetails", id);

		return loanRequest;
	}

	@RequestMapping(value = "/loanrequests/create/{loanRequest}", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public boolean create(@PathVariable LoanRequest loanRequest) {

		ibatisTemplate.insert("GoSmarter.loanRequestInsert", loanRequest);

		return true;
	}

	@RequestMapping(value = "/loanrequests/update/{loanRequest}", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public boolean update(@PathVariable LoanRequest loanRequest) {
		logger.debug("Delegating to service to update existing PcUser");

		ibatisTemplate.update("GoSmarter.loanRequestUpdate", loanRequest);

		return true;
	}

	@RequestMapping(value = "/loanrequests/delete/{id}", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public boolean delete(@PathVariable Integer id) {
		logger.debug("Delegating to service to delete existing PcUser");
		ibatisTemplate.update("GoSmarter.loanRequestDelete", id);

		return true;
	}

}
