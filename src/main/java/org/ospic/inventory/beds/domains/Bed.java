package org.ospic.inventory.beds.domains;

import com.fasterxml.jackson.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.ospic.inventory.wards.domain.Ward;
import org.ospic.patient.infos.domain.Patient;
import org.ospic.util.constants.DatabaseConstants;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

/**
 * This file was created by eli on 06/11/2020 for org.ospic.inventory.beds.domains
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
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor
@Entity(name = DatabaseConstants.BEDS_TABLE)
@Table(name = DatabaseConstants.BEDS_TABLE)
@ApiModel(value = "Patient", description = "A Patient row containing specific patient information's")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Bed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private @Setter(AccessLevel.PROTECTED) Long id;

    @NotBlank
    @Column(name = "identifier", nullable = false, unique = true, length = 20)
    private String identifier;

    @Column(name = "patient_id",unique = true)
    private  Long patientId;


    @Column(
            name = "is_occupied",
            nullable = false,
            columnDefinition = "boolean default false"
    )
    private Boolean isOccupied;

    @ManyToOne
    @JoinColumn(name = "ward_id")
    @ApiModelProperty(position = 1, required = true, hidden = true, notes = "used to ward")
    @JsonIgnore
    private Ward ward;

    public Bed(Long patientId, Boolean isOccupied){
        this.isOccupied = isOccupied;
        this.patientId = patientId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bed)) return false;
        return id != null && id.equals(((Bed) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}