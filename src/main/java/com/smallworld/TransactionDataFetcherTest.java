package com.smallworld;

import com.smallworld.data.Transaction;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionDataFetcherTest {
    private TransactionDataFetcher dataFetcher;

    @BeforeEach
    public void setUp() {
        dataFetcher = new TransactionDataFetcher();
    }

    @Test
    public void testGetTotalTransactionAmount() {
        TransactionDataFetcher dataFetcher = new TransactionDataFetcher();
        double totalAmount = dataFetcher.getTotalTransactionAmount();
        Assert.assertEquals(4371.37, totalAmount, 0);
    }


    @Test
    public void testGetTotalTransactionAmountSentBy() {
        TransactionDataFetcher dataFetcher = new TransactionDataFetcher();
        double totalAmount = dataFetcher.getTotalTransactionAmountSentBy("Tom Shelby");
        Assert.assertEquals(828.26, totalAmount, 0);
    }

    @Test
    public void testGetMaxTransactionAmount() {
        TransactionDataFetcher dataFetcher = new TransactionDataFetcher();
        double maxAmount = dataFetcher.getMaxTransactionAmount();
        Assert.assertEquals(985.0, maxAmount, 0);
    }

    @Test
    public void testCountUniqueClients() {
        long uniqueClients = dataFetcher.countUniqueClients();
        assertEquals(14, uniqueClients);
    }

    @Test
    public void testHasOpenComplianceIssues() {
        assertTrue(dataFetcher.hasOpenComplianceIssues("Tom Shelby"));
        assertFalse(dataFetcher.hasOpenComplianceIssues("Aunt Polly"));
    }

    @Test
    public void testGetTransactionsByBeneficiaryName() {
        Map<String, List<Transaction>> transactionsByBeneficiary = dataFetcher.getTransactionsByBeneficiaryName();
        assertEquals(10, transactionsByBeneficiary.size());
        assertTrue(transactionsByBeneficiary.containsKey("Alfie Solomons"));
        assertTrue(transactionsByBeneficiary.containsKey("Arthur Shelby"));
    }

    @Test
    public void testGetUnsolvedIssueIds() {
        Set<Integer> unsolvedIssueIds = dataFetcher.getUnsolvedIssueIds();
        assertEquals(5, unsolvedIssueIds.size());
        assertTrue(unsolvedIssueIds.contains(1));
        assertTrue(unsolvedIssueIds.contains(3));
        assertTrue(unsolvedIssueIds.contains(54));
    }

    @Test
    public void testGetAllSolvedIssueMessages() {
        List<String> solvedIssueMessages = dataFetcher.getAllSolvedIssueMessages();
        assertEquals(8, solvedIssueMessages.size());
        assertTrue(solvedIssueMessages.contains("Never gonna give you up"));
        assertTrue(solvedIssueMessages.contains("Never gonna let you down"));
    }

    @Test
    public void testGetTop3TransactionsByAmount() {
        List<Transaction> top3Transactions = dataFetcher.getTop3TransactionsByAmount();
        assertEquals(3, top3Transactions.size());
        assertTrue(top3Transactions.get(0).getAmount() >= top3Transactions.get(1).getAmount());
        assertTrue(top3Transactions.get(1).getAmount() >= top3Transactions.get(2).getAmount());
    }

    @Test
    public void testGetTopSender() {
        Optional<String> topSender = dataFetcher.getTopSender();
        assertTrue(topSender.isPresent());
        assertEquals("Grace Burgess", topSender.get());
    }
}
