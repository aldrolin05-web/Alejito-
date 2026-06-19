package com.example.servidor.repository;

import com.example.servidor.entity.Api;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ApiRepository extends JpaRepository<Api, Integer> {}
