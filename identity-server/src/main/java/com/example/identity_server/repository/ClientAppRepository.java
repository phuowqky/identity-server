package com.example.identity_server.repository;

import com.example.identity_server.entity.ClientApp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientAppRepository extends JpaRepository<ClientApp, String> {
}