package stockapi;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

@ManagedBean
@SessionScoped
public class SellStock {
	private List<Stock> availablestocks;
	String selectedSymbol;
	int SelectedQty;
	double amt;
	double price;
	Map<Double, Boolean> l = new HashMap<Double, Boolean>();

	public Map<Double, Boolean> getL() {
		return l;
	}

	public void setL(Map<Double, Boolean> l) {
		this.l = l;
	}

	public double getAmt() {
		return amt;
	}

	public void setAmt(double amt) {
		this.amt = amt;
	}

	public SellStock() {
		sellStock();
	}

	public String getSelectedSymbol() {
		return selectedSymbol;
	}

	public void setSelectedSymbol(String selectedSymbol) {
		this.selectedSymbol = selectedSymbol;
	}

	public List<Stock> getAvailablestocks() {
		return availablestocks;
	}

	public void setAvailablestocks(List<Stock> availablestocks) {
		this.availablestocks = availablestocks;
	}

	public int getSelectedQty() {
		return SelectedQty;
	}

	public void setSelectedQty(int selectedQty) {
		SelectedQty = selectedQty;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void test() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String action = params.get("symbol");
		double price = 0;
		for (Map.Entry<Double, Boolean> a : l.entrySet()) {
			if (a.getValue()) {
				price = a.getKey();
			}
		}

		StockApiBean b = new StockApiBean();
		if (b.Sell(action, this.SelectedQty, price)) {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucessfully sold", "success"));
		} else {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "error"));
		}

		System.out.println(price + "  " + this.SelectedQty + " " + action);

	}
	
	public void requestSale() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String action = params.get("symbol");
		StockApiBean b1 = new StockApiBean();
		if (b1.sendRequest(action, this.SelectedQty)) {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Request Sent", "success"));
		} else {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "error"));
		}

	}

	public void sellStock() {
		StockApiBean b1 = new StockApiBean();
		List<Stock> b = b1.purchaseStock();
		this.availablestocks = b;
	}
}
