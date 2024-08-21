package com.ESFE.Asistencias.Controladores;

import com.ESFE.Asistencias.Entidades.Docente;
import com.ESFE.Asistencias.Entidades.DocenteGrupo;
import com.ESFE.Asistencias.Entidades.Grupo;
import com.ESFE.Asistencias.Servicios.Implementaciones.DocenteGrupoServices;
import com.ESFE.Asistencias.Servicios.Interfaces.IDocenteGrupoServices;
import com.ESFE.Asistencias.Servicios.Interfaces.IDocenteServices;
import com.ESFE.Asistencias.Servicios.Interfaces.IGrupoServices;
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
@RequestMapping("/docenteGrupos")
public class DocenteGrupoController {

    @Autowired
    private IDocenteGrupoServices docenteGrupoService;
    @Autowired
    private IDocenteServices docenteServices;
    @Autowired
    private IGrupoServices grupoServices;



    @Autowired
    private DocenteGrupoServices docenteGrupoServices;
    @GetMapping
    public String index(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size")Optional<Integer> size){
        int currentPage = page.orElse(1) - 1;
        int pageSize = size.orElse(5);
        Pageable pageable = PageRequest.of(currentPage,pageSize);

        Page<DocenteGrupo> docenteGrupos = docenteGrupoServices.BuscarTodosPaginados(pageable);
        model.addAttribute("docenteGrupos", docenteGrupos);


        int totalPage = docenteGrupos.getTotalPages();
        if (totalPage > 0){
            List<Integer> pageNumber = IntStream.rangeClosed(1,totalPage)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumber", pageNumber);
        }

        return "docenteGrupo/index";

    }
    @GetMapping("/create")
    public String create(Model model){
        model.addAttribute("docenteGrupo", new DocenteGrupo());
        model.addAttribute("docentes", docenteServices.ObtenerTodos());
        model.addAttribute("grupos",grupoServices.ObtenerTodos());
        return "docenteGrupo/create";
    }
    @PostMapping("/save")
    public String save (@RequestParam Integer docenteId,
                        @RequestParam Integer grupoId,
                        @RequestParam Integer anio,
                        @RequestParam String ciclo,
                        RedirectAttributes attributes){
        Docente docente = docenteServices.BuscarPorId(docenteId).get();
        Grupo grupo = grupoServices.BuscarPorId(grupoId).get();

        if(docente != null && grupo !=null){
            DocenteGrupo docenteGrupo = new DocenteGrupo();
            docenteGrupo.setDocente(docente);
            docenteGrupo.setGrupo(grupo);
            docenteGrupo.setAnio(anio);
            docenteGrupo.setCiclo(ciclo);
            docenteGrupoServices.CrearOeditar(docenteGrupo);
            attributes.addFlashAttribute("msg","Asignacion modificada correctamente");
        }
        return "redirect:/docenteGrupos";
    }


    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") Integer id, Model model){
        DocenteGrupo docenteGrupo = docenteGrupoServices.BuscarPorId(id).get();
        model.addAttribute("docenteGrupo", docenteGrupo);
        return "docenteGrupo/details";
    }
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model){
        DocenteGrupo docenteGrupo = docenteGrupoServices.BuscarPorId(id).get();
        model.addAttribute("docentes", docenteServices.ObtenerTodos());
        model.addAttribute("grupos",grupoServices.ObtenerTodos());
        model.addAttribute("docenteGrupo", docenteGrupo);
        return "docenteGrupo/edit";
    }
    @GetMapping("/remove/{id}")
    public String remove(@PathVariable("id") Integer id, Model model){
        DocenteGrupo docenteGrupo = docenteGrupoServices.BuscarPorId(id).get();
        model.addAttribute("docenteGrupo", docenteGrupo);
        return "docenteGrupo/delete";

    }
    @PostMapping("/delete")
    public String delete(DocenteGrupo docenteGrupo, RedirectAttributes attributes){
        docenteGrupoService.EliminarPorId(docenteGrupo.getId());
        attributes.addFlashAttribute("msg","Asignacion eliminada correctamente");
        return "redirect:/docenteGrupos";
    }
    @PostMapping("/update")
    public String update(@RequestParam Integer id, @RequestParam Integer docenteId,
                         @RequestParam Integer grupoId,
                         @RequestParam Integer anio, @RequestParam String ciclo,
                         RedirectAttributes attributes){
        Docente docente = docenteServices.BuscarPorId(docenteId).get();
        Grupo grupo = grupoServices.BuscarPorId(grupoId).get();

        if (docente != null && grupo != null){
            DocenteGrupo docenteGrupo = new DocenteGrupo();
            docenteGrupo.setId(id);
            docenteGrupo.setDocente(docente);
            docenteGrupo.setGrupo(grupo);
            docenteGrupo.setAnio(anio);
            docenteGrupo.setCiclo(ciclo);

            docenteGrupoService.CrearOeditar(docenteGrupo);
            attributes.addFlashAttribute("msg", "Asignación guardada correctamente");
        }
        return "redirect:/docenteGrupos";
    }
}
