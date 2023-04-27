package com.venturedive.blaze.repository;

import com.venturedive.blaze.domain.TestCase;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TestCase entity.
 *
 * When extending this class, extend TestCaseRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface TestCaseRepository
    extends TestCaseRepositoryWithBagRelationships, JpaRepository<TestCase, Long>, JpaSpecificationExecutor<TestCase> {
    default Optional<TestCase> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<TestCase> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<TestCase> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select distinct testCase from TestCase testCase left join fetch testCase.priority",
        countQuery = "select count(distinct testCase) from TestCase testCase"
    )
    Page<TestCase> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct testCase from TestCase testCase left join fetch testCase.priority")
    List<TestCase> findAllWithToOneRelationships();

    @Query("select testCase from TestCase testCase left join fetch testCase.priority where testCase.id =:id")
    Optional<TestCase> findOneWithToOneRelationships(@Param("id") Long id);
}
