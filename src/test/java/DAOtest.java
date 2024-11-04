/*import dat.config.HibernateConfig;
import dat.daos.TaskDAO;
import dat.daos.UserDAO;
import dat.entities.Task;
import dat.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DAOtest {

    private static TaskDAO taskDAO;
    private static UserDAO userDAO;
    private static EntityManagerFactory emfTest;

    private Task t1, t2, t3;
    private User testUser;

    @BeforeAll
    static void setUpAll() {
        emfTest = HibernateConfig.getEntityManagerFactoryForTest();
        taskDAO = new TaskDAO(emfTest);
        userDAO = new UserDAO(emfTest);
    }

    @AfterAll
    static void tearDown() {
        emfTest.close();
    }

    @BeforeEach
    void setUp() {
        EntityManager em = emfTest.createEntityManager();

        testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPassword("password");
        testUser.setEmail("testuser@example.com");

        t1 = new Task("Clean Room", testUser.getUserId(), LocalDate.now().plusDays(1), LocalTime.of(14, 0), false, false, testUser);
        t2 = new Task("Grocery Shopping", testUser.getUserId(), LocalDate.now().plusDays(2), LocalTime.of(17, 0), true, false, testUser);
        t3 = new Task("Do Laundry", testUser.getUserId(), LocalDate.now().plusDays(3), LocalTime.of(10, 0), false, true, testUser);

        em.getTransaction().begin();
        em.createQuery("DELETE FROM Task").executeUpdate();
        em.createQuery("DELETE FROM User").executeUpdate();

        em.persist(testUser);

        em.persist(t1);
        em.persist(t2);
        em.persist(t3);
        em.getTransaction().commit();
        em.close();
    }

    @Test
    public void testCreateTask() {
        Task newTask = new Task("New Task", testUser.getUserId(), LocalDate.now().plusDays(4), LocalTime.of(12, 0), false, false, testUser);
        Task createdTask = taskDAO.createTask(newTask);

        assertNotNull(createdTask);
        assertEquals("New Task", createdTask.getDescription());
        assertEquals(testUser.getUserId(), createdTask.getUser().getUserId());
    }

    @Test
    public void testGetTaskById() {
        Task retrievedTask = taskDAO.getTaskById(t1.getTaskID());

        assertNotNull(retrievedTask);
        assertEquals("Clean Room", retrievedTask.getDescription());
    }

    @Test
    public void testGetAllTasksFromUser() {
        List<Task> tasks = taskDAO.getAllTasksFromUser(testUser.getUserId());

        assertNotNull(tasks);
        assertEquals(3, tasks.size());
    }

    @Test
    public void testDeleteTask() {
        taskDAO.deleteTask(t1.getTaskID());

        Task deletedTask = taskDAO.getTaskById(t1.getTaskID());
        assertNull(deletedTask);
    }

    @Test
    public void testUpdateTask() {
        t2.setDescription("Updated Grocery Shopping");
        taskDAO.updateTask(t2);

        Task updatedTask = taskDAO.getTaskById(t2.getTaskID());
        assertNotNull(updatedTask);
        assertEquals("Updated Grocery Shopping", updatedTask.getDescription());
    }

    @Test
    public void testGetAllTasks() {
        List<Task> allTasks = taskDAO.getAllTasks();

        assertNotNull(allTasks);
        assertEquals(3, allTasks.size());
    }

    @AfterEach
    void cleanUp() {
        EntityManager em = emfTest.createEntityManager();
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Task").executeUpdate();
        em.createQuery("DELETE FROM User").executeUpdate();
        em.getTransaction().commit();
        em.close();
    }
}
*/