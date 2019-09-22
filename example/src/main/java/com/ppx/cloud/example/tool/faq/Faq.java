package com.ppx.cloud.example.tool.faq;

import java.util.ArrayList;
import java.util.List;

import com.ppx.cloud.base.jdbc.annotation.Column;
import com.ppx.cloud.base.jdbc.annotation.Id;
import com.ppx.cloud.base.jdbc.annotation.Table;

@Table("core_faq")
public class Faq {
	@Id
	private Integer faqId;
	private String faqTitle;
	private String faqCategory;
	private String faqTimeType;
	private String faqCreated;
	private String faqDescr;
	private String faqAnswer;
	
	@Column(readonly=true)
	private List<String> sub = new ArrayList<String>();
	@Column(readonly=true)
	private List<String> answerSub = new ArrayList<String>();

	public Integer getFaqId() {
		return faqId;
	}

	public void setFaqId(Integer faqId) {
		this.faqId = faqId;
	}

	public String getFaqTitle() {
		return faqTitle;
	}

	public void setFaqTitle(String faqTitle) {
		this.faqTitle = faqTitle;
	}

	public String getFaqCategory() {
		return faqCategory;
	}

	public void setFaqCategory(String faqCategory) {
		this.faqCategory = faqCategory;
	}

	public String getFaqTimeType() {
		return faqTimeType;
	}

	public void setFaqTimeType(String faqTimeType) {
		this.faqTimeType = faqTimeType;
	}

	public String getFaqCreated() {
		return faqCreated;
	}

	public void setFaqCreated(String faqCreated) {
		this.faqCreated = faqCreated;
	}

	public String getFaqDescr() {
	    faqDescr = (faqDescr == null ? "" : faqDescr);
		return faqDescr;
	}

	public void setFaqDescr(String faqDescr) {
		this.faqDescr = faqDescr;
	}

	public String getFaqAnswer() {
	    faqAnswer = (faqAnswer == null ? "" : faqAnswer);
		return faqAnswer;
	}

	public void setFaqAnswer(String faqAnswer) {
		this.faqAnswer = faqAnswer;
	}

	public List<String> getSub() {
		return sub;
	}

	public void setSub(List<String> sub) {
		this.sub = sub;
	}

	public List<String> getAnswerSub() {
		return answerSub;
	}

	public void setAnswerSub(List<String> answerSub) {
		this.answerSub = answerSub;
	}
	
}
