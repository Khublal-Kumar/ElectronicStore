package com.red.ElectronicStore.dto;

import com.red.ElectronicStore.Enumeration.RoleName;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class RoleDTO {

    private Integer roleId;

    private RoleName roleName;

}
