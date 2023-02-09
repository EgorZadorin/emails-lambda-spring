package com.egorza.spring.rest.crud.repository;

import com.egorza.spring.rest.crud.model.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscribersRepository extends JpaRepository<Subscriber, Integer> {
  List<Subscriber> findByEmail(String email);
}
