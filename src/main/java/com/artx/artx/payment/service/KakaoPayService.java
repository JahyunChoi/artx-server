package com.artx.artx.payment.service;

import com.artx.artx.common.error.ErrorCode;
import com.artx.artx.common.exception.BusinessException;
import com.artx.artx.order.entity.Order;
import com.artx.artx.order.repository.OrderRepository;
import com.artx.artx.payment.entity.KakaoPayment;
import com.artx.artx.payment.entity.Payment;
import com.artx.artx.payment.model.CreatePayment;
import com.artx.artx.payment.repository.KakaoPaymentRepository;
import com.artx.artx.payment.repository.PaymentRepository;
import com.artx.artx.payment.type.PaymentStatus;
import com.artx.artx.payment.type.PaymentType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class KakaoPayService implements PaymentService {

	private final RestTemplate restTemplate;
	private final KakaoPaymentRepository kakaoPaymentRepository;
	private final PaymentRepository paymentRepository;
	private final OrderRepository orderRepository;

	@Value("${kakaopay.key}")
	private String apiKey;

	@Value("${kakaopay.cid}")
	private String cid;

	@Value("${kakaopay.ready}")
	private String readyApiAddress;

	@Value("${kakaopay.approval}")
	private String approvalApiAddress;

	@Value("${kakaopay.cancel}")
	private String cancelApiAddress;

	@Value("${serverAddress}")
	private String serverAddress;

	@Transactional
	public CreatePayment.ReadyResponse readyPayment(Order order) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "KakaoAK " + apiKey);
		headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8");

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("cid", cid);
		params.add("partner_order_id", order.getId().toString());
		params.add("partner_user_id", order.getUser().getUserId().toString());
		params.add("item_name", order.getTitle());
		params.add("quantity", "1");
		params.add("total_amount", String.valueOf(order.getTotalAmount()));
		params.add("tax_free_amount", "0");
		params.add("approval_url", serverAddress + "/api/payments/approval?partner_order_id=" + order.getId());
		params.add("cancel_url", serverAddress + "/api/payments/cancel");
		params.add("fail_url", serverAddress + "/api/payments/fail");

		HttpEntity request = new HttpEntity(params, headers);

		try {
			CreatePayment.ReadyResponse readyResponse = restTemplate.postForObject(readyApiAddress, request, CreatePayment.ReadyResponse.class);
			Payment payment = Payment.from(
					order,
					readyResponse.getTid(),
					PaymentType.KAKAOPAY,
					PaymentStatus.PAYMENT_READY
			);
			KakaoPayment kakaoPayment = kakaoPaymentRepository.save(KakaoPayment.builder()
					.tid(readyResponse.getTid())
					.payment(payment)
					.build()
			);

			order.setPayment(payment);

			return readyResponse;
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(ErrorCode.FAILED_KAKAOPAY_PAYMENT);
		}

	}

	public CreatePayment.ApprovalResponse approvalPayment(Long orderId, String pgToken) {

		Payment payment = paymentRepository.findByOrder_Id(orderId)
				.orElseThrow(() -> new BusinessException(ErrorCode.PAYMENT_NOT_FOUND));

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "KakaoAK " + apiKey);
		headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8");

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("cid", cid);
		params.add("tid", payment.getTid());
		params.add("partner_order_id", String.valueOf(payment.getOrder().getId()));
		params.add("partner_user_id", String.valueOf(payment.getOrder().getUser().getUserId()));
		params.add("pg_token", pgToken);

		try {
			HttpEntity request = new HttpEntity(params, headers);
			CreatePayment.ApprovalResponse response = restTemplate.postForObject(approvalApiAddress, request, CreatePayment.ApprovalResponse.class);
			payment.toPaymentSuccess();
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(ErrorCode.FAILED_KAKAOPAY_PAYMENT);
		}

	}

}