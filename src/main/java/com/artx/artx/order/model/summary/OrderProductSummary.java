package com.artx.artx.order.model.summary;

import com.artx.artx.order.entity.OrderProduct;
import com.artx.artx.product.entity.Product;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderProductSummary {

	private Long productId;
	private String productTitle;
	private String productRepresentativeImage;

	public static OrderProductSummary of(OrderProduct orderProduct){
		Product product = orderProduct.getProduct();
		return OrderProductSummary.builder()
				.productId(product.getId())
				.productTitle(product.getTitle())
				.productRepresentativeImage(product.getRepresentativeImage())
				.build();
	}
}
