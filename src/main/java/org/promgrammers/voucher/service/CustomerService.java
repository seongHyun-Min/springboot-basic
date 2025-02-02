package org.promgrammers.voucher.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.promgrammers.voucher.domain.Customer;
import org.promgrammers.voucher.domain.Voucher;
import org.promgrammers.voucher.domain.dto.customer.CustomerListResponseDto;
import org.promgrammers.voucher.domain.dto.customer.CustomerRequestDto;
import org.promgrammers.voucher.domain.dto.customer.CustomerResponseDto;
import org.promgrammers.voucher.repository.CustomerRepository;
import org.promgrammers.voucher.repository.VoucherRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final VoucherRepository voucherRepository;

    @Transactional
    public CustomerResponseDto createCustomer(CustomerRequestDto customerRequest) {

        Customer customer = new Customer(UUID.randomUUID(), customerRequest.getUsername());
        customerRepository.save(customer);

        return new CustomerResponseDto(customer);
    }

    @Transactional
    public void assignVoucherToCustomer(UUID customerId, UUID voucherId) {
        Voucher voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new NoSuchElementException("해당 바우처를 찾을수 없습니다"));

        customerRepository.assignVoucherToCustomer(customerId, voucher);
    }


    @Transactional(readOnly = true)
    public CustomerResponseDto findCustomerById(UUID customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NoSuchElementException("해당 고객을 찾을수 없습니다"));

        return new CustomerResponseDto(customer);
    }

    @Transactional(readOnly = true)
    public CustomerResponseDto findCustomerByUsername(String username) {
        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("해당 고객을 찾을수 없습니다"));

        return new CustomerResponseDto(customer);
    }

    @Transactional(readOnly = true)
    public CustomerResponseDto findByVoucherId(UUID voucherId) {
        Voucher voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new NoSuchElementException("해당 바우처를 찾을수 없습니다"));

        Customer customer = customerRepository.findByVoucherId(voucher.getId())
                .orElseThrow(() -> new NoSuchElementException("해당 바우처를 가진 고객을 찾을수 없습니다"));

        return new CustomerResponseDto(customer);
    }


    @Transactional(readOnly = true)
    public CustomerListResponseDto findAllCustomers() {
        List<Customer> customerList = customerRepository.findAll();

        if (customerList == null || customerList.isEmpty()) {
            throw new NoSuchElementException("해당 고객을 찾을수 없습니다.");
        }

        List<CustomerResponseDto> customerResponseList = customerRepository.findAll()
                .stream()
                .map(CustomerResponseDto::new)
                .collect(Collectors.toList());

        return new CustomerListResponseDto(customerResponseList);
    }

    @Transactional
    public void deleteAllCustomers() {
        customerRepository.deleteAll();
    }

    @Transactional
    public CustomerResponseDto updateCustomer(CustomerRequestDto updateCustomerRequest) {
        Customer customer = customerRepository.findById(updateCustomerRequest.getId())
                .orElseThrow(() -> new NoSuchElementException("해당 고객을 찾을수 없습니다"));


        customer.updateUsername(updateCustomerRequest.getUsername());
        customerRepository.update(customer);

        CustomerResponseDto customerResponse = new CustomerResponseDto(customer);
        return customerResponse;
    }


    @Transactional
    public void deleteById(UUID customerId) {
        customerRepository.findById(customerId)
                .orElseThrow(() -> new NoSuchElementException("해당 고객을 찾을수 없습니다"));
        log.info("컨트롤러 실행");

        customerRepository.deleteById(customerId);
    }
}

