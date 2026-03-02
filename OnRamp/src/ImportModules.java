//
// You can now import a module
// All exported packages of the module are available
// In Default classes the module java.base is imported by default, so you can use all its packages without importing it explicitly
//
import module java.sql;

void main() throws SQLException {
    var connection = DriverManager.getConnection("jdbc:sqlite:C:\\XIVY\\dev\\test.db");
    var stmt = connection.createStatement();    
}    
