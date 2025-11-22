package projectCooking.Configure;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import projectCooking.Repository.UserRepo;
import projectCooking.Repository.Entity.User;
import projectCooking.Service.JWTService;
import projectCooking.Service.Implements.MyUserDetailsService;

@Component
public class JWTConfig extends OncePerRequestFilter {
	@Autowired
	JWTService jwtService;
	@Autowired
	ApplicationContext context;
	@Autowired
	UserRepo userRepo;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authen = request.getHeader("Authorization");
		String Token = null;
		String Username = null;
		if (authen != null && authen.startsWith("Bearer ")) {
			Token = authen.substring(7).trim();
			Username = jwtService.extractUserName(Token);
		}
		if (Username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetail = context.getBean(MyUserDetailsService.class).loadUserByUsername(Username);
			if (jwtService.validateToken(Token, userDetail)) {
				// Check if user is active
				User user = userRepo.findByUserName(Username);
				if (user == null || !user.getIsActive()) {
					response.setStatus(HttpServletResponse.SC_FORBIDDEN);
					response.getWriter().write("Account is deactivated. Please contact support.");
					return;
				}

				Claims claims = jwtService.extractAllClaims(Token);
				String role = claims.get("roles", String.class);

				List<GrantedAuthority> authorities = Arrays.asList(
						new SimpleGrantedAuthority("ROLE_" + role));

				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(Username, null,
						authorities);
				SecurityContextHolder.getContext().setAuthentication(auth);
			}

		}
		filterChain.doFilter(request, response);
	}

}
