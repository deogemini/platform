package org.ospic.patient.service.domain;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ospic.inventory.admission.domains.Admission;
import org.ospic.patient.diagnosis.domains.Diagnosis;
import org.ospic.patient.infos.domain.Patient;
import org.ospic.util.constants.DatabaseConstants;

import javax.persistence.*;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor
@Entity(name = DatabaseConstants.PATIENT_QUERY_INFO)
@Table(name = DatabaseConstants.PATIENT_QUERY_INFO)
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class PatientQueryInfo {

    @Id
    @Column(name = "id", unique = true)
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "patient_info")
    private Patient patient;

    @OneToOne
    @MapsId
    @JoinColumn(name = "admission_info")
    private Admission admission;

    @OneToOne
    @MapsId
    @JoinColumn(name = "diagnosis_id")
    private Diagnosis diagnosis;


    public PatientQueryInfo(Long id, Patient patient, Admission admission, Diagnosis diagnosis) {
        this.id = id;
        this.patient = patient;
        this.admission = admission;
        this.diagnosis = diagnosis;
    }
}
