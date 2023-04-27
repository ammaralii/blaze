package com.venturedive.blaze.repository;

import com.venturedive.blaze.domain.TestCase;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface TestCaseRepositoryWithBagRelationships {
    Optional<TestCase> fetchBagRelationships(Optional<TestCase> testCase);

    List<TestCase> fetchBagRelationships(List<TestCase> testCases);

    Page<TestCase> fetchBagRelationships(Page<TestCase> testCases);
}
