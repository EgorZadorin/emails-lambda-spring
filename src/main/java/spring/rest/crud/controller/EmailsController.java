package spring.rest.crud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.rest.crud.dto.EmailDto;
import spring.rest.crud.exceptions.ConflictException;
import spring.rest.crud.exceptions.NotFoundException;
import spring.rest.crud.model.Email;
import spring.rest.crud.repository.EmailsRepository;
import spring.rest.crud.service.EmailsService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
public class EmailsController {

	@Autowired
	EmailsRepository emailsRepository;

	@Autowired
	EmailsService emailsService;

/*	@GetMapping("/subscribers")
	public ResponseEntity<List<EmailDto>> getAllEmails() {
		try {
			List<Email> emails = new ArrayList<>(emailsRepository.findAll());

			List<EmailDto> emailsDtos = emails.stream()
					.map(emailsService::decryptEmail)
					.map(Email::toDto)
					.collect(Collectors.toList());

			if (emails.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(emailsDtos, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}*/

	@GetMapping("/subscribers/{id}")
	public ResponseEntity<EmailDto> getEmailById(@PathVariable("id") UUID id) {
		Optional<Email> emailData = emailsRepository.findById(id);

		if (emailData.isPresent()) {
			Email decryptedEmail = emailsService.decryptEmail(emailData.get());
			return new ResponseEntity<>(decryptedEmail.toDto(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/subscribers-hidden")
	public ResponseEntity<List<EmailDto>> getHiddenEmails() {
		try {
			List<Email> emails = emailsRepository.findAll();

			List<EmailDto> hiddenEmails = emails.stream()
					.map(emailsService::decryptEmail)
					.map(Email::toDto)
					.map(dto -> new EmailDto(maskEmail(dto.getEmail())))
					.collect(Collectors.toList());

			if (hiddenEmails.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(hiddenEmails, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private String maskEmail(String email) {
		return email.replaceAll("(?<=.).(?=[^@]*?@)", "*");
	}

	@GetMapping("/subscribers-amount")
	public Long getEmailsAmount() {
		return emailsRepository.count();
	}

	@PostMapping("/subscribers")
	public ResponseEntity<EmailDto> postEmail(@RequestBody EmailDto emailDto) {
		try {
			Email email = new Email(emailDto);
			Email _email = emailsService.encryptAndSaveOrDelete(email, false);
			return new ResponseEntity<>(new EmailDto(maskEmail(_email.getEmail())), HttpStatus.CREATED);
		} catch (ConflictException e) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/subscribers/{id}")
	public ResponseEntity<EmailDto> updateEmail(@PathVariable("id") Integer id, @RequestBody EmailDto emailDto) {
		Optional<Email> emailData = emailsRepository.findById(id);

		if (emailData.isPresent()) {
			Email _email = emailData.get();
			_email.setEmail(emailDto.getEmail());
			return new ResponseEntity<>(emailsRepository.save(_email).toDto(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/subscribers/{id}")
	public ResponseEntity<HttpStatus> deleteEmailById(@PathVariable("id") Integer id) {
		try {
			emailsRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/subscribers/email/{email}")
	public ResponseEntity<HttpStatus> deleteEmailByName(@PathVariable String email) throws NotFoundException {
		try {
			Email _email = new Email(new EmailDto(email));
			emailsService.encryptAndSaveOrDelete(_email, true);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (NotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

/*	@DeleteMapping("/subscribers")
	public ResponseEntity<HttpStatus> deleteAllEmails() {
		try {
			emailsRepository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}*/

}
