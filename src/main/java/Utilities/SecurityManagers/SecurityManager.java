package Utilities.SecurityManagers;

import javax.servlet.http.HttpServletRequest;
import DomainModel.UserAdministration.Account;
import DomainModel.UserAdministration.Doctor;
import DomainModel.UserAdministration.Secretary;

public class SecurityManager {
    public final static boolean ENABLE_AUTHENTICATION_VERIFICATION = true;
    public final static String SESSION_SCOPE_ACCOUNT_KEY = "MY_ACCOUNT";
    
    public boolean isAuthenticated(HttpServletRequest request) {
        return !(ENABLE_AUTHENTICATION_VERIFICATION && request.getSession().getAttribute(SESSION_SCOPE_ACCOUNT_KEY) == null);
    }
    
    
    public void authenticateUser(Account account, HttpServletRequest request){
    	if(account.getPerson() instanceof Doctor || (account.getPerson() instanceof Secretary && ((Secretary) account.getPerson()).getConfirmed()))
    		request.getSession().setAttribute(SESSION_SCOPE_ACCOUNT_KEY, account);
    }
    
    public void disconnectUser(HttpServletRequest request){
        request.getSession().invalidate();
    }
}
