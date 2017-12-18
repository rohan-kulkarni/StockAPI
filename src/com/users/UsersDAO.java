package com.users;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import com.db.singleton.DatabaseConnection;
import com.register.RegisterBean;

public class UsersDAO {
	static Connection con = DatabaseConnection.getConnection();

	public List<RegisterBean> getData() {
		List<RegisterBean> b1 = new ArrayList<RegisterBean>();
		try {
			RegisterBean b = null;

			int uid = Integer.parseInt(
					(String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("uid"));
			PreparedStatement ps = con.prepareStatement("select mid from userManager where	uid=?");
			ps.setInt(1, uid);
			ResultSet rs = ps.executeQuery();
			rs.next();
			// String
			// uid=(String)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("uid");
			int mid = rs.getInt(1);
			ps = con.prepareStatement("select * from users where uid=?");
			ps.setInt(1, mid);
			rs = ps.executeQuery();
			System.out.println(uid);

			if (rs.next()) {
				b = new RegisterBean();
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
		String table2Markup = "";
		try {
			String interval = "1min";
			PreparedStatement ps = con.prepareStatement("select * from watchlist where uid = ?");
			int uid = Integer.parseInt(
					(String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("uid"));
			ps.setInt(1, uid);

			ResultSet rs = ps.executeQuery();
			table2Markup += "<table class='table table-hover'>";
			table2Markup += "<thead><tr><th>Symbol</th><th>Price</th></tr></thead>";
			table2Markup += "<tbody>";
			while (rs.next()) {

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
							table2Markup += "<tr>" + "<td>" + rs.getString("symbol") + "</td>" + "<td>"
									+ subJsonObj.getString("4. close") + "</td>";
							table2Markup += "</tr>";
							break;

						}

					}
				}
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return table2Markup;
	}

	public boolean addtoWatchlist(String sym) {
		try {
			int uid = Integer.parseInt(
					(String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("uid"));
			PreparedStatement ps = con.prepareStatement("select * from watchlist where uid = ? and symbol =?");
			ps.setInt(1, uid);
			ps.setString(2, sym);
			ResultSet rs = ps.executeQuery();
			int count = 0;
			while (rs.next()) {
				count++;
			}
			if (count >= 1) {
				return false;
			} else {
				ps = con.prepareStatement("insert into watchlist(uid,symbol) values(?,?)");
				// int uid
				// =Integer.parseInt((String)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("uid"));
				ps.setInt(1, uid);
				ps.setString(2, sym);
				int i = ps.executeUpdate();
				if (i > 0) {
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
	public boolean putTransactionRecord(String symbol, double price, int qty, double amt) {
		int uid = Integer.parseInt(
				(String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("uid"));
		int balanceavail=0;
		try {
			
			
			PreparedStatement ps = con.prepareStatement("select balance from users where uid = ?");
			ps.setInt(1, uid);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				balanceavail=rs.getInt("balance");
			}
			
			if(balanceavail>amt) {
				Date d=new Date();
				
				ps = con.prepareStatement("insert into transactions(uid,symbol,type,quantity,amount,price,date) values(?,?,?,?,?,?,?)");
				ps.setInt(1, uid);
				ps.setString(2, symbol);
				ps.setString(3, "Purchase");
				ps.setString(4, ""+qty);
				ps.setString(5, ""+amt);
				ps.setString(6, ""+price);
				ps.setString(7, d.toString() );
				int i = ps.executeUpdate();
				if (i > 0) {
					ps = con.prepareStatement("update users set balance = ? where uid = ?");
					ps.setDouble(1, balanceavail+amt);
					ps.setInt(2, uid);
					i=ps.executeUpdate();
					if(i>0) {
						return true;
					}
					
				}
			}
			else {
				FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO, "Insufficient balance",""));
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}
	
	public List<Transactions> getUserTransactions(){
		List<Transactions> data=new ArrayList<Transactions>();
		int uid = Integer.parseInt(
				(String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("uid"));
		PreparedStatement ps;
		try {
			ps = con.prepareStatement("select * from transactions where uid=?");
			ps.setInt(1, uid);
			ResultSet rs = ps.executeQuery();
			rs = ps.executeQuery();
			System.out.println(uid);
			Transactions b=null;
			while(rs.next()) {
				b=new Transactions();
				b.setSymbol(rs.getString("symbol"));
				b.setD(rs.getString("date"));
				b.setAmount(Double.parseDouble(rs.getString("amount")));
				b.setPrice(Double.parseDouble(rs.getString("price")));
				b.setQty(Integer.parseInt(rs.getString("quantity")));
				b.setType(rs.getString("type"));
				data.add(b);
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return data;
	}
	

}
