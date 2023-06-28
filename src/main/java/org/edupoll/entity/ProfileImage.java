package org.edupoll.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "profileImages")
@Builder
@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileImage {
	
	@Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	
	private String fileAddress;
	private String url;
	
}
