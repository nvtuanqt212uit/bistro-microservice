package com.bistrocheese.paymentservice.service.impl;

import com.bistrocheese.paymentservice.constant.APIStatus;
import com.bistrocheese.paymentservice.dto.request.BillRequest;
import com.bistrocheese.paymentservice.exception.CustomException;
import com.bistrocheese.paymentservice.model.Bill;
import com.bistrocheese.paymentservice.model.Discount;
import com.bistrocheese.paymentservice.repository.BillRepository;
import com.bistrocheese.paymentservice.service.BillService;
import com.bistrocheese.paymentservice.service.DiscountService;
import com.bistrocheese.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;



@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {
    private final BillRepository billRepository;

    private final DiscountService discountService;
    private final PaymentService paymentService;

    @Override
    public void create(BillRequest billRequest) {

        if (billRepository.findByOrderId(billRequest.getOrderId()).isPresent()) {
            throw new CustomException(APIStatus.ORDER_PAID);
        }

        Bill bill = new Bill();
        BigDecimal subTotalOrder = billRequest.getSubTotal();

        // Create payment
//        PaymentRequest paymentRequest = copyProperties(billRequest, PaymentRequest.class);
//        Payment payment = paymentService.create(paymentRequest);

        // Find discount by id
        if (billRequest.getDiscountId() != null) {
            Discount discount = discountService.getById(billRequest.getDiscountId());
            bill.setDiscount(discount);
        }

        BigDecimal total = subTotalOrder.subtract(discountService.calculateDiscount(subTotalOrder, bill.getDiscount()));

        bill.setOrderId(billRequest.getOrderId());
//        bill.setPayment(payment);
        bill.setTotal(total);
        bill.setSubTotal(billRequest.getSubTotal());
        bill.setPaid(billRequest.getPaid());
        bill.setChange(billRequest.getPaid().subtract(total));

//        bill.setCusIn(order.getCreatedAt());
        bill.setCusOut(new Date());

        billRepository.save(bill);
        //TODO: Update order status
        //TODO: Update table status
    }
}