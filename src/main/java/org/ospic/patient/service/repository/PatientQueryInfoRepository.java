package org.ospic.patient.service.repository;

import org.ospic.patient.service.domain.PatientQueryInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientQueryInfoRepository extends JpaRepository<PatientQueryInfo, Long> {
}
