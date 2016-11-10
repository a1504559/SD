package fi.haagahelia.bzot.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * The persistent class for the dictionary database table.
 */
@Entity
@Table(name = "tb_dictionary")
public class Record {  
	//insert works only if methods after field's definition !!!
	
    private Integer id;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
	public Integer getId() {
		return this.id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	private String word;
	@Column(name = "word")
	public String getWord() {
		return this.word;
	}

	public void setWord(String word) {
		this.word = word;
	}	
	
	private String content;
	@Column(name = "content")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	//there is no actual "direction" field in DB
	private String direction;
	@Transient
	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}	
	
	public Record() {

	}

	public Record(Integer id, String word, String content, String direction) {
		this.id = id;
		this.word = word;
		this.content = content;
		this.direction = direction;
	}
		
	@Override
	public String toString() {
		return "Record [" + id + " " + word + " " + content + " " + direction + "]";
	}
}