package com.egorza.spring.rest.crud.controller;

import com.egorza.spring.rest.crud.model.Subscriber;
import com.egorza.spring.rest.crud.repository.SubscribersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class SubscriberController {

	@Autowired
	SubscribersRepository subscribersRepository;

	@GetMapping("/subscribers")
	public ResponseEntity<List<Subscriber>> getAllTutorials(@RequestParam(required = false) String email) {
		try {
			List<Subscriber> subscribers = new ArrayList<>();

			if (email == null)
				subscribers.addAll(subscribersRepository.findAll());
			else
				subscribers.addAll(subscribersRepository.findByEmail(email));

			if (subscribers.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(subscribers, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/subscribers/{id}")
	public ResponseEntity<Subscriber> getTutorialById(@PathVariable("id") Integer id) {
		Optional<Subscriber> tutorialData = subscribersRepository.findById(id);

		if (tutorialData.isPresent()) {
			return new ResponseEntity<>(tutorialData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/subscribers")
	public ResponseEntity<Subscriber> createTutorial(@RequestBody Subscriber subscriber) {
		try {
			Subscriber _subscriber = subscribersRepository
					.save(new Subscriber(subscriber.getEmail()));
			return new ResponseEntity<>(_subscriber, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/subscribers/{id}")
	public ResponseEntity<Subscriber> updateTutorial(@PathVariable("id") Integer id, @RequestBody Subscriber subscriber) {
		Optional<Subscriber> tutorialData = subscribersRepository.findById(id);

		if (tutorialData.isPresent()) {
			Subscriber _subscriber = tutorialData.get();
			_subscriber.setEmail(subscriber.getEmail());
			return new ResponseEntity<>(subscribersRepository.save(_subscriber), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/subscribers/{id}")
	public ResponseEntity<HttpStatus> deleteTutorial(@PathVariable("id") Integer id) {
		try {
			subscribersRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/subscribers")
	public ResponseEntity<HttpStatus> deleteAllTutorials() {
		try {
			subscribersRepository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
