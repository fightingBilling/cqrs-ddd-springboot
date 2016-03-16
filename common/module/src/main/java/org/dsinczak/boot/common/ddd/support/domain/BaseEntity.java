package org.dsinczak.boot.common.ddd.support.domain;

import javax.persistence.*;

@MappedSuperclass
public abstract class BaseEntity {

		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		@Column(updatable = false)
		private Long id;

		public Long getId() {
				return id;
		}
}