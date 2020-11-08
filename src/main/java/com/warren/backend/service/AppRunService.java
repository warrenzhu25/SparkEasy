package com.warren.backend.service;

import com.warren.backend.data.entity.AppRun;
import com.warren.backend.data.entity.User;
import com.warren.backend.data.livy.BatchResponse;
import com.warren.backend.repositories.AppRunRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.scheduler.Schedulers;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

@Slf4j
@Service
public class AppRunService implements FilterableCrudService<AppRun> {

	private final AppRunRepository appRunRepository;
	private final Map<String, WebClient> webClients = new ConcurrentHashMap<>();

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
		AppRun appRun = getRepository().saveAndFlush(entity);
		submit(appRun);
		return appRun;
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

	private void submit(AppRun appRun) {
		String livyUrl = appRun.getCluster().getUrl();
		webClients.putIfAbsent(livyUrl, WebClient.builder().baseUrl(livyUrl).build());
		WebClient webClient = webClients.get(livyUrl);

		webClient.post()
				.uri("/batches/")
				.contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromValue(appRun.getSparkApp().getLivyBody()))
				.retrieve()
				.bodyToMono(BatchResponse.class)
				.subscribeOn(Schedulers.single())
				.subscribe(c -> onAppSubmitted(appRun, c));
	}

	private void onAppSubmitted(AppRun appRun , BatchResponse response) {
		appRun.setLivyId(response.getId());
		appRun.setState(response.getState());
		if (!StringUtils.isBlank(response.getAppId())) {
			appRun.setAppId(response.getAppId());
		}

		log.info("Spark app submitted: {}", response);
		appRunRepository.save(appRun);
	}
}
