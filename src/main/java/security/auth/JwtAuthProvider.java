package security.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

public class JwtAuthProvider extends AbstractUserDetailsAuthenticationProvider {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
            UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) authentication;

            Object principal = authenticationToken.getPrincipal();
            Object creds     = authenticationToken.getCredentials();

            if(!(principal instanceof String) || StringUtils.isEmpty(principal))
                throw new BadCredentialsException(String.format("error parcing login [%s]", principal));
            if(!(creds instanceof String) || StringUtils.isEmpty(creds))
                throw new BadCredentialsException(String.format("error parcing password"));

            String login    = String.valueOf(principal);
            String password = String.valueOf(creds);

            UserDetails dbUser = null;

            try {
                dbUser = userDetailsService.loadUserByUsername(login);
            }catch (UsernameNotFoundException ex){
                throw new BadCredentialsException(ex.getMessage());
            }

            if(dbUser != null && passEncoder.matches(password, dbUser.getPassword()))
                return createSuccessAuthentication(principal, authentication, dbUser);
            else
                return null;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {

    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        return null;
    }
}
