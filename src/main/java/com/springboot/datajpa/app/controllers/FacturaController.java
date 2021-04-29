package com.springboot.datajpa.app.controllers;

import com.springboot.datajpa.app.models.entities.Cliente;
import com.springboot.datajpa.app.models.entities.Factura;
import com.springboot.datajpa.app.models.entities.Producto;
import com.springboot.datajpa.app.models.service.IClienteService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/factura")
@SessionAttributes("factura")
public class FacturaController {
    
    @Autowired
    private IClienteService clienteService;
    
    @GetMapping("/form/{clienteId}")
    public String crear(@PathVariable(value="clienteId") Long clienteId, Model model){
        
        Cliente cliente = clienteService.findOne(clienteId);
        if(cliente == null){
            return "redirect:/listar";
        }
        
        Factura factura = new Factura();
        factura.setCliente(cliente);
        
        model.addAttribute("factura", factura);
        model.addAttribute("titulo", "crear Factura");
        
        return "factura/form";
    }
    
    @GetMapping(value = "/cargar-productos/{term}", produces = {"application/json"})
    public @ResponseBody List<Producto> cargarProductos(@PathVariable String term){
        return clienteService.findByName(term);
    }
}
