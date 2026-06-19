package com.example.servidor.repository;

import com.example.servidor.entity.Endpoint;
import org.springframework.data.jpa.repository.JpaRepository;
public interface EndpointRepository extends JpaRepository<Endpoint, Integer> {}
