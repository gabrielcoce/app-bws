package com.gcoce.bc.ws.services.peso_cabal;

import com.gcoce.bc.ws.repositories.peso_cabal.ParcialidadPcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Gabriel Coc Estrada
 * @since 30/05/2023
 */
@Service
@Transactional(transactionManager = "pesoCabalTransactionManager")
public class ParcialidadPcSvc {
    @Autowired
    private ParcialidadPcRepository pcRepository;
}
