package com.login;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class UserBean {

    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String validateUser() {
        String valid = LoginDAO.validate(username, password);
        System.out.println(valid);
        if (valid.equals("User")) {

            return valid;
        } else if(valid.equals("Manager")) {
            //FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_WARN, "Incorrect Username and Passowrd. Please enter correct username and Password",""));
            return valid;
        }else if(valid.equals("Admin")) {
            //FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_WARN, "Incorrect Username and Passowrd. Please enter correct username and Password",""));
            return valid;
        }
        else {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_WARN, "Incorrect Username and Passowrd. Please enter correct username and Password",""));
            return "login";
        }
    }

    // logout event, invalidate session
    public void logout() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
    }
}
