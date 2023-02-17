import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.matchers.Any;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.rest.app.Book;
import com.rest.app.BookController;
import com.rest.app.BookRepositoryInterface;

class TestConfig {

	

	@Bean
	BookController bookController() {
		return new BookController();
	};

}

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@ContextConfiguration(classes = { TestConfig.class })
public class BookContollerTest {

	MockMvc mockMvc;

	ObjectMapper objectMapper = new ObjectMapper();
	ObjectWriter objectWriter = objectMapper.writer();

	@MockBean
	private BookRepositoryInterface bookRepository;
	
	@Autowired
	private BookController bookController;

	Book RECORD_1 = new Book(1L, "Atomic Habits", "How to build better habits", 5);
	Book RECORD_2 = new Book(2l, "Thinking Fast and Slow", "How to create good models about thinking", 4);
	Book RECORD_3 = new Book(3L, "Grokking Algorithm", "Learn Algorithm the fun way", 5);

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this); 
		this.mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
	}

	@Test
	public void getAllRecords_success() throws Exception {
		List<Book> records = new ArrayList<>(Arrays.asList(RECORD_1, RECORD_2, RECORD_3));

		Mockito.when(bookRepository.findAll()).thenReturn(records);

		mockMvc.perform(MockMvcRequestBuilders.get("/book").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(3)))
				.andExpect(jsonPath("$[2].name", is("Grokking Algorithm")));
	}
	@Test
	public void getBookById_success() throws Exception{
		Mockito.when(bookRepository.findById(RECORD_1.getBookId())).thenReturn(java.util.Optional.of(RECORD_1));
		
	mockMvc.perform(MockMvcRequestBuilders.get("/book/1").contentType(MediaType.APPLICATION_JSON))
	.andExpect(status().isOk())
	.andExpect(jsonPath("$",notNullValue()))
	.andExpect(jsonPath("$.name",is("Atomic Habits")));
	}
	@Test
	public void createBookRecord_success() throws Exception{
		Book record=new Book();
		record.setId(4L);
		record.setName("Introduction to C");
		record.setSummary("The name but longer");
		record.setRating(5);
////		
		
		Mockito.when(bookRepository.save(Mockito.any())).thenReturn((record));
		String content=objectWriter.writeValueAsString(record);
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/book")
	            .contentType(MediaType.APPLICATION_JSON)
	            .accept(MediaType.APPLICATION_JSON)
	            .content(content);

	    mockMvc.perform(mockRequest)
	    		.andExpect(status().isOk())
	            .andExpect(jsonPath("$", notNullValue()))
	            .andExpect(jsonPath("$.name", is("Introduction to C"))).
	            andReturn().getResponse().getContentAsString();
	            
	            
	    }
	@Test
	public void updateBookRecord_success() throws Exception {
		Book updatedRecord=new Book();
		updatedRecord.setId(1L);
		updatedRecord.setName("Updated Book name");
		updatedRecord.setSummary("Updated summary");
		updatedRecord.setRating(1);

	    Mockito.when(bookRepository.findById(RECORD_1.getBookId())).thenReturn(java.util.Optional.of(RECORD_1));
	    Mockito.when(bookRepository.save(Mockito.any())).thenReturn(updatedRecord);

	    MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/book")
	            .contentType(MediaType.APPLICATION_JSON)
	            .accept(MediaType.APPLICATION_JSON)
	            .content(this.objectMapper.writeValueAsString(updatedRecord));

	    mockMvc.perform(mockRequest)
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$", notNullValue()))
	            .andExpect(jsonPath("$.name", is("Updated Book name")));
	}
	@Test
	public void deleteBookById_success() throws Exception{
		
	Mockito.when(bookRepository.findById(RECORD_2.getBookId())).thenReturn(java.util.Optional.of(RECORD_2));
	mockMvc.perform(MockMvcRequestBuilders.delete("/book/2").contentType(MediaType.APPLICATION_JSON))
	.andExpect(status().isOk())
	.andExpect(jsonPath("$",notNullValue()))
	.andExpect(jsonPath("$.name",is("Thinking Fast and Slow")));
	
	}
	
	
		
		
		
	}


