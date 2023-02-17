package com.rest.app;



import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javassist.NotFoundException;

@RestController
	@RequestMapping(value="/book")
	public class BookController{
		@Autowired
		BookRepositoryInterface bookRepository;
		
		@GetMapping
		public List<Book> getAllBookRecords(){
			return bookRepository.findAll();
		}
		@GetMapping(value ="{bookId}")
		public Book getBookById(@PathVariable(value="bookId") Long bookId){
			return bookRepository.findById(bookId).get();
		}
		
		@PostMapping
		public Book createBookRecord(@RequestBody @Valid Book bookRecord) {
			Book test = bookRepository.save(bookRecord);
			return test;
		}
		@PutMapping
		public Book updateBookRecord(@RequestBody @Valid Book bookRecord) throws NotFoundException {
			if(bookRecord==null || bookRecord.getBookId()==null) {
				throw new NotFoundException("BookRecord or ID must not be null");
				
			}
			Optional<Book> optionalBook=bookRepository.findById(bookRecord.getBookId());
			if(!(optionalBook.isPresent())){
				throw new NotFoundException("Book With ID: "+ bookRecord.getBookId()+"does not exist");
			}
			 Book existingPatientRecord = optionalBook.get();

			    existingPatientRecord.setName(bookRecord.getName());
			    existingPatientRecord.setSummary(bookRecord.getSummary());
			    existingPatientRecord.setRating(bookRecord.getRating());
				
			    return bookRepository.save(existingPatientRecord);
		} 
		
	@DeleteMapping(value="{bookId}")
      public  Book deleteBookById(@PathVariable(value="bookId") Long bookId) throws NotFoundException {
			
			
			 bookRepository.deleteById(bookId);
			 Book test=bookRepository.findById(bookId).get();	
			 return test;
			
			 
		}
			
			}
		
	


