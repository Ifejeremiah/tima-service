package com.tima.service;

import com.tima.dao.BaseDao;
import com.tima.exception.NotFoundException;
import com.tima.model.BaseObject;
import com.tima.model.Page;

import java.util.List;

public class BaseService<T extends BaseObject> {
    protected BaseDao<T> dao;

    public long create(T model) {
        return dao.create(model);
    }

    public void update(T model) {
        dao.update(model);
    }

    public Page<T> findAll(Integer pageNum, Integer pageSize) {
        return dao.findAll(pageNum, pageSize);
    }

    public List<T> findAll() {
        return dao.findAll();
    }

    public T findById(Integer id) throws NotFoundException {
        T data = dao.find(id);
        if (data == null) throw new NotFoundException("Entity with this ID could not be found");
        return data;
    }
}
