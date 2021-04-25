package com.springboot.datajpa.app.models.service;

import com.springboot.datajpa.app.models.entities.Cliente;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.springboot.datajpa.app.models.dao.IClienteDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class ClienteServiceImpl implements IClienteService{

    @Autowired
    private IClienteDao clientesDao;
    
    @Override
    @Transactional(readOnly = true)
    public List<Cliente> findAll() {
        return (List<Cliente>) clientesDao.findAll();
    }

    @Override
    @Transactional
    public void save(Cliente cliente) {
        clientesDao.save(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente findOne(Long id) {
        return clientesDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        clientesDao.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Cliente> findAll(Pageable pageable) {
        return clientesDao.findAll(pageable);
    }
    
}
