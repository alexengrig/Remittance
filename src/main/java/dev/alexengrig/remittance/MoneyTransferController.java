package dev.alexengrig.remittance;

import dev.alexengrig.remittance.service.moneytransfer.MoneyTransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MoneyTransferController {

    private final MoneyTransferService moneyTransferService;

    @PostMapping
    public void transfer(long fromAccountId, long toAccountId, long amount) {
        moneyTransferService.transfer(fromAccountId, toAccountId, amount);
    }
}
