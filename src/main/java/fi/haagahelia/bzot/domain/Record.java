package fi.haagahelia.bzot.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The persistent class for the dictionary database table.
 */
@Entity
@Table(name = "tb_dictionary")
public class Record {
	private Integer id;
	private String word;
	private String content;
	private String direction;
	
	public Record() {

	}

	public Record(Integer id, String word, String content, String direction) {
		this.id = id;
		this.word = word;
		this.content = content;
		this.direction = direction;
	}

	@Id
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getWord() {
		return this.word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}
}