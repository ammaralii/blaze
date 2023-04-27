package com.venturedive.blaze.repository;

import com.venturedive.blaze.domain.ApplicationUser;
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
public class ApplicationUserRepositoryWithBagRelationshipsImpl implements ApplicationUserRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<ApplicationUser> fetchBagRelationships(Optional<ApplicationUser> applicationUser) {
        return applicationUser.map(this::fetchProjects).map(this::fetchRoles);
    }

    @Override
    public Page<ApplicationUser> fetchBagRelationships(Page<ApplicationUser> applicationUsers) {
        return new PageImpl<>(
            fetchBagRelationships(applicationUsers.getContent()),
            applicationUsers.getPageable(),
            applicationUsers.getTotalElements()
        );
    }

    @Override
    public List<ApplicationUser> fetchBagRelationships(List<ApplicationUser> applicationUsers) {
        return Optional.of(applicationUsers).map(this::fetchProjects).map(this::fetchRoles).orElse(Collections.emptyList());
    }

    ApplicationUser fetchProjects(ApplicationUser result) {
        return entityManager
            .createQuery(
                "select applicationUser from ApplicationUser applicationUser left join fetch applicationUser.projects where applicationUser is :applicationUser",
                ApplicationUser.class
            )
            .setParameter("applicationUser", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<ApplicationUser> fetchProjects(List<ApplicationUser> applicationUsers) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, applicationUsers.size()).forEach(index -> order.put(applicationUsers.get(index).getId(), index));
        List<ApplicationUser> result = entityManager
            .createQuery(
                "select distinct applicationUser from ApplicationUser applicationUser left join fetch applicationUser.projects where applicationUser in :applicationUsers",
                ApplicationUser.class
            )
            .setParameter("applicationUsers", applicationUsers)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    ApplicationUser fetchRoles(ApplicationUser result) {
        return entityManager
            .createQuery(
                "select applicationUser from ApplicationUser applicationUser left join fetch applicationUser.roles where applicationUser is :applicationUser",
                ApplicationUser.class
            )
            .setParameter("applicationUser", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<ApplicationUser> fetchRoles(List<ApplicationUser> applicationUsers) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, applicationUsers.size()).forEach(index -> order.put(applicationUsers.get(index).getId(), index));
        List<ApplicationUser> result = entityManager
            .createQuery(
                "select distinct applicationUser from ApplicationUser applicationUser left join fetch applicationUser.roles where applicationUser in :applicationUsers",
                ApplicationUser.class
            )
            .setParameter("applicationUsers", applicationUsers)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
