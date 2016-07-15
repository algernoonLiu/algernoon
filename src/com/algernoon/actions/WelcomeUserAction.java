package com.algernoon.actions;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;

import com.opensymphony.xwork2.ActionSupport;

//@Namespace("/User")
//@ResultPath(value="/")
public class WelcomeUserAction extends ActionSupport{

	private static final long serialVersionUID = -3228220621565800528L;
	
	private String username;

	@Override
//	@Action(value="Welcome", results={@Result(name="success",location="/welcome_user.jsp")})
	public String execute() throws Exception {
		return SUCCESS;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
