import javax.swing.table.AbstractTableModel;
import java.util.List;

public class ClientTableModel extends AbstractTableModel {

    private static final int ID_COL = 0, FIRST_NAME_COL = 1, LAST_NAME_COL=2, EMAIL_COL=3, CAR_COL=4,PHONE_NUMBER_COL=5;

    private String[] columnNames = {
            "ID",
            "First Name",
            "Last Name",
            "Email",
            "Car",
            "Phone number"
    };

    private List<Client> clients = null;

    public ClientTableModel(List<Client> theClients){
        clients = theClients;
    }

    @Override
    public int getColumnCount(){
        return columnNames.length;
    }

    @Override
    public int getRowCount(){
        return clients.size();
    }

    @Override
    public String getColumnName(int column){
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int row, int column){
        Client tempClient = clients.get(row);

        switch (column){

            case ID_COL:
                return tempClient.getId();
            case FIRST_NAME_COL:
                return tempClient.getFirstName();
            case LAST_NAME_COL:
                return tempClient.getLastName();
            case EMAIL_COL:
                return tempClient.getEmail();
            case CAR_COL:
                return tempClient.getCar();
            case PHONE_NUMBER_COL:
                return tempClient.getPhoneNumber();
            default:
                return tempClient.getLastName();

        }

    }

    @Override
    public Class getColumnClass(int c){
        return getValueAt(0,c).getClass();
    }

}
