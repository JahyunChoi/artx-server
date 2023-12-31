package com.artx.artx.domain.product.domain;

import com.artx.artx.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductCategory extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(unique = true, nullable = false)
	private ProductCategoryType type;

	@Column(nullable = false)
	private String description;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private ProductCategoryImage productCategoryImage;

//하위 카테고리 생성 시 사용
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "parent_category_id")
//	private ProductCategory productCategory;

}
