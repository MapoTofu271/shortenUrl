package org.example.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtTokenProvider;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        try {
            //Get JWT from Header
            String jwt = jwtTokenProvider.getJwtFromHeader(request);

            if(jwt != null && jwtTokenProvider.validateToken(jwt)) {
                String username = jwtTokenProvider.getUserNameFromJwtToken(jwt);
                //Using userDetailsServiceImpl class
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if(userDetails != null) {
                    //UsernamePasswordAuthenticationToken has null param
                    //fuck fuck fuck
                    UsernamePasswordAuthenticationToken authentication= new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
                    //wtf is this 2 lines?
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            //Validate token
            //IF valid get user details
                // get username -> load user -> Set the auth context?


        } catch (Exception e) {
            e.printStackTrace();
        }

        //after doing the logic above need to add: and wtf this thing do
        filterChain.doFilter(request, response);
    }
}
