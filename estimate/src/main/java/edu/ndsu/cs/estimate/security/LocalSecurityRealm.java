package edu.ndsu.cs.estimate.security;

import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.SimpleByteSource;

import edu.ndsu.cs.estimate.entities.interfaces.RoleInterface;
import edu.ndsu.cs.estimate.entities.interfaces.UserAccount;
import edu.ndsu.cs.estimate.services.database.interfaces.UserAccountDatabaseService;

public class LocalSecurityRealm extends AuthorizingRealm{

	private UserAccountDatabaseService userAccountDatabaseService; 
	
	public LocalSecurityRealm(UserAccountDatabaseService userAccountDatabaseService) {
		this.userAccountDatabaseService = userAccountDatabaseService;
	}
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		if(principals == null || principals.isEmpty()) {
			return null; 
		}
		
		// Look up the user associated with that userID (username used to login)
		String userID = principals.getPrimaryPrincipal().toString(); 
		UserAccount userAccount = userAccountDatabaseService.getUserAccount(userID); 
		
		// If we have a user with that userID add their roles
		if(userAccount != null) {
			SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo(); 
			List<? extends RoleInterface> roles = userAccount.getRoles();
			for(RoleInterface role : roles) {
				authorizationInfo.addRole(role.getName());
			}
			return authorizationInfo;
		}
		return null; 
	}
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		// Look up the user associated with the userID (username used to login)
		String userID = token.getPrincipal().toString();
		UserAccount userAccount = userAccountDatabaseService.getUserAccount(userID); 
		
		// If we have a user with that ID, get their stored authentication information
		if(userAccount != null) {
			SimpleByteSource salt = new SimpleByteSource(userAccount.getPasswordSalt());
			String hash = userAccount.getPasswordHash();
			
			return new SimpleAuthenticationInfo(userID, hash, salt, this.getName()); 
		}	
		return null; 
	}
}

