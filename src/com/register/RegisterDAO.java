package com.register;

import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.db.singleton.DatabaseConnection;


public class RegisterDAO {
	public static boolean register(RegisterBean b) {
		Connection con = DatabaseConnection.getConnection();
		String status="Approved";
		try {
			if(b.role.equals("Manager")) {
				status="pending";
			}
            PreparedStatement ps = con.prepareStatement("insert into users(firstname,lastname,address,phonenumber,email,username,password,role,status) values(?,?,?,?,?,?,?,?,?)");
            ps.setString(1, b.fname);
            ps.setString(2, b.lname);
            ps.setString(3, b.address);
            ps.setString(4, b.PhNo);
            ps.setString(5, b.email);
            ps.setString(6, b.username);
            ps.setString(7, b.password);
            ps.setString(8, b.role);
            ps.setString(9, status);
            int i=ps.executeUpdate();
            if(i>0) {
            	FacesContext facesContext = FacesContext.getCurrentInstance();
        		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Registered Succesfully", "Registered Succesfully"));
        		return true;
            	}
            else {
            	return false;
            }
        } catch (Exception e2) {
            System.out.println(e2);
            return false;
        }
		
	}
}
