package com.rest.app;
import javax.persistence.*;

import lombok.*;

	@Entity
	@Table(name="book_record")
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public class Book{
		@Id
		@GeneratedValue(strategy=GenerationType.AUTO)
		private Long bookId;
		@NonNull
		private String name;
		@NonNull
		private String summary;
		
		private int ratings;
		public Book() {}
		
		public Book(Long bookId,String name,String summary,int ratings){
			this.bookId=bookId;
			this.name=name;
			this.summary=summary;
			this.ratings=ratings;
			
		}
		

		public Long getBookId() {
			return this.bookId;
		}
		public  String getName() {
			return this.name;
		}
		public String getSummary() {
			return this.summary;
		}
		public int getRating() {
			return this.ratings;
		}
		
		public  void setName(String name) {
			 this.name=name;
		}
		public void setSummary(String summary) {
			this.summary=summary;
		}
		public void setRating(int ratings) {
			
		}
		public void setId(long bookId) {
			
			 this.bookId=bookId;
		}

		public static Object builder() {
			return new Book();
		}

		

		
		
		
		
	}
	