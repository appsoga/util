package oga.binary.bind.test;

import java.util.Date;

import oga.binary.bind.annotation.BinaryElement;
import oga.binary.bind.annotation.BinaryObject;
import oga.binary.bind.annotation.BinaryTypes;

@BinaryObject
public class SimpleMessage {

	@BinaryElement(order = 1, type = BinaryTypes.INT32)
	private long id;

	@BinaryElement(order = 2, type = BinaryTypes.CHAR, length = 10)
	private String name;

	@BinaryElement(order = 3, type = BinaryTypes.CHAR)
	private char type;

	private String description;

	@BinaryElement(order = 4, type = BinaryTypes.BCD7)
	private Date createdOn;
	
	@BinaryElement
	private SimpleChildMessage childMessage;

	public String getDescription() {
		return description;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public char getType() {
		return type;
	}

	public void setType(char type) {
		this.type = type;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@Override
	public String toString() {
		return "SimpleMessage [id=" + id + ", name=" + name + ", type=" + type
				+ ", description=" + description + ", createdOn=" + createdOn+", childMessage=" + childMessage
				+ "]";
	}

	public SimpleChildMessage getChildMessage() {
		return childMessage;
	}

	public void setChildMessage(SimpleChildMessage childMessage) {
		this.childMessage = childMessage;
	}

}
