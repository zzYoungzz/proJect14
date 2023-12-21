package com.project14.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<SiteUser, Long>{
	
	SiteUser findAllByusername(String username);
	Optional<SiteUser> findByusername(String username); 
	
	
	
}
