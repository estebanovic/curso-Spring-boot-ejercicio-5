package com.springboot.datajpa.app.models.dao;

import com.springboot.datajpa.app.models.entities.Producto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IProductoDao extends JpaRepository<Producto,Long>{
    
    @Query("select p from Producto p where p.nombre like %?1%")
    public List<Producto> findByName(String term);
    
}
