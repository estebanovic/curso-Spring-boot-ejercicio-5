package com.springboot.datajpa.app.models.dao;

import com.springboot.datajpa.app.models.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IClienteDao extends JpaRepository<Cliente, Long>{

    
}
