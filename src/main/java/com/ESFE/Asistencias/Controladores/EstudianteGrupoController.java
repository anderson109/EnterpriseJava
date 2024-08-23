package com.ESFE.Asistencias.Controladores;


import com.ESFE.Asistencias.Entidades.*;
import com.ESFE.Asistencias.Servicios.Implementaciones.EstudianteGrupoServices;
import com.ESFE.Asistencias.Servicios.Interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/estudianteGrupos")
public class EstudianteGrupoController {

    @Autowired
    private IEstudianteGrupoServices estudiantesGrupoServices;
    @Autowired
    private IEstudianteServices estudianteServices;
    @Autowired
    private IGrupoServices grupoServices;



    @Autowired
    private EstudianteGrupoServices estudianteGrupoServices;
    @GetMapping
    public String index(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size")Optional<Integer> size){
        int currentPage = page.orElse(1) - 1;
        int pageSize = size.orElse(5);
        Pageable pageable = PageRequest.of(currentPage,pageSize);

        Page<EstudianteGrupo> estudianteGrupos = estudianteGrupoServices.BuscarTodosPaginados(pageable);
        model.addAttribute("estudianteGrupos", estudianteGrupos);


        int totalPage = estudianteGrupos.getTotalPages();
        if (totalPage > 0){
            List<Integer> pageNumber = IntStream.rangeClosed(1,totalPage)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumber", pageNumber);
        }

        return "estudianteGrupo/index";

    }
    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("estudianteGrupo", new EstudianteGrupo());
        model.addAttribute("estudiantes", estudianteServices.ObtenerTodos());
        model.addAttribute("grupos", grupoServices.ObtenerTodos());
        return "estudianteGrupo/create"; //
    }
    @PostMapping("/save")
    public String save (@RequestParam Integer estudianteId,
                        @RequestParam Integer grupoId,
                        RedirectAttributes attributes){
        Estudiante estudiante = estudianteServices.BuscarPorId(estudianteId).get();
        Grupo grupo = grupoServices.BuscarPorId(grupoId).get();

        if(estudiante != null && grupo !=null){
            EstudianteGrupo estudianteGrupo = new EstudianteGrupo();
            estudianteGrupo.setEstudiante(estudiante);
            estudianteGrupo.setGrupo(grupo);
            estudianteGrupoServices.CrearOeditar(estudianteGrupo);
            attributes.addFlashAttribute("msg","Asignacion modificada correctamente");
        }
        return "redirect:/estudianteGrupos";
    }


    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") Integer id, Model model){
        EstudianteGrupo estudianteGrupo = estudianteGrupoServices.BuscarPorId(id).get();
        model.addAttribute("estudianteGrupo", estudianteGrupo);
        return "estudianteGrupo/details";
    }
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model){
        EstudianteGrupo estudianteGrupo = estudianteGrupoServices.BuscarPorId(id).get();
        model.addAttribute("estudiantes", estudianteServices.ObtenerTodos());
        model.addAttribute("grupos",grupoServices.ObtenerTodos());
        model.addAttribute("estudianteGrupo", estudianteGrupo);
        return "estudianteGrupo/edit";
    }
    @GetMapping("/remove/{id}")
    public String remove(@PathVariable("id") Integer id, Model model){
        EstudianteGrupo estudianteGrupo = estudianteGrupoServices.BuscarPorId(id).get();
        model.addAttribute("estudianteGrupo", estudianteGrupo);
        return "estudianteGrupo/delete";

    }
    @PostMapping("/delete")
    public String delete(EstudianteGrupo estudianteGrupo, RedirectAttributes attributes){
        estudianteGrupoServices.EliminarPorId(estudianteGrupo.getId());
        attributes.addFlashAttribute("msg","Asignacion eliminada correctamente");
        return "redirect:/estudianteGrupos";
    }
    @PostMapping("/update")
    public String update(@RequestParam Integer id, @RequestParam Integer estudianteId,
                         @RequestParam Integer grupoId,
                         RedirectAttributes attributes){
        Estudiante estudiante = estudianteServices.BuscarPorId(estudianteId).get();
        Grupo grupo = grupoServices.BuscarPorId(grupoId).get();

        if (estudiante != null && grupo != null){
            EstudianteGrupo estudianteGrupo = new EstudianteGrupo();
            estudianteGrupo.setId(id);
            estudianteGrupo.setEstudiante(estudiante);
            estudianteGrupo.setGrupo(grupo);
            estudianteServices.CrearOeditar(estudianteGrupo.getEstudiante());
            attributes.addFlashAttribute("msg", "Asignación guardada correctamente");
        }
        return "redirect:/estudianteGrupos";
    }
}
