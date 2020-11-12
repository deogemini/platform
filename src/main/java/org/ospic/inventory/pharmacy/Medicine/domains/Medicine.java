package org.ospic.inventory.pharmacy.Medicine.domains;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.ospic.inventory.pharmacy.Categories.domains.MedicineCategory;
import org.ospic.inventory.pharmacy.Groups.domains.MedicineGroup;
import org.ospic.physicians.domains.Physician;
import org.ospic.util.constants.DatabaseConstants;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * This file was created by eli on 12/11/2020 for org.ospic.inventory.pharmacy.Medicine.domains
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
@Data
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor
@Entity(name = DatabaseConstants.TABLE_PHARMACY_MEDICINES)
@Table(name = DatabaseConstants.TABLE_PHARMACY_MEDICINES,
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"name","company","compositions"}),
        })
@ApiModel(value = "Medicine", description = "Contain all medicine's available")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private @Setter(AccessLevel.PROTECTED)
    Long id;


    @NotBlank
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @NotBlank
    @Column(name = "company", length = 50, nullable = false)
    private String company;

    @NotBlank
    @Column(name = "compositions", length = 200, nullable = false)
    private String compositions;


    @NotNull
    @Column(name = "units", length = 5)
    private int units;

    @ManyToOne
    @JoinColumn(name = "grp_id")
    @ApiModelProperty(position = 1, required = true, hidden = true, notes = "used to display medicine group")
    private MedicineGroup group;

    @ManyToOne
    @JoinColumn(name = "cat_id")
    @ApiModelProperty(position = 1, required = true, hidden = true, notes = "used to display Medicine category")
    private MedicineCategory category;

    public Medicine(
            String name, String company, String compositions, int units) {
        this.name = name;
        this.company = company;
        this.compositions = compositions;

        this.units = units;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Medicine)) return false;
        return id != null && id.equals(((Medicine) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }


}
