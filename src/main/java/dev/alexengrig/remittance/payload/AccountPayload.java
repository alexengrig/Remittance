package dev.alexengrig.remittance.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class AccountPayload implements Serializable {

    private long id;
    private long balance;
}
