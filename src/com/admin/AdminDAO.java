package com.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.db.singleton.DatabaseConnection;
import com.login.DataConnect;
import com.login.UserBean;
import com.register.RegisterBean;

import jdk.internal.dynalink.beans.StaticClass;

public class AdminDAO {
	static Connection con = DatabaseConnection.getConnection();
	public List<RegisterBean> getData(){
		con = DatabaseConnection.getConnection();
		
	        PreparedStatement ps = null;
	        //UpdateBean b = new UpdateBean();
	        RegisterBean b=null;
	        List<RegisterBean> b1=new ArrayList<RegisterBean>();
	        try {
	            con = DataConnect.getConnection();
	            ps = con.prepareStatement("select * from users where role = 'Manager' and status='pending'");
	            
	            ResultSet rs = ps.executeQuery();

	            while (rs.next()) {
	            	b=new RegisterBean();
	            	b.setUsername(rs.getString("username"));
	            	b.setFname(rs.getString("firstname"));
	            	b.setLname(rs.getString("lastname"));
	            	b.setEmail(rs.getString("email"));
	            	b.setPhNo(rs.getString("phonenumber"));
	            	b.setAddress(rs.getString("address"));
	            	b1.add(b);
	                
	            }
	            //System.out.println(b.fname);
	        } catch (SQLException ex) {
	            System.out.println("Login error -->" + ex.getMessage());
	        } finally {

	        }
			return b1;
			
	}
	
	public boolean approveStatus(String uname) {
		
		try {
			
            PreparedStatement ps = con.prepareStatement("update users set status='Approved' where username=?");
            ps.setString(1, uname);
            int i=ps.executeUpdate();
            if(i>0) {
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
