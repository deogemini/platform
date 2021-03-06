package org.ospic.inventory.pharmacy.Medicine.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.ospic.inventory.pharmacy.Categories.domains.MedicineCategory;
import org.ospic.inventory.pharmacy.Categories.repository.MedicineCategoryRepository;
import org.ospic.inventory.pharmacy.Groups.api.MedicineGroupsApiResources;
import org.ospic.inventory.pharmacy.Groups.domains.MedicineGroup;
import org.ospic.inventory.pharmacy.Groups.repository.MedicineGroupRepository;
import org.ospic.inventory.pharmacy.Medicine.data.MedicineDataTemplate;
import org.ospic.inventory.pharmacy.Medicine.domains.Medicine;
import org.ospic.inventory.pharmacy.Medicine.repository.MedicineRepository;
import org.ospic.patient.infos.domain.Patient;
import org.ospic.util.constants.DatabaseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

/**
 * This file was created by eli on 14/11/2020 for org.ospic.inventory.pharmacy.Medicine.service
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
public class MedicineReadServiceImpl implements MedicineReadService {

    @Autowired
    SessionFactory sessionFactory;
    @Autowired
    MedicineRepository medicineRepository;
    @Autowired
    MedicineGroupRepository medicineGroupRepository;
    @Autowired
    MedicineCategoryRepository medicineCategoryRepository;

    @Autowired
    public MedicineReadServiceImpl(MedicineRepository medicineRepository,
                                   MedicineGroupRepository medicineGroupRepository,
                                   MedicineCategoryRepository medicineCategoryRepository){
        this.medicineRepository = medicineRepository;
        this.medicineGroupRepository = medicineGroupRepository;
        this.medicineCategoryRepository = medicineCategoryRepository;
    }

    @Override
    public List<Medicine> fetchAllMedicine() {
        Session session = this.sessionFactory.openSession();
        List<Medicine> medicines = session.createQuery(String.format("from %s", DatabaseConstants.TABLE_PHARMACY_MEDICINES)).list();
        medicines.forEach(medicine -> {
            if(medicine.getGroup() != null){
                MedicineGroup medicineGroup = medicineGroupRepository.findById(medicine.getGroup().getId()).get();
                medicine.setGroup(medicineGroup);
            }
            if(medicine.getCategory() != null){
                MedicineCategory medicineCategory = medicineCategoryRepository.findById(medicine.getCategory().getId()).get();
                medicine.setCategory(medicineCategory);
            }
        });

        session.close();
        return medicines;
    }

    @Override
    public MedicineDataTemplate readMedicineDataTemplate() {
        List<MedicineGroup> medicineGroups = medicineGroupRepository.findAll();
        List<MedicineCategory> medicineCategories = medicineCategoryRepository.findAll();

        return new MedicineDataTemplate(medicineCategories, medicineGroups);
    }
}
