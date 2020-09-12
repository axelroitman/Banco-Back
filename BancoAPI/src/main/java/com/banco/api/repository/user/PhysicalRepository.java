package com.banco.api.repository.user;

import com.banco.api.model.user.Physical;
import org.springframework.stereotype.Repository;

@Repository
public interface PhysicalRepository extends UserBaseRepository<Physical> {

	Physical findByUsernameAndUserTypeNumber(String username, int userTypeNumber);

	Physical findByDni(String dni);

	Physical findByLastName(String lastName);
}