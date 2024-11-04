package dat;

import dat.config.AppConfig;
import dat.config.HibernateConfig;
import jakarta.persistence.EntityManagerFactory;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory("");
        AppConfig.startServer(7007);
    }
}