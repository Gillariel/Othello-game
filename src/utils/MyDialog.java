/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.List;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;

/**
 *
 * @author User
 */
public class MyDialog {

    public static void dialogWithoutHeader(String title, String text) {
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle(title);
        info.setContentText(text);
        info.setHeaderText(null);
        info.showAndWait();
    }
    
    public static void dialog(String title, String header, String text) {
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle(title);
        info.setContentText(text);
        info.setHeaderText(header);
        info.showAndWait();
    }
    
    public static void warningDialog(String title, String text) {
        Alert info = new Alert(Alert.AlertType.WARNING);
        info.setTitle(title);
        info.setContentText(text);
        info.setHeaderText(null);
        info.showAndWait();
    }
    
    /**
     * <summary>
     * Show A modal Ok/Cancel dialog box 
     * </summary>
     * @param title Title of the Box
     * @param header Header of the Box
     * @param text MainText of the Box
     * @return Ok -> true, Cancel -> false
     */
    public static boolean confirmationDialog(String title, String header, String text) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(text);

        Optional<ButtonType> result = alert.showAndWait();
        return (result.get() == ButtonType.OK);
    }
    
    /**
     * <summary>
     * Not operational yet
     * </summary>
     * @param title
     * @param header
     * @param text
     * @param buttonsNames 
     */
    public static void confirmationDialog(String title, String header, String text, List<String> buttonsNames) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Look, a Confirmation Dialog");
        alert.setContentText("Are you ok with this?");

        for(String texte : buttonsNames)
            alert.getButtonTypes().add(new ButtonType(texte));
        
        alert.getButtonTypes().add(new ButtonType("Cancel", ButtonData.CANCEL_CLOSE));
        
        Optional<ButtonType> result = alert.showAndWait();
        
        /*switch(result.get() == ){
            == ButtonType.OK
        }*/
    }
    
}
