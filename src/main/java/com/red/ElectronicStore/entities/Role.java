package com.red.ElectronicStore.entities;

import com.red.ElectronicStore.Enumeration.RoleName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Id
    private Integer roleId;

    @Column(unique = true, nullable = false)
    private String roleName;
}
