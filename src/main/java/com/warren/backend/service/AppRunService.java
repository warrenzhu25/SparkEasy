package com.warren.backend.service;

import com.warren.backend.data.entity.AppRun;
import com.warren.backend.data.entity.User;
import com.warren.backend.repositories.AppRunRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AppRunService implements FilterableCrudService<AppRun> {

	private final AppRunRepository appRunRepository;

	@Autowired
	public AppRunService(AppRunRepository appRunRepository) {
		this.appRunRepository = appRunRepository;
	}

	public Page<AppRun> findAnyMatching(Optional<String> filter, Pageable pageable) {
		if (filter.isPresent()) {
			String repositoryFilter = "%" + filter.get() + "%";
			return getRepository().findByNameLikeIgnoreCase(repositoryFilter, pageable);
		} else {
			return find(pageable);
		}
	}

	@Override
	public long countAnyMatching(Optional<String> filter) {
		if (filter.isPresent()) {
			String repositoryFilter = "%" + filter.get() + "%";
			return appRunRepository.countByNameLikeIgnoreCase(repositoryFilter);
		} else {
			return count();
		}
	}

	@Override
	public AppRunRepository getRepository() {
		return appRunRepository;
	}

	public Page<AppRun> find(Pageable pageable) {
		return getRepository().findBy(pageable);
	}

	@Override
	public AppRun save(User currentUser, AppRun entity) {
		return getRepository().saveAndFlush(entity);
	}

	@Override
	@Transactional
	public void delete(User currentUser, AppRun appRunToDelete) {
		FilterableCrudService.super.delete(currentUser, appRunToDelete);
	}

	@Override
	public AppRun createNew(User currentUser) {
		return new AppRun();
	}
}
