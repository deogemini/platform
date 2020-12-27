package org.ospic.patient.service.service;

import org.ospic.inventory.admission.data.AdmissionResponseData;
import org.ospic.inventory.admission.domains.Admission;
import org.ospic.patient.diagnosis.domains.Diagnosis;
import org.ospic.patient.infos.domain.Patient;
import org.ospic.patient.service.domain.PatientQueryInfo;
import org.ospic.util.exceptions.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@Component
@Service
public interface PatientQueryInfoReadService {


    ResponseEntity<PatientQueryInfo> retrievePatientById(Long id);

    Collection<AdmissionResponseData> retrieveAdmissionById(Long id);

//    ResponseEntity<Diagnosis> retrievePatientDiagnosis(Long patientId);
}
