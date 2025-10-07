package com.db.duckbill.web.controller;
import com.db.duckbill.service.DespesaService;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Map;

@RestController 
@RequestMapping("/api/v1/cambio")
public class CambioController {
  private final DespesaService service;

  public CambioController(DespesaService service) {
    this.service = service;
  }
  @GetMapping
  public Map<String,Object> convert(@RequestParam String from, @RequestParam String to, @RequestParam BigDecimal valor){
    return Map.of("from", from, "to", to, "valor", valor, "convertido", service.converter(valor, from, to));
  }
}
