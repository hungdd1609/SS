package com.goSmarter.activiti.loanrequest.domain;

public class LoanRequest {

	private String processId = "";

	private String customerName = "krishna";

	//private Double amount = 0.0;

	private Integer id = 1;

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

/*	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
*/
	public Integer getId() {
		// TODO Auto-generated method stub
		return this.id;
	}

	public String toString() {
		return "{\"processId\":\"" + processId + "\",\"customerName\":\""
				+ customerName + ",\"id\":" + id
				+ "}";

	}

	public void setId(int id) {
		this.id = id;
	}
}
