package org.ospic.patient.service.api;


import io.swagger.annotations.ApiOperation;
import org.ospic.inventory.admission.data.AdmissionResponseData;
import org.ospic.inventory.admission.domains.Admission;
import org.ospic.patient.diagnosis.domains.Diagnosis;
import org.ospic.patient.service.domain.PatientQueryInfo;
import org.ospic.patient.service.repository.PatientQueryInfoRepository;
import org.ospic.patient.service.service.PatientQueryInfoReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController()
@RequestMapping("/api/patientService")
public class PatientQueryInfoApiResources {
    PatientQueryInfoReadService patientQueryInfoReadService;
    PatientQueryInfoRepository patientQueryInfoRepository;

    @Autowired
    public PatientQueryInfoApiResources(PatientQueryInfoReadService patientQueryInfoReadService, PatientQueryInfoRepository patientQueryInfoRepository) {
        this.patientQueryInfoReadService = patientQueryInfoReadService;
        this.patientQueryInfoRepository = patientQueryInfoRepository;
    }
//retrieving patient details by id

    @ApiOperation(value = "RETRIEVE assigned patient", notes = "RETRIEVE patient by id")
    @RequestMapping(value = "/patientId", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<PatientQueryInfo> getPatientById(Long id){
        return patientQueryInfoReadService.retrievePatientById(id);
    }

//retrieving admission details by the same id
    @RequestMapping(value = "/admissionId", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    Collection<AdmissionResponseData> getPatientAdmission(Long id){
        return patientQueryInfoReadService.retrieveAdmissionById(id);
    }


//    @ResponseBody
//    ResponseEntity<<Diagnosis> getPatientDiagnosis(Long id){
//        return patientQueryInfoReadService.retrievePatientDiagnosis(id);
//    }
}
