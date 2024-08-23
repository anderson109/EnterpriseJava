package com.ESFE.Asistencias.Repositorios;

import com.ESFE.Asistencias.Entidades.DocenteGrupo;
import com.ESFE.Asistencias.Entidades.EstudianteGrupo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEstudianteGrupoRepository extends JpaRepository<EstudianteGrupo, Integer> {
    Page<EstudianteGrupo> findByOrderByEstudianteDesc(Pageable pageable);
}
