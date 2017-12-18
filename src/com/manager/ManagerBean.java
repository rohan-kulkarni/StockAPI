package com.manager;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.register.RegisterBean;
import com.users.Requests;
import com.users.Transactions;
import com.users.UsersDAO;

import stockapi.Stock;
import stockapi.StockApiBean;
@ManagedBean
@SessionScoped
public class ManagerBean {
List<RegisterBean> list;
List<Requests> req;
String watchlist;
List<Transactions> trans;
public String getWatchlist() {
return watchlist;
}

public void setWatchlist(String watchlist) {
this.watchlist = watchlist;
}

public List<Requests> getReq() {
	return req;
}

public void setReq(List<Requests> req) {
	this.req = req;
}

	public List<RegisterBean> getList() {
		return list;
	}

	public void setList(List<RegisterBean> list) {
		this.list = list;
	}

	public  ManagerBean() {
		getData();
		getRequests();
		getUserTransactions();
	}
	
	public List<Transactions> getTrans() {
		return trans;
	}

	public void setTrans(List<Transactions> trans) {
		this.trans = trans;
	}

	public void getData() {
		ManagerDAO b1=new ManagerDAO();
		List<RegisterBean>b = b1.getData();
		this.list = b;
		System.out.println(list);
//		return this;
		
	}
	public void getRequests() {
		ManagerDAO b1=new ManagerDAO();
		List<Requests> data=b1.getUserRequests();
		this.req = data;
	}
	public void sellStockClient(String symbol,int qty,double currprice,int uid) {
		StockApiBean b=new StockApiBean();
		if (b.SellClients(symbol, qty, currprice,uid)) {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucessfully sold", "success"));
		} else {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "error"));
		}
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
		return "watchlist";
	}
	public void getUserWatchlist() {
		UsersDAO b1=new UsersDAO();
		String watchlistdata=b1.getUserWatchlist();
		this.watchlist = watchlistdata;
		
	}
	public void getUserTransactions(){
		UsersDAO b1=new UsersDAO();
		List<Transactions> data=b1.getUserTransactions();
		this.trans = data;
	} 
}
