package com.ESFE.Asistencias.Controladores;

import com.ESFE.Asistencias.Entidades.Docente;
import com.ESFE.Asistencias.Entidades.Grupo;
import com.ESFE.Asistencias.Servicios.Implementaciones.DocenteServices;
import com.ESFE.Asistencias.Servicios.Interfaces.IDocenteServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/Docentes")
public class DocenteController {

    @Autowired
    private DocenteServices docenteServices;

    @GetMapping
    public String index(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size")Optional<Integer> size){
        int currentPage = page.orElse(1) - 1;
        int pageSize = size.orElse(5);
        Pageable pageable = PageRequest.of(currentPage,pageSize);

        Page<Docente> docentes = docenteServices.BuscarTodosPaginados(pageable);
        model.addAttribute("Docentes", docentes);

        int totalPage = docentes.getTotalPages();
        if (totalPage > 0){
            List<Integer> pageNumber = IntStream.rangeClosed(1,totalPage)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumber", pageNumber);
        }

        return "docente/index";

    }
    @GetMapping("/create")
    public String create(Docente docente){
        return "docente/create";
    }
    @PostMapping("/save")
    public String save(Docente docente, BindingResult result, Model model, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            model.addAttribute(docente);
            attributes.addFlashAttribute("msg", "No se pudo guardar debido a un Error.");
            return "docente/create";
        }
        docenteServices.CrearOeditar(docente);
        attributes.addFlashAttribute("msg","Docente creado correctamente");
        return "redirect:/Docentes";
    }
    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") Integer id, Model model){
        Docente docente = docenteServices.BuscarPorId(id).get();
        model.addAttribute("docente", docente);
        return "docente/details";
    }
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model){
        Docente docente = docenteServices.BuscarPorId(id).get();
        model.addAttribute("docente", docente);
        return "docente/edit";
    }

    @GetMapping("/remove/{id}")
    public  String remove(@PathVariable("id") Integer id, Model model){
        Docente docente = docenteServices.BuscarPorId(id).get();
        model.addAttribute("docente", docente);
        return "docente/delete";
    }

    @PostMapping("/delete")
    public String delete(Docente docente, RedirectAttributes attributes) {
        docenteServices.EliminarPorId(docente.getId());
        attributes.addFlashAttribute("msg", "Docente eliminado correctamente");
        return "redirect:/Docentes";
    }
}
