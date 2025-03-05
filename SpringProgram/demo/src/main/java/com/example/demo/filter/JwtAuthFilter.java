package com.example.demo.filter;
import com.example.demo.service.PersonService;
import com.example.demo.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
 
@Component
public class JwtAuthFilter extends OncePerRequestFilter{
    
    @Autowired
    private JwtService jwtService;

    @Autowired
    private PersonService personService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        System.out.println("Filter started");

        if(authHeader != null && authHeader.startsWith("Bearer ")){
            token = authHeader.substring(7);
            username = jwtService.extractUserName(token);
            System.out.println("Header contains " + token + " username "+username);
        }
        else
        {
            System.out.println("Header is incorrectly equal to " + authHeader);
        }

        if(username != null && (SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser"))){
            System.out.println("Username retrived and security context null");
            UserDetails userDetails = personService.loadUserByUsername(username);
            if(jwtService.validateToken(token, userDetails)){
                System.out.println("Token validated ");
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        else
        {
            System.out.println(SecurityContextHolder.getContext().getAuthentication().toString());

            System.out.println(SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString());
        }

        filterChain.doFilter(request,response);
    } 
} 
