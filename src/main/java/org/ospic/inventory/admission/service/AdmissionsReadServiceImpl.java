package org.ospic.inventory.admission.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.ospic.inventory.admission.data.AdmissionResponseData;
import org.ospic.inventory.admission.domains.Admission;
import org.ospic.inventory.admission.repository.AdmissionRepository;
import org.ospic.inventory.beds.domains.Bed;
import org.ospic.inventory.beds.repository.BedRepository;
import org.ospic.patient.infos.domain.Patient;
import org.ospic.patient.infos.repository.PatientInformationRepository;
import org.ospic.util.constants.DatabaseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This file was created by eli on 09/11/2020 for org.ospic.inventory.admission.service
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
public class AdmissionsReadServiceImpl implements AdmissionsReadService {

    @Autowired
    AdmissionRepository admissionRepository;
    @Autowired
    BedRepository bedRepository;
    @Autowired
    PatientInformationRepository patientRepository;
    @Autowired
    SessionFactory sessionFactory;

    public AdmissionsReadServiceImpl(
            BedRepository bedRepository,
            PatientInformationRepository patientRepository,
            AdmissionRepository admissionRepository) {
        this.admissionRepository = admissionRepository;
        this.bedRepository = bedRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public ResponseEntity<List<Admission>> retrieveAllAdmissions() {
        Session session = this.sessionFactory.openSession();
        List<Admission> admissions = session.createQuery(String.format("from %s", DatabaseConstants.TABLE_ADMISSION_INFO)).list();
        session.close();

        return ResponseEntity.ok(admissions);
    }

    @Override
    public ResponseEntity<List<AdmissionResponseData>> retrieveListOfAdmissionInBedId(Long bedId) {
        Session session = this.sessionFactory.openSession();
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT a.id, a.is_active, a.start_date, a.end_date, bed_id as bed, pa.patient_id as patient FROM "+ DatabaseConstants.TABLE_ADMISSION_INFO+ " a ");
        sb.append(" INNER JOIN b_admissions ba ON a.id = ba.b_admission_id  ");
        sb.append(" INNER JOIN p_admissions pa ON a.id = pa.p_admission_id  ");
        sb.append(" WHERE ba.bed_id = 1");
        String query = sb.toString();
        List<AdmissionResponseData> admissions = session.createQuery(query).list();
        session.close();
        return ResponseEntity.ok().body(admissions);
    }

    @Override
    public ResponseEntity<List<Admission>> retrieveListOfAdmissionInWardId(Long wardId) {
        return null;
    }

    @Override
    public ResponseEntity<List<AdmissionResponseData>> retrieveListOfPatientAdmission(Long patientId) {
        Session session = this.sessionFactory.openSession();
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT a.*, bed_id as bed, pa.patient_id as patient FROM m_admissions a   ");
        sb.append(" INNER JOIN b_admissions ba ON a.id = ba.b_admission_id  ");
        sb.append(" INNER JOIN p_admissions pa ON a.id = pa.p_admission_id  ");
        sb.append(" WHERE pa.patient_id =    ".concat(patientId.toString()));

        String query = sb.toString();
        List<AdmissionResponseData> admissions = session.createNativeQuery(String.valueOf(sb),AdmissionResponseData.class).getResultList();
        session.close();
        return ResponseEntity.ok().body(admissions);
    }

    @Override
    public ResponseEntity<List<Admission>> retrieveListOfActiveAdmissions() {
        return null;
    }

    @Override
    public ResponseEntity<List<Admission>> retrieveListOfInactiveAdmissions() {
        return null;
    }
}
