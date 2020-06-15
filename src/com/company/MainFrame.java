package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

//
public class MainFrame {
    JFrame j;
    JLabel titleText,t1,t2,displayPortfolio,displayBalance,warningText,templateText; // Different text components
    JTextField textField1,removeStockField,depositMoney,stockQuantity; //stock lookup field
    ArrayList<ArrayList<String>> stockPortfolio; //ArrayList that holds all of stocks in portfolio
    JButton searchButton,addStockButton,updateData,removeStockButton,depositButton;
    String enteredStock,removeStockLabel;
    ArrayList<String> enteredStockData;
    int NumOfStocksAdded;
    JPanel mainPanel;
    double balance;
    public MainFrame(){
        NumOfStocksAdded = 0;
        initializeLayout();
        buttonActions();

    }
    public void buttonActions(){
        searchButton.addActionListener(new ActionListener() { //Calls getStockPrice() to search up the value of the entered stock and displays its value
            @Override
            public void actionPerformed(ActionEvent e) {
                enteredStock = textField1.getText();
                try {
                    enteredStockData = new ArrayList<String>();
                    enteredStockData.add("PLACEHOLDER");
                    enteredStockData.add("PLACEHOLDER");
                    enteredStockData.add("1.0");
                    addStockButton.setVisible(true);
                    //stockQuantity.setVisible(true);
                    t2.setText(enteredStock + " Price: " + getStockPrice(enteredStock));
                    enteredStockData.set(0,enteredStock);
                    enteredStockData.set(1,getStockPrice(enteredStock));

                    System.out.println(enteredStockData);

                }catch (Exception exception){
                    System.out.println(e);
                }
            }
        });
        addStockButton.addActionListener(new ActionListener() { //Adds selected stock to the arraylist "stockPortfolio" when clicked
            @Override
            public void actionPerformed(ActionEvent e) {
                double desiredQuant = Double.parseDouble(stockQuantity.getText());
                try {
                    enteredStockData.set(1, getStockPrice(enteredStock));
                }catch (Exception d){
                    System.out.println(d);
                }
                double stockTotal = Double.parseDouble(enteredStockData.get(1)) * desiredQuant;
                boolean duplicateStock = false;

                if(balance-stockTotal>0){ // Checks if the user has adequate funds to make the purchase
                    for(int i = 0;i<stockPortfolio.size();i++){ //checks to see if stock is already in portfolio and increases the quantity if it is
                        if(stockPortfolio.get(i).contains(enteredStock)){
                            double currentQuant = Double.parseDouble(stockPortfolio.get(i).get(2));
                            stockPortfolio.get(i).set(2,(Double.toString(currentQuant+1)));
                            displayPortfolio.setText("Your portfolio:" +stockPortfolio);
                            duplicateStock = true;
                        }
                    }
                    if(duplicateStock==false){ //Adds stock to portfolio if it is not already in it
                        stockPortfolio.add(enteredStockData);
                    }
                    balance -= stockTotal;
                    displayBalance.setText("Balance: $"+balance);
                    warningText.setText(" ");
                    displayPortfolio.setText("Your portfolio:" +stockPortfolio);
                } else{
                    warningText.setText("Unable to purchase stock...funds too low");
                }
                System.out.println(stockPortfolio);
            }

        });
        updateData.addActionListener(new ActionListener() { //Action listener for the "refresh stocks" button. Calls the function updateStockValues()
            @Override
            public void actionPerformed(ActionEvent e) {
                updateStockValues();
            }
        });
        removeStockButton.addActionListener(new ActionListener() { //Sells stock and updates balance accordingly
            @Override
            public void actionPerformed(ActionEvent e) {
                removeStockLabel = removeStockField.getText();
                for(int i = 0;i<stockPortfolio.size();i++){
                    double currentQuant = Double.parseDouble(stockPortfolio.get(i).get(2));
                    if(stockPortfolio.get(i).contains(removeStockLabel)){
                        updateStockValues();
                        try {
                            balance += Double.parseDouble(getStockPrice(stockPortfolio.get(i).get(0))); //gets the latest price of the stock before selling
                            displayBalance.setText("Balance: $"+balance);
                        }catch (Exception q){
                            System.out.println(q);
                        }
                        if(stockPortfolio.get(i).get(2).equals("1.0")) {
                            stockPortfolio.remove(i);

                        }else{

                            stockPortfolio.get(i).set(2,Double.toString(currentQuant-1));
                            displayPortfolio.setText("Your portfolio:" + stockPortfolio);

                            displayBalance.setText("Balance: $"+balance);
                        }
                        displayPortfolio.setText("Your portfolio:" + stockPortfolio);
                    }
                }
            }
        });
        depositButton.addActionListener(new ActionListener() { // Adds entered amount of money to balance
            @Override
            public void actionPerformed(ActionEvent e) {
                double depositAmount = Double.parseDouble(depositMoney.getText());
                balance+=depositAmount;
                displayBalance.setText("Balance: $"+balance);
            }
        });
    }
    public void updateStockValues(){ // Will update all of the stock prices in your portfolio
        for(int i = 0;i<stockPortfolio.size();i++){
            try {
                stockPortfolio.get(i).set(1, getStockPrice(stockPortfolio.get(i).get(0)));
                displayPortfolio.setText("Your portfolio:" +stockPortfolio);
            }catch (Exception a){
                System.out.println(a);
            }
        }
    }
    public void initializeLayout(){ // Initializes a layout. Sets sizes and default texts of the frame components.Color backGroundColor = new Color(0xCB1818);

        balance = 0;
        warningText = new JLabel("");

        warningText.setBounds(50,150,  500,500);
        stockQuantity = new JTextField("1.0");
        depositButton = new JButton("Deposit Money");
        depositMoney = new JTextField();
        displayPortfolio = new JLabel();
        stockPortfolio = new ArrayList<ArrayList<String>>(); //Holds Stock Data (Current Price,)
        textField1 = new JTextField();
        addStockButton = new JButton("Buy Stock");
        searchButton = new JButton("Search");
        removeStockButton = new JButton("Sell Stock");
        displayBalance = new JLabel();
        removeStockField = new JTextField();
        updateData = new JButton("Refresh stocks");
        j = new JFrame();
        mainPanel = new JPanel();
        mainPanel.setBackground(new Color(0xCB1818));
        j.add(mainPanel);
        j.setSize(1400,800);
        titleText = new JLabel();
        t1 = new JLabel();
        t2 = new JLabel();
        t1.setText("Enter a company symbol to look up a stock");
        displayBalance.setBounds(750,5,300,50);
        t1.setBounds(50,5,500,50);
        t2.setBounds(50,120,500,50);
        displayPortfolio.setBounds(50,200,  500,500);
        textField1.setBounds(50,42,60,30);
        stockQuantity.setBounds(250,160,40,30);
        removeStockButton.setBounds(400,80,120,30);
        removeStockField.setBounds(400,42,80,30);
        searchButton.setBounds(50,80,80,30);
        addStockButton.setBounds(50,160,160,30);
        updateData.setBounds(50,220,160,30);
        depositMoney.setBounds(750,42,80,30);
        depositButton.setBounds(750,80,120,30);
        addStockButton.setVisible(false);
        stockQuantity.setVisible(false);
        j.add(t2);
        j.add(t1); j.add(textField1);j.add(searchButton);j.add(addStockButton);j.add(updateData);j.add(depositMoney);
        j.add(stockQuantity);
        j.add(removeStockButton);
        j.add(removeStockField);
        j.add(displayBalance);
        j.add(depositButton);
        j.add(warningText);
        j.setTitle("Stock Market Simulator");
        displayBalance.setText("Balance: $"+balance);
        j.add(displayPortfolio);
        j.setLayout(null);
        j.setVisible(true);
        j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
    private String getStockPrice(String SYMBOL) throws IOException{ //Scrapes yahoo finance for stock price and return stock price as a string
        String stockPrice = " ";
        URL stockURL = new URL("https://finance.yahoo.com/quote/"+SYMBOL);
        URLConnection connection = stockURL.openConnection();
        InputStreamReader myReader = new InputStreamReader(connection.getInputStream());
        BufferedReader b = new BufferedReader(myReader);
        String line = b.readLine();
        String lineOFInterest = " ";
        while(line != null){
            if(line.contains("Fz(36px) Mb(-4px) D(b)\" data-reactid=\"14\">")){ //This is the HTML that directly precedes the stock price
                lineOFInterest = line;
                break;
            }
            line = b.readLine();
        }
        boolean priceFOund = false;
        int i = 0;
        while(priceFOund == false && i<lineOFInterest.length()-10){ //Creates substring and checks to see if the substring matches the html that precedes the stock price
            String pat = lineOFInterest.substring(i,i+33);
            if(pat.equals("Mb(-4px) D(b)\" data-reactid=\"14\">")){
                stockPrice = lineOFInterest.substring(i+33,i+41);
                stockPrice = stockPrice.replaceAll("[^\\d.]", ""); //Removes all non-numeric digits in the stock price except for periods
                priceFOund = true;
            }
            i++;
        }
        return stockPrice;
    }

}
