package project;

import javafx.scene.control.TextField;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


/**
 * @author Sahil Patel
 */
public final class Util {
    /**
     * Private constructor to restrict instantiation of utility class Util
     * */
    private Util(){}
    /**
    * Create session factory for hibernate
    * */
    private final static SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
    /**
    * This will create a new session for interacting with database through hibernate
    * */
    public static Session openSession(){
        return sessionFactory.openSession();
    }

    /**
     * This will only allow numbers
     */
    public static void formatToNumberOnly(TextField textField){
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    /**
     * This is for having decimals
     */
    public static void formatToDecimalOnly(TextField textField){
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d?") && !newValue.matches("\\d+\\.?\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d.]", ""));
                if(textField.getText().chars().filter(ch->ch=='.').count()>1){
                    textField.setText(withOneDot(textField.getText()));
                }
            }
        });
    }

    /**
     * this is only for allowing the letters
     */
    public static void formatToAlphabetOnly(TextField textField){
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z ]*")) {
                textField.setText(newValue.replaceAll("[^a-zA-Z ]", ""));
            }
        });
    }

    /**
     This will only allow one decimal in a text field
     * if you have 99.999 you cannot change to 999.99
     * instead all you can change to is 9.99
     * you would have to remove the whole thing and write again
     */
    private static String withOneDot(String text){
        int index = text.indexOf(".")+1;
        return text.substring(0,index)+text.substring(index).replaceAll("\\.","");
    }

}

