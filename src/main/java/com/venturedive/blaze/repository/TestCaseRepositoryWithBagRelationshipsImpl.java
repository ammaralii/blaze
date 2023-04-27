package com.venturedive.blaze.repository;

import com.venturedive.blaze.domain.TestCase;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class TestCaseRepositoryWithBagRelationshipsImpl implements TestCaseRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<TestCase> fetchBagRelationships(Optional<TestCase> testCase) {
        return testCase.map(this::fetchTestLevels);
    }

    @Override
    public Page<TestCase> fetchBagRelationships(Page<TestCase> testCases) {
        return new PageImpl<>(fetchBagRelationships(testCases.getContent()), testCases.getPageable(), testCases.getTotalElements());
    }

    @Override
    public List<TestCase> fetchBagRelationships(List<TestCase> testCases) {
        return Optional.of(testCases).map(this::fetchTestLevels).orElse(Collections.emptyList());
    }

    TestCase fetchTestLevels(TestCase result) {
        return entityManager
            .createQuery(
                "select testCase from TestCase testCase left join fetch testCase.testLevels where testCase is :testCase",
                TestCase.class
            )
            .setParameter("testCase", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<TestCase> fetchTestLevels(List<TestCase> testCases) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, testCases.size()).forEach(index -> order.put(testCases.get(index).getId(), index));
        List<TestCase> result = entityManager
            .createQuery(
                "select distinct testCase from TestCase testCase left join fetch testCase.testLevels where testCase in :testCases",
                TestCase.class
            )
            .setParameter("testCases", testCases)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
