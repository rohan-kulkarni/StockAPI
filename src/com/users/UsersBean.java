package com.users;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.register.RegisterBean;

@ManagedBean
@SessionScoped
public class UsersBean {
List<RegisterBean> list;
String watchlist;
	
	public String getWatchlist() {
	return watchlist;
}

public void setWatchlist(String watchlist) {
	this.watchlist = watchlist;
}

	public List<RegisterBean> getList() {
		return list;
	}

	public void setList(List<RegisterBean> list) {
		this.list = list;
	}

	public UsersBean() {
		getData();
		getUserWatchlist();
	}
	
	public void getData() {
		UsersDAO b1=new UsersDAO();
		List<RegisterBean>b = b1.getData();
		this.list = b;
		System.out.println(list);
//		return this;
		
	}
	public String addToWatchlist(String sym) {
		UsersDAO b1=new UsersDAO();
		boolean watchlistdata=b1.addtoWatchlist(sym);
		if(watchlistdata) {
			
			FacesContext facesContext = FacesContext.getCurrentInstance();
    		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,sym+" Added Succesfully", "Manager Approved Succesfully"));
		}
		else {
			FacesContext facesContext = FacesContext.getCurrentInstance();
    		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Can be added only once", "error"));
		}
		return "Watchlist";
	}
	public void getUserWatchlist() {
		UsersDAO b1=new UsersDAO();
		String watchlistdata=b1.getUserWatchlist();
		this.watchlist = watchlistdata;
		
	}
	
	
	
}
