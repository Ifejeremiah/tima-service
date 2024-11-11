package com.tima.service;

import com.tima.model.BaseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.context.SecurityContextHolder;

public class BaseService<T extends BaseObject> {
    @Autowired
    MongoTemplate mongoTemplate;

    protected void updateById(String id, T model) {
        Query query = new Query(Criteria.where("id").is(id));
        mongoTemplate.findAndReplace(query, model);
    }

    protected String fetchCurrentUserId() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
