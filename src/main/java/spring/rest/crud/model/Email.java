package spring.rest.crud.model;

import spring.rest.crud.dto.EmailDto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "emails")
public class Email {

	@Id
	@Column(columnDefinition = "uuid")
	private UUID id;

	@Column(name = "email")
	private String email;

	@Column(name = "created_at")
	private Timestamp createdAt;


	public Email() {

	}

	public Email(EmailDto emailDto) {
		this.id = UUID.randomUUID();
		this.email = emailDto.getEmail();
		this.createdAt = new Timestamp(System.currentTimeMillis());
	}

	public UUID getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public EmailDto toDto() {
		return new EmailDto(this.email);
	}

	@Override
	public String toString() {
		return "Email [id=" + id + ", title=" + email + "]";
	}

}
