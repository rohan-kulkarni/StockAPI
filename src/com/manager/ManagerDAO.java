package com.manager;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import com.db.singleton.DatabaseConnection;
import com.login.DataConnect;
import com.register.RegisterBean;
import com.users.Requests;

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
	
	public List<Requests> getUserRequests(){
		List<Requests> req=new ArrayList<Requests>();
		Requests r=null;
		String uid=(String)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("uid");
		try {
            con = DataConnect.getConnection();
           PreparedStatement ps = con.prepareStatement("select * from Requests where mid=?");
            ps.setString(1, uid);
            ResultSet rs = ps.executeQuery();
            System.out.println(uid);
            while (rs.next()) {
            	r=new Requests();
            	r.setUserid(Integer.parseInt(rs.getString("uid")));
            	r.setMid(Integer.parseInt(rs.getString("mid")));
            	r.setQty(Integer.parseInt(rs.getString("qty")));
            	r.setSymbols(rs.getString("Symbol"));
            	
            	String url = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol="
						+ rs.getString("symbol") + "&interval=1min&apikey=AF93E6L5I01EA39O";
				InputStream inputStream = new URL(url).openStream();

				// convert the json string back to object
				JsonReader jsonReader = Json.createReader(inputStream);
				JsonObject mainJsonObj = jsonReader.readObject();
				for (String key : mainJsonObj.keySet()) {
					if (!key.equals("Meta Data")) {
						JsonObject dataJsonObj = mainJsonObj.getJsonObject(key);

						
						for (String subKey : dataJsonObj.keySet()) {

							JsonObject subJsonObj = dataJsonObj.getJsonObject(subKey);
							 String curr=subJsonObj.getString("4. close");
							 r.setCurrprice(Double.parseDouble(curr));
							break;

						}

					}
				}
            	
            	//r.setPhNo(rs.getString("symbol"));
            	req.add(r);
                
            }
            //System.out.println(b.fname);
        } catch (SQLException | IOException ex) {
            System.out.println("Login error -->" + ex.getMessage());
        } finally {

        }
		
		return req;
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
