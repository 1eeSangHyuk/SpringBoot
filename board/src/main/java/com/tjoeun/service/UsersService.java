package com.tjoeun.service;

import javax.persistence.EntityNotFoundException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tjoeun.dto.UsersFormDTO;
import com.tjoeun.entity.Users;
import com.tjoeun.repository.UsersRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsersService {
	
	private final UsersRepository usersRepository;
	
	public Users createUsers(UsersFormDTO usersFormDTO) {

		// 비밀번호 암호화
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encPassword = passwordEncoder.encode(usersFormDTO.getPassword1());
		
		Users users = new Users();
		users.setUserName(usersFormDTO.getUserName());
		users.setPassword(encPassword);
		users.setEmail(usersFormDTO.getEmail());
		
		usersRepository.save(users);
		return users;
	}
	
	public Users getUsers(String userName) {
		Users user = usersRepository.findByUserName(userName)
																.orElseThrow(() -> new EntityNotFoundException("헤당하는 회원이 존재하지 않습니다."));
//		if(tmpUser.isPresent()) {
//			Users user = tmpUser.get();
//		} else {
//			throw new EntityNotFoundException("헤당하는 회원이 존재하지 않습니다.");
//		}
		return user;
	}
}
