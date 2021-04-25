package com.springboot.datajpa.app.controllers;

import com.springboot.datajpa.app.models.entities.Cliente;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import com.springboot.datajpa.app.models.service.IClienteService;
import com.springboot.datajpa.app.models.service.IUploadFileService;
import com.springboot.datajpa.app.util.paginator.PageRender;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@SessionAttributes("cliente")
public class ClienteController {

    @Autowired
    private IClienteService clientesService;

    @Autowired
    private IUploadFileService uploadFileService;

    @GetMapping("/uploads/{filename:.+}")
    private ResponseEntity<Resource> verFoto(@PathVariable String filename) {

        Resource resource = null;
        try {
            resource = uploadFileService.load(filename);
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment: filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/ver/{id}")
    public String ver(@PathVariable(value = "id") Long id, Model model) {

        Cliente cliente = clientesService.findOne(id);

        if (cliente == null) {
            return "redirect:/listar";
        }
        model.addAttribute("cliente", cliente);
        model.addAttribute("titulo", "Detalle cliente: " + cliente.getNombre().concat(" " + cliente.getApellido()));

        return "ver";
    }

    @GetMapping("/listar")
    public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

        Pageable pageRequest = PageRequest.of(page, 10);

        Page<Cliente> clientes = clientesService.findAll(pageRequest);

        PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);
        model.addAttribute("titulo", "Listado de clientes");
        model.addAttribute("clientes", clientes);
        model.addAttribute("page", pageRender);
        return "listar";
    }

    @RequestMapping("/form")
    public String crear(Model model) {

        Cliente cliente = new Cliente();
        model.addAttribute("cliente", cliente);
        model.addAttribute("titulo", "Formulario de Cliente");
        model.addAttribute("boton", "Crear cliente");
        return "form";
    }

    @RequestMapping("/form/{id}")
    public String editar(@PathVariable(value = "id") Long id, Model model) {
        Cliente cliente = null;

        if (id > 0) {
            cliente = clientesService.findOne(id);
        } else {
            return "redirect:/listar";
        }
        model.addAttribute("cliente", cliente);
        model.addAttribute("titulo", "Editar cliente");
        model.addAttribute("boton", "Editar cliente");
        return "form";
    }

    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable(value = "id") Long id) {

        if (id > 0) {
            Cliente cliente = clientesService.findOne(id);
            clientesService.delete(id);

            uploadFileService.delete(cliente.getFoto());

        }
        return "redirect:/listar";
    }

    @PostMapping("/form")
    public String guardar(@Valid Cliente cliente, BindingResult result, Model model, @RequestParam("file") MultipartFile foto, RedirectAttributes flash, SessionStatus status) {

        if (result.hasErrors()) {
            model.addAttribute("titulo", "Formulario de Cliente");
            return "form";
        }

        if (!foto.isEmpty()) {
            if (cliente.getId() != null
                    && cliente.getId() > 0
                    && cliente.getFoto() != null
                    && cliente.getFoto().length() > 0) {

                uploadFileService.delete(cliente.getFoto());
            }
            String uniqueFilename = null;
            try {
                uniqueFilename = uploadFileService.copy(foto);
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
            }

            cliente.setFoto(uniqueFilename);
        }

        clientesService.save(cliente);
        status.setComplete();
        return "redirect:/listar";
    }

}
