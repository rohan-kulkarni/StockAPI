package com.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import com.db.singleton.DatabaseConnection;
import com.login.DataConnect;
import com.register.RegisterBean;

public class ManagerDAO {
	static Connection con = DatabaseConnection.getConnection();
	public List<RegisterBean> getData(){
		con = DatabaseConnection.getConnection();
		
	        PreparedStatement ps = null;
	        //UpdateBean b = new UpdateBean();
	        RegisterBean b=null;
	        List<RegisterBean> b1=new ArrayList<RegisterBean>();
	        try {
	            con = DataConnect.getConnection();
	            ps = con.prepareStatement("select * from users where role = 'Manager' and status='Approved'");
	            
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
	public boolean addManager(String Username) {
		try {
			System.out.println(Username);
			 PreparedStatement ps = con.prepareStatement("select uid from users where role = 'Manager' and status='Approved' and username=?");
			 ps.setString(1, Username);
			 ResultSet rs = ps.executeQuery();
			 rs.next();
			 String uid=(String)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("uid");
			 int mid= rs.getInt(1);
			 
			 System.out.println(uid);
			 ps = con.prepareStatement("insert into userManager values(?,?)");
			 ps.setInt(1, Integer.parseInt(uid));
			 ps.setInt(2,mid);
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
