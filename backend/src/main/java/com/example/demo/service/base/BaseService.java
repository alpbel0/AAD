package com.example.demo.service.base;

import com.example.demo.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public abstract class BaseService<T, ID> {

    protected final BaseRepository<T, ID> repository;

    public BaseService(BaseRepository<T, ID> repository) {
        this.repository = repository;
    }

    public List<T> findAll() {
        return repository.findAll();
    }

    public Page<T> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Optional<T> findById(ID id) {
        return repository.findById(id);
    }

    public T save(T entity) {
        return repository.save(entity);
    }

    public List<T> saveAll(List<T> entities) {
        return repository.saveAll(entities);
    }

    public T update(T entity) {
        return repository.save(entity);
    }

    public void deleteById(ID id) {
        repository.deleteById(id);
    }

    public void delete(T entity) {
        repository.delete(entity);
    }

    public boolean existsById(ID id) {
        return repository.existsById(id);
    }
}