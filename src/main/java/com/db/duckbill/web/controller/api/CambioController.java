package com.db.duckbill.web.controller.api;
import com.db.duckbill.service.CambioService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.springframework.hateoas.EntityModel;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController 
@RequestMapping("/api/v1/cambio")
@Validated
public class CambioController {
  private final CambioService service;

  public CambioController(CambioService service) {
    this.service = service;
  }
  @GetMapping
  public EntityModel<Map<String,Object>> convert(
      @RequestParam @NotBlank @Pattern(regexp = "[A-Z]{3}") String from,
      @RequestParam @NotBlank @Pattern(regexp = "[A-Z]{3}") String to,
      @RequestParam @Positive BigDecimal valor
  ){
    Map<String,Object> result = Map.of("from", from, "to", to, "valor", valor, "convertido", service.converter(valor, from, to));
    return EntityModel.of(result,
        linkTo(methodOn(CambioController.class).convert(from, to, valor)).withSelfRel()
    );
  }
}
