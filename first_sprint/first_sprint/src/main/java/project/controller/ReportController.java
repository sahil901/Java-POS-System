package project.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import project.JavaFxApplication;
import project.managers.ReportManager;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * @author Sahil Patel
 */
public class ReportController extends JavaFxApplication implements Initializable {

    /*
    * This is all the fxml code
    */
    @FXML
    private Button btnPrint;
    @FXML
    private DatePicker dpDate;
    @FXML
    private TextField txtReportID;
    @FXML
    private TextField txtStartCash;
    @FXML
    private TextField txtGrossGen;
    @FXML
    private TextField txtRefund;
    @FXML
    private TextField txtTax;
    @FXML
    private TextField txtIncome;
    @FXML
    private TextField txtEndCash;
    @FXML
    private TextArea txtReceipt;


    /**
    * Once you click the home button in the top left it will take you back
    */
    @FXML
    public void home(ActionEvent event) throws Exception {
        try {
            changeScreen(event, "views/Home.fxml");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
    * This is for the receipt that i have on the right hand size.
    * I set how it looks here and then get the values from the textFields and displays them
    */
    public void print() {
        txtReceipt.clear();
     	txtReceipt.setText(txtReceipt.getText() + "   TECH CATALYST REPORT                \n");
    	txtReceipt.setText(txtReceipt.getText() + "************************************\n");
    	txtReceipt.setText(txtReceipt.getText()+  "   Report Date:      "+ dpDate.getValue() +"\n");
    	txtReceipt.setText(txtReceipt.getText() + "************************************\n");
    	txtReceipt.setText(txtReceipt.getText() + "   Start of Day Cash($):      " + txtStartCash.getText() + "\n");
    	txtReceipt.setText(txtReceipt.getText() + "   Gross Generated($):       " + txtGrossGen.getText() + "\n");
    	txtReceipt.setText(txtReceipt.getText() + "   Total Refunds($):            " + txtRefund.getText() +  "\n");
    	txtReceipt.setText(txtReceipt.getText() + "   Total Tax($):                   " +	txtTax.getText() +"\n");
    	txtReceipt.setText(txtReceipt.getText() + "   Income($):                      " + txtIncome.getText() +"\n");
    	txtReceipt.setText(txtReceipt.getText() + "   End of Day Cash($):        " + txtEndCash.getText() + "\n");
    }
    /**
     * Searches the database for the report for the given Date in dpDate.
     * Does nothing if no date is selected.
     * */
    public void search(){
        if(dpDate.getValue() != null){
            ReportManager reportManager = new ReportManager(dpDate.getValue());
            BigDecimal startCash = reportManager.getStartOfDayCash();
            BigDecimal grossGen = reportManager.getGeneratedGross();
            BigDecimal totalRefunds = reportManager.getTotalRefundsForToday();
            txtStartCash.setText(startCash.toString());
            txtGrossGen.setText(grossGen.toString());
            txtRefund.setText(totalRefunds.toString());
            txtTax.setText(grossGen.divide(BigDecimal.valueOf(1.13), RoundingMode.DOWN).multiply(BigDecimal.valueOf(0.13)).toString());
            txtIncome.setText(grossGen.subtract(totalRefunds).toString());
            txtEndCash.setText(startCash.add(grossGen).subtract(totalRefunds).toString());
            reportManager.closeSession();
        }
    }


    /**
     * It sets the date to the current date
     * So if today is the 4th of april it cannot search for a report that is on the 5th of april.
     * limiting any possible errors for the report. Also disables print button
     */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

        dpDate.setDayCellFactory(d ->
            new DateCell() {
            @Override public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(item.isAfter(LocalDate.now()) );
        }});
        btnPrint.setDisable(false);

	}
 
}
