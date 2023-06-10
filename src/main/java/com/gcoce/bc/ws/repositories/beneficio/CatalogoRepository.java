package com.gcoce.bc.ws.repositories.beneficio;

import com.gcoce.bc.ws.entities.beneficio.Catalogo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Gabriel Coc Estrada
 * @since 28/05/2023
 */
@Repository
public interface CatalogoRepository extends JpaRepository<Catalogo, Integer> {
}
