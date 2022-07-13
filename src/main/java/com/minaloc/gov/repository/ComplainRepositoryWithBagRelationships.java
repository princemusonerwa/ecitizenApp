package com.minaloc.gov.repository;

import com.minaloc.gov.domain.Complain;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface ComplainRepositoryWithBagRelationships {
    Optional<Complain> fetchBagRelationships(Optional<Complain> complain);

    List<Complain> fetchBagRelationships(List<Complain> complains);

    Page<Complain> fetchBagRelationships(Page<Complain> complains);
}
