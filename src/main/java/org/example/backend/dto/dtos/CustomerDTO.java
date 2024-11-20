package org.example.backend.dto.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.backend.db.enums.Currency;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private Currency preferredCurrency;
    private String photoUrl;
}
