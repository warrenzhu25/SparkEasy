package com.warren.backend.data.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

@Data
@Entity
public class SparkApp extends AbstractEntity {

	@Size(max = 255)
	@Column(unique = true)
	private String name;

	private String submitCommand;

	@Column(length = 100000)
	private String livyBody;

}
