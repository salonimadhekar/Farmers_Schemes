import java.sql.*;
import java.util.Scanner;

class Account {
    private int adharNo;
    private String name;
    private String mobNo;
    private String password;

    public void setAdharNo(int adharNo) {
        this.adharNo = adharNo;
    }

    public int getAdharNo() {
        return adharNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobNo() {
        return mobNo;
    }

    public void setMobNo(String mobNo) {
        this.mobNo = mobNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

class Allwork {
    Scanner sc = new Scanner(System.in);
    Account acc = new Account();

    public void createAccount() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/farmers?useSSL=false", "root", "mysql@Sm19");

            String sql = "INSERT INTO login (AdharNo, farmer_name, mobnum, Passkey) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);

            System.out.print("Enter Aadhar Number: ");
            acc.setAdharNo(sc.nextInt());
            ps.setInt(1, acc.getAdharNo());

            sc.nextLine(); // Consume newline

            System.out.print("Enter Name: ");
            acc.setName(sc.nextLine());
            ps.setString(2, acc.getName());

            System.out.print("Enter Mobile Number: ");
            acc.setMobNo(sc.nextLine());
            ps.setString(3, acc.getMobNo());

            System.out.print("Enter Password: ");
            acc.setPassword(sc.nextLine());
            ps.setString(4, acc.getPassword());

            ps.executeUpdate();
            System.out.println("Account created successfully!");

            ps.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetPassword() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/farmers?useSSL=false", "root", "mysql@Sm19");

            System.out.print("Enter your Aadhar Number: ");
            int adharNo = sc.nextInt();
            sc.nextLine(); // Consume newline

            System.out.print("Enter your Registered Mobile Number: ");
            String mobNo = sc.nextLine();

            // Check if Aadhar and Mobile Number exist in the database
            String checkQuery = "SELECT * FROM login WHERE AdharNo = ? AND mobnum = ?";
            PreparedStatement checkPs = con.prepareStatement(checkQuery);
            checkPs.setInt(1, adharNo);
            checkPs.setString(2, mobNo);

            ResultSet rs = checkPs.executeQuery();

            if (rs.next()) {
                System.out.print("Enter New Password: ");
                String newPassword = sc.nextLine();

                // Update Password
                String updateQuery = "UPDATE login SET Passkey = ? WHERE AdharNo = ?";
                PreparedStatement updatePs = con.prepareStatement(updateQuery);
                updatePs.setString(1, newPassword);
                updatePs.setInt(2, adharNo);

                updatePs.executeUpdate();
                System.out.println("Password Reset Successfully!");

                updatePs.close();
            } else {
                System.out.println("Invalid Aadhar Number or Mobile Number!");
            }

            rs.close();
            checkPs.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getSchemesByState(String state) {
        String query = "SELECT Schemes, Descript FROM schemes WHERE State = ?";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/farmers?useSSL=false", "root", "mysql@Sm19");

            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, state);
            ResultSet rs = pstmt.executeQuery();

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("\nScheme Name: " + rs.getString("Schemes"));
                System.out.println("Description: " + rs.getString("Descript"));
                System.out.println("--------------------------------------");
            }

            if (!found) {
                System.out.println("No schemes found for the selected state.");
            }

            rs.close();
            pstmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getInsurancepolicy(String state) {
        String getpolicySQL = "SELECT Policy_name, About_policy FROM policy WHERE State=?";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/farmers?useSSL=false", "root", "mysql@Sm19");

            PreparedStatement pmpi = conn.prepareStatement(getpolicySQL);
            pmpi.setString(1, state);
            ResultSet sr = pmpi.executeQuery();

            boolean found = false;
            while (sr.next()) {
                found = true;
                System.out.println("\nPolicy Name: " + sr.getString("Policy_name"));
                System.out.println("Description: " + sr.getString("About_policy"));
                System.out.println("--------------------------------------");
            }

            if (!found) {
                System.out.println("No policies found for the selected state.");
            }

            sr.close();
            pmpi.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

public class SchemesF {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/farmers?useSSL=false", "root", "mysql@Sm19");
            Statement stmt = conn.createStatement();

            String createTableSQL = "CREATE TABLE IF NOT EXISTS schemes ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "State VARCHAR(20), "
                    + "Schemes VARCHAR(50), "
                    + "Descript LONGTEXT)";
            stmt.executeUpdate(createTableSQL);

            Scanner scanner = new Scanner(System.in);
            Allwork infoj = new Allwork();

            while (true) {
                System.out.println("\n----- MENU -----");
                System.out.println("1. Create Account");
                System.out.println("2. Reset Password");
                System.out.println("3. Check Schemes by State");
                System.out.println("4. Check Insurance Policy by State");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        infoj.createAccount();
                        break;
                    case 2:
                        infoj.resetPassword();
                        break;
                    case 3:
                        System.out.print("Enter State: ");
                        String state = scanner.nextLine().trim();
                        infoj.getSchemesByState(state);
                        break;
                    case 4:
                        System.out.print("Enter State: ");
                        String state4 = scanner.nextLine().trim();
                        infoj.getInsurancepolicy(state4);
                        break;
                    case 5:
                        System.out.println("Exiting Program...");
                        scanner.close();
                        stmt.close();
                        conn.close();
                        return;
                    default:
                        System.out.println("Invalid choice! Please enter again.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

