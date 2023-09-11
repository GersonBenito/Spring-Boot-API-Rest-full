package com.application.rest.controllers;

import com.application.rest.controllers.dto.MakerDTO;
import com.application.rest.entities.Maker;
import com.application.rest.service.IMakerService;
import org.springframework.beans.factory.annotation.Autowired;
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

        if(makerOptional.isPresent()){
            Maker maker = makerOptional.get(); // el .get() obtiene la entidad

            // debemos de retornar un DTO debido a que es una mala practica retornan una entidad
            MakerDTO makerDTO = MakerDTO.builder() // uso del patron de diseño Builder
                    .id(maker.getId()) // asignar los datos, equivalente a setId()
                    .name(maker.getName())
                    .productList(maker.getProductList())
                    .build(); // construimos un objeto de tipo MakerDTO

            return ResponseEntity.ok(makerDTO);
        }

        Map<String, String> response = new HashMap<>();

        response.put("status", "404");
        response.put("message", "Product with id " + id + " not found");

        return ResponseEntity.notFound().build();
    }

    @GetMapping // en la ruta raiz
    public ResponseEntity<?> findAll(){
        List<MakerDTO> markerList = makerService.findAll()
                .stream() // convertir a un MakerDTO
                .map(maker -> MakerDTO.builder() // funcion landa -->
                        .id(maker.getId())
                        .name(maker.getName())
                        .productList(maker.getProductList())
                        .build()
                ).toList();

        return ResponseEntity.ok(markerList);

    }

    @PostMapping // en la ruta raiz
    public ResponseEntity<?> save(@RequestBody MakerDTO makerDTO) throws URISyntaxException {
        if(makerDTO.getName().isBlank()){
            return ResponseEntity.badRequest().build();
        }

        // convertir MakerDTO a Maker
        makerService.save(
                Maker.builder()
                        .name(makerDTO.getName())
                        .build()
        );

        return ResponseEntity.created(new URI("/api/maker")).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMaker(@PathVariable Long id, @RequestBody MakerDTO makerDTO){
        // verificar si existe el registro a actualizar
        Optional<Maker> makerOptional = makerService.findById(id);

        if(makerOptional.isPresent()){
            Maker maker = makerOptional.get();
            maker.setName(makerDTO.getName()); // unicamente se actualizara el nombre

            makerService.save(maker);

            return ResponseEntity.ok("Registro actualizado");
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id){
        if(id == null){
            return ResponseEntity.badRequest().build();
        }

        Optional<Maker> makerOptional = makerService.findById(id);
        if(makerOptional.isPresent()){
            makerService.deleteById(id);
            return ResponseEntity.ok("Registro eliminado");
        }

        return ResponseEntity.notFound().build();
    }
}