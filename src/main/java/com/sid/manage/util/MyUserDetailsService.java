package com.sid.manage.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.sid.manage.dao.UtilisateurRepo;
import com.sid.manage.entities.*;

import java.util.Collections;

@Component
public class MyUserDetailsService implements UserDetailsService {

    @Autowired private UtilisateurRepo utilisateurRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	Utilisateur userRes = utilisateurRepo.findByUsername(username);
    	System.out.println("///////////////////////************************//////////////////////");
    	System.out.println(userRes.getUsername());
    	System.out.println(userRes.getTypeU());
        if(userRes.equals(null))
            {throw new UsernameNotFoundException("Could not findUser with username = " + username);}
        else if(userRes.getTypeU().equals("adminMTC")) {
        	return new org.springframework.security.core.userdetails.User(
                    username,
                    userRes.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN_MTC")));
        } else if(userRes.getTypeU().equals("adminCNI")) {
            return new org.springframework.security.core.userdetails.User(
                    username,
                    userRes.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN_CNI")));
        }else {
        	return new org.springframework.security.core.userdetails.User(
                    username,
                    userRes.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_MEMBRE")));
        }


    }
}