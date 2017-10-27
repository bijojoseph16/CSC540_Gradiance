package code;

import java.sql.*;
import java.util.*;

import dbconnect.Connect;

public class Gradience {

        public static void main(String[] args) throws SQLException{
            Scanner ip = new Scanner(System.in);
            Connect.getConnection();
            Login.startPage(ip);
    }
}
