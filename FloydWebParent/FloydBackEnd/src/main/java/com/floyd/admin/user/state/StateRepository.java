package com.floyd.admin.user.state;

import com.floyd.common.entity.State;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StateRepository extends CrudRepository<State, Integer> {
}
