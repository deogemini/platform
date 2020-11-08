package org.ospic.inventory.beds.service;

import org.hibernate.SessionFactory;
import org.ospic.inventory.beds.domains.Bed;
import org.ospic.inventory.beds.repository.BedRepository;
import org.ospic.util.constants.DatabaseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * This file was created by eli on 06/11/2020 for org.ospic.inventory.beds.service
 * --
 * --
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
@Repository
public class BedWriteServiceImpl implements BedWriteService {

    @Autowired
    BedRepository bedRepository;
    @Autowired
    SessionFactory sessionFactory;

    public BedWriteServiceImpl(BedRepository bedRepository) {
        this.bedRepository = bedRepository;
    }

    @Override
    public ResponseEntity<String> createNewBed(Bed bed) {
        if (bedRepository.existsByIdentifier(bed.getIdentifier())) {
            return ResponseEntity.badRequest().body(String.format("Bed with the same identifier %s already exist", bed.getIdentifier()));
        }
        EntityManager entityManager = sessionFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(bed);
        entityManager.getTransaction().commit();
        entityManager.close();
        return ResponseEntity.ok().body("Bed Created Successfully");
    }
}
