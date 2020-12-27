package org.ospic.patient.service.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.ospic.inventory.admission.data.AdmissionResponseData;
import org.ospic.inventory.admission.domains.Admission;
import org.ospic.inventory.admission.service.AdmissionsReadServiceImpl;
import org.ospic.patient.diagnosis.domains.Diagnosis;
import org.ospic.patient.infos.domain.Patient;
import org.ospic.patient.infos.repository.PatientInformationRepository;
import org.ospic.patient.service.domain.PatientQueryInfo;
import org.ospic.patient.service.repository.PatientQueryInfoRepository;

import org.ospic.util.constants.DatabaseConstants;
import org.ospic.util.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Repository
public class PatientQueryInfoReadServiceImpl implements PatientQueryInfoReadService {

    private PatientQueryInfoRepository patientQueryInfoRepository;
    private AdmissionsReadServiceImpl admissionsReadService;
    private JdbcTemplate jdbcTemplate;
    private AdmissionResponseData admissionResponseData;
    SessionFactory sessionFactory;



    @Autowired
    public PatientQueryInfoReadServiceImpl(PatientQueryInfoRepository patientQueryInfoRepository, AdmissionsReadServiceImpl admissionsReadService, SessionFactory sessionFactory) {
        this.patientQueryInfoRepository = patientQueryInfoRepository;
        this.admissionsReadService = admissionsReadService;
        this.sessionFactory = sessionFactory;
    }

    @Override
    public ResponseEntity<PatientQueryInfo> retrievePatientById(Long id) {
            PatientQueryInfo patientQueryInfo = patientQueryInfoRepository.findById(id).get();
            return ResponseEntity.ok().body(patientQueryInfo);

    }

    
@Override
public Collection<AdmissionResponseData> retrieveAdmissionById(Long admissionId) {
    final AdmissionsReadServiceImpl.AdmissionResponseDataRowMapper rm = new AdmissionsReadServiceImpl.AdmissionResponseDataRowMapper();
    final String sql = "select " + rm.schema() + " where a.id = ?  order by a.id DESC ";
    return this.jdbcTemplate.query(sql, rm, new Object[]{admissionId});
}


//    @Override
//    public ResponseEntity<List<Diagnosis>> retrievePatientDiagnosis(Long patientId) {
//            Session session = this.sessionFactory.openSession();
//            List<Diagnosis> diagnoses = session.createQuery(String.format("from %s WHERE patient_id = %2d order by date ASC" ,  DatabaseConstants.DIAGNOSES_TABLE, patientId)).list();
//            session.close();
//            return ResponseEntity.ok().body(diagnoses);
//        }
}
