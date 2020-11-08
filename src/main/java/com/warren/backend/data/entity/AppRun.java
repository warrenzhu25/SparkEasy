package com.warren.backend.data.entity;

import com.warren.backend.data.RunState;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class AppRun extends AbstractEntity {

	private String appId;
	private Cluster cluster;
	private Long livyId;

	@ManyToOne
	private SparkApp sparkApp;

	private String name;
	@Column(length = 100000)
	private String livyBody;

	private String state;

	@CreatedDate
	@Column(updatable = false)
	private Date createdDate;

	@LastModifiedDate
	private Date lastModifiedDate;

	@CreatedBy
	private User createdBy;
}
