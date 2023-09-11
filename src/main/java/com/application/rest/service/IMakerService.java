package com.application.rest.service;

import com.application.rest.entities.Maker;

import java.util.List;
import java.util.Optional;

public interface IMakerService {
    public List<Maker> findAll();

    public Optional<Maker> findById(Long id);

    public void save(Maker maker);

    public void deleteById(Long id);
}
