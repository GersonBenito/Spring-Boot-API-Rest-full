package com.application.rest.controllers;

import com.application.rest.controllers.dto.MakerDTO;
import com.application.rest.entities.Maker;
import com.application.rest.service.IMakerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/maker")
public class MakerController {

    @Autowired
    private IMakerService makerService;

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){

        Optional<Maker> makerOptional = makerService.findById(id);

        Map<String, Object> response = new HashMap<>();

        if(makerOptional.isPresent()){
            Maker maker = makerOptional.get(); // el .get() obtiene la entidad

            // debemos de retornar un DTO debido a que es una mala practica retornan una entidad
            MakerDTO makerDTO = MakerDTO.builder() // uso del patron de diseño Builder
                    .id(maker.getId()) // asignar los datos, equivalente a setId()
                    .name(maker.getName())
                    .productList(maker.getProductList())
                    .build(); // construimos un objeto de tipo MakerDTO

            //response.put("status", HttpStatus.OK);
            response.put("Data", makerDTO);
            response.put("message", "Registro obtenido");

            return ResponseEntity.ok(response);
        }

        response.put("message", "Product with id " + id + " not found");

        return new ResponseEntity<Object>(response, HttpStatus.NOT_FOUND);
    }

    @GetMapping // en la ruta raiz
    public ResponseEntity<Map<String, Object>> findAll(){

        Map<String, Object> response = new HashMap<>();

        List<MakerDTO> markerList = makerService.findAll()
                .stream() // convertir a un MakerDTO
                .map(maker -> MakerDTO.builder() // funcion landa -->
                        .id(maker.getId())
                        .name(maker.getName())
                        .productList(maker.getProductList())
                        .build()
                ).toList();

        response.put("Data", markerList);
        response.put("message", "Registros obtenidos");

        return ResponseEntity.ok(response);

    }

    @PostMapping // en la ruta raiz
    public ResponseEntity<?> save(@RequestBody MakerDTO makerDTO) throws URISyntaxException {

        Map<String, Object> response = new HashMap<>();

        if(makerDTO.getName().isBlank()){
            response.put("message", "El nombre del fabricante esta vacio");
            return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
        }

        // convertir MakerDTO a Maker
        makerService.save(
                Maker.builder()
                        .name(makerDTO.getName())
                        .build()
        );

        response.put("message", "Registro creado");
        response.put("url", new URI("/api/maker"));

        return new ResponseEntity<Object>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMaker(@PathVariable Long id, @RequestBody MakerDTO makerDTO){
        // verificar si existe el registro a actualizar
        Optional<Maker> makerOptional = makerService.findById(id);

        Map<String, Object> response = new HashMap<>();

        if(makerOptional.isPresent()){

            Maker maker = makerOptional.get();

            if(makerDTO.getName().isBlank()){
                response.put("message", "Los datos estan vacios");
                return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
            }

            maker.setName(makerDTO.getName()); // unicamente se actualizara el nombre
            makerService.save(maker);

            response.put("message", "Registro actualizado");
            return ResponseEntity.ok(response);
        }

        response.put("message", "Registro con id " + id + " no encontrado");
        return new ResponseEntity<Object>(response, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id){
        Map<String, Object> response = new HashMap<>();
        if(id == null){
            response.put("message", "id null");
            return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
        }

        Optional<Maker> makerOptional = makerService.findById(id);
        if(makerOptional.isPresent()){
            makerService.deleteById(id);

            response.put("message", "Registro eliminado");
            return ResponseEntity.ok(response);
        }

        response.put("message", "Registro con id "+ id +" no encontrado");
        return new ResponseEntity<Object>(response, HttpStatus.NOT_FOUND);
    }
}