package dev.alexengrig.remittance.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode
@RequiredArgsConstructor
public abstract class BaseAccount implements Account {

    @Getter
    private final long id;
}
