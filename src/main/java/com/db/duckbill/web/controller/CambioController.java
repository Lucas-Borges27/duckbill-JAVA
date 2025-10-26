package com.db.duckbill.web.controller;
import com.db.duckbill.service.DespesaService;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController 
@RequestMapping("/api/v1/cambio")
public class CambioController {
  private final DespesaService service;

  public CambioController(DespesaService service) {
    this.service = service;
  }
  @GetMapping
  public EntityModel<Map<String,Object>> convert(@RequestParam String from, @RequestParam String to, @RequestParam BigDecimal valor){
    Map<String,Object> result = Map.of("from", from, "to", to, "valor", valor, "convertido", service.converter(valor, from, to));
    return EntityModel.of(result,
        linkTo(methodOn(CambioController.class).convert(from, to, valor)).withSelfRel()
    );
  }
}
