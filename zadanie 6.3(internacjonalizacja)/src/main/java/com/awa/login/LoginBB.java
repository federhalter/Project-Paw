package com.awa.login;

import java.util.List;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.simplesecurity.RemoteClient;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import com.awa.dao.UserDAO;
import com.awa.entities.User;

@Named
@RequestScoped

public class LoginBB {
	private static final String PAGE_MAIN = "list/studentsList?faces-redirect=true";
	private static final String PAGE_LOGIN = "/login";
	private static final String PAGE_STAY_AT_THE_SAME = null;

	private String login;
	private String pass;
@Inject        
        private com.awa.login.UserSession userSession;
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	@Inject
	UserDAO userDAO;

	public String doLogin() {
		FacesContext ctx = FacesContext.getCurrentInstance();

		
		User user = userDAO.loginUser(login, pass);

		
		if (user == null) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Niepoprawny login lub hasło", null));
			return PAGE_STAY_AT_THE_SAME;
		}

		
		
		RemoteClient<User> client = new RemoteClient<User>(); 
                client.setDetails(user);
                
		
		List<String> roles = userDAO.getUserRole(user); 
		
		if (roles != null) { 
			for (String role: roles) {
				client.getRoles().add(role);
			}
		}
	
		
		HttpServletRequest request = (HttpServletRequest) ctx.getExternalContext().getRequest();
		client.store(request);

		userSession.setUser(user);
                userSession.setRoles(roles);
                
		return PAGE_MAIN;
	}
	
	public String doLogout() {
    userSession.invalidate();

    HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
            .getExternalContext().getSession(false);
    if (session != null) {
        session.invalidate();
    }
    return PAGE_LOGIN;
}
	
        public List<String>  getUserRole() {
    if (login == null) return null;   
    User user = userDAO.loginUser(login, pass);
    if (user == null) return null;

            return userDAO.getUserRole(user);
}
}
