package org.promgrammers.voucher.service;

import lombok.extern.slf4j.Slf4j;
import org.promgrammers.voucher.domain.FixedAmountVoucher;
import org.promgrammers.voucher.domain.PercentDiscountVoucher;
import org.promgrammers.voucher.domain.Voucher;
import org.promgrammers.voucher.domain.VoucherType;
import org.promgrammers.voucher.domain.dto.voucher.VoucherCreateRequestDto;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.UUID;


@Component
@Slf4j
public class VoucherFactory {

    private static final long MIN_FIXED_AMOUNT = 0L;
    private static final long MIN_PERCENTAGE = 0L;
    private static final long MAX_PERCENTAGE = 100L;

    public static Voucher createVoucher(VoucherCreateRequestDto voucherCreateRequestDto) {


        UUID id = voucherCreateRequestDto.getId();
        VoucherType voucherType = voucherCreateRequestDto.getVoucherType();
        long amount = voucherCreateRequestDto.getAmount();

        switch (voucherType) {
            case FixedAmount:
                validateFixedAmount(amount);
                return new FixedAmountVoucher(amount, id);
            case PercentDiscount:
                validatePercentage(amount);
                return new PercentDiscountVoucher(amount, id);
            default:
                log.error("유효하지 않은 바우처 타입 -> {}", voucherType);
                throw new NoSuchElementException("유효하지 않은 Voucher 입니다.");
        }
    }

    private static void validatePercentage(long percentage) {
        if (!(MIN_PERCENTAGE <= percentage && percentage <= MAX_PERCENTAGE)) {
            log.warn("할인 비율 오류 -> {}", percentage);
            throw new IllegalArgumentException("할인 비율은 0이상 100이하여야 합니다.");
        }
    }

    private static void validateFixedAmount(long fixedAmount) {
        if (!(MIN_FIXED_AMOUNT <= fixedAmount)) {
            log.warn("할인 금액 오류 -> {}", fixedAmount);
            throw new IllegalArgumentException("할인 금액은 0이상이어야 합니다.");
        }
    }
}
