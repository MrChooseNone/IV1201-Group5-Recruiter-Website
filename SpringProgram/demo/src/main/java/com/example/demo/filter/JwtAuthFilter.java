package com.example.demo.filter;
import com.example.demo.service.PersonService;

import io.jsonwebtoken.ExpiredJwtException;

import com.example.demo.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.lang.NonNull;

import java.io.IOException;
 
@Component
public class JwtAuthFilter extends OncePerRequestFilter{
    
    @Autowired
    private JwtService jwtService;

    @Autowired
    private PersonService personService;

    //This excludes specific urls from the filtering, to allow access without a JWT token
    //Link: https://www.baeldung.com/spring-exclude-filter 
    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request)
      throws ServletException {
        String path = request.getRequestURI();
        return "/auth/generateToken".equals(path);
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,@NonNull FilterChain filterChain) throws ServletException, IOException{
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        try {
            
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            token = authHeader.substring(7);
            username = jwtService.extractUserName(token);
        }

        if(username != null && (SecurityContextHolder.getContext().getAuthentication()==null||SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser"))){
            UserDetails userDetails = personService.loadUserByUsername(username);
            if(jwtService.validateToken(token, userDetails)){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("Token validated");
            }
        }
        else
        {
            if (SecurityContextHolder.getContext().getAuthentication()!=null) {
                System.out.println(SecurityContextHolder.getContext().getAuthentication().toString());
            }
        }

        filterChain.doFilter(request,response);
    
        } 
        //Handling for expired token
        // https://stackoverflow.com/questions/73052974/why-is-the-expiredjwtexception-not-being-caught-and-handled 
        catch (ExpiredJwtException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("JWT token expired, please refresh it");
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            return;
        }
        catch(Exception e)
        {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Unknown error occured while inspecting jwt token");
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            return;
        }
    } 
} 
