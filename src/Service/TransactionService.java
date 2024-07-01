package src.Service;

import src.Entity.Account;
import src.Entity.Status;
import src.Entity.Transaction;
import src.Generic.IBankService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class TransactionService implements IBankService {
    public static List<Transaction> transactionList;
    public static AccountService accountService;

    public TransactionService(){}

    @Override
    public Optional findByID(int id) {
        return Optional.empty();
    }
}
