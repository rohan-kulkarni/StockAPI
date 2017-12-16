package com.admin;

import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.register.RegisterBean;
import com.update.UpdateDAO;
@ManagedBean
@SessionScoped
public class AdminBean {
	
	List<RegisterBean> list;
	
	public List<RegisterBean> getList() {
		return list;
	}

	public void setList(List<RegisterBean> list) {
		this.list = list;
	}

	public AdminBean() {
		getData();
	}
	
	public void getData() {
		AdminDAO b1=new AdminDAO();
		List<RegisterBean>b = b1.getData();
		this.list = b;
		System.out.println(list);
//		return this;
		
	}
	public void approve(String username) {
		AdminDAO b = new AdminDAO();
		if(b.approveStatus(username)){
			FacesContext facesContext = FacesContext.getCurrentInstance();
    		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Manager Approved Succesfully", "Manager Approved Succesfully"));
		}
		else {
			FacesContext facesContext = FacesContext.getCurrentInstance();
    		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Error", "Updated Succesfully"));
		}
	}
}
