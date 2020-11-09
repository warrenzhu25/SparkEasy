package com.warren.backend.data.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.*;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractEntity implements Serializable {

	@Id
	@GeneratedValue
	private Long id;

	@Version
	private int version;

	@LastModifiedDate
	private Date lastModifiedDate;

	@CreatedDate
	@Column(updatable = false)
	private Date createdDate;
}
