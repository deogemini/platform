package org.ospic.inventory.beds.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.ospic.inventory.beds.domains.Bed;
import org.ospic.inventory.beds.repository.BedRepository;
import org.ospic.patient.infos.domain.Patient;
import org.ospic.util.constants.DatabaseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
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
public class BedReadServiceImpl implements BedReadService {
    @Autowired
    BedRepository bedRepository;
    @Autowired
    SessionFactory sessionFactory;
    public BedReadServiceImpl(BedRepository bedRepository){
        this.bedRepository = bedRepository;
    }
    @Override
    public ResponseEntity<List<Bed>> retrieveBedList() {
        EntityManager entityManager = sessionFactory.createEntityManager();
        entityManager.getTransaction().begin();
        List<Bed> beds = entityManager.createQuery("from "+ DatabaseConstants.BEDS_TABLE, Bed.class).getResultList();
        entityManager.getTransaction().commit();
        entityManager.close();
        return ResponseEntity.ok().body(beds);
    }

    @Override
    public ResponseEntity<Bed> retrieveBedById(Long id) {
        EntityManager entityManager = sessionFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Bed bed = entityManager.find(Bed.class, id);
        if (bed == null){
            throw new EntityNotFoundException(String.format("Can't find bed with id %s",id));
        }
        entityManager.getTransaction().commit();
        entityManager.close();
        return ResponseEntity.ok(bed);
    }
}
