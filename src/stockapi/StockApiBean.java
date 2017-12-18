package stockapi;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;

import com.login.DataConnect;
import com.users.UsersBean;
import com.users.UsersDAO;

@ManagedBean
@SessionScoped
public class StockApiBean {

    private static final long serialVersionUID = 1L;
    static final String API_KEY = "AF93E6L5I01EA39O";

    private String symbol;
    private double price;
    private int qty;
    private double amt;
    private String table1Markup;
    private String table2Markup;

    private String selectedSymbol;
    private List<SelectItem> availableSymbols;
    
    public String getPurchaseSymbol() {
        if (getRequestParameter("symbol") != null) {
            symbol = getRequestParameter("symbol");
        }
        return symbol;
    }
    
    public void setPurchaseSymbol(String purchaseSymbol) {
        System.out.println("func setPurchaseSymbol()");  //check
    }

    public double getPurchasePrice() {
        if (getRequestParameter("price") != null) {
            price = Double.parseDouble(getRequestParameter("price"));
            System.out.println("price: " + price);
        }
        return price;
    }

    public void setPurchasePrice(double purchasePrice) {
        System.out.println("func setPurchasePrice()");  //check
    }
    
    private String getRequestParameter(String name) {
        return ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getParameter(name);
    }

    @PostConstruct
    public void init() {
        //initially populate stock list
        availableSymbols = new ArrayList<SelectItem>();
        availableSymbols.add(new SelectItem("AAPL", "Apple"));
        availableSymbols.add(new SelectItem("AMZN", "Amazon LLC"));
        availableSymbols.add(new SelectItem("AR", "Antero Resources"));
        availableSymbols.add(new SelectItem("EBAY", "Ebay"));
        availableSymbols.add(new SelectItem("FB", "Facebook, Inc."));
        availableSymbols.add(new SelectItem("GOLD", "Gold"));
        availableSymbols.add(new SelectItem("GOOGL", "Google"));
        availableSymbols.add(new SelectItem("MSFT", "Microsoft"));
        availableSymbols.add(new SelectItem("SLV", "Silver"));
        availableSymbols.add(new SelectItem("TWTR", "Twitter, Inc."));

        //initially populate intervals for stock api
        availableIntervals = new ArrayList<SelectItem>();
        availableIntervals.add(new SelectItem("1min", "1min"));
        availableIntervals.add(new SelectItem("5min", "5min"));
        availableIntervals.add(new SelectItem("15min", "15min"));
        availableIntervals.add(new SelectItem("30min", "30min"));
        availableIntervals.add(new SelectItem("60min", "60min"));
    }

    private String selectedInterval;
    private List<SelectItem> availableIntervals;

    public String getSelectedInterval() {
        return selectedInterval;
    }

    public void setSelectedInterval(String selectedInterval) {
        this.selectedInterval = selectedInterval;
    }

    public List<SelectItem> getAvailableIntervals() {
        return availableIntervals;
    }

    public void setAvailableIntervals(List<SelectItem> availableIntervals) {
        this.availableIntervals = availableIntervals;
    }

    public String getSelectedSymbol() {
        return selectedSymbol;
    }

    public void setSelectedSymbol(String selectedSymbol) {
        this.selectedSymbol = selectedSymbol;
    }

    public List<SelectItem> getAvailableSymbols() {
        return availableSymbols;
    }

    public void setAvailableSymbols(List<SelectItem> availableSymbols) {
        this.availableSymbols = availableSymbols;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getAmt() {
        return amt;
    }

    public void setAmt(double amt) {
        this.amt = amt;
    }

    public String getTable1Markup() {
        return table1Markup;
    }

    public void setTable1Markup(String table1Markup) {
        this.table1Markup = table1Markup;
    }

    public String getTable2Markup() {
        return table2Markup;
    }

    public void setTable2Markup(String table2Markup) {
        this.table2Markup = table2Markup;
    }
    public String SellStock(String symbol, double price, int qty, double amt) {
    	return "";
    }
    public String createDbRecord(String symbol, double price, int qty, double amt) {
        try {
            Connection conn = DataConnect.getConnection();
            Statement statement = conn.createStatement();
            Integer uid = Integer.parseInt((String) FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getSessionMap().get("uid"));
            
            System.out.println(uid);
            System.out.println("symbol:" + symbol);
            System.out.println("price:" + price);
            System.out.println("qty:" + qty);
            System.out.println("amt:" + amt);
            
            
            PreparedStatement ps = conn.prepareStatement("select * from purchase where stock_symbol = ? and uid = ?");
			ps.setString(1, symbol);
			ps.setInt(2, uid);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
            	int newqty=rs.getInt("qty");
            	newqty = newqty+qty;
            	statement.executeUpdate("update `purchase` set `qty`='" + newqty + "' where stock_symbol = '"+symbol+"'");
            	
            }
            else {
            	statement.executeUpdate("INSERT INTO `purchase` (`id`, `uid`, `stock_symbol`, `qty`) "
                    + "VALUES (NULL,'" + uid + "','" + symbol + "','" + qty + "')");
            }
            UsersDAO b=new UsersDAO();
            if(b.putTransactionRecord(symbol,price,qty,amt)) {
            	FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully purchased stock",""));
            }
            statement.close();
            conn.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "purchase";
    }

    public void installAllTrustingManager() {
        TrustManager[] trustAllCerts;
        trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            System.out.println("Exception :" + e);
        }
        return;
    }

    public void timeseries() throws MalformedURLException, IOException {

        installAllTrustingManager();

        //System.out.println("selectedItem: " + this.selectedSymbol);
        //System.out.println("selectedInterval: " + this.selectedInterval);
        String symbol = this.selectedSymbol;
        String interval = this.selectedInterval;
        String url = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=" + symbol + "&interval=" + interval + "&apikey=" + API_KEY;

        this.table1Markup += "URL::: <a href='" + url + "'>Data Link</a><br>";
        InputStream inputStream = new URL(url).openStream();

        // convert the json string back to object
        JsonReader jsonReader = Json.createReader(inputStream);
        JsonObject mainJsonObj = jsonReader.readObject();
        for (String key : mainJsonObj.keySet()) {
            if (key.equals("Meta Data")) {
                this.table1Markup = null; // reset table 1 markup before repopulating
                JsonObject jsob = (JsonObject) mainJsonObj.get(key);
                this.table1Markup += "<style>#detail >tbody > tr > td{ text-align:center;}</style><b>Stock Details</b>:<br>";
                this.table1Markup += "<table>";
                this.table1Markup += "<tr><td>Information</td><td>" + jsob.getString("1. Information") + "</td></tr>";
                this.table1Markup += "<tr><td>Symbol</td><td>" + jsob.getString("2. Symbol") + "</td></tr>";
                this.table1Markup += "<tr><td>Last Refreshed</td><td>" + jsob.getString("3. Last Refreshed") + "</td></tr>";
                this.table1Markup += "<tr><td>Interval</td><td>" + jsob.getString("4. Interval") + "</td></tr>";
                this.table1Markup += "<tr><td>Output Size</td><td>" + jsob.getString("5. Output Size") + "</td></tr>";
                this.table1Markup += "<tr><td>Time Zone</td><td>" + jsob.getString("6. Time Zone") + "</td></tr>";
                this.table1Markup += "</table>";
            } else {
                this.table2Markup = null; // reset table 2 markup before repopulating
                JsonObject dataJsonObj = mainJsonObj.getJsonObject(key);
                this.table2Markup += "<table class='table table-hover'>";
                this.table2Markup += "<thead><tr><th>Timestamp</th><th>Open</th><th>High</th><th>Low</th><th>Close</th><th>Volume</th></tr></thead>";
                this.table2Markup += "<tbody>";
                int i = 0;
                for (String subKey : dataJsonObj.keySet()) {
                    JsonObject subJsonObj = dataJsonObj.getJsonObject(subKey);
                    this.table2Markup
                            += "<tr>"
                            + "<td>" + subKey + "</td>"
                            + "<td>" + subJsonObj.getString("1. open") + "</td>"
                            + "<td>" + subJsonObj.getString("2. high") + "</td>"
                            + "<td>" + subJsonObj.getString("3. low") + "</td>"
                            + "<td>" + subJsonObj.getString("4. close") + "</td>"
                            + "<td>" + subJsonObj.getString("5. volume") + "</td>";
                    if (i == 0) {
                        String path = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
                        this.table2Markup += "<td><a class='btn btn-success' href='" + path + "/faces/purchase.xhtml?symbol=" + symbol + "&price=" + subJsonObj.getString("4. close") + "'>Buy Stock</a></td>";
                    }
                    this.table2Markup += "</tr>";
                    i++;
                }
                this.table2Markup += "</tbody></table>";
            }
        }
        return;
    }

    public List<Stock> purchaseStock() {
    	Connection conn = DataConnect.getConnection();
    	List<Stock> listnew=new ArrayList<Stock>(); 
		try {

		
        Integer uid = Integer.parseInt((String) FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap().get("uid"));
        
        PreparedStatement ps = conn.prepareStatement("select * from purchase where uid = ?");
		ps.setInt(1, uid);
		Stock s=null;
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
        	s=new Stock();
        	s.setSymbol(rs.getString("stock_symbol"));
        	
        	String url = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol="
					+ rs.getString("stock_symbol") + "&interval=1min&apikey=AF93E6L5I01EA39O";
			InputStream inputStream = new URL(url).openStream();

			// convert the json string back to object
			JsonReader jsonReader = Json.createReader(inputStream);
			JsonObject mainJsonObj = jsonReader.readObject();
			for (String key : mainJsonObj.keySet()) {
				if (!key.equals("Meta Data")) {
					JsonObject dataJsonObj = mainJsonObj.getJsonObject(key);

					
					for (String subKey : dataJsonObj.keySet()) {
						
						JsonObject subJsonObj = dataJsonObj.getJsonObject(subKey);
						Double price=Double.parseDouble(subJsonObj.getString("4. close"));
						s.setAmount(price);
						int qty = rs.getInt("qty");
						
						s.setQty(qty);
						break;

					}

				}
			}
        	
        	listnew.add(s);
        	
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
		 return listnew;
    }
    public boolean Sell(String symbol,int qty, double amount) {
    	Connection conn = DataConnect.getConnection();
        try {
			Statement statement = conn.createStatement();
			 Integer uid = Integer.parseInt((String) FacesContext.getCurrentInstance()
		                .getExternalContext()
		                .getSessionMap().get("uid"));
			 PreparedStatement ps = conn.prepareStatement("select * from purchase where stock_symbol = ? and uid = ?");
				ps.setString(1, symbol);
				ps.setInt(2, uid);
	            ResultSet rs = ps.executeQuery();
	            if(rs.next()) {
	            	int newqty=rs.getInt("qty");
	            	newqty = newqty-qty;
	            	if(newqty<0) {
	            		
	            		return false;
	            	}
	            	else {
	            		double prices=qty*amount;
	            		Date d=new Date();
	            		statement.executeUpdate("update `purchase` set `qty`='" + newqty + "' where stock_symbol = '"+symbol+"' and uid ='"+uid+"'");
	            		statement.executeUpdate("update `users` set `balance`= `balance`+'" + prices + "' where uid ='"+uid+"'");
	            		statement.executeUpdate("INSERT INTO `transactions` (`uid`, `symbol`,`type`, `quantity`,`amount`,`price`,`date`) "
	                            + "VALUES ('" + uid + "','" + symbol + "','SALE','" + qty + "','"+prices+"','"+amount+"','"+d.toString()+"')");
	            		return true;
	            	}
	            }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
        
    	
    	
    	
    	return false;
    }
    public boolean sendRequest(String symbol, int quantity) {
    	Connection conn = DataConnect.getConnection();
    	try {
    		 Integer uid = Integer.parseInt((String) FacesContext.getCurrentInstance()
		                .getExternalContext()
		                .getSessionMap().get("uid"));
			 PreparedStatement ps = conn.prepareStatement("select mid from userManager where uid=?");
			 ps.setInt(1, uid);
			 ResultSet rs = ps.executeQuery();
			 rs.next();
			 int mid= rs.getInt(1);
			 
			 System.out.println(uid);
			 ps = conn.prepareStatement("insert into Requests(`uid`,`mid`,`symbol`,`qty`) values(?,?,?,?)");
			 ps.setInt(1,uid);
			 ps.setInt(2,mid);
			 ps.setString(3,symbol);
			 ps.setInt(4, quantity);
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
