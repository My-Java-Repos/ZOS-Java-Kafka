package com.org.demo.kafka.consumer;

public class MFResponseRec {
	private String lst_nm;
	private String first_nm;
	private String policy_nbr;
	private String subscriber_id;
	private String dob;
	private String electronicAddress;
	private String paperlessIndicator;
	private String undeliveredReasonTypeCode;
	private String reqIdentifier;
	private String status;

	public String toOutput() {
		return lst_nm 
		   	 + first_nm
		 	 + policy_nbr 
			 + subscriber_id 
			 + dob
			 + electronicAddress 
			 + paperlessIndicator 
			 + undeliveredReasonTypeCode
			 + reqIdentifier;
		 }
	public String toError() {
		return lst_nm 
		   	 + first_nm
		 	 + policy_nbr 
			 + subscriber_id 
			 + dob
			 + reqIdentifier
			 + status;
	}
	public String getReqIdentifier() {
		return reqIdentifier;
	}
	public void setReqIdentifier(String reqIdentifier) {
		this.reqIdentifier = reqIdentifier;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLst_nm() {
		return lst_nm;
	}
	public void setLst_nm(String lst_nm) {
		this.lst_nm = lst_nm;
	}
	public String getFirst_nm() {
		return first_nm;
	}
	public void setFirst_nm(String first_nm) {
		this.first_nm = first_nm;
	}
	public String getPolicy_nbr() {
		return policy_nbr;
	}
	public void setPolicy_nbr(String policy_nbr) {
		this.policy_nbr = policy_nbr;
	}
	public String getSubscriber_id() {
		return subscriber_id;
	}
	public void setSubscriber_id(String subscriber_id) {
		this.subscriber_id = subscriber_id;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getElectronicAddress() {
		return electronicAddress;
	}
	public void setElectronicAddress(String electronicAddress) {
		this.electronicAddress = electronicAddress;
	}
	public String getpaperlessIndicator() {
		return paperlessIndicator;
	}
	public void setpaperlessIndicator(String paperlessIndicator) {
		this.paperlessIndicator = paperlessIndicator;
	}
	public String getUndeliveredReasonTypeCode() {
		return undeliveredReasonTypeCode;
	}
	public void setUndeliveredReasonTypeCode(String undeliveredReasonTypeCode) {
		this.undeliveredReasonTypeCode = undeliveredReasonTypeCode;
	}
}

