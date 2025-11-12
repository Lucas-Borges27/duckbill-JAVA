package com.db.duckbill.domain.repo;

import com.db.duckbill.domain.entity.TransacaoAtivo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransacaoAtivoRepository extends JpaRepository<TransacaoAtivo, Long> {
}
