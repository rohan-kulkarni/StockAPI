package com.update;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.db.singleton.DatabaseConnection;
import com.login.DataConnect;
import com.login.UserBean;
import com.register.RegisterBean;

public class UpdateDAO {
	static Connection con = DatabaseConnection.getConnection();
	public Map<String,String> getData(String Uname) {
        PreparedStatement ps = null;
        //UpdateBean b = new UpdateBean();
        Map<String, String> b=new HashMap<>();
        try {
            con = DataConnect.getConnection();
            ps = con.prepareStatement("select * from users where username = ? ");
            ps.setString(1,Uname);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
            	b.put("fname",rs.getString("firstname"));
            	b.put("lname",rs.getString("lastname"));
            	b.put("address",rs.getString("address"));
            	b.put("PhNo",rs.getString("phonenumber"));
            	b.put("email",rs.getString("email"));
            	b.put("username",rs.getString("username"));
            	b.put("password",rs.getString("password"));
            	b.put("role",rs.getString("role"));
                
            }
            //System.out.println(b.fname);
        } catch (SQLException ex) {
            System.out.println("Login error -->" + ex.getMessage());
        } finally {

        }
		return b;
	}
	
	public static boolean update(UpdateBean b) {
		
		
		try {
			
            PreparedStatement ps = con.prepareStatement("update users set firstname=?,lastname=?,address=?,phonenumber=?,email=? where username=?");
            ps.setString(1, b.fname);
            ps.setString(2, b.lname);
            ps.setString(3, b.address);
            ps.setString(4, b.PhNo);
            ps.setString(5, b.email);
            ps.setString(6, b.username);
            int i=ps.executeUpdate();
            if(i>0) {
            	FacesContext facesContext = FacesContext.getCurrentInstance();
        		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Updated Succesfully", "Updated Succesfully"));
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
