package com.ds;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.client.RestTemplate;

class RepaymentAmountTests {
	
	@Spy
	LoanApplication loanApplication;
	
	LoanCalculatorController controller;
	
	@BeforeEach
	public void setup() {
		loanApplication = spy(new LoanApplication());
		controller = new LoanCalculatorController();
		
		LoanRepository data = mock(LoanRepository.class);
		JavaMailSender mailSender = mock(JavaMailSender.class);
		RestTemplate restTemplate = mock(RestTemplate.class);
		
		controller.setData(data);
		controller.setMailSender(mailSender);
		controller.setRestTemplate(restTemplate);
	}

	@Test
	void test1YearLoanWholePounds() {
		
		loanApplication.setPrincipal(1200);
		loanApplication.setTermInMonths(12);
		// set the interest rate to 10%
		doReturn(new BigDecimal(10)).when(loanApplication).getInterestRate();
		
		controller.processNewLoanApplication(loanApplication);
		assertEquals(new BigDecimal(110), loanApplication.getRepayment());
	}

	@Test
	void test2YearLoanWholePounds() {
		
		loanApplication.setPrincipal(1200);
		loanApplication.setTermInMonths(24);
		// set the interest rate to 10%
		doReturn(new BigDecimal(10)).when(loanApplication).getInterestRate();
		
		controller.processNewLoanApplication(loanApplication);
		assertEquals(new BigDecimal(60), loanApplication.getRepayment());
	}

	@Test
	void test5YearLoanWithRounding() {
		
		loanApplication.setPrincipal(5000);
		loanApplication.setTermInMonths(60);
		// set the interest rate to 10%
		doReturn(new BigDecimal(6.5)).when(loanApplication).getInterestRate();
		
		controller.processNewLoanApplication(loanApplication);
		assertEquals(new BigDecimal(111), loanApplication.getRepayment());
	}

}
