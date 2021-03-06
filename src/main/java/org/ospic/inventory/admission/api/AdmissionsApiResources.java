package org.ospic.inventory.admission.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.ospic.domain.CustomReponseMessage;
import org.ospic.inventory.admission.data.AdmissionRequest;
import org.ospic.inventory.admission.data.AdmissionResponseData;
import org.ospic.inventory.admission.data.EndAdmissionRequest;
import org.ospic.inventory.admission.repository.AdmissionRepository;
import org.ospic.inventory.admission.service.AdmissionsReadService;
import org.ospic.inventory.admission.service.AdmissionsWriteService;
import org.ospic.inventory.admission.visits.data.VisitPayload;
import org.ospic.inventory.admission.visits.service.VisitsReadPrincipleService;
import org.ospic.inventory.admission.visits.service.VisitsWritePrincipleService;
import org.ospic.inventory.admission.visits.service.VisitsWritePrincipleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;

/**
 * This file was created by eli on 09/11/2020 for org.ospic.inventory.admission.api
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
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController()
@Component
@RequestMapping("/api/admissions")
@Api(value = "/api/admissions", tags = "Admissions", description = "Admissions API resources")
public class AdmissionsApiResources {
    @Autowired
    AdmissionsWriteService admissionsWriteService;
    @Autowired
    AdmissionsReadService admissionsReadService;
    @Autowired
    AdmissionRepository admissionRepository;
    @Autowired
    VisitsWritePrincipleService visitsWritePrincipleService;
    @Autowired
    VisitsReadPrincipleService visitsReadPrincipleService;

    public AdmissionsApiResources(AdmissionsWriteService admissionsWriteService,
                                  AdmissionsReadService admissionsReadService,
                                  AdmissionRepository admissionRepository,
                                  VisitsWritePrincipleServiceImpl visitsWritePrincipleService,
                                  VisitsReadPrincipleService visitsReadPrincipleService) {
        this.admissionsWriteService = admissionsWriteService;
        this.admissionsReadService = admissionsReadService;
        this.admissionRepository = admissionRepository;
        this.visitsReadPrincipleService = visitsReadPrincipleService;
        this.visitsWritePrincipleService = visitsWritePrincipleService;
    }

    @ApiOperation(value = "RETRIEVE Admissions", notes = "RETRIEVE Admissions")
    @RequestMapping(value = "/", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<?> retrieveAllAdmissions() {
        return admissionsReadService.retrieveAllAdmissions();
    }

    @ApiOperation(value = "RETRIEVE Admission by ID", notes = "RETRIEVE Admission by ID")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
   @ResponseBody
    ResponseEntity<?> retrieveAdmissionByID(@NotNull @PathVariable("id") Long id, @RequestParam(value = "command", required = false) String command) {
        if (null != command){
            if (command.equals("bed")){
                return admissionsReadService.retrieveListOfAdmissionInBedId(id);
            }
            if (command.equals("patient")){
                return ResponseEntity.ok().body(admissionsReadService.retrieveListOfPatientAdmission(id));
            }

        }
        Collection<AdmissionResponseData> admission = admissionsReadService.retrieveAdmissionById(id);
        return  ResponseEntity.ok().body(admission);
    }




    @ApiOperation(value = "CREATE new  admission", notes = "CREATE new admission")
    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<CustomReponseMessage> requestPatientAdmission(@Valid @RequestBody AdmissionRequest admissionRequest) {
        return admissionsWriteService.admitPatient(admissionRequest);
    }

    @ApiOperation(value = "End patient admission", notes = "End patient admission admission")
    @RequestMapping(value = "/end", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<?> requestPatientUnAdmission( @Valid @RequestBody EndAdmissionRequest r) {
        return admissionsWriteService.endPatientAdmission(r);
    }

    @ApiOperation(value = "RETRIEVE Active admission in this bed", notes = "RETRIEVE active admission in this bed")
    @RequestMapping(value = "/inbed/{bedId}",method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<?> retrieveAdmissionInThisBed(@NotNull @PathVariable("bedId") Long bedId){
        return admissionsReadService.retrieveAdmissionInThisBed(bedId);
    }

    @ApiOperation(value = "RETRIEVE Admission visits", notes = "RETRIEVE Admission visits")
    @RequestMapping(value = "/{admissionId}/visits", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<?> retrieveAdmissionVisits( @PathVariable("admissionId") Long admissionId){
        return visitsReadPrincipleService.retrieveAdmissionVisits(admissionId);
    }

    @ApiOperation(value = "CREATE Admission visits", notes = "CREATE Admission visits")
    @RequestMapping(value = "/visits", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<?> visitAdmission(@Valid @RequestBody VisitPayload visitPayload){
        return visitsWritePrincipleService.createVisits(visitPayload);
    }

}
