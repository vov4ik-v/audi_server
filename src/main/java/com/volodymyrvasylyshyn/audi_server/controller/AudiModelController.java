package com.volodymyrvasylyshyn.audi_server.controller;

import com.volodymyrvasylyshyn.audi_server.common.ApiResponse;
import com.volodymyrvasylyshyn.audi_server.model.AudiModel;
import com.volodymyrvasylyshyn.audi_server.service.AudiModelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/model")
@CrossOrigin
public class AudiModelController {
    private final AudiModelService audiModelService;

    public AudiModelController(AudiModelService audiModelService) {
        this.audiModelService = audiModelService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<AudiModel>> getAllModels(){
        List<AudiModel> audiModels = audiModelService.getAll();
        return new ResponseEntity<>(audiModels, HttpStatus.OK);
    }
    @GetMapping("/getOne/{id}")
    public ResponseEntity<AudiModel> getOneModel(@PathVariable("id") Integer id){
        AudiModel audiModel = audiModelService.getOneModel(id);
        return new ResponseEntity<>(audiModel, HttpStatus.OK);

    }
    @PostMapping("/addModel")
    public ResponseEntity<ApiResponse> addNewAudiModel(@RequestBody AudiModel audiModel){
         audiModelService.createModel(audiModel);
        return new ResponseEntity<>(new ApiResponse(true,"Audi Model added success"), HttpStatus.CREATED);

    }
    @PutMapping("/edit/{id}")
    public ResponseEntity<ApiResponse> addNewAudiModel(@PathVariable("id") Integer id,@RequestBody AudiModel updatedModel){
        audiModelService.editModel(updatedModel,id);
        return new ResponseEntity<>(new ApiResponse(true,"Audi Model edited success"), HttpStatus.OK);

    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteAudiModel(@PathVariable("id") Integer id){
        audiModelService.deleteModel(id);
        return new ResponseEntity<>(new ApiResponse(true,"Audi Model deleted success"), HttpStatus.OK);

    }
}
