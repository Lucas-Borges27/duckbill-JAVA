package com.db.duckbill.service;

import com.db.duckbill.domain.entity.Categoria;
import com.db.duckbill.domain.entity.Despesa;
import com.db.duckbill.domain.entity.Usuario;
import com.db.duckbill.domain.repo.DespesaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DespesaServiceTest {

    @Mock DespesaRepository despesaRepository;

    @InjectMocks DashboardService dashboardService;

    @Test
    void totalMes_somaValoresDoMes() {
        Usuario u = new Usuario();
        u.setId(1L);
        Categoria c = new Categoria(1L, "Alimentação");

        Despesa d1 = new Despesa();
        d1.setUsuario(u);
        d1.setCategoria(c);
        d1.setValor(new BigDecimal("10.50"));
        d1.setDataCompra(LocalDate.of(2026, 3, 1));

        Despesa d2 = new Despesa();
        d2.setUsuario(u);
        d2.setCategoria(c);
        d2.setValor(new BigDecimal("20.00"));
        d2.setDataCompra(LocalDate.of(2026, 3, 2));

        YearMonth ym = YearMonth.of(2026, 3);
        when(despesaRepository.findByUsuario_IdAndDataCompraBetween(1L, ym.atDay(1), ym.atEndOfMonth()))
            .thenReturn(List.of(d1, d2));

        BigDecimal total = dashboardService.totalMes(1L, ym);
        assertThat(total).isEqualByComparingTo("30.50");
    }
}
