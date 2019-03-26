package sample;

import Connection.ConnectionClass;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.sql.Connection;

public class Controller implements Initializable
{
    @FXML private Button Check,addSelected,Purchase,Clear,Cancel;

    @FXML Label totalLabel,taxLabel,completeOrder;

    @FXML private CheckBox meatLovers,Supreme,Veggie,Philly,Hawaiian,buffaloChicken,Deluxe,bbqChicken,customPizza,
    cocaCola,Sprite,Fanta,Pepperoni,Mushrooms,Onions,Sausage,Bacon,eCheese,Olives,gPeppers;

    @FXML private ChoiceBox expMonth,expYear,City,State,orderType,mlSize,mlType,mlQnty,sSize,sType,sQnty,vSize,vType,vQnty,pSize,pType,pQnty,
    hSize,hType,hQnty,bcSize,bcType,bcQnty,dSize,dType,dQnty,bbqcSize,bbqcType,bbqcQnty,cpSize,cpType,cpQnty,ccSize,
    ccQnty,spSize,spQnty,fSize,fQnty;

    @FXML private HBox meatLoversHBox,supremeHBox,VeggieHBox,PhillyHBox,HawaiianHBox,buffaloChickenHBox,DeluxeHBox,
    bbqChickenHBox,CustomPizzaHBox,cocaColaHBox,SpriteHBox,FantaHBox;

    @FXML private VBox reviewOrderVBox;

    @FXML private TextField phoneNumber,firstName,lastName,Street,ccNum,CVC;

    @FXML private TextArea noteTextArea;

    private double Cost,Total,Tax,totalAfterTax = 0;

    private static Label purchaseStatus;

    private int numOfToppings = 0;

    private String Order,Toppings = "";

    @FXML private TableView<ModelTable> table;
    @FXML private TableColumn<ModelTable,String> col_date,col_phone,col_last_name,col_street,col_city,col_state,col_order_type,
    col_order,col_total,col_note;
    ObservableList<ModelTable> oblist = FXCollections.observableArrayList();

    @FXML private void handleCheckBox()
    {
        if(meatLovers.isSelected()){meatLoversHBox.setDisable(false);meatLoversHBox.setEffect(null);}else{meatLoversHBox.setDisable(true);meatLoversHBox.setEffect(new GaussianBlur(10.0));}
        if(Supreme.isSelected()){supremeHBox.setDisable(false);supremeHBox.setEffect(null);}else{supremeHBox.setDisable(true);supremeHBox.setEffect(new GaussianBlur(10.0));}
        if(Veggie.isSelected()){VeggieHBox.setDisable(false);VeggieHBox.setEffect(null);}else{VeggieHBox.setDisable(true);VeggieHBox.setEffect(new GaussianBlur(10.0));}
        if(Philly.isSelected()){PhillyHBox.setDisable(false);PhillyHBox.setEffect(null);}else{PhillyHBox.setDisable(true);PhillyHBox.setEffect(new GaussianBlur(10.0));}
        if(Hawaiian.isSelected()){HawaiianHBox.setDisable(false);HawaiianHBox.setEffect(null);}else{HawaiianHBox.setDisable(true);HawaiianHBox.setEffect(new GaussianBlur(10.0));}
        if(buffaloChicken.isSelected()){buffaloChickenHBox.setDisable(false);buffaloChickenHBox.setEffect(null);}else{buffaloChickenHBox.setDisable(true);buffaloChickenHBox.setEffect(new GaussianBlur(10.0));}
        if(Deluxe.isSelected()){DeluxeHBox.setDisable(false);DeluxeHBox.setEffect(null);}else{DeluxeHBox.setDisable(true);DeluxeHBox.setEffect(new GaussianBlur(10.0));}
        if(bbqChicken.isSelected()){bbqChickenHBox.setDisable(false);bbqChickenHBox.setEffect(null);}else{bbqChickenHBox.setDisable(true);bbqChickenHBox.setEffect(new GaussianBlur(10.0));}
        if(customPizza.isSelected()){CustomPizzaHBox.setDisable(false);CustomPizzaHBox.setEffect(null);}
        else{CustomPizzaHBox.setDisable(true);CustomPizzaHBox.setEffect(new GaussianBlur(10.0));Pepperoni.setSelected(false);Mushrooms.setSelected(false);Onions.setSelected(false);Sausage.setSelected(false);
            Bacon.setSelected(false);eCheese.setSelected(false);Olives.setSelected(false);gPeppers.setSelected(false);}
        if(cocaCola.isSelected()){cocaColaHBox.setDisable(false);cocaColaHBox.setEffect(null);}else{cocaColaHBox.setDisable(true);cocaColaHBox.setEffect(new GaussianBlur(10.0));}
        if(Sprite.isSelected()){SpriteHBox.setDisable(false);SpriteHBox.setEffect(null);}else{SpriteHBox.setDisable(true);SpriteHBox.setEffect(new GaussianBlur(10.0));}
        if(Fanta.isSelected()){FantaHBox.setDisable(false);FantaHBox.setEffect(null);}else{FantaHBox.setDisable(true);FantaHBox.setEffect(new GaussianBlur(10.0));}
    }

    @FXML private void AddSelected()
    {
        //Two decimal places
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);

        if(meatLovers.isSelected())
        {
            if(mlSize.getValue().equals("Small")){Cost = 9.00;}else if(mlSize.getValue().equals("Medium")){Cost = 12.00;}else if(mlSize.getValue().equals("Large")){Cost = 15.00;}
            if(mlType.getValue().equals("Thin")){Cost = Cost - 2.00;} else if(mlType.getValue().equals("Pan")){Cost = Cost + 2.00;}
            Order = "$" +df.format(Cost * Double.parseDouble(mlQnty.getValue().toString())) + " Meat Lovers (" + mlSize.getValue().toString() + ") (" + mlType.getValue().toString() + ") (" + mlQnty.getValue().toString() + ")";
            completeOrder.setText(completeOrder.getText() + Order + "\n");
            Cost = Cost * Double.parseDouble(mlQnty.getValue().toString());
            Total = Total + Cost;
            meatLovers.setSelected(false);
            meatLoversHBox.setDisable(true);
            meatLoversHBox.setEffect(new GaussianBlur(10.0));
        }
        if(Supreme.isSelected())
        {
            if(sSize.getValue().equals("Small")){Cost = 9.00;}else if(sSize.getValue().equals("Medium")){Cost = 12.00;}else if(sSize.getValue().equals("Large")){Cost = 15.00;}
            if(sType.getValue().equals("Thin")){Cost = Cost - 2.00;} else if(sType.getValue().equals("Pan")){Cost = Cost + 2.00;}
            Order = "$" +df.format(Cost * Double.parseDouble(sQnty.getValue().toString())) + " Supreme (" + sSize.getValue().toString() + ") (" + sType.getValue().toString() + ") (" + sQnty.getValue().toString() + ")";
            completeOrder.setText(completeOrder.getText() + Order + "\n");
            Cost = Cost * Double.parseDouble(sQnty.getValue().toString());
            Total = Total + Cost;
            Supreme.setSelected(false);
            supremeHBox.setDisable(true);
            supremeHBox.setEffect(new GaussianBlur(10.0));
        }
        if(Veggie.isSelected())
        {
            if(vSize.getValue().equals("Small")){Cost = 9.00;}else if(vSize.getValue().equals("Medium")){Cost = 12.00;}else if(vSize.getValue().equals("Large")){Cost = 15.00;}
            if(vType.getValue().equals("Thin")){Cost = Cost - 2.00;} else if(vType.getValue().equals("Pan")){Cost = Cost + 2.00;}
            Order = "$" +df.format(Cost * Double.parseDouble(vQnty.getValue().toString())) + " Veggie (" + vSize.getValue().toString() + ") (" + vType.getValue().toString() + ") (" + vQnty.getValue().toString() + ")";
            completeOrder.setText(completeOrder.getText() + Order + "\n");
            Cost = Cost * Double.parseDouble(vQnty.getValue().toString());
            Total = Total + Cost;
            Veggie.setSelected(false);
            VeggieHBox.setDisable(true);
            VeggieHBox.setEffect(new GaussianBlur(10.0));
        }
        if(Philly.isSelected())
        {
            if(pSize.getValue().equals("Small")){Cost = 9.00;}else if(pSize.getValue().equals("Medium")){Cost = 12.00;}else if(pSize.getValue().equals("Large")){Cost = 15.00;}
            if(pType.getValue().equals("Thin")){Cost = Cost - 2.00;} else if(pType.getValue().equals("Pan")){Cost = Cost + 2.00;}
            Order = "$" +df.format(Cost * Double.parseDouble(pQnty.getValue().toString())) + " Philly (" + pSize.getValue().toString() + ") (" + pType.getValue().toString() + ") (" + pQnty.getValue().toString() + ")";
            completeOrder.setText(completeOrder.getText() + Order + "\n");
            Cost = Cost * Double.parseDouble(pQnty.getValue().toString());
            Total = Total + Cost;
            Philly.setSelected(false);
            PhillyHBox.setDisable(true);
            PhillyHBox.setEffect(new GaussianBlur(10.0));
        }
        if(Hawaiian.isSelected())
        {
            if(hSize.getValue().equals("Small")){Cost = 9.00;}else if(hSize.getValue().equals("Medium")){Cost = 12.00;}else if(hSize.getValue().equals("Large")){Cost = 15.00;}
            if(hType.getValue().equals("Thin")){Cost = Cost - 2.00;} else if(hType.getValue().equals("Pan")){Cost = Cost + 2.00;}
            Order = "$" +df.format(Cost * Double.parseDouble(hQnty.getValue().toString())) + " Hawaiian (" + hSize.getValue().toString() + ") (" + hType.getValue().toString() + ") (" + hQnty.getValue().toString() + ")";
            completeOrder.setText(completeOrder.getText() + Order + "\n");
            Cost = Cost * Double.parseDouble(hQnty.getValue().toString());
            Total = Total + Cost;
            Hawaiian.setSelected(false);
            HawaiianHBox.setDisable(true);
            HawaiianHBox.setEffect(new GaussianBlur(10.0));
        }
        if(buffaloChicken.isSelected())
        {
            if(bcSize.getValue().equals("Small")){Cost = 9.00;}else if(bcSize.getValue().equals("Medium")){Cost = 12.00;}else if(bcSize.getValue().equals("Large")){Cost = 15.00;}
            if(bcType.getValue().equals("Thin")){Cost = Cost - 2.00;} else if(bcType.getValue().equals("Pan")){Cost = Cost + 2.00;}
            Order = "$" +df.format(Cost * Double.parseDouble(bcQnty.getValue().toString())) + " Buffalo Chicken (" + bcSize.getValue().toString() + ") (" + bcType.getValue().toString() + ") (" + bcQnty.getValue().toString() + ")";
            completeOrder.setText(completeOrder.getText() + Order + "\n");
            Cost = Cost * Double.parseDouble(bcQnty.getValue().toString());
            Total = Total + Cost;
            buffaloChicken.setSelected(false);
            buffaloChickenHBox.setDisable(true);
            buffaloChickenHBox.setEffect(new GaussianBlur(10.0));
        }
        if(Deluxe.isSelected())
        {
            if(dSize.getValue().equals("Small")){Cost = 9.00;}else if(dSize.getValue().equals("Medium")){Cost = 12.00;}else if(dSize.getValue().equals("Large")){Cost = 15.00;}
            if(dType.getValue().equals("Thin")){Cost = Cost - 2.00;} else if(dType.getValue().equals("Pan")){Cost = Cost + 2.00;}
            Order = "$" +df.format(Cost * Double.parseDouble(dQnty.getValue().toString())) + " Deluxe (" + dSize.getValue().toString() + ") (" + dType.getValue().toString() + ") (" + dQnty.getValue().toString() + ")";
            completeOrder.setText(completeOrder.getText() + Order + "\n");
            Cost = Cost * Double.parseDouble(dQnty.getValue().toString());
            Total = Total + Cost;
            Deluxe.setSelected(false);
            DeluxeHBox.setDisable(true);
            DeluxeHBox.setEffect(new GaussianBlur(10.0));
        }
        if(bbqChicken.isSelected())
        {
            if(bbqcSize.getValue().equals("Small")){Cost = 9.00;}else if(bbqcSize.getValue().equals("Medium")){Cost = 12.00;}else if(bbqcSize.getValue().equals("Large")){Cost = 15.00;}
            if(bbqcType.getValue().equals("Thin")){Cost = Cost - 2.00;} else if(bbqcType.getValue().equals("Pan")){Cost = Cost + 2.00;}
            Order = "$" +df.format(Cost * Double.parseDouble(bbqcQnty.getValue().toString())) + " BBQ Chicken (" + bbqcSize.getValue().toString() + ") (" + bbqcType.getValue().toString() + ") (" + bbqcQnty.getValue().toString() + ")";
            completeOrder.setText(completeOrder.getText() + Order + "\n");
            Cost = Cost * Double.parseDouble(bbqcQnty.getValue().toString());
            Total = Total + Cost;
            bbqChicken.setSelected(false);
            bbqChickenHBox.setDisable(true);
            bbqChickenHBox.setEffect(new GaussianBlur(10.0));
        }
        if(customPizza.isSelected())
        {
            if(Pepperoni.isSelected()){numOfToppings++;Toppings = Toppings + "P ";}
            if(Mushrooms.isSelected()){numOfToppings++;Toppings = Toppings + "M ";}
            if(Onions.isSelected()){numOfToppings++;Toppings = Toppings + "O ";}
            if(Sausage.isSelected()){numOfToppings++;Toppings = Toppings + "S ";}
            if(Bacon.isSelected()){numOfToppings++;Toppings = Toppings + "B ";}
            if(eCheese.isSelected()){numOfToppings++;Toppings = Toppings + "EC ";}
            if(Olives.isSelected()){numOfToppings++;Toppings = Toppings + "OL ";}
            if(gPeppers.isSelected()){numOfToppings++;Toppings = Toppings + "GP ";}
            if(cpSize.getValue().equals("Small")){Cost = 7.00;}else if(cpSize.getValue().equals("Medium")){Cost = 10.00;}else if(cpSize.getValue().equals("Large")){Cost = 13.00;}
            if(cpType.getValue().equals("Thin")){Cost = Cost - 2.00;} else if(cpType.getValue().equals("Pan")){Cost = Cost + 2.00;}
            Order = "$" +df.format(((Cost * Double.parseDouble(cpQnty.getValue().toString()))) + (numOfToppings * Double.parseDouble(cpQnty.getValue().toString()))) + " Custom Pizza " + Toppings + " (" + cpSize.getValue().toString() + ") (" + cpType.getValue().toString() + ") (" + cpQnty.getValue().toString() + ")";
            completeOrder.setText(completeOrder.getText() + Order + "\n");
            Cost = (Cost * Double.parseDouble(cpQnty.getValue().toString())) + (numOfToppings * Double.parseDouble(cpQnty.getValue().toString()));
            Total = Total + Cost;
            customPizza.setSelected(false);
            CustomPizzaHBox.setDisable(true);
            CustomPizzaHBox.setEffect(new GaussianBlur(10.0));
            numOfToppings = 0;
            Toppings = "";
            Pepperoni.setSelected(false);Mushrooms.setSelected(false);Onions.setSelected(false);Sausage.setSelected(false);
            Bacon.setSelected(false);eCheese.setSelected(false);Olives.setSelected(false);gPeppers.setSelected(false);
        }
        if(cocaCola.isSelected())
        {
            if(ccSize.getValue().equals("20 oz")){Cost = 2.00;}if(ccSize.getValue().equals("2 liter")){Cost = 4.00;}
            Order = "$" +df.format(Cost * Double.parseDouble(ccQnty.getValue().toString())) + " Coca Cola (" + ccSize.getValue().toString() + ") (" + ccQnty.getValue().toString() + ")";
            completeOrder.setText(completeOrder.getText() + Order + "\n");
            Cost = Cost * Double.parseDouble(ccQnty.getValue().toString());
            Total = Total + Cost;
            cocaCola.setSelected(false);
            cocaColaHBox.setDisable(true);
            cocaColaHBox.setEffect(new GaussianBlur(10.0));
        }
        if(Sprite.isSelected())
        {
            if(spSize.getValue().equals("20 oz")){Cost = 2.00;}if(spSize.getValue().equals("2 liter")){Cost = 4.00;}
            Order = "$" +df.format(Cost * Double.parseDouble(spQnty.getValue().toString())) + " Sprite (" + spSize.getValue().toString() + ") (" + spQnty.getValue().toString() + ")";
            completeOrder.setText(completeOrder.getText() + Order + "\n");
            Cost = Cost * Double.parseDouble(spQnty.getValue().toString());
            Total = Total + Cost;
            Sprite.setSelected(false);
            SpriteHBox.setDisable(true);
            SpriteHBox.setEffect(new GaussianBlur(10.0));
        }
        if(Fanta.isSelected())
        {
            if(fSize.getValue().equals("20 oz")){Cost = 2.00;}if(fSize.getValue().equals("2 liter")){Cost = 4.00;}
            Order = "$" +df.format(Cost * Double.parseDouble(fQnty.getValue().toString())) + " Fanta (" + fSize.getValue().toString() + ") (" + fQnty.getValue().toString() + ")";
            completeOrder.setText(completeOrder.getText() + Order + "\n");
            Cost = Cost * Double.parseDouble(fQnty.getValue().toString());
            Total = Total + Cost;
            Fanta.setSelected(false);
            FantaHBox.setDisable(true);
            FantaHBox.setEffect(new GaussianBlur(10.0));
        }
        Tax = Total*.04;
        totalAfterTax = Total+Tax;
        taxLabel.setText("Tax: $" + df.format(Tax));
        totalLabel.setText("Total: $" + df.format(totalAfterTax));
        reviewOrderVBox.setPrefWidth(completeOrder.getMaxWidth());
        reviewOrderVBox.setPrefHeight(completeOrder.getMaxHeight());

        if(totalAfterTax > 0)
        {
            Purchase.setDisable(false);
        }
    }

    @FXML public void Purchase(ActionEvent actionEvent) throws SQLException
    {
        boolean vPhone = false,vFN = false,vLN = false,vS = false,vCCNUM = false,vCVC = false;

        if(phoneNumber.getText().length() == 14){vPhone = true;}
        if(firstName.getText().length() > 0){vFN = true;}
        if(lastName.getText().length() > 0){vLN = true;}
        if(Street.getText().length() > 0){vS = true;}
        if(ccNum.getText().length() == 19){vCCNUM = true;}
        if(CVC.getText().length() == 3){vCVC = true;}

        if(vPhone&&vFN&&vLN&&vS&&vCCNUM&&vCVC)
        {
            purchaseStatus = new Label("Approved!");

            ConnectionClass connectionClass = new ConnectionClass();
            Connection connection = connectionClass.getConnection();

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();

            //sends customer to database
            Statement statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO CUSTOMER (date,phone,first_name,last_name,street,city,state," +
                    "card_number,exp_month,exp_year,order_type,complete_order,total,note) "
                    +"VALUES ('"+dateFormat.format(date)+"','"+phoneNumber.getText()+"','"+firstName.getText()+"','"+lastName.getText()+"'" +
                    ",'"+Street.getText()+"','"+City.getValue().toString()+"','"+State.getValue().toString()+"'" +
                    ",'"+ccNum.getText()+"','"+expMonth.getValue().toString()+"','"+expYear.getValue().toString()+"'" +
                    ",'"+orderType.getValue().toString()+"','"+completeOrder.getText()+"','"+"$"+totalAfterTax+"'" +
                    ",'"+noteTextArea.getText()+"')");

            updateTable();
            Cancel();
        }
        else
            purchaseStatus = new Label("Denied! Make sure information is entered correctly.");

        completePurchase();
    }

    private void completePurchase()
    {
        Stage window = new Stage();
        window.setTitle("System Message");
        window.initModality(Modality.APPLICATION_MODAL);
        window.setMinWidth(300);
        window.setMinHeight(150);

        Button closeButton = new Button("Okay");
        closeButton.setOnAction(event -> {
            window.close();
        });

        VBox layout = new VBox();
        layout.setSpacing(10);
        layout.getChildren().addAll(purchaseStatus,closeButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    @FXML private void Clear()
    {
        Total = 0;
        Cost = 0;
        numOfToppings = 0;
        taxLabel.setText("");
        totalLabel.setText("Total: $0.00");
        completeOrder.setText("");
        Purchase.setDisable(true);
    }

    @FXML private void Cancel()
    {
        Clear();
        phoneNumber.clear();
        firstName.clear();
        lastName.clear();
        Street.clear();
        City.setValue("Marietta");
        State.setValue("Georgia");
        ccNum.clear();
        expMonth.setValue("01");
        expYear.setValue(2019);
        CVC.clear();
        orderType.setValue("Carry Out");
        noteTextArea.clear();
    }

    private void updateTable()
    {
        table.getItems().clear();
        col_date.setSortType(TableColumn.SortType.DESCENDING);

        try
        {
            Connection con = ConnectionClass.getConnection();
            ResultSet rs = con.createStatement().executeQuery("select * from customer");

            while(rs.next())
            {
                oblist.add(new ModelTable(rs.getString("date"),rs.getString("phone"),
                        rs.getString("last_name"),rs.getString("street"),
                        rs.getString("city"),rs.getString("state"),rs.getString("order_type"),
                        rs.getString("complete_order"),rs.getString("total"),rs.getString("note")));
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE,null,ex);
        }

        col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        col_phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        col_last_name.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        col_street.setCellValueFactory(new PropertyValueFactory<>("street"));
        col_city.setCellValueFactory(new PropertyValueFactory<>("city"));
        col_state.setCellValueFactory(new PropertyValueFactory<>("state"));
        col_order_type.setCellValueFactory(new PropertyValueFactory<>("orderType"));
        col_order.setCellValueFactory(new PropertyValueFactory<>("order"));
        col_total.setCellValueFactory(new PropertyValueFactory<>("total"));
        col_note.setCellValueFactory(new PropertyValueFactory<>("note"));

        table.setItems(oblist);
        table.getSortOrder().add(col_date);
        autoResizeColumns(table);
    }

    private static void autoResizeColumns( TableView<?> table )
    {
        //Set the right policy
        table.setColumnResizePolicy( TableView.UNCONSTRAINED_RESIZE_POLICY);
        table.getColumns().stream().forEach( (column) ->
        {
            //Minimal width = columnheader
            Text t = new Text( column.getText() );
            double max = t.getLayoutBounds().getWidth();
            for ( int i = 0; i < table.getItems().size(); i++ )
            {
                //cell must not be empty
                if ( column.getCellData( i ) != null )
                {
                    t = new Text( column.getCellData( i ).toString() );
                    double calcwidth = t.getLayoutBounds().getWidth();
                    //remember new max-width
                    if ( calcwidth > max )
                    {
                        max = calcwidth;
                    }
                }
            }
            //set the new max-widht with some extra space
            column.setPrefWidth( max + 10.0d );
        } );
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        updateTable();
        //ChoiceBoxes
        expMonth.getItems().addAll("01","02","03","04","05","06","07","08","09","10","11","12"); expMonth.setValue("01");
        expYear.getItems().addAll(2018,2019,2020,2021,2022,2023,2024,2025,2026,2027,2028); expYear.setValue(2019);
        City.getItems().addAll("Acworth","Kennesaw","Marietta"); City.setValue("Marietta");
        State.getItems().add("Georgia"); State.setValue("Georgia");
        orderType.getItems().addAll("Carry Out","Delivery"); orderType.setValue("Carry Out");
        mlSize.getItems().addAll("Small","Medium","Large"); mlSize.setValue("Medium");
        mlType.getItems().addAll("Thin","Hand Toss","Pan"); mlType.setValue("Hand Toss");
        mlQnty.getItems().addAll(1,2,3,4,5,6,7,8,9,10); mlQnty.setValue(1);
        sSize.getItems().addAll("Small","Medium","Large"); sSize.setValue("Medium");
        sType.getItems().addAll("Thin","Hand Toss","Pan"); sType.setValue("Hand Toss");
        sQnty.getItems().addAll(1,2,3,4,5,6,7,8,9,10); sQnty.setValue(1);
        vSize.getItems().addAll("Small","Medium","Large"); vSize.setValue("Medium");
        vType.getItems().addAll("Thin","Hand Toss","Pan"); vType.setValue("Hand Toss");
        vQnty.getItems().addAll(1,2,3,4,5,6,7,8,9,10); vQnty.setValue(1);
        pSize.getItems().addAll("Small","Medium","Large"); pSize.setValue("Medium");
        pType.getItems().addAll("Thin","Hand Toss","Pan"); pType.setValue("Hand Toss");
        pQnty.getItems().addAll(1,2,3,4,5,6,7,8,9,10); pQnty.setValue(1);
        hSize.getItems().addAll("Small","Medium","Large"); hSize.setValue("Medium");
        hType.getItems().addAll("Thin","Hand Toss","Pan"); hType.setValue("Hand Toss");
        hQnty.getItems().addAll(1,2,3,4,5,6,7,8,9,10); hQnty.setValue(1);
        bcSize.getItems().addAll("Small","Medium","Large"); bcSize.setValue("Medium");
        bcType.getItems().addAll("Thin","Hand Toss","Pan"); bcType.setValue("Hand Toss");
        bcQnty.getItems().addAll(1,2,3,4,5,6,7,8,9,10); bcQnty.setValue(1);
        dSize.getItems().addAll("Small","Medium","Large"); dSize.setValue("Medium");
        dType.getItems().addAll("Thin","Hand Toss","Pan"); dType.setValue("Hand Toss");
        dQnty.getItems().addAll(1,2,3,4,5,6,7,8,9,10); dQnty.setValue(1);
        bbqcSize.getItems().addAll("Small","Medium","Large"); bbqcSize.setValue("Medium");
        bbqcType.getItems().addAll("Thin","Hand Toss","Pan"); bbqcType.setValue("Hand Toss");
        bbqcQnty.getItems().addAll(1,2,3,4,5,6,7,8,9,10); bbqcQnty.setValue(1);
        cpSize.getItems().addAll("Small","Medium","Large"); cpSize.setValue("Medium");
        cpType.getItems().addAll("Thin","Hand Toss","Pan"); cpType.setValue("Hand Toss");
        cpQnty.getItems().addAll(1,2,3,4,5,6,7,8,9,10); cpQnty.setValue(1);
        ccSize.getItems().addAll("20 oz","2 liter"); ccSize.setValue("2 liter");
        ccQnty.getItems().addAll(1,2,3,4,5,6,7,8,9,10); ccQnty.setValue(1);
        spSize.getItems().addAll("20 oz","2 liter"); spSize.setValue("2 liter");
        spQnty.getItems().addAll(1,2,3,4,5,6,7,8,9,10); spQnty.setValue(1);
        fSize.getItems().addAll("20 oz","2 liter"); fSize.setValue("2 liter");
        fQnty.getItems().addAll(1,2,3,4,5,6,7,8,9,10); fQnty.setValue(1);


        //phone number formatting
        UnaryOperator<TextFormatter.Change> filter = new UnaryOperator<TextFormatter.Change>()
        {
            @Override
            public TextFormatter.Change apply(TextFormatter.Change change)
            {
                if (!change.isContentChange())
                {
                    /* nothing is added or deleted but change must be returned
                     * as it contains selection info and caret position
                     */
                    return change;
                }

                int maxlength = 14;
                if (change.getControlText().indexOf('(') == -1)
                {
                    maxlength = 10;
                }

                if (change.getControlNewText().length() > maxlength
                        || change.getText().matches("\\D+"))
                {
                    // invalid input. Cancel the change
                    return null;
                }
                return change;
            }
        };

        StringConverter<String> converter = new StringConverter<String>()
        {

            // updates displayed text from committed value
            @Override
            public String toString(String committedText)
            {
                if (committedText == null)
                {
                    // don't change displayed text
                    return phoneNumber.getText();
                }

                if (committedText.length() == 10 && !committedText.matches("\\D+"))
                {
                    return String.format("(%s) %s-%s", committedText.substring(0, 3), committedText.substring(3, 6),
                            committedText.substring(6, 10));
                } else
                    {
                    /* Commited text can be either null or 10 digits.
                     * Nothing else is allowed by fromString() method unless changed directly
                     */
                    throw new IllegalStateException(
                            "Unexpected or incomplete phone number value: " + committedText);
                }
            }

            // commits displayed text to value
            @Override
            public String fromString(String displayedText)
            {
                // remove formatting characters
                Pattern p = Pattern.compile("[\\p{Punct}\\p{Blank}]", Pattern.UNICODE_CHARACTER_CLASS);
                Matcher m = p.matcher(displayedText);
                displayedText = m.replaceAll("");

                if (displayedText.length() != 10)
                {
                    // user is not done typing the number. Don't commit
                    return null;
                }

                return displayedText;
            }
        };
        TextFormatter<String> formatter = new TextFormatter<String>(converter, null, filter);
        phoneNumber.setTextFormatter(formatter);
        //label.textProperty().bind(formatter.valueProperty());

        //Name Formatting
        Pattern pattern = Pattern.compile("[a-zA-Z]*");
        UnaryOperator<TextFormatter.Change> nameFilter = c -> {
            if (pattern.matcher(c.getControlNewText()).matches())
            {
                if(firstName.getText().length() < 1)
                {
                    c.setText(c.getText().toUpperCase());
                }
                else
                    c.setText(c.getText().toLowerCase());

                return c;
            }
            else
            {
                return null ;
            }
        };
        TextFormatter<String> firstNameFormatter = new TextFormatter<>(nameFilter);
        firstName.setTextFormatter(firstNameFormatter);
        addTextLimiter(firstName,14);

        Pattern pattern2 = Pattern.compile("[a-zA-Z]*");
        UnaryOperator<TextFormatter.Change> nameFilter2 = c -> {
            if (pattern2.matcher(c.getControlNewText()).matches())
            {
                if(lastName.getText().length() < 1)
                {
                    c.setText(c.getText().toUpperCase());
                }
                else
                    c.setText(c.getText().toLowerCase());

                return c;
            }
            else
            {
                return null ;
            }
        };
        TextFormatter<String> lastNameFormatter = new TextFormatter<>(nameFilter2);
        lastName.setTextFormatter(lastNameFormatter);
        addTextLimiter(lastName,14);

        //CVC Formatting
        CVC.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    CVC.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        addTextLimiter(CVC,3);

        //credit card formatting
        UnaryOperator<TextFormatter.Change> filterCC = new UnaryOperator<TextFormatter.Change>()
        {
            @Override
            public TextFormatter.Change apply(TextFormatter.Change changeCC)
            {
                if (!changeCC.isContentChange())
                {
                    /* nothing is added or deleted but change must be returned
                     * as it contains selection info and caret position
                     */
                    return changeCC;
                }

                int maxlength = 19;
                if (changeCC.getControlText().indexOf('(') == -1)
                {
                    maxlength = 16;
                }

                if (changeCC.getControlNewText().length() > maxlength
                        || changeCC.getText().matches("\\D+"))
                {
                    // invalid input. Cancel the change
                    return null;
                }
                return changeCC;
            }
        };

        StringConverter<String> converterCC = new StringConverter<String>()
        {

            // updates displayed text from committed value
            @Override
            public String toString(String committedText)
            {
                if (committedText == null)
                {
                    // don't change displayed text
                    return ccNum.getText();
                }

                if (committedText.length() == 16 && !committedText.matches("\\D+"))
                {
                    return String.format("%s %s %s %s", committedText.substring(0, 4), committedText.substring(4, 8),
                            committedText.substring(8, 12),committedText.substring(12,16));
                } else
                {
                    /* Commited text can be either null or 10 digits.
                     * Nothing else is allowed by fromString() method unless changed directly
                     */
                    throw new IllegalStateException(
                            "Unexpected or incomplete phone number value: " + committedText);
                }
            }

            // commits displayed text to value
            @Override
            public String fromString(String displayedText)
            {
                // remove formatting characters
                Pattern p = Pattern.compile("[\\p{Punct}\\p{Blank}]", Pattern.UNICODE_CHARACTER_CLASS);
                Matcher m = p.matcher(displayedText);
                displayedText = m.replaceAll("");

                if (displayedText.length() != 16)
                {
                    // user is not done typing the number. Don't commit
                    return null;
                }

                return displayedText;
            }
        };
        TextFormatter<String> formatterCC = new TextFormatter<String>(converterCC, null, filterCC);
        ccNum.setTextFormatter(formatterCC);
        //label.textProperty().bind(formatter.valueProperty());
    }

    //limits TextField Lengths
    private static void addTextLimiter(final TextField tf, final int maxLength)
    {
        tf.textProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue)
            {
                if (tf.getText().length() > maxLength)
                {
                    String s = tf.getText().substring(0, maxLength);
                    tf.setText(s);
                }
            }
        });
    }
}
