package org.edupoll.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "feeds")
public class Feed {
	
	@Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id; // 기본키
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "writerId")
	private User writer; // 작성자
	
	private String description; // 본문
	
	private Long viewCount; // 조회수
	
	@OneToMany(mappedBy = "feed", fetch = FetchType.LAZY)
	private List<FeedAttach> attaches;

	public Feed(User writer, String description) {
		super();
		this.writer = writer;
		this.description = description;
		this.viewCount = 0L;
	}
}
