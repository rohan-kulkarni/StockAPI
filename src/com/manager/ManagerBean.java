package com.manager;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.register.RegisterBean;
@ManagedBean
@SessionScoped
public class ManagerBean {
List<RegisterBean> list;
	
	public List<RegisterBean> getList() {
		return list;
	}

	public void setList(List<RegisterBean> list) {
		this.list = list;
	}

	public  ManagerBean() {
		getData();
	}
	
	public void getData() {
		ManagerDAO b1=new ManagerDAO();
		List<RegisterBean>b = b1.getData();
		this.list = b;
		System.out.println(list);
//		return this;
		
	}
	public void addManager(String username) {
		ManagerDAO b=new ManagerDAO();
		System.out.println(username);
//		AdminDAO b = new AdminDAO();
		if(b.addManager(username)){
			FacesContext facesContext = FacesContext.getCurrentInstance();
    		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Manager Added Succesfully", "Manager Approved Succesfully"));
		}
		else {
			FacesContext facesContext = FacesContext.getCurrentInstance();
    		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Error", "Updated Succesfully"));
		}
	}
}
