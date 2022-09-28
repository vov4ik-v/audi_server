package com.volodymyrvasylyshyn.audi_server.service;

import com.volodymyrvasylyshyn.audi_server.exeptions.AudiModelNotFoundException;
import com.volodymyrvasylyshyn.audi_server.model.AudiModel;
import com.volodymyrvasylyshyn.audi_server.repository.AudiModelRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AudiModelService {
    private final AudiModelRepository audiModelRepository;

    public AudiModelService(AudiModelRepository audiModelRepository) {
        this.audiModelRepository = audiModelRepository;
    }

    public List<AudiModel> getAll() {
        return audiModelRepository.findAll();
    }

    public AudiModel getOneModel(Integer id) {
        Optional<AudiModel> optionalAudiModel = audiModelRepository.findById(id);
        if (optionalAudiModel.isEmpty()){
            throw  new AudiModelNotFoundException("audiModel with id: " + id +" not found");
        }
        return optionalAudiModel.get();
    }

    public void createModel(AudiModel audiModel) {
        audiModelRepository.save(audiModel);
    }

    public void editModel(AudiModel updatedModel, Integer id) {
        Optional<AudiModel> optionalAudiModel = audiModelRepository.findById(id);
        if (optionalAudiModel.isEmpty()){
            throw  new AudiModelNotFoundException("audiModel with id: " + id +" not found");
        }
        updatedModel.setId(id);
        audiModelRepository.save(updatedModel);
    }

    public void deleteModel(Integer id) {
        audiModelRepository.deleteById(id);
    }
}
