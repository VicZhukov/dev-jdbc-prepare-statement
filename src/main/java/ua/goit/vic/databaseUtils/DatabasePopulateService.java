package ua.goit.vic.databaseUtils;


import java.sql.*;
import java.util.Arrays;

public class DatabasePopulateService {
    static final String WORKER_SQL = "INSERT INTO worker (id, name, birthday, level, salary) VALUES (?, ?, ?, ?, ?)";
    static final String CLIENT_SQL = "INSERT INTO client (id, name) VALUES (?, ?)";
    static final String PROJECT_SQL = "INSERT INTO project (id, client_id, start_date, finish_date) VALUES (?, ?, ?, ?)";
    static final String PROJECT_WORKER_SQL = "INSERT INTO project_worker (project_id, worker_id) VALUES (?, ?)";

    public static void main(String[] args) {
        try (Connection conn = Database.getInstance().getConnection()){
            conn.setAutoCommit(false);
            try {
//--------------Insert workers
                PreparedStatement pstmt_w = conn.prepareStatement(WORKER_SQL);

                int[] worker_ids = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
                String[] worker_names = {"Adam", "Ben", "Claus", "Douglas", "Ethan", "Frank", "Liam", "Martin", "Victor", "Rick"};
                Date[] worker_birthdays = {
                        Date.valueOf("1994-01-01"),
                        Date.valueOf("1995-03-02"),
                        Date.valueOf("1994-01-03"),
                        Date.valueOf("1998-05-04"),
                        Date.valueOf("1993-10-05"),
                        Date.valueOf("1991-07-06"),
                        Date.valueOf("1997-08-07"),
                        Date.valueOf("1994-09-08"),
                        Date.valueOf("1993-06-05"),
                        Date.valueOf("1992-10-15"),
                };
                String[] worker_levels = {"Trainee", "Trainee", "Junior", "Junior", "Middle", "Middle", "Senior", "Senior", "Senior", "Senior"};
                int[] worker_salaries = {950, 850, 1500, 1450, 3100, 3500, 5300, 5100, 5600, 5400};

                for (int i = 0; i < worker_ids.length; i++) {
                    pstmt_w.setInt(1, worker_ids[i]);
                    pstmt_w.setString(2, worker_names[i]);
                    pstmt_w.setDate(3,Date.valueOf(String.valueOf(worker_birthdays[i])));
                    pstmt_w.setString(4, worker_levels[i]);
                    pstmt_w.setInt(5, worker_salaries[i]);
                    pstmt_w.addBatch();
                }

                int[] result_workers = pstmt_w.executeBatch();

                System.out.println("Number of worker rows inserted: " + Arrays.stream(result_workers).sum());

//------------- Insert clients
                PreparedStatement pstmt_c = conn.prepareStatement(CLIENT_SQL);

                int[] client_ids = {1, 2, 3, 4, 5};
                String[] client_names = {"Elon", "Mark", "Bill", "James", "Steve"};

                for (int i = 0; i < client_ids.length; i++) {
                    pstmt_c.setInt(1, client_ids[i]);
                    pstmt_c.setString(2, client_names[i]);
                    pstmt_c.addBatch();
                }

                int[] result_clients = pstmt_c.executeBatch();

                System.out.println("Number of client rows inserted: " + Arrays.stream(result_clients).sum());

//--------------Insert project
                PreparedStatement pstmt_p = conn.prepareStatement(PROJECT_SQL);

                int[] p_ids = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
                int[] p_client_ids = {1, 2, 3, 4, 4, 1, 2, 3, 2, 5};
                Date[] p_start_dates = {
                        Date.valueOf("2023-03-01"),
                        Date.valueOf("2023-03-01"),
                        Date.valueOf("2023-03-01"),
                        Date.valueOf("2023-03-01"),
                        Date.valueOf("2023-03-01"),
                        Date.valueOf("2023-03-01"),
                        Date.valueOf("2023-03-01"),
                        Date.valueOf("2023-03-01"),
                        Date.valueOf("2023-03-01"),
                        Date.valueOf("2023-03-01")
                };
                Date[] p_finish_dates = {
                        Date.valueOf("2023-04-01"),
                        Date.valueOf("2023-05-01"),
                        Date.valueOf("2023-06-01"),
                        Date.valueOf("2024-03-01"),
                        Date.valueOf("2025-03-01"),
                        Date.valueOf("2025-04-01"),
                        Date.valueOf("2028-04-01"),
                        Date.valueOf("2028-04-01"),
                        Date.valueOf("2030-06-01"),
                        Date.valueOf("2030-03-01"),
                };

                for (int i = 0; i < p_ids.length; i++) {
                    pstmt_p.setInt(1, p_ids[i]);
                    pstmt_p.setInt(2, p_client_ids[i]);
                    pstmt_p.setDate(3, Date.valueOf(String.valueOf(p_start_dates[i])));
                    pstmt_p.setDate(4, Date.valueOf(String.valueOf(p_finish_dates[i])));
                    pstmt_p.addBatch();
                }

                int[] result_projects = pstmt_p.executeBatch();

                System.out.println("Number of project rows inserted: " + Arrays.stream(result_projects).sum());

//--------------Insert project_worker
                PreparedStatement pstmt_pw = conn.prepareStatement(PROJECT_WORKER_SQL);

                int[] projects_id = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
                int[] workers_ids = {5, 7, 6, 4, 1, 3, 2, 10, 9, 8};

                for (int i = 0; i < projects_id.length; i++) {
                    pstmt_pw.setInt(1, projects_id[i]);
                    pstmt_pw.setInt(2, workers_ids[i]);
                    pstmt_pw.addBatch();
                }

                int[] result_pw = pstmt_pw.executeBatch();

                System.out.println("Number of project_worker rows inserted: " + Arrays.stream(result_pw).sum());

                conn.commit();

            } catch (SQLException e) {
                conn.rollback();
            } finally {
                conn.setAutoCommit(true);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}