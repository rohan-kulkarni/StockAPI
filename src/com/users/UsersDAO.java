package com.users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.json.JsonObject;

import com.db.singleton.DatabaseConnection;
import com.register.RegisterBean;

public class UsersDAO {
	static Connection con = DatabaseConnection.getConnection();
	public List<RegisterBean> getData() {
		List<RegisterBean> b1=new ArrayList<RegisterBean>();
		try {
			RegisterBean b=null;
			
			int uid =Integer.parseInt((String)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("uid"));
			 PreparedStatement ps = con.prepareStatement("select mid from userManager where	uid=?");
			 ps.setInt(1,uid );
			 ResultSet rs = ps.executeQuery();
			 rs.next();
			 //String uid=(String)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("uid");
			 int mid= rs.getInt(1);
			 ps = con.prepareStatement("select * from users where uid=?");
			 ps.setInt(1,mid );
			 rs = ps.executeQuery();
			 System.out.println(uid);
			 
			 if (rs.next()) {
	            	b=new RegisterBean();
	            	b.setUsername(rs.getString("username"));
	            	b.setFname(rs.getString("firstname"));
	            	b.setLname(rs.getString("lastname"));
	            	b.setEmail(rs.getString("email"));
	            	b.setPhNo(rs.getString("phonenumber"));
	            	b.setAddress(rs.getString("address"));
	            	b1.add(b);
	            }
			 System.out.println(b1);
		
        } catch (Exception e2) {
            System.out.println(e2);
        }
		 return b1;
	}
	
	public String getUserWatchlist() {
		String table2Markup="";
		try {
			PreparedStatement ps = con.prepareStatement("select * from watchlist where uid = ?");
			int uid =Integer.parseInt((String)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("uid"));
			ps.setInt(1, uid);
			
			ResultSet rs = ps.executeQuery();
			table2Markup += "<table class='table table-hover'>";
            table2Markup += "<thead><tr><th>Symbol</th></thead>";
            table2Markup += "<tbody>";
            while(rs.next()) {
               // JsonObject subJsonObj = dataJsonObj.getJsonObject(subKey);
                table2Markup
                        += "<tr>"
                        + "<td>" + rs.getString("symbol") +"</td>";
                table2Markup += "</tr>";
            }
           table2Markup += "</tbody></table>";
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return table2Markup;
	}
	
	public boolean addtoWatchlist(String sym) {
		try {
			int uid =Integer.parseInt((String)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("uid"));
			PreparedStatement ps = con.prepareStatement("select * from watchlist where uid = ? and symbol =?");
			ps.setInt(1, uid);
			ps.setString(2,sym);
			ResultSet rs=ps.executeQuery();
			int count =0;
			while(rs.next()) {
				count++;
			}
			if(count>=1) {
				return false;
			}
			else {
				ps = con.prepareStatement("insert into watchlist(uid,symbol) values(?,?)");
				//int uid =Integer.parseInt((String)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("uid"));
				ps.setInt(1, uid);
				ps.setString(2, sym);
				int i= ps.executeUpdate();
				if(i>0) {
					return true;
				}
				
			}
			
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(sym);
		return false;
	}
	
}
