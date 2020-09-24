package com.banco.api.repository.user;

import com.banco.api.model.user.User;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface UserBaseRepository<T extends User> extends CrudRepository<T, Integer> {

    T findByUsername(String username);

    List<T> findByUsernameContainingIgnoreCase(String username);

    List<T> findByCuitCuilCdi(String cuitCuilCdi);

	/*public boolean existsByUsername(String username);

    @Query("select userTypeNumber from users where users.username = ?")
	public int getUserTypeNumber(String username);*/
}
