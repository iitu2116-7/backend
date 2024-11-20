package org.example.backend.dto.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ModeratorDTO {
    private Long id;
    private String email;
    private String photoUrl;
}
