package com.equipo.cargalectiva;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import tools.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api")
public class RestControllerCarga {
 private final Map<String,Object> parametros;
 private final Map<String,Object> catalogos;
 public RestControllerCarga() throws Exception {
  ObjectMapper mapper=new ObjectMapper();
  try(var in=new ClassPathResource("parametros.json").getInputStream()) { parametros=new LinkedHashMap<>(mapper.readValue(in,Map.class)); }
  try(var in=new ClassPathResource("catalogos.json").getInputStream()) { catalogos=mapper.readValue(in,Map.class); }
 }
 @GetMapping("/parametros") public synchronized Map<String,Object> parametros() {return new LinkedHashMap<>(parametros);}
 @GetMapping("/catalogos") public Map<String,Object> catalogos() {return catalogos;}
 public record Cambio(String clave,Object valor) {}
 @PostMapping("/parametros/actualizar") public synchronized Map<String,Object> actualizar(@RequestBody Cambio cambio) {
  if(cambio.clave()==null || !parametros.containsKey(cambio.clave())) throw new ApiError(400,"Parámetro desconocido");
  Object anterior=parametros.get(cambio.clave()), valor=cambio.valor();
  if(anterior instanceof Number) {
   if(!(valor instanceof Number n) || !Double.isFinite(n.doubleValue()) || n.doubleValue()!=Math.rint(n.doubleValue()) || n.doubleValue()<1 || n.doubleValue()>10000) throw new ApiError(400,"Ingrese un entero positivo válido");
   valor=((Number)valor).intValue();
  } else if(!(valor instanceof String s) || s.isBlank()) throw new ApiError(400,"El texto es obligatorio");
  Map<String,Object> propuesta=new LinkedHashMap<>(parametros);propuesta.put(cambio.clave(),valor);
  if(numero(propuesta,"MIN_HORAS_DOCENTE")>numero(propuesta,"MAX_HORAS_DOCENTE") || numero(propuesta,"HORAS_OBJETIVO_CARGO_ADMIN")>numero(propuesta,"MAX_HORAS_CARGO_ADMIN") || numero(propuesta,"MAX_HORAS_CARGO_ADMIN")>numero(propuesta,"MAX_HORAS_DOCENTE") || numero(propuesta,"MAX_HORAS_OTRA_UNIVERSIDAD_TC")>numero(propuesta,"MAX_HORAS_DOCENTE")) throw new ApiError(400,"Los límites de horas son inconsistentes");
  parametros.put(cambio.clave(),valor);
  return Map.of("mensaje","Parámetro actualizado", "clave",cambio.clave(),"valor",valor);
 }
 private int numero(Map<String,Object> p,String k) {return ((Number)p.get(k)).intValue();}
 static class ApiError extends RuntimeException {final int status;ApiError(int s,String m){super(m);status=s;}}
 @ExceptionHandler(ApiError.class) public ResponseEntity<Map<String,String>> error(ApiError e){return ResponseEntity.status(e.status).body(Map.of("mensaje",e.getMessage()));}
}
