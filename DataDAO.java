package dao;

import db.MyConnection;
import model.Data;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataDAO {

    public static List<Data> getAllFiles(String email) throws SQLException {
        Connection connection = MyConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM data WHERE email = ?");
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();
        List<Data> files = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt(1);
            String name = rs.getString(2);
            String path = rs.getString(3);
            files.add(new Data(id, name, path));
        }
        return files;
    }

    public static int hideFile(Data file) throws SQLException, IOException {
        Connection connection = MyConnection.getConnection();
        File f = new File(file.getPath());

        if (!f.exists() || !f.isFile()) {
            System.out.println(" File not found or invalid.");
            return 0;
        }

        PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO data(name, path, email, bin_data) VALUES (?, ?, ?, ?)"
        );
        ps.setString(1, file.getFileName());
        ps.setString(2, file.getPath());
        ps.setString(3, file.getEmail());

        FileInputStream fis = new FileInputStream(f);
        ps.setBinaryStream(4, fis, f.length());
        int ans = ps.executeUpdate();
        fis.close();

        if (f.delete()) {
            System.out.println(" File hidden and deleted from disk.");
        } else {
            System.out.println(" File hidden, but deletion failed.");
        }

        return ans;
    }

    public static void unhide(int id) throws SQLException, IOException {
        Connection connection = MyConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT path, bin_data FROM data WHERE id = ?");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (!rs.next()) {
            System.out.println(" No hidden file found with this ID.");
            return;
        }

        String path = rs.getString("path");
        Blob blob = rs.getBlob("bin_data");

        InputStream is = blob.getBinaryStream();
        FileOutputStream fos = new FileOutputStream(path);

        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = is.read(buffer)) != -1) {
            fos.write(buffer, 0, bytesRead);
        }

        fos.close();
        is.close();

        ps = connection.prepareStatement("DELETE FROM data WHERE id = ?");
        ps.setInt(1, id);
        ps.executeUpdate();

        System.out.println(" File successfully unhidden.");
    }
}
//package dao;
//
//import db.MyConnection;
//import model.Data;
//
//import java.io.*;
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class DataDAO {
//
//    public static List<Data> getAllFiles(String email) throws SQLException {
//        Connection connection = MyConnection.getConnection();
//        PreparedStatement ps = connection.prepareStatement("SELECT * FROM data WHERE email = ?");
//        ps.setString(1, email);
//        ResultSet rs = ps.executeQuery();
//        List<Data> files = new ArrayList<>();
//        while (rs.next()) {
//            int id = rs.getInt(1);
//            String name = rs.getString(2);
//            String path = rs.getString(3);
//            files.add(new Data(id, name, path));
//        }
//        return files;
//    }
//
//    public static int hideFile(Data file) throws SQLException, IOException {
//        Connection connection = MyConnection.getConnection();
//        File f = new File(file.getPath());
//
//        // File existence and type checks
//        if (!f.exists()) {
//            System.out.println(" File does not exist.");
//            return 0;
//        }
//        if (!f.isFile()) {
//            System.out.println(" Provided path is not a file.");
//            return 0;
//        }
//
//        PreparedStatement ps = connection.prepareStatement(
//                "INSERT INTO data(name, path, email, bin_data) VALUES (?, ?, ?, ?)"
//        );
//        ps.setString(1, file.getFileName());
//        ps.setString(2, file.getPath());
//        ps.setString(3, file.getEmail());
//
//        FileInputStream fis = new FileInputStream(f);
//        ps.setBinaryStream(4, fis, f.length());
//        int ans = ps.executeUpdate();
//        fis.close();
//
//        // Delete file from disk after hiding
//        if (f.delete()) {
//            System.out.println("File hidden and deleted from disk.");
//        } else {
//            System.out.println(" File hidden, but deletion failed.");
//        }
//
//        return ans;
//    }
//
//    public static void unhide(int id) throws SQLException, IOException {
//        Connection connection = MyConnection.getConnection();
//        PreparedStatement ps = connection.prepareStatement("SELECT path, bin_data FROM data WHERE id = ?");
//        ps.setInt(1, id);
//        ResultSet rs = ps.executeQuery();
//
//        if (!rs.next()) {
//            System.out.println(" No hidden file found with this ID.");
//            return;
//        }
//
//        String path = rs.getString("path");
//        Blob blob = rs.getBlob("bin_data");
//
//        InputStream is = blob.getBinaryStream();
//        FileOutputStream fos = new FileOutputStream(path);
//
//        byte[] buffer = new byte[4096];
//        int bytesRead;
//
//        while ((bytesRead = is.read(buffer)) != -1) {
//            fos.write(buffer, 0, bytesRead);
//        }
//
//        fos.close();
//        is.close();
//
//        // Delete entry from DB
//        ps = connection.prepareStatement("DELETE FROM data WHERE id = ?");
//        ps.setInt(1, id);
//        ps.executeUpdate();
//
//        System.out.println(" File successfully unhidden.");
//    }
//}
////package dao;
////
////import db.MyConnection;
////import model.Data;
////
////import java.io.*;
////import java.sql.*;
////import java.util.ArrayList;
////import java.util.List;
////
////public class DataDAO {
////    public static List <Data> getAllFiles(String email)throws SQLException {
////        Connection connection = MyConnection.getConnection();
////        PreparedStatement ps = connection.prepareStatement("select * from data where email=?");
////        ps.setString(1,email);
////        ResultSet rs = ps.executeQuery();
////        List<Data>files = new ArrayList<>();
////        while (rs.next()){
////            int id = rs.getInt(1);
////            String name = rs.getString(2);
////            String path = rs.getString(3);
////            files.add(new Data(id , name, path));
////
////        }
////        return files;
////    }
////    public static int hideFile(Data file) throws SQLException, IOException {
////        Connection connection = MyConnection.getConnection();
////        PreparedStatement ps = connection.prepareStatement("insert into data(name , path, email, bin_data) values(?,?,?,?)");
////        ps.setString(1,file.getFileName());
////        ps.setString(2, file.getPath());
////        ps.setString(3, file.getEmail());
////        File f = new File(file.getPath());
////        FileReader fr = new FileReader(f);
////        ps.setCharacterStream(4,fr,f.length());
////        int ans = ps.executeUpdate();
////        fr.close();
////        f.delete();
////        return ans;
////    }
////    public  static  void unhide (int id) throws SQLException, IOException{
////        Connection connection = MyConnection.getConnection();
////        PreparedStatement ps = connection.prepareStatement("select path,bin_data from data where id = ?");
////        ps.setInt(1, id);
////        ResultSet rs =ps.executeQuery();
////        rs.next();
////        String path = rs.getString("path");
////        Clob c = rs.getClob("bin_data");
////        Reader r = c.getCharacterStream();
////        FileWriter fw = new FileWriter(path);
////        int i;
////        while ((i = r.read()) != -1){
////            fw.write((char)i);
////        }
////        fw.close();
////        ps = connection.prepareStatement("Delete form data where id =?");
////        ps.setInt(1,id);
////        ps.executeUpdate();
////        System.out.println("Successfully Unhidden");
////
////        }
////
////
////}
//
