package com.venturedive.blaze.repository;

import com.venturedive.blaze.domain.Template;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface TemplateRepositoryWithBagRelationships {
    Optional<Template> fetchBagRelationships(Optional<Template> template);

    List<Template> fetchBagRelationships(List<Template> templates);

    Page<Template> fetchBagRelationships(Page<Template> templates);
}
