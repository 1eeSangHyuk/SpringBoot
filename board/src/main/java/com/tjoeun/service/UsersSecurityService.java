package com.tjoeun.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tjoeun.constant.UsersRole;
import com.tjoeun.entity.Users;
import com.tjoeun.repository.UsersRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsersSecurityService implements UserDetailsService{
	
	private final UsersRepository usersRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	
		Optional<Users> tmpUsers = usersRepository.findByUserName(username);
		if(tmpUsers.isEmpty()) {
			throw new UsernameNotFoundException("헤당 사용자명의 사용자가 존재하지 않습니다.");
		}
		Users users = tmpUsers.get();

		// ROLE(ADMIN / USER) 확인
		List<GrantedAuthority> authorities = new ArrayList<>();
		if("admin".equals(username)) {
			authorities.add(new SimpleGrantedAuthority(UsersRole.ADMIN.getValue()));	// ROLE_ADMIN
		} else {
			authorities.add(new SimpleGrantedAuthority(UsersRole.USER.getValue()));	// ROLE_USER
		}
		return new User(users.getUserName(), users.getPassword(), authorities);
	}
}
