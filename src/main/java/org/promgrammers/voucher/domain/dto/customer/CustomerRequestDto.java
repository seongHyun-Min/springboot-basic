package org.promgrammers.voucher.domain.dto.customer;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.promgrammers.voucher.domain.CustomerType;

import java.util.UUID;

@Getter
@ToString
@AllArgsConstructor
public class CustomerRequestDto {

    private UUID id;
    private String username;

}
