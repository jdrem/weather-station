package net.remgant.weather.dao;

import net.remgant.weather.model.Authority;
import net.remgant.weather.model.AuthorityPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AuhtorityRepository extends JpaRepository<Authority, AuthorityPK>, JpaSpecificationExecutor<Authority> {
}
