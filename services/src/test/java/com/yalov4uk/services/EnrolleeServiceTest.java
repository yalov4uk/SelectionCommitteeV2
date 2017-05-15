package com.yalov4uk.services;

import com.yalov4uk.beans.Faculty;
import com.yalov4uk.beans.Role;
import com.yalov4uk.beans.SubjectName;
import com.yalov4uk.beans.User;
import com.yalov4uk.interfaces.ICRUDService;
import com.yalov4uk.interfaces.IEnrolleeService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by valera on 5/6/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest//(classes = Application.class)
public class EnrolleeServiceTest {

    @Autowired
    private IEnrolleeService enrolleeService;
    @Autowired
    private ICRUDService crudService;

    private User user;
    private Role role;
    private Faculty faculty1;
    private Faculty faculty2;
    private SubjectName subjectName1;
    private SubjectName subjectName2;
    private SubjectName subjectName3;

    @Before
    public void setUp() throws Exception {
        user = new User("test", "test", "test");
        role = new Role("test");
        user.setRole(role);
        crudService.create(role);
        crudService.create(user);

        faculty1 = new Faculty("test1", 10);
        faculty2 = new Faculty("test2", 8);

        Set<SubjectName> subjectNames1 = new HashSet<>();
        Set<SubjectName> subjectNames2 = new HashSet<>();

        subjectName1 = new SubjectName("test1");
        subjectName2 = new SubjectName("test2");
        subjectName3 = new SubjectName("test3");
        crudService.create(subjectName1);
        crudService.create(subjectName2);
        crudService.create(subjectName3);

        subjectNames1.add(subjectName1);
        subjectNames1.add(subjectName2);

        subjectNames2.add(subjectName1);
        subjectNames2.add(subjectName2);
        subjectNames2.add(subjectName3);

        faculty1.setRequiredSubjects(subjectNames1);
        faculty2.setRequiredSubjects(subjectNames2);
        crudService.create(faculty1);
        crudService.create(faculty2);
    }

    @After
    public void tearDown() throws Exception {
        faculty1.setRequiredSubjects(new HashSet<>());
        faculty2.setRequiredSubjects(new HashSet<>());
        faculty1.setRegisteredUsers(new HashSet<>());
        faculty2.setRegisteredUsers(new HashSet<>());
        crudService.update(faculty1);
        crudService.update(faculty2);
        user.getSubjects().forEach(subject -> crudService.delete(subject));
        crudService.delete(faculty1);
        crudService.delete(faculty2);
        crudService.delete(subjectName1);
        crudService.delete(subjectName2);
        crudService.delete(subjectName3);
        crudService.delete(user);
        crudService.delete(role);
    }

    @Test
    public void getRequiredSubjectNames() throws Exception {
        List<SubjectName> actualSubjectNames1 = enrolleeService.getRequiredSubjectNames(user, faculty1);
        assertTrue("List doesn't contains subjectName1", actualSubjectNames1.contains(subjectName1));
        assertTrue("List doesn't contains subjectName2", actualSubjectNames1.contains(subjectName2));

        List<Integer> values1 = new ArrayList<>();
        values1.add(10);
        values1.add(20);
        enrolleeService.registerToFaculty(user, faculty1, actualSubjectNames1, values1);

        List<SubjectName> thisShouldBeNull = enrolleeService.getRequiredSubjectNames(user, faculty1);
        assertNull("Enrollee can twice register to the same faculty", thisShouldBeNull);

        List<SubjectName> actualSubjectNames2 = enrolleeService.getRequiredSubjectNames(user, faculty2);
        assertFalse("List contains subjectName1", actualSubjectNames2.contains(subjectName1));
        assertFalse("List contains subjectName2", actualSubjectNames2.contains(subjectName2));
        assertTrue("List doesn't contains subjectName3", actualSubjectNames2.contains(subjectName3));
    }

    @Test
    public void registerToFaculty() throws Exception {
        List<SubjectName> actualSubjectNames1 = enrolleeService.getRequiredSubjectNames(user, faculty1);

        List<Integer> values1 = new ArrayList<>();
        values1.add(10);
        values1.add(20);
        enrolleeService.registerToFaculty(user, faculty1, actualSubjectNames1, values1);
        assertTrue("User doesn't registered to faculty1", faculty1.getRegisteredUsers().contains(user));

        List<SubjectName> actualSubjectNames2 = enrolleeService.getRequiredSubjectNames(user, faculty2);

        List<Integer> values2 = new ArrayList<>();
        values2.add(30);
        enrolleeService.registerToFaculty(user, faculty2, actualSubjectNames2, values2);
        assertTrue("User doesn't registered to faculty2", faculty2.getRegisteredUsers().contains(user));
    }
}