package com.yalov4uk.services;

import com.yalov4uk.beans.*;
import com.yalov4uk.interfaces.IAdminService;
import com.yalov4uk.interfaces.ICRUDService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by valera on 5/6/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest//(classes = Application.class)
public class AdminServiceTest {

    @Autowired
    private IAdminService adminService;
    @Autowired
    private ICRUDService crudService;

    private User user1;
    private User user2;
    private Role role;
    private SubjectName subjectName1;
    private SubjectName subjectName2;
    private Faculty faculty;

    @Before
    public void setUp() throws Exception {
        user1 = new User("test1", "test1", "test1");
        user2 = new User("test2", "test2", "test2");
        role = new Role("test");
        user1.setRole(role);
        user2.setRole(role);
        crudService.create(role);
        crudService.create(user1);
        crudService.create(user2);

        subjectName1 = new SubjectName("test1");
        subjectName2 = new SubjectName("test2");
        crudService.create(subjectName1);
        crudService.create(subjectName2);

        faculty = new Faculty("test", 10);
        Set<SubjectName> subjectNames = new HashSet<>();
        subjectNames.add(subjectName1);
        subjectNames.add(subjectName2);
        faculty.setRequiredSubjects(subjectNames);
        crudService.create(faculty);

        Subject subject1 = new Subject(1);
        Subject subject2 = new Subject(5);
        Subject subject3 = new Subject(3);
        Subject subject4 = new Subject(4);

        subject1.setSubjectName(subjectName1);
        subject2.setSubjectName(subjectName2);
        subject3.setSubjectName(subjectName1);
        subject4.setSubjectName(subjectName2);

        subject1.setUser(user1);
        subject2.setUser(user1);
        subject3.setUser(user2);
        subject4.setUser(user2);

        crudService.create(subject1);
        crudService.create(subject2);
        crudService.create(subject3);
        crudService.create(subject4);

        user1.getSubjects().add(subject1);
        user1.getSubjects().add(subject2);
        user2.getSubjects().add(subject3);
        user2.getSubjects().add(subject4);

        crudService.update(user1);
        crudService.update(user2);
    }

    @After
    public void tearDown() throws Exception {
        faculty.getStatements().forEach(statement -> crudService.delete(statement));
        user1.getSubjects().forEach(subject -> crudService.delete(subject));
        user2.getSubjects().forEach(subject -> crudService.delete(subject));
        crudService.delete(faculty);
        crudService.delete(subjectName1);
        crudService.delete(subjectName2);
        crudService.delete(user1);
        crudService.delete(user2);
        crudService.delete(role);
    }

    @Test
    public void registerStatement() throws Exception {
        Statement statement1 = adminService.registerStatement(user1, faculty);
        assertEquals("Statement1 doesn't created", statement1.getUser(), user1);
        Statement statement2 = adminService.registerStatement(user2, faculty);
        assertEquals("Statement2 doesn't created", statement2.getUser(), user2);
    }

    @Test
    public void calculateEntrants() throws Exception {
        registerStatement();
        List<Statement> statements = faculty.getStatements();
        statements = adminService.calculateEntrants(statements);
        assertTrue("Size > faculty maxSize", statements.size() <= faculty.getMaxSize());
        assertEquals("Temp1.averageScore > temp2.averageScore", statements.get(0).getUser(), user2);
    }
}