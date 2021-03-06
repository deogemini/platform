package org.ospic.organization.staffs.api;

import io.swagger.annotations.ApiOperation;
import org.ospic.organization.staffs.domains.Staff;
import org.ospic.organization.staffs.service.StaffsReadPrinciplesService;
import io.swagger.annotations.Api;
import org.ospic.organization.staffs.service.StaffsWritePrinciplesService;
import org.ospic.patient.infos.service.PatientInformationReadServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/staffs")
@Api(value = "/api/staffs", tags = "Physicians")
public class StaffsApiResource {

    StaffsReadPrinciplesService readServices;
    StaffsWritePrinciplesService writeServices;
    PatientInformationReadServices patientReadServices;
    

    @Autowired
    StaffsApiResource(StaffsReadPrinciplesService readServices,
                      StaffsWritePrinciplesService writeServices, PatientInformationReadServices patientReadServices) {
        this.readServices = readServices;
        this.writeServices = writeServices;
        this.patientReadServices = patientReadServices;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    List<Staff> all() {
        return readServices.retrieveAllStaffs();
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> createWithIteration(@Valid @RequestBody List<Staff> staffs) {
        return writeServices.createByStaffListIterate(staffs);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getOne(@PathVariable Long id) {
        return readServices.getStaffById(id);
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    ResponseEntity<?> update(@PathVariable Long id, @RequestBody Staff staff){
        return  writeServices.updateStaff(id, staff);
    }

    @ApiOperation(value = "GET patients assigned to this staff",notes = "GET patients assigned to this staff")
    @RequestMapping(value = "/{staffId}/patients", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<?> getPatientAssignedToThisStaff(@PathVariable Long staffId) {
        return ResponseEntity.ok().body(patientReadServices.retrievePatientAssignedToThisStaff(staffId));
    }



}
