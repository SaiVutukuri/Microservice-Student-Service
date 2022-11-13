package com.sai.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.sai.entity.Student;
import com.sai.feignclients.AddressFeignClient;
import com.sai.repository.StudentRepository;
import com.sai.request.CreateStudentRequest;
import com.sai.response.StudentResponse;

@Service
public class StudentService {

	Logger logger = LoggerFactory.getLogger(StudentService.class);

	@Autowired
	StudentRepository studentRepository;

	@Autowired
	WebClient webClient;

	@Autowired
	AddressFeignClient addressFeignClient;

	@Autowired
	CommonService commonService;

	public StudentResponse createStudent(CreateStudentRequest createStudentRequest) {

		Student student = new Student();
		student.setFirstName(createStudentRequest.getFirstName());
		student.setLastName(createStudentRequest.getLastName());
		student.setEmail(createStudentRequest.getEmail());

		student.setAddressId(createStudentRequest.getAddressId());
		student = studentRepository.save(student);

		StudentResponse studentResponse = new StudentResponse(student);

		//studentResponse.setAddressResponse(getAddressById(student.getAddressId()));

		studentResponse.setAddressResponse(commonService.getAddressById(student.getAddressId()));

		return studentResponse;
	}

	public StudentResponse getById (long id) {

		logger.info("Inside Student getById");

		Student student = studentRepository.findById(id).get();
		StudentResponse studentResponse = new StudentResponse(student);

		//studentResponse.setAddressResponse(getAddressById(student.getAddressId()));

		studentResponse.setAddressResponse(commonService.getAddressById(student.getAddressId()));

		return studentResponse;
	}

	/*
	 * @CircuitBreaker(name = "addressService", fallbackMethod =
	 * "fallbackGetAddressById") public AddressResponse getAddressById (long
	 * addressId) { AddressResponse addressResponse =
	 * addressFeignClient.getById(addressId);
	 *
	 * return addressResponse; }
	 *
	 * public AddressResponse fallbackGetAddressById (long addressId, Throwable th)
	 * { return new AddressResponse(); }
	 */
}
