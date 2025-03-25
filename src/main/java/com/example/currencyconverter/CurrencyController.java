package com.example.currencyconverter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class CurrencyController implements Initializable {
    ArrayList<String> CurrencyList = new ArrayList<>(
            Arrays.asList("AED", "AFN", "ALL", "AMD", "ANG", "AOA", "ARS", "AUD", "AWG", "AZN",
                    "BAM", "BBD", "BDT", "BGN", "BHD", "BIF", "BMD", "BND", "BOB", "BRL", "BSD", "BTN", "BWP", "BYN", "BZD",
                    "CAD", "CDF", "CHF", "CLP", "CNY", "COP", "CRC", "CUP", "CVE", "CZK",
                    "DFJ", "DKK", "DOP", "DZD",
                    "EGP", "ERN", "ETB", "EUR",
                    "FJD", "FKP", "FOK",
                    "GBP", "GEL", "GGP", "GHS", "GIP", "GMD", "GNF", "GTQ", "GYD",
                    "HKD", "HNL", "HRK", "HTG", "HUF",
                    "IDR", "ILS", "IMP", "INR", "IQD", "IRR", "ISK",
                    "JEP", "JMD", "JOD", "JPY",
                    "KES", "KGS", "KHR", "KID", "KMF", "KRW", "KWD", "KYD", "KZT",
                    "LAK", "LBP", "LKR", "LRD", "LSL", "LYD",
                    "MAD", "MDL", "MGA", "MKD", "MMK", "MNT", "MOP", "MRU", "MUR", "MVR", "MWK", "MXN", "MYR", "MZN",
                    "NAD", "NGN", "NIO", "NOK", "NPR", "NZD",
                    "OMR",
                    "PAB", "PEN", "PGK", "PHP", "PKR", "PLN", "PYG",
                    "QAR",
                    "RON", "RSD", "RUB", "RWF",
                    "SAR", "SBD", "SCR", "SDG", "SEK", "SGD", "SHP", "SLE", "SOS", "SRD", "SSP", "STN", "SYP", "SZL",
                    "THB", "TJS", "TMT", "TND", "TOP", "TRY", "TTD", "TVD", "TWD", "TZS",
                    "UAH", "UGX", "USD", "UYU", "UYU", "UZS",
                    "VES", "VND", "VUV",
                    "WST",
                    "XAF", "XCD", "XDR", "XOF", "XPF",
                    "YER",
                    "ZAR", "ZMW", "ZWL")

            );

    @FXML
    private Label myLabel;

    @FXML
    private TextField myField0;
    @FXML
    private ListView<String> myList0;
    @FXML
    private TextField myField1;
    @FXML
    private ListView<String> myList1;



    @FXML
    private TextField myAmount;

    @FXML
    private Label myDisplayConversion;


    @FXML
    void search(KeyEvent event)
    {
        myList0.getItems().clear();
        myList0.getItems().addAll(searchList(myField0.getText(),CurrencyList, true));
    }

    @FXML
    void search1(KeyEvent event)
    {
        myList1.getItems().clear();
        myList1.getItems().addAll(searchList(myField1.getText(), CurrencyList, false));
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        //myList0.getItems().addAll(CurrencyList);
    }

    private List<String> searchList(String searchWords, List<String> listOfStrings, boolean isFirstList) {
        List<String> searchWordsArray = Arrays.asList(searchWords.trim().split(" "));

        // Filter the list to get items that match the search criteria
        List<String> results = listOfStrings.stream().filter(input -> {
            // Check if the input contains all words in searchWordsArray (case-insensitive)
            boolean matchesAllWords = searchWordsArray.stream().allMatch(word ->
                    input.toLowerCase().contains(word.toLowerCase()));
            return matchesAllWords;
        }).collect(Collectors.toList());


        if (results.size() == 1 && results.get(0).equalsIgnoreCase(searchWords.trim())) {

            Platform.runLater(() -> {
                if (isFirstList) {

                    myList0.getItems().clear();
                } else {

                    myList1.getItems().clear();
                }
            });
        }

        return results;
    }

    @FXML
    protected void onConvertButtonClick() {

        String API_KEY = "560ea6b04b41fc09c861231c";
        String url_str = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/pair/" + myField0.getText().toUpperCase() + "/" + myField1.getText().toUpperCase() + "/" + myAmount.getText();
        // Get Field Text And put out GET Request
        URL url = null;
        try {
            url = new URL(url_str);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        HttpURLConnection request = null;
        try {
            request = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            request.connect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        JsonParser jp = new JsonParser();
        JsonElement root = null;
        try {
            root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JsonObject jsonobj = root.getAsJsonObject();


        String req_result = jsonobj.get("result").getAsString();
        myDisplayConversion.setText(myField0.getText() + " " + myAmount.getText() + " -> " + myField1.getText() + " " + jsonobj.get("conversion_result").getAsString());
    }

}