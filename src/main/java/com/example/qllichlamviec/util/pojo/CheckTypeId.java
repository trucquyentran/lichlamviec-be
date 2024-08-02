package com.example.qllichlamviec.util.pojo;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class CheckTypeId {@Autowired
private MongoTemplate mongoTemplate;

    public String checkIdType(ObjectId id) {
        if (mongoTemplate.exists(Query.query(Criteria.where("_id").is(id)), "TaiKhoan")) {
            return "taiKhoan";
        } else if (mongoTemplate.exists(Query.query(Criteria.where("_id").is(id)), "DonVi")) {
            return "donVi";
        } else {
            throw new IllegalArgumentException("Invalid ID");
        }
    }
}
