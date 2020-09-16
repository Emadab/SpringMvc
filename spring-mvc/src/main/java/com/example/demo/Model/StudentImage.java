package com.example.demo.Model;

import javax.persistence.*;
import com.example.demo.Model.Student;
import org.codehaus.jackson.annotate.JsonIgnore;

import static java.lang.String.format;

@Entity
@Table(name = "student_img")
public class StudentImage {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int id;

	@OneToOne(mappedBy = "studentImage")
	private Student student;

	@Column(name = "name")
	private String name;
	@Column(name = "download_uri")
	private String downloadUri;
	@Column(name = "file_type")
	private  String fileType;

	@Lob
	@Column(name = "img")
	private byte[] img;

	public StudentImage(){}

	public StudentImage(Student student, String name, byte[] img, String fileType){
		this.student = student;
		this.name = name;
		this.img = img;
		this.fileType = fileType;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getImg() {
		return img;
	}

	public void setImg(byte[] img) {
		this.img = img;
	}

	@JsonIgnore
	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public String getDownloadUri() {
		return downloadUri;
	}

	public void setDownloadUri(String downloadUri) {
		this.downloadUri = downloadUri;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
}
