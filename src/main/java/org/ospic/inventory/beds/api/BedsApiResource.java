package org.ospic.inventory.beds.api;

import io.swagger.annotations.*;
import org.ospic.inventory.beds.domains.Bed;
import org.ospic.inventory.beds.repository.BedRepository;
import org.ospic.inventory.beds.service.BedReadService;
import org.ospic.inventory.beds.service.BedWriteService;
import org.ospic.patient.infos.domain.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * This file was created by eli on 06/11/2020 for org.ospic.inventory.beds.api
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
@RequestMapping("/api/beds")
@Api(value = "/api/beds", tags = "Beds", description = "Beds API resources")
public class BedsApiResource {

    private final BedReadService bedReadService;
    private final BedWriteService bedWriteService;
    private final BedRepository bedRepository;

    @Autowired
    public BedsApiResource(
            BedReadService bedReadService,
            BedWriteService bedWriteService,
            BedRepository bedRepository) {
        this.bedReadService = bedReadService;
        this.bedWriteService = bedWriteService;
        this.bedRepository = bedRepository;
    }


    @ApiOperation(
            value = "LIST all Beds",
            notes = "LIST all Beds"
    )
    @RequestMapping(value = "/",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody

    ResponseEntity<List<Bed>> retrieveBeds() {
        return bedReadService.retrieveBedList();
    }

    @ApiOperation(
            value = "CREATE Bed",
            notes = "CREATE bed"
    )
    @RequestMapping(
            value = "/",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.ALL_VALUE
    )

    @ResponseBody
    ResponseEntity<String> createBed(@Valid  @ApiParam(name = "Bed Entity", required = true)  @RequestBody  Bed bedData){
        return bedWriteService.createNewBed(bedData);
    }

    @ApiOperation(
            value = "GET Bed by ID",
            notes = "GET Bed by ID"
    )
    @RequestMapping(
            value = "/{bedId}",
            method = RequestMethod.GET,
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    ResponseEntity<Bed> retrieveBedById(@ApiParam(name = "Bed ID", required = true) @PathVariable Long bedId){
        return ResponseEntity.ok().body(bedRepository.findById(bedId).get());
    }
    @ApiOperation(
            value = "GET Bed by IDentifier",
            notes = "GET Bed by IDentifier"
    )
    @RequestMapping(
            value = "/identifier/{Identifier}",
            method = RequestMethod.GET,
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    ResponseEntity<List<Bed>> retrieveBedByIdentifier(@ApiParam(name = "Bed Identifier", required = true) @PathVariable String Identifier){
        return ResponseEntity.ok().body(bedRepository.findByIdentifierEquals(Identifier).get());
    }
}
