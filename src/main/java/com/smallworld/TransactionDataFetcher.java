package com.smallworld;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smallworld.data.Transaction;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class TransactionDataFetcher {

    private List<Transaction> transactions;

    public TransactionDataFetcher() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            transactions = objectMapper.readValue(
                    new File("transactions.json"),
                    new TypeReference<>() {}
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the sum of the amounts of all transactions
     */
    public double getTotalTransactionAmount() {
        return transactions.stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    /**
     * Returns the sum of the amounts of all transactions sent by the specified client
     */
    public double getTotalTransactionAmountSentBy(String senderFullName) {
        return transactions.stream()
                .filter(transaction -> senderFullName.equals(transaction.getSenderFullName()))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    /**
     * Returns the highest transaction amount
     */
    public double getMaxTransactionAmount() {
        return transactions.stream()
                .mapToDouble(Transaction::getAmount)
                .max()
                .orElse(0.0); // Default to 0 if no transactions exist
    }



    /**
     * Counts the number of unique clients that sent or received a transaction
     */
    public long countUniqueClients() {
        Set<String> uniqueClients = new HashSet<>();
        transactions.forEach(transaction -> {
            uniqueClients.add(transaction.getSenderFullName());
            uniqueClients.add(transaction.getBeneficiaryFullName());
        });
        return uniqueClients.size();
    }

    /**
     * Returns whether a client (sender or beneficiary) has at least one transaction with a compliance
     * issue that has not been solved
     */
    public boolean hasOpenComplianceIssues(String clientFullName) {
        return transactions.stream()
                .anyMatch(transaction ->
                        (clientFullName.equals(transaction.getSenderFullName()) ||
                                clientFullName.equals(transaction.getBeneficiaryFullName())) &&
                                !transaction.isIssueSolved());
    }
    /**
     * Returns all transactions indexed by beneficiary name
     */
    public Map<String, List<Transaction>> getTransactionsByBeneficiaryName() {
        return transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getBeneficiaryFullName));
    }
    /**
     * Returns the identifiers of all open compliance issues
     */
    public Set<Integer> getUnsolvedIssueIds() {
        return transactions.stream()
                .filter(transaction -> !transaction.isIssueSolved())
                .map(Transaction::getIssueId)
                .collect(Collectors.toSet());
    }

    /**
     * Returns a list of all solved issue messages
     */
    public List<String> getAllSolvedIssueMessages() {
        return transactions.stream()
                .filter(Transaction::isIssueSolved)
                .map(Transaction::getIssueMessage)
                .collect(Collectors.toList());
    }

    /**
     * Returns the 3 transactions with the highest amount sorted by amount descending
     */
    public List<Transaction> getTop3TransactionsByAmount() {
        return transactions.stream()
                .sorted(Comparator.comparingDouble(Transaction::getAmount).reversed())
                .limit(3)
                .collect(Collectors.toList());
    }

    /**
     * Returns the senderFullName of the sender with the most total sent amount
     */
    public Optional<String> getTopSender() {
        Map<String, Double> senderAmountMap = transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getSenderFullName,
                        Collectors.summingDouble(Transaction::getAmount)));

        Optional<Map.Entry<String, Double>> topSenderEntry = senderAmountMap.entrySet().stream()
                .max(Map.Entry.comparingByValue());

        return topSenderEntry.map(Map.Entry::getKey);
    }

}
