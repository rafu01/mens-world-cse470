package com.mensworld.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.mensworld.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Override
    List<Category> findAll();
    @Override
    Category getReferenceById(Integer id);
    @Override
    void deleteById(Integer id);
    @Override
    void deleteAllByIdInBatch(Iterable<Integer> ids);
}