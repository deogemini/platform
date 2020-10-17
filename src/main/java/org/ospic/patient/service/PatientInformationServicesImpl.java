package org.ospic.patient.service;

import org.ospic.contacts.domain.ContactsInformation;
import org.ospic.contacts.repository.ContactsInformationRepository;
import org.ospic.contacts.services.ContactsInformationService;
import org.ospic.fileuploads.service.FilesStorageService;
import org.ospic.patient.data.PatientData;
import org.ospic.patient.domain.Patient;
import org.ospic.patient.repository.PatientInformationRepository;
import org.ospic.payload.response.MessageResponse;
import org.ospic.physicians.domains.Physician;
import org.ospic.physicians.service.PhysicianInformationService;
import org.ospic.util.exceptions.ResourceNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

/**
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
public class PatientInformationServicesImpl implements PatientInformationServices {

    @Autowired
    private PatientInformationRepository patientInformationRepository;
    @Autowired
    ContactsInformationRepository contactsInformationRepository;
    @Autowired
    ContactsInformationService contactsInformationService;
    @Autowired
    SessionFactory sessionFactory;
    FilesStorageService filesStorageService;

    PhysicianInformationService physicianInformationService;

    @Autowired
    public PatientInformationServicesImpl(
            PhysicianInformationService physicianInformationService,
            FilesStorageService filesStorageService
    ) {
        this.physicianInformationService = physicianInformationService;
        this.filesStorageService = filesStorageService;
    }

    @Override
    public List<Patient> retrieveAllPatients() {
        Session session = this.sessionFactory.openSession();
        List<Patient> patientList = session.createQuery("from m_patient").list();
        session.close();
        return patientList;
    }

    @Override
    public ResponseEntity<List<Patient>> retrieveAllAssignedPatients() {
        Session session = this.sessionFactory.openSession();
        List<Patient> patients = session.createQuery("from m_patient WHERE physician_id IS NOT NULL").list();
        session.close();
        return ResponseEntity.status(HttpStatus.OK).body(patients);
    }

    @Override
    public ResponseEntity<List<Patient>> retrieveAllUnAssignedPatients() {
        Session session = this.sessionFactory.openSession();
        List<Patient> patients = session.createQuery("from m_patient WHERE physician_id IS NULL").list();
        session.close();
        return ResponseEntity.status(HttpStatus.OK).body(patients);
    }

    @Override
    public ResponseEntity retrievePatientCreationDataTemplate() {
        List<Physician> physiciansOptions = physicianInformationService.retrieveAllPhysicians();
        for (int i = 0; i < physiciansOptions.size(); i++) {
            physiciansOptions.get(i).getPatients().clear();
        }
        return ResponseEntity.ok().body(PatientData.patientCreationTemplate(physiciansOptions));
    }

    @Override
    public Patient createNewPatient(Patient patientInformation) {
        return patientInformationRepository.save(patientInformation);
    }

    @Override
    public List<Patient> createByPatientListIterate(List<Patient> patientInformationList) {
        return patientInformationRepository.saveAll(patientInformationList);
    }

    @Override
    public ResponseEntity retrievePatientById(Long id) throws ResourceNotFoundException {
        Patient patient = patientInformationRepository.findById(id).get();
        if (patient.getPhysician() != null) {
            patient.getPhysician().getPatients().clear();
        }
        return ResponseEntity.ok().body(patient);
    }

    @Override
    public ResponseEntity deletePatientById(Long id) {
        if (patientInformationRepository.existsById(id)) {
            patientInformationRepository.deleteById(id);
            return ResponseEntity.ok(new MessageResponse("Patient deleted Successfully"));
        } else {
            return ResponseEntity.ok(new MessageResponse("Patient with a given ID is either not available or has being deleted by someone else"));
        }
    }

    @Override
    public ResponseEntity updatePatient(Long id, Patient update) {
        return patientInformationRepository.findById(id)
                .map(patient -> {
                    patient.setCountry(update.getCountry() == null ? patient.getCountry() : update.getCountry());
                    patient.setDob(update.getDob() == null ? patient.getDob() : update.getDob());
                    patient.setEthnicity(update.getEthnicity() == null ? patient.getEthnicity() : update.getEthnicity());
                    patient.setFirst_name(update.getFirst_name() == null ? patient.getFirst_name() : update.getFirst_name());
                    patient.setMiddle_name(update.getMiddle_name() == null ? patient.getMiddle_name() : update.getMiddle_name());
                    patient.setLast_name(update.getLast_name() == null ? patient.getLast_name() : update.getLast_name());
                    patient.setMdn(update.getMdn() == null ? patient.getMdn() : update.getMdn());
                    patient.setGender(update.getGender() == null ? patient.getGender() : update.getGender());
                    patient.setSuffix(update.getSuffix() == null ? patient.getSuffix() : update.getSuffix());
                    patient.setPrincipal_tribe(update.getPrincipal_tribe() == null ? patient.getPrincipal_tribe() : update.getPrincipal_tribe());
                    patient.setContactsInformation(patient.getContactsInformation());
                    patient.setSsn(update.getSsn() == null ? patient.getSsn() : update.getSsn());
                    return ResponseEntity.ok(patientInformationRepository.save(patient));

                }).orElseThrow(() -> new EntityNotFoundException());
    }

    @Override
    public ContactsInformation updatePatientContacts(Long patientId, ContactsInformation contactsInformationRequest) {
        return patientInformationRepository.findById(patientId).map(patientInformation -> {
            ContactsInformation contactsInformation = new ContactsInformation(
                    contactsInformationRequest.getIsReachable(), contactsInformationRequest.getEmail_address(),
                    contactsInformationRequest.getZipcode(), contactsInformationRequest.getCity(),
                    contactsInformationRequest.getState(), contactsInformationRequest.getPhysical_address(),
                    contactsInformationRequest.getWork_phone(), contactsInformationRequest.getWork_phone(), patientInformation
            );
            patientInformation.setContactsInformation(contactsInformation);
            contactsInformation.setPatient(patientInformation);

            patientInformationRepository.save(patientInformation);
            return contactsInformation;
        }).orElseGet(() -> {
            return null;
        });
    }

    @Transactional
    @Override
    public ResponseEntity assignPatientToPhysician(Long patientId, Long physicianId) throws ResourceNotFoundException {
        return patientInformationRepository.findById(patientId).map(patient -> {
            physicianInformationService.retrievePhysicianById(physicianId).ifPresent(physician -> {

                patient.setPhysician(physician);
                patientInformationRepository.save(patient);

            });

            return ResponseEntity.ok(physicianInformationService.getPhysicianById(physicianId));
        }).orElseThrow(() -> new ResourceNotFoundException("Physician not set"));

    }

    @Transactional
    @Override
    public ResponseEntity uploadPatientImage(Long patientId, MultipartFile file) {
        try {
            return patientInformationRepository.findById(patientId).map(patient -> {
                String imagePath = filesStorageService.uploadPatientImage(patientId, "images", file);
                patient.setImageThumbnail(imagePath);
                return ResponseEntity.ok().body(patientInformationRepository.save(patient));
            }).orElseThrow(() -> new ResourceNotFoundException("patient with id: " + patientId + "not found"));
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Transactional
    @Override
    public ResponseEntity deletePatientImage(Long patientId, String fileName) {
        try {
            patientInformationRepository.findById(patientId).map(patient -> {
                filesStorageService.deletePatientFileOrDocument("images", patientId, fileName);
                patient.setImageThumbnail(null);
                patientInformationRepository.save(patient);
                return ResponseEntity.ok().body(patientInformationRepository.findById(patientId));
            }).orElseThrow(() -> new ResourceNotFoundException("patient with id: " + patientId + "not found"));
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}