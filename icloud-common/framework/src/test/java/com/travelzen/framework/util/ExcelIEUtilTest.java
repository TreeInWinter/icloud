/**
* author: Simon Lee
* Date  : Aug 28, 2013
*/
package com.travelzen.framework.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.joda.time.DateTime;

public class ExcelIEUtilTest {

	public static void main(String[] args) {
		// Export
		Map<String, String> fields = new HashMap<String, String>();
		fields.put("name", "姓名");
		fields.put("gender", "性别");
		fields.put("height", "身高");
		fields.put("birthday", "出生日期");
		fields.put("schoolName", "学校名称");
		List<Student> students = new ArrayList<>();
		String[] schools = new String[] {"上海交通大学", "北京大学", "香港中文大学"};
		String[] namess = new String[] {"小明", "阿毛", "大黄"};
		Integer[] heights = new Integer[] {175, 165, 180};
		String[] genders = new String[] {"男", "女"};
		for (int i = 0; i < 10000; i++) {
			Random random = new Random();
			int x = random.nextInt(3);
			Student student = new Student();
			student.setSchoolName(schools[x]);
			x = random.nextInt(3);
			student.setName(namess[x]);
			x = random.nextInt(3);
			student.setHeight(heights[x]);
			student.setGender(genders[i % 2]);
			student.setBirthday(DateTime.now());
			students.add(student);
		}

		ExcelIEUtil.exportExcel(fields, students, "/tmp/students.xls", 500);


		// Import
//		List<Student> studentList = ExcelIEUtil.importFromExcel(Student.class, "/tmp/students_1.xls");
//		if (studentList == null) {
//			System.out.println("read nothing!!!");
//		} else {
//			Student student = studentList.get(0);
//			System.out.println(student);
//		}
	}

	static class Person {
		private String gender;
		private Integer height;
		private String name;
		private DateTime birthday;
		public DateTime getBirthday() {
			return birthday;
		}
		public void setBirthday(DateTime birthday) {
			this.birthday = birthday;
		}
		public String getGender() {
			return gender;
		}
		public void setGender(String gender) {
			this.gender = gender;
		}
		public Integer getHeight() {
			return height;
		}
		public void setHeight(Integer height) {
			this.height = height;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		@Override
		public String toString() {
			return "Person [gender=" + gender + ", height=" + height + ", name=" + name + ", birthday=" + birthday
					+ "]";
		}
	}

	static class Student extends Person {
		private String schoolName;

		public String getSchoolName() {
			return schoolName;
		}

		public void setSchoolName(String schoolName) {
			this.schoolName = schoolName;
		}
		@Override
		public String toString() {
			return "Student [schoolName=" + schoolName + ", getBirthday()=" + getBirthday() + ", getGender()="
					+ getGender() + ", getHeight()=" + getHeight() + ", getName()=" + getName() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + "]";
		}
	}

}
