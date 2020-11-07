package com.warren.backend.service;

import com.warren.backend.data.entity.SparkApp;
import com.warren.backend.data.entity.User;
import com.warren.backend.repositories.SparkAppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SparkAppService implements FilterableCrudService<SparkApp> {

	private final SparkAppRepository sparkAppRepository;

	@Autowired
	public SparkAppService(SparkAppRepository sparkAppRepository) {
		this.sparkAppRepository = sparkAppRepository;
	}

	@Override
	public Page<SparkApp> findAnyMatching(Optional<String> filter, Pageable pageable) {
		if (filter.isPresent()) {
			String repositoryFilter = "%" + filter.get() + "%";
			return sparkAppRepository.findByNameLikeIgnoreCase(repositoryFilter, pageable);
		} else {
			return find(pageable);
		}
	}

	@Override
	public long countAnyMatching(Optional<String> filter) {
		if (filter.isPresent()) {
			String repositoryFilter = "%" + filter.get() + "%";
			return sparkAppRepository.countByNameLikeIgnoreCase(repositoryFilter);
		} else {
			return count();
		}
	}

	public Page<SparkApp> find(Pageable pageable) {
		return sparkAppRepository.findBy(pageable);
	}

	@Override
	public JpaRepository<SparkApp, Long> getRepository() {
		return sparkAppRepository;
	}

	@Override
	public SparkApp createNew(User currentUser) {
		return new SparkApp();
	}

	@Override
	public SparkApp save(User currentUser, SparkApp entity) {
		try {
			return FilterableCrudService.super.save(currentUser, entity);
		} catch (DataIntegrityViolationException e) {
			throw new UserFriendlyDataException(
					"There is already a product with that name. Please select a unique name for the product.");
		}

	}

}
