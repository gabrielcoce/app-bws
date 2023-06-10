package com.gcoce.bc.ws.services.beneficio;

import com.gcoce.bc.ws.repositories.beneficio.CatalogoRepository;
import org.springframework.stereotype.Service;

/**
 * @author Gabriel Coc Estrada
 * @since 29/05/2023
 */
@Service
public class CatalogoSvc {
    private final CatalogoRepository catalogoRepository;

    public CatalogoSvc(CatalogoRepository catalogoRepository){
        this.catalogoRepository = catalogoRepository;
    }


}
