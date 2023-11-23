package com.artx.artx.artist.delivery.controller;


import com.artx.artx.artist.delivery.model.ArtistDeliveryCreate;
import com.artx.artx.artist.delivery.service.ArtistDeliveryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@Tag(name = "작가 배송 관리")
@RestController
@RequestMapping("/api/artist/deliveries")
@RequiredArgsConstructor
@Secured("ROLE_ARTIST")
public class ArtistDeliveryController {

	private final ArtistDeliveryService artistDeliveryService;

	@PatchMapping("/{deliveryId}")
	public ResponseEntity<ArtistDeliveryCreate.Response> updateDelivery(
			@PathVariable String deliveryId,
			@RequestBody ArtistDeliveryCreate.Request request
	){
		return ResponseEntity.ok(artistDeliveryService.updateDelivery(deliveryId, request));
	}

}
