package com.update;

import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.register.RegisterDAO;

@ManagedBean
@SessionScoped
public class UpdateBean {
	String fname,lname,address,email,PhNo,password,username,role;

	public UpdateBean() {
		this.username=(String)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username");
		getData(this.username);
	}
	
	public String getFname() {
		return fname;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}
	public String getAddress()
	{
		return address;
	}
	public void setAddress(String address)
	{
		this.address = address;
	}
	public String getPhNo()
	{
		return PhNo;
	}
	public void setPhNo(String PhNo)
	{
		this.PhNo = PhNo;
	}
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	public String getusername() {
		return username;
	}

	public void setusername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	public void getData(String uname) {
		UpdateDAO b1=new UpdateDAO();
		Map<String, String> b = b1.getData(uname);
		System.out.println(b.get("fname"));
		this.fname=b.get("fname");
		this.lname=b.get("lname");
		this.email=b.get("email");
		this.PhNo=b.get("PhNo");
		this.password= b.get("password");
		this.address=b.get("address");
		this.role=b.get("role");
	}
	public String updateUser() {
        boolean valid = UpdateDAO.update(this);
        if (valid == true) {
        	FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_WARN, "Values Updated",""));
           if(this.role.equals("Manager")) {
        	   return "updateManager";
           }
        	return "updateUser";
        } else {
        	FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_WARN, "FUCKKK YOUUUUUUU",""));
        	if(this.role.equals("Manager")) {
         	   return "updateManager";
            }
         	return "updateUser";
        }
    }
}
