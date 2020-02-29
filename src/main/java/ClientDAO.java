import java.io.FileInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {

    final static String url = "jdbc:mysql://localhost:3306/uczenie";
    final String user = "root";
    final String password = "admin";

    private Connection connection;

    public ClientDAO() throws Exception{
        connection = DriverManager.getConnection(url,user,password);
        System.out.println("Succesfull connection to database.");
    }

    public List<Client> getAllClients() throws Exception{

        List<Client> list = new ArrayList<>();

        Statement statement = null;
        ResultSet resultSet = null;

        try {

            statement = connection.createStatement();
            resultSet = statement.executeQuery("select * from clients");

            while (resultSet.next()){
                Client tempClient = convertRowToClient(resultSet);
                list.add(tempClient);
            }

            return list;

        }finally {
            close(statement,resultSet);
        }

    }

    private Client convertRowToClient(ResultSet resultSet) throws Exception{
        int id = resultSet.getInt("id");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        String car = resultSet.getString("car");
        String email = resultSet.getString("email");
        int phoneNumber = resultSet.getInt("phone_number");

        Client tempClient = new Client(firstName,lastName,email,car,id,phoneNumber);

        return tempClient;

    }

    private static void close(Connection connection, Statement statement, ResultSet resultSet)throws SQLException {
        if(resultSet!=null){
            resultSet.close();
        }
        if(statement!=null){

        }
        if(connection!=null){
            connection.close();
        }
    }

    private void close(Statement statement, ResultSet resultSet)throws SQLException{
        close(null,statement,resultSet);
    }

    public List<Client> searchClients(String lastName)throws Exception{
        List<Client> list = new ArrayList<>();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            lastName+="%";
            preparedStatement = connection.prepareStatement("select * from clients where last_name like ?");
            preparedStatement.setString(1,lastName);

            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                Client tempClient = convertRowToClient(resultSet);
                list.add(tempClient);
            }
            return list;
        }finally {
            close(preparedStatement,resultSet);
        }
    }

    public void addClient(Client client){

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `uczenie`.`clients` (`id`, `first_name`, `last_name`, `email`, `car`, `phone_number`)" +
                    " VALUES (?, ?, ?, ?, ?, ?)");
            preparedStatement.setInt(1,client.getId());
            preparedStatement.setString(2,client.getFirstName());
            preparedStatement.setString(3,client.getLastName());
            preparedStatement.setString(4,client.getEmail());
            preparedStatement.setString(5,client.getCar());
            preparedStatement.setInt(6,client.getPhoneNumber());

            preparedStatement.executeUpdate();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void deleteClient(int clientId){
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM `uczenie`.`clients` WHERE (`id` = ?)");
            preparedStatement.setInt(1,clientId);
            preparedStatement.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void editClient(int clientId, String firstName, String lastName, String email, String car, int phoneNmber){
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `uczenie`.`clients` SET " +
                    "`first_name` = ?, `last_name` = ?, `email` = ?, `car` = ?, `phone_number` = ? WHERE (`id` = ?)");
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, car);
            preparedStatement.setInt(5, phoneNmber);
            preparedStatement.setInt(6,clientId);

            preparedStatement.executeUpdate();

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public int getFreeId(List<Client> clients){
        for(int i=1; i<clients.size(); i++){
            if((clients.get(i).getId()-clients.get(i-1).getId())>1){
                return i+1;
            }
        }
        return clients.size()+1;
    }

    public String checkUserType(UserAccount userAccount){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select account_type from user where login = ? AND password = ? ");
            preparedStatement.setString(1,userAccount.getLogin());
            preparedStatement.setString(2,userAccount.getPassword());
            ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next()) {
                String s = resultSet.getString("account_type");
                return s;
            }else{
                return "incorrect data";
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "No data";
    }



    public static void main(String[] args)throws Exception {



    }

}
