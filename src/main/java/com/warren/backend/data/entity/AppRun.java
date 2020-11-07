package com.warren.backend.data.entity;

import com.warren.backend.data.RunState;
import lombok.Data;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@Data
@Entity
public class AppRun extends AbstractEntity {

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@OrderColumn
	@JoinColumn
	@BatchSize(size = 1000)
	@NotEmpty
	@Valid
	private SparkApp sparkApp;

	private RunState state;

	public AppRun(User createdBy, SparkApp app) {
		this.state = RunState.NEW;
		this.sparkApp = app;
	}

	public void changeState(User user, RunState state) {

	}
}
