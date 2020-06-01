package com.xws.tim12.serviceImpl;

import com.xws.tim12.dto.LoggedInUserDTO;
import com.xws.tim12.model.Admin;
import com.xws.tim12.model.Agent;
import com.xws.tim12.model.Authority;
import com.xws.tim12.model.NormalUser;
import com.xws.tim12.model.UserTokenState;
import com.xws.tim12.repository.AuthRepository;
import com.xws.tim12.security.TokenUtils;
import com.xws.tim12.security.auth.JwtAuthenticationRequest;
import com.xws.tim12.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private AuthRepository authorityRepository;

    @Override
    public Set<Authority> findById(Long id) {
        Authority authority = this.authorityRepository.getOne(id);
        Set<Authority> authorities = new HashSet<>();
        authorities.add(authority);
        return authorities;
    }

    @Override
    public Set<Authority> findByName(String name) {
        Authority authority = this.authorityRepository.findByName(name);
        Set<Authority> authorities = new HashSet<>();
        authorities.add(authority);
        return authorities;
    }

    @Override
    public LoggedInUserDTO login(JwtAuthenticationRequest jwtAuthenticationRequest) {
        final Authentication authentication = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                jwtAuthenticationRequest.getUsername(),
                jwtAuthenticationRequest.getPassword()
            )
        );
      
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String username = returnUsername(authentication.getPrincipal());
        if (username == null) {
            return null;
        }

        String jwtToken = tokenUtils.generateToken(username);
        int expiresIn = tokenUtils.getExpiredIn();
      
        return returnLoggedInUser(
            authentication.getPrincipal(),
            new UserTokenState(jwtToken, expiresIn)
        );
    }

    private String returnUsername(Object object) {
        if (object instanceof Admin) {
            return ((Admin) object).getUsername();
        } else if (object instanceof NormalUser) {
            return ((NormalUser) object).getUsername();
        } else if (object instanceof Agent) {
        	return ((Agent) object).getUsername();
        }
        return null;
    }

    private LoggedInUserDTO returnLoggedInUser(Object object, UserTokenState userTokenState) {
        if (object instanceof Admin) {
            Admin admin = (Admin) object;
            return new LoggedInUserDTO(
                admin.getId(),
                admin.getUsername(),
                "ROLE_ADMIN",
                userTokenState
            );
        } else if (object instanceof NormalUser) {
            NormalUser normalUser = (NormalUser) object;
            return new LoggedInUserDTO(
                normalUser.getId(),
                normalUser.getUsername(),
                "ROLE_NORMAL_USER",
                userTokenState
            );
        } else if (object instanceof Agent) {
            Agent agent = (Agent) object;
            return new LoggedInUserDTO(
                agent.getId(),
                agent.getUsername(),
                "ROLE_AGENT",
                userTokenState
            );
        }
        return null;
    }

}
