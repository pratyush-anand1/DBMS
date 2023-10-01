//package dbmsProject;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.plaf.DimensionUIResource;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static java.lang.System.exit;
import static java.lang.System.out;


class ConnectionProvidr {
    Connection con;

    public Connection createCon() {
        try {
            // loading driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            String user = "root";
            String password = "root";
            String url = "jdbc:mysql://localhost:3306/dbms_project";
            con = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            System.out.println(e);
        }
        return con;
    }
}

class Find extends JFrame implements ActionListener {
    Connection con=null;
    JPanel west;
    JPanel north;
    JPanel south;
    JPanel east;
    JComboBox cStartYear;

    JComboBox cEndYear;
    JComboBox cStartPrice;
    JComboBox cEndPrice;
    JComboBox cSellRent;
    JComboBox cSoldRented;
    JComboBox cLocation;
    JComboBox cZip;
    JComboBox cMinBedrooms;
    JComboBox cYear;

    JComboBox cAgent;


   JLabel lStartYear;
   JLabel lEndYear;
   JLabel lStartPrice;
   JLabel lEndPrice;
   JLabel lLocation;
   JLabel lZip;
   JLabel lMinBedrooms;
   JLabel lYear;

   JLabel lAgent;

   JButton close;
   JButton search;

   ArrayList<String> listDate;
   String s[];

   JPanel center;
   JPanel table;
   JPanel calculate;
   JLabel averagePrice;
   JLabel minPrice;
   JLabel maxPrice;
   JLabel averageTime;
   JLabel agentName;
   JTextField tAveragePrice;
   JTextField tMinPrice;
   JTextField tMaxPrice;
   JTextField tAverageTime;
   JTextField tAgentName;

   int noOfButtons;
   JButton find[];
   JButton refresh;

   JTable dataTable;
   DefaultTableModel model;
   JScrollPane scrollPane;
   ResultSetMetaData rsetMetaData;
   String tableData[][];
   String tableAttribute[];
   int rowCount;
   int columnCount;

   JCheckBox checkAddress;


    Find()
    {
        listDate=new ArrayList();
        Font font=new Font("Arial",Font.BOLD,13);

        setTitle("Get details");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
        setLocationRelativeTo(null);
        Container container=getContentPane();
        container.setLayout(new BorderLayout());
        center=new JPanel(new FlowLayout());
        table=new JPanel();
        table.setBackground(new Color(196, 250, 135));
        center.add(table);
        calculate=new JPanel(new GridLayout(19,3,0,10));
        calculate.setBackground(new Color(228, 250, 140));
        center.add(calculate);
        north = new JPanel();
        north.setBackground(new Color(168, 91, 240));
        north.setPreferredSize(new Dimension(JFrame.WIDTH,40));


        //SELL OR RENT
        String[] sellRent = new String[2];
        sellRent[0] = "Sell";
        sellRent[1] = "Rent";
        cSellRent = new JComboBox(sellRent);
        cSellRent.setBackground(new Color(168, 91, 240));
        cSellRent.setForeground(Color.WHITE);
        cSellRent.setSelectedIndex(0);
        cSellRent.addActionListener(this);
        north.add(cSellRent);

        //SOLD OR RENTED

        String[] soldRented = new String[2];
        soldRented[0] = "Sold";
        soldRented[1] = "Rented";
        cSoldRented = new JComboBox(soldRented);
        cSoldRented.setBackground(new Color(168, 91, 240));
        cSoldRented.setForeground(Color.WHITE);
        cSoldRented.setSelectedIndex(0);
        cSoldRented.addActionListener(this);
        north.add(cSoldRented);


        //YEAR
        s=new String[1];
        s[0]="Select";
        cStartYear = new JComboBox(s);
        cStartYear.setBackground(new Color(168, 91, 240));
        cStartYear.setForeground(Color.WHITE);
        cStartYear.setSelectedIndex(0);
        cEndYear = new JComboBox(s);
        cEndYear.setBackground(new Color(168, 91, 240));
        cEndYear.setForeground(Color.WHITE);
        cEndYear.setSelectedIndex(0);
        lStartYear = new JLabel("Min Year");
        lStartYear.setFont(font);
        north.add(lStartYear);
        north.add(cStartYear);
        lEndYear = new JLabel("Max Year");
        lEndYear.setFont(font);
        north.add(lEndYear);
        north.add(cEndYear);




        //PRICE

        cStartPrice = new JComboBox(s);
        cStartPrice.setBackground(new Color(168, 91, 240));
        cStartPrice.setForeground(Color.WHITE);
        cStartPrice.setSelectedIndex(0);
        cEndPrice = new JComboBox(s);
        cEndPrice.setBackground(new Color(168, 91, 240));
        cEndPrice.setForeground(Color.WHITE);
        cEndPrice.setSelectedIndex(0);
        lStartPrice = new JLabel("Min Price");
        lStartPrice.setFont(font);
        north.add(lStartPrice);
        north.add(cStartPrice);
        lEndPrice = new JLabel("Max Price");
        lEndPrice.setFont(font);
        north.add(lEndPrice);
        north.add(cEndPrice);

        //Address

        String query;
        cZip=new JComboBox(s);
        cZip.setBackground(new Color(168, 91, 240));
        cZip.setForeground(Color.WHITE);
        cZip.setSelectedIndex(0);
        lZip=new JLabel("ZIP");
        lZip.setFont(font);
        north.add(lZip);
        north.add(cZip);
        query="select distinct zip from address order by zip asc";
        getZIP(query);
        cZip.addActionListener(this);


        cLocation=new JComboBox(s);
        cLocation.setBackground(new Color(168, 91, 240));
        cLocation.setForeground(Color.WHITE);
        cLocation.setSelectedIndex(0);
        lLocation=new JLabel("Location");
        lLocation.setFont(font);
        north.add(lLocation);
        north.add(cLocation);
        query="select distinct location from address order by location asc";
        getLocation(query);
        cLocation.addActionListener(this);

        cMinBedrooms=new JComboBox(s);
        cMinBedrooms.setBackground(new Color(168, 91, 240));
        cMinBedrooms.setForeground(Color.WHITE);
        cMinBedrooms.setSelectedIndex(0);
        lMinBedrooms=new JLabel("Min Bedrooms");
        lMinBedrooms.setFont(font);
        north.add(lMinBedrooms);
        north.add(cMinBedrooms);
        cMinBedrooms.addActionListener(this);

        cYear=new JComboBox(s);
        cYear.setBackground(new Color(168, 91, 240));
        cYear.setForeground(Color.WHITE);
        cYear.setSelectedIndex(0);
        lYear=new JLabel("Year");
        lYear.setFont(font);
        north.add(lYear);
        north.add(cYear);
        cYear.addActionListener(this);


        cAgent=new JComboBox(s);
        cAgent.setBackground(new Color(168, 91, 240));
        cAgent.setForeground(Color.WHITE);
        cAgent.setSelectedIndex(0);
        lAgent=new JLabel("Agent");
        lAgent.setFont(font);
        north.add(lAgent);
        north.add(cAgent);
        query="select distinct aid from agent order by aid asc";
        getAgent(query);
        cAgent.addActionListener(this);


        south=new JPanel();
        south.setPreferredSize(new DimensionUIResource(getWidth(),80));
        south.setBorder(BorderFactory.createEmptyBorder(20,0,0,0));
        south.setBackground(new Color(133, 126, 140));
        close=new JButton("Close");
        close.addActionListener(this);
        south.add(close);

        search=new JButton("Search");
        south.add(search);
        search.addActionListener(this);

        checkAddress=new JCheckBox("Address",false);
        south.add(checkAddress);


        //table and calculation
        noOfButtons=5;
        find=new JButton[noOfButtons];
        int i;
        for(i=0;i<noOfButtons;i++)
        {
            find[i]=new JButton("Find");
        }
        averagePrice=new JLabel("Average Price");
        calculate.add(averagePrice);
        tAveragePrice=new JTextField();
        tAveragePrice.setEditable(false);
        calculate.add(tAveragePrice);
        calculate.add(find[0]);
        averagePrice.setFont(font);
        find[0].addActionListener(this);

        query="select distinct bedrooms from sale_property order by bedrooms asc";
        getBedrooms(query);

        minPrice=new JLabel("Min Price");
        calculate.add(minPrice);
        tMinPrice=new JTextField();
        tMinPrice.setEditable(false);
        calculate.add(tMinPrice);
        //find[1].setPreferredSize(new DimensionUIResource(15, tMinPrice.getHeight()));
        find[1].setBorder(BorderFactory.createEmptyBorder(0,15,0,0));
        calculate.add(find[1]);
        minPrice.setFont(font);
        find[1].addActionListener(this);


        maxPrice=new JLabel("Max Price");
        calculate.add(maxPrice);
        tMaxPrice=new JTextField();
        tMaxPrice.setEditable(false);
        calculate.add(tMaxPrice);
        calculate.add(find[2]);
        maxPrice.setFont(font);
        find[2].addActionListener(this);

        averageTime=new JLabel("Average Time");
        calculate.add(averageTime);
        tAverageTime=new JTextField();
        tAverageTime.setEditable(false);
        calculate.add(tAverageTime);
        calculate.add(find[3]);
        averageTime.setFont(font);
        find[3].addActionListener(this);

        agentName=new JLabel("Agent's Name");
        calculate.add(agentName);
        tAgentName=new JTextField();
        tAgentName.setEditable(false);
        calculate.add(tAgentName);
        calculate.add(find[4]);
        agentName.setFont(font);
        find[4].addActionListener(this);

        JLabel j[]=new JLabel[42];
        for(i=0;i<42;i++)
        {
            j[i]=new JLabel();
            calculate.add(j[i]);
        }

        refresh=new JButton("Refresh");
        refresh.addActionListener(this);
        south.add(refresh);



        table.setPreferredSize(new Dimension(1000,getHeight()));
        calculate.setPreferredSize(new Dimension((getWidth()-1050),getHeight()));
        calculate.setBorder(BorderFactory.createEmptyBorder(10,15,0,15));
        container.add(north,BorderLayout.NORTH);
        container.add(south,BorderLayout.SOUTH);
        container.add(center,BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==close)
        {
            try{
                if(con!=null)
                {
                    con.close();

                }

            }catch (Exception e1)
            {
                JOptionPane.showMessageDialog(null,"Unable to close connection");
            }

            this.dispose();
        }


        if(e.getSource()==cSellRent)
        {
            if(cSellRent.getSelectedIndex()==0)
            {
                lStartPrice.setText("Min Price(BHK)");
                lEndPrice.setText(("Max Price(BHK)"));
                averagePrice.setText("Average Price(BHK)");
                minPrice.setText("Min Price(BHK)");
                maxPrice.setText("Max price(BHK)");
                cSoldRented.setEnabled(false);

                String query="select distinct year_of_construction from sale_property order by year_of_construction asc";
                getYear(query);
                query="select distinct selling_price from sale_property order by selling_price asc";
                getPrice(query);
                query="select distinct bedrooms from sale_property order by bedrooms asc";
                getBedrooms(query);

            }
            if(cSellRent.getSelectedIndex()==1)
            {
                lStartPrice.setText("Min Price(Per Month)");
                lEndPrice.setText(("Max Price(Per Month)"));
                averagePrice.setText("Average Price(Per Month)");
                minPrice.setText("Min Price(Per Month)");
                maxPrice.setText("Max price(Per Month)");
                cSoldRented.setEnabled(false);

                String query="select distinct year_of_construction from rent_property order by year_of_construction asc";
                getYear(query);
                query="select distinct monthly_rent from rent_property order by monthly_rent asc";
                getPrice(query);
                query="select distinct bedrooms from rent_property order by bedrooms asc";
                getBedrooms(query);
            }
        }

        if(e.getSource()==cSoldRented)
        {
            if(cSoldRented.getSelectedIndex()==0)
            {
                String query;
                lStartPrice.setText("Min Price(BHK)");
                lEndPrice.setText(("Max Price(BHK)"));
                averagePrice.setText("Average Price(BHK)");
                minPrice.setText("Min Price(BHK)");
                maxPrice.setText("Max price(BHK)");
                query="select distinct year_of_construction from sale_property where cid is not null order by year_of_construction asc";
                getYear(query);
                query="select distinct selling_price from sale_property where cid is not null order by selling_price asc";
                getPrice(query);
                query="select distinct bedrooms from sale_property where cid is not null order by bedrooms asc";
                getBedrooms(query);
                query="select distinct year(date_of_sell) as years from selling_dates order by years asc";
                getYear(query);
                cSellRent.setEnabled(false);

            }

            if(cSoldRented.getSelectedIndex()==1)
            {
                String query;
                lStartPrice.setText("Min Price(Per Month)");
                lEndPrice.setText(("Max Price(Per Month)"));
                averagePrice.setText("Average Price(Per Month)");
                minPrice.setText("Min Price(Per Month)");
                maxPrice.setText("Max price(Per Month)");
                query="select distinct year_of_construction from rent_property where cid is not null order by year_of_construction asc";
                getYear(query);
                query="select distinct monthly_rent from rent_property where cid is not null order by monthly_rent asc";
                getPrice(query);
                query="select distinct bedrooms from rent_property where cid is not null order by bedrooms asc";
                getBedrooms(query);
                query="select distinct year(date_of_rent) as years from rent_dates order by years asc";
                getYear(query);
                cSellRent.setEnabled(false);
            }
        }

        if(e.getSource()==cZip)
        {
            if(cZip.getSelectedIndex()!=0)
            {
                String query="select distinct location from address where zip='"+cZip.getSelectedItem()+"'";
                getLocation(query);

            }
            if(cZip.getSelectedIndex()==0)
            {
                String query="select distinct location from address order by location asc";
                getLocation(query);

            }
        }

        if(e.getSource()==cMinBedrooms)
        {

        }

        if(e.getSource()==search)
        {
            int startPrice=0;
            int endPrice=0;
            int startYear=0;
            int endYear=0;
            String location="";
            int NoOfBedrooms=0;
            String query;

            if(cSellRent.isEnabled() && cSoldRented.isEnabled())
            {
                cSoldRented.setEnabled(false);
            }
            if(cSellRent.getSelectedIndex()==0 && cSellRent.isEnabled())
            {
                setTitle("Available properties to sell");
                boolean isTrue=true;
                try
                {
                    startPrice=Integer.parseInt(cStartPrice.getSelectedItem().toString());
                    endPrice=Integer.parseInt(cEndPrice.getSelectedItem().toString());
                    try
                    {
                        startYear=Integer.parseInt(cStartYear.getSelectedItem().toString());
                        endYear=Integer.parseInt(cEndYear.getSelectedItem().toString());
                        query="select pid,selling_price, area ,bedrooms ,year_of_construction from sale_property where cid is null and selling_price>= "+startPrice+" and selling_price<="+endPrice+" and year_of_construction>="+startYear+" and year_of_construction<="+endYear;
                        showData(query);
                        isTrue=false;

                    }catch (Exception e2)
                    {

                    }

                    if(isTrue)
                    {
                        if(checkAddress.isSelected())
                        {
                            if(cLocation.getSelectedIndex()!=0)
                            {
                                if(cMinBedrooms.getSelectedIndex()!=0)
                                {
                                    try
                                    {
                                        location=cLocation.getSelectedItem().toString();
                                        NoOfBedrooms=Integer.parseInt(cMinBedrooms.getSelectedItem().toString());
                                        query="select *from address where location='"+location+"' and pid in(select pid from sale_property where cid is null and selling_price>= "+startPrice+" and selling_price<="+endPrice+"  and bedrooms>="+NoOfBedrooms+" )";
                                        showData(query);
                                    }catch (Exception e1)
                                    {

                                    }
                                }else

                                if(cMinBedrooms.getSelectedIndex()==0)
                                {
                                    try
                                    {
                                        location=cLocation.getSelectedItem().toString();
                                        query="select *from address where location='"+location+"' and pid in(select pid from sale_property where cid is null and selling_price>= "+startPrice+" and selling_price<="+endPrice+" )";
                                        showData(query);
                                    }catch (Exception e1)
                                    {

                                    }

                                }
                            }
                            else
                            {
                                query="select *from address where pid in(select pid from sale_property where cid is null and selling_price>= "+startPrice+" and selling_price<="+endPrice+" )";
                                showData(query);
                            }
                        }
                        else {

                            if(cLocation.getSelectedIndex()!=0)
                            {

                                if(cMinBedrooms.getSelectedIndex()!=0)
                                {
                                    try
                                    {
                                        NoOfBedrooms=Integer.parseInt(cMinBedrooms.getSelectedItem().toString());
                                        location=cLocation.getSelectedItem().toString();
                                        query="select  pid ,selling_price, area, bedrooms, year_of_construction from sale_property where cid is null and bedrooms>="+NoOfBedrooms+" and selling_price>= "+startPrice+" and selling_price<="+endPrice+" and pid in(select pid from address where location='"+location+"' )";
                                        showData(query);

                                    }catch (Exception e1)
                                    {

                                    }
                                }
                                else
                                {
                                    location=cLocation.getSelectedItem().toString();
                                    query="select  pid ,selling_price, area, bedrooms, year_of_construction from sale_property where cid is null and selling_price>= "+startPrice+" and selling_price<="+endPrice+" and pid in(select pid from address where location='"+location+"' )";
                                    showData(query);
                                }

                            }
                            else
                            {
                                if(cMinBedrooms.getSelectedIndex()!=0)
                                {
                                    try
                                    {
                                        NoOfBedrooms=Integer.parseInt(cMinBedrooms.getSelectedItem().toString());
                                        query="select  pid ,selling_price, area, bedrooms, year_of_construction from sale_property where cid is null and bedrooms>="+NoOfBedrooms+" and selling_price>= "+startPrice+" and selling_price<="+endPrice;
                                        showData(query);

                                    }catch (Exception e1)
                                    {

                                    }
                                }
                                else
                                {
                                    query="select  pid ,selling_price, area, bedrooms, year_of_construction from sale_property where cid is null and selling_price>= "+startPrice+" and selling_price<="+endPrice;
                                    showData(query);
                                }
                            }
                        }

                    }

                }catch (Exception e1)
                {
                    JOptionPane.showMessageDialog(null,"Please select price from above");
                }


            }
            if(cSellRent.getSelectedIndex()==1 && cSellRent.isEnabled())
            {
                setTitle("Available properties to rent");
                boolean isTrue=true;
                try
                {
                    startPrice=Integer.parseInt(cStartPrice.getSelectedItem().toString());
                    endPrice=Integer.parseInt(cEndPrice.getSelectedItem().toString());
                    try
                    {
                        startYear=Integer.parseInt(cStartYear.getSelectedItem().toString());
                        endYear=Integer.parseInt(cEndYear.getSelectedItem().toString());
                        query="select  pid ,monthly_rent, area, bedrooms, year_of_construction from rent_property where cid is null and monthly_rent>= "+startPrice+" and monthly_rent<="+endPrice+" and year_of_construction>="+startYear+" and year_of_construction<="+endYear;
                        showData(query);
                        isTrue=false;

                    }catch (Exception e2)
                    {

                    }
                   if(isTrue)
                    {
                        if(checkAddress.isSelected())
                        {
                            if(cLocation.getSelectedIndex()!=0)
                            {
                                if(cMinBedrooms.getSelectedIndex()!=0)
                                {
                                    try
                                    {
                                        location=cLocation.getSelectedItem().toString();
                                        NoOfBedrooms=Integer.parseInt(cMinBedrooms.getSelectedItem().toString());
                                        query="select *from address where location='"+location+"' and pid in(select pid from rent_property where cid is null and monthly_rent>= "+startPrice+" and monthly_rent<="+endPrice+"  and bedrooms>="+NoOfBedrooms+" )";
                                        showData(query);
                                    }catch (Exception e1)
                                    {

                                    }
                                }else

                                    if(cMinBedrooms.getSelectedIndex()==0)
                                {
                                    try
                                    {
                                        location=cLocation.getSelectedItem().toString();
                                        query="select *from address where location='"+location+"' and pid in(select pid from rent_property where cid is null and monthly_rent>= "+startPrice+" and monthly_rent<="+endPrice+" )";
                                        showData(query);
                                    }catch (Exception e1)
                                    {

                                    }

                                }
                            }
                            else
                            {
                                query="select *from address where pid in(select pid from rent_property where cid is null and monthly_rent>= "+startPrice+" and monthly_rent<="+endPrice+" )";
                                showData(query);
                            }
                        }
                        else {

                            if(cLocation.getSelectedIndex()!=0)
                            {

                                if(cMinBedrooms.getSelectedIndex()!=0)
                                {
                                    try
                                    {
                                        NoOfBedrooms=Integer.parseInt(cMinBedrooms.getSelectedItem().toString());
                                        location=cLocation.getSelectedItem().toString();
                                        query="select  pid ,monthly_rent, area, bedrooms, year_of_construction from rent_property where cid is null and bedrooms>="+NoOfBedrooms+" and monthly_rent>= "+startPrice+" and monthly_rent<="+endPrice+" and pid in(select pid from address where location='"+location+"' )";
                                        showData(query);

                                    }catch (Exception e1)
                                    {

                                    }
                                }
                                else
                                {
                                    location=cLocation.getSelectedItem().toString();
                                    query="select  pid ,monthly_rent, area, bedrooms, year_of_construction from rent_property where cid is null and monthly_rent>= "+startPrice+" and monthly_rent<="+endPrice+" and pid in(select pid from address where location='"+location+"' )";
                                    showData(query);
                                }

                            }
                            else
                            {
                                if(cMinBedrooms.getSelectedIndex()!=0)
                                {
                                    try
                                    {
                                        NoOfBedrooms=Integer.parseInt(cMinBedrooms.getSelectedItem().toString());
                                        query="select  pid ,monthly_rent, area, bedrooms, year_of_construction from rent_property where cid is null and bedrooms>="+NoOfBedrooms+" and monthly_rent>= "+startPrice+" and monthly_rent<="+endPrice;
                                        showData(query);

                                    }catch (Exception e1)
                                    {

                                    }
                                }
                                else
                                {
                                    query="select  pid ,monthly_rent, area, bedrooms, year_of_construction from rent_property where cid is null and monthly_rent>= "+startPrice+" and monthly_rent<="+endPrice;
                                    showData(query);
                                }
                            }
                        }
                    }
                }catch (Exception e1)
                {
                    JOptionPane.showMessageDialog(null,"Please select price  from above");
                    System.out.println(e1);
                }


            }
            if(cSoldRented.getSelectedIndex()==0 && cSoldRented.isEnabled())
            {
                setTitle("Sold properties");
                boolean isTrue=true;
                try
                {
                    startPrice=Integer.parseInt(cStartPrice.getSelectedItem().toString());
                    endPrice=Integer.parseInt(cEndPrice.getSelectedItem().toString());
                    try
                    {
                        startYear=Integer.parseInt(cStartYear.getSelectedItem().toString());
                        endYear=Integer.parseInt(cEndYear.getSelectedItem().toString());
                        query="select * from sale_property where cid is not null and selling_price>= "+startPrice+" and selling_price<="+endPrice+" and year_of_construction>="+startYear+" and year_of_construction<="+endYear;
                        showData(query);
                        isTrue=false;

                    }catch (Exception e2)
                    {

                    }

                    if(isTrue)
                    {
                        if(checkAddress.isSelected())
                        {
                            if(cLocation.getSelectedIndex()!=0)
                            {
                                if(cMinBedrooms.getSelectedIndex()!=0)
                                {
                                    try
                                    {
                                        location=cLocation.getSelectedItem().toString();
                                        NoOfBedrooms=Integer.parseInt(cMinBedrooms.getSelectedItem().toString());
                                        query="select *from address where location='"+location+"' and pid in(select pid from sale_property where cid is not null and selling_price>= "+startPrice+" and selling_price<="+endPrice+"  and bedrooms>="+NoOfBedrooms+" )";
                                        showData(query);
                                    }catch (Exception e1)
                                    {

                                    }
                                }else

                                if(cMinBedrooms.getSelectedIndex()==0)
                                {
                                    try
                                    {
                                        location=cLocation.getSelectedItem().toString();
                                        query="select *from address where location='"+location+"' and pid in(select pid from sale_property where cid is not null and selling_price>= "+startPrice+" and selling_price<="+endPrice+" )";
                                        showData(query);
                                    }catch (Exception e1)
                                    {

                                    }

                                }
                            }
                            else
                            {
                                if(cMinBedrooms.getSelectedIndex()!=0)
                                {
                                    try
                                    {
                                        NoOfBedrooms=Integer.parseInt(cMinBedrooms.getSelectedItem().toString());
                                        query="select *from address where pid in(select pid from sale_property where cid is not null and selling_price>= "+startPrice+" and selling_price<="+endPrice+" and bedrooms>="+NoOfBedrooms+" )";
                                        showData(query);

                                    }catch (Exception e1)
                                    {

                                    }
                                }else {
                                    query="select *from address where pid in(select pid from sale_property where cid is not null and selling_price>= "+startPrice+" and selling_price<="+endPrice+" )";
                                    showData(query);
                                }
                            }
                        }
                        else {

                            if(cLocation.getSelectedIndex()!=0)
                            {

                                if(cMinBedrooms.getSelectedIndex()!=0)
                                {
                                    try
                                    {
                                        NoOfBedrooms=Integer.parseInt(cMinBedrooms.getSelectedItem().toString());
                                        location=cLocation.getSelectedItem().toString();
                                        query="select  pid ,selling_price, area, bedrooms, year_of_construction from sale_property where cid is not null and bedrooms>="+NoOfBedrooms+" and selling_price>= "+startPrice+" and selling_price<="+endPrice+" and pid in(select pid from address where location='"+location+"' )";
                                        showData(query);

                                    }catch (Exception e1)
                                    {

                                    }
                                }
                                else
                                {
                                    location=cLocation.getSelectedItem().toString();
                                    query="select  pid ,selling_price, area, bedrooms, year_of_construction from sale_property where cid is not null and selling_price>= "+startPrice+" and selling_price<="+endPrice+" and pid in(select pid from address where location='"+location+"' )";
                                    showData(query);
                                }

                            }
                            else
                            {
                                if(cMinBedrooms.getSelectedIndex()!=0)
                                {
                                    try
                                    {
                                        NoOfBedrooms=Integer.parseInt(cMinBedrooms.getSelectedItem().toString());
                                        query="select  pid ,selling_price, area, bedrooms, year_of_construction from sale_property where cid is not null and bedrooms>="+NoOfBedrooms+" and selling_price>= "+startPrice+" and selling_price<="+endPrice;
                                        showData(query);

                                    }catch (Exception e1)
                                    {

                                    }
                                }
                                else
                                {
                                    query="select  pid ,selling_price, area, bedrooms, year_of_construction from sale_property where cid is not null and selling_price>= "+startPrice+" and selling_price<="+endPrice;
                                    showData(query);
                                }
                            }
                        }
                    }

                }catch (Exception e1)
                {
                    JOptionPane.showMessageDialog(null,"Please select price from above");
                }

            }
            if(cSoldRented.getSelectedIndex()==1 && cSoldRented.isEnabled())
            {
                setTitle("Rented properties");
                boolean isTrue=true;
                try
                {
                    startPrice=Integer.parseInt(cStartPrice.getSelectedItem().toString());
                    endPrice=Integer.parseInt(cEndPrice.getSelectedItem().toString());
                    try
                    {
                        startYear=Integer.parseInt(cStartYear.getSelectedItem().toString());
                        endYear=Integer.parseInt(cEndYear.getSelectedItem().toString());
                        query="select  *from rent_property where cid is not null and monthly_rent>= "+startPrice+" and monthly_rent<="+endPrice+" and year_of_construction>="+startYear+" and year_of_construction<="+endYear;
                        showData(query);
                        isTrue=false;

                    }catch (Exception e2)
                    {

                    }
                    if(isTrue)
                    {
                        if(checkAddress.isSelected())
                        {
                            if(cLocation.getSelectedIndex()!=0)
                            {
                                if(cMinBedrooms.getSelectedIndex()!=0)
                                {
                                    try
                                    {
                                        location=cLocation.getSelectedItem().toString();
                                        NoOfBedrooms=Integer.parseInt(cMinBedrooms.getSelectedItem().toString());
                                        query="select *from address where location='"+location+"' and pid in(select pid from rent_property where cid is not null and monthly_rent>= "+startPrice+" and monthly_rent<="+endPrice+"  and bedrooms>="+NoOfBedrooms+" )";
                                        showData(query);
                                    }catch (Exception e1)
                                    {

                                    }
                                }else

                                if(cMinBedrooms.getSelectedIndex()==0)
                                {
                                    try
                                    {
                                        location=cLocation.getSelectedItem().toString();
                                        query="select *from address where location='"+location+"' and pid in(select pid from rent_property where cid is not null and monthly_rent>= "+startPrice+" and monthly_rent<="+endPrice+" )";
                                        showData(query);
                                    }catch (Exception e1)
                                    {

                                    }

                                }
                            }
                            else
                            {
                               if(cMinBedrooms.getSelectedIndex()!=0)
                               {
                                   try
                                   {
                                       NoOfBedrooms=Integer.parseInt(cMinBedrooms.getSelectedItem().toString());
                                       query="select *from address where pid in(select pid from rent_property where cid is not null and monthly_rent>= "+startPrice+" and monthly_rent<="+endPrice+" and bedrooms>="+NoOfBedrooms+" )";
                                       showData(query);

                                   }catch (Exception e1)
                                   {

                                   }

                               }else {
                                   query="select *from address where pid in(select pid from rent_property where cid is not null and monthly_rent>= "+startPrice+" and monthly_rent<="+endPrice+" )";
                                   showData(query);
                               }
                            }
                        }
                        else {

                            if(cLocation.getSelectedIndex()!=0)
                            {

                                if(cMinBedrooms.getSelectedIndex()!=0)
                                {
                                    try
                                    {
                                        NoOfBedrooms=Integer.parseInt(cMinBedrooms.getSelectedItem().toString());
                                        location=cLocation.getSelectedItem().toString();
                                        query="select  pid ,monthly_rent, area, bedrooms, year_of_construction from rent_property where cid is not null and bedrooms>="+NoOfBedrooms+" and monthly_rent>= "+startPrice+" and monthly_rent<="+endPrice+" and pid in(select pid from address where location='"+location+"' )";
                                        showData(query);

                                    }catch (Exception e1)
                                    {

                                    }
                                }
                                else
                                {
                                    location=cLocation.getSelectedItem().toString();
                                    query="select  pid ,monthly_rent, area, bedrooms, year_of_construction from rent_property where cid is not null and monthly_rent>= "+startPrice+" and monthly_rent<="+endPrice+" and pid in(select pid from address where location='"+location+"' )";
                                    showData(query);
                                }

                            }
                            else
                            {
                                if(cMinBedrooms.getSelectedIndex()!=0)
                                {
                                    try
                                    {
                                        NoOfBedrooms=Integer.parseInt(cMinBedrooms.getSelectedItem().toString());
                                        query="select  pid ,monthly_rent, area, bedrooms, year_of_construction from rent_property where cid is not null and bedrooms>="+NoOfBedrooms+" and monthly_rent>= "+startPrice+" and monthly_rent<="+endPrice;
                                        showData(query);

                                    }catch (Exception e1)
                                    {

                                    }
                                }
                                else
                                {
                                    query="select  pid ,monthly_rent, area, bedrooms, year_of_construction from rent_property where cid is not null and monthly_rent>= "+startPrice+" and monthly_rent<="+endPrice;
                                    showData(query);
                                }
                            }
                        }
                    }
                }catch (Exception e1)
                {
                    JOptionPane.showMessageDialog(null,"Please select price from above");
                }

            }



        }

        if(e.getSource()==find[0])
        {
            String query;
            if(cSoldRented.getSelectedIndex()==0)
            {
                int aid=0;
                try
                {
                    aid=Integer.parseInt(cAgent.getSelectedItem().toString());
                    query="select avg(selling_price) from sale_property natural join sold where cid is not null and aid="+aid;
                    averagePrice(query);
                }catch (Exception e1)
                {
                    JOptionPane.showMessageDialog(null,"Please select agent");
                }


            }
            if(cSoldRented.getSelectedIndex()==1)
            {
                int aid=0;
                try
                {
                    aid=Integer.parseInt(cAgent.getSelectedItem().toString());
                    query="select avg(monthly_rent) from rent_property natural join rented where cid is not null and aid="+aid;
                    averagePrice(query);
                }catch (Exception e1)
                {
                    JOptionPane.showMessageDialog(null,"Please select agent");
                }

            }
        }
        if(e.getSource()==find[1])
        {
            String query;
           if(cSoldRented.getSelectedIndex()==0)
           {
               int aid=0;
               try
               {
                   aid=Integer.parseInt(cAgent.getSelectedItem().toString());
                   query="select min(selling_price) from sale_property natural join sold where cid is not null and aid="+aid;
                   minPrice(query);
               }catch (Exception e1)
               {
                   JOptionPane.showMessageDialog(null,"Please select agent");
               }


           }
           if(cSoldRented.getSelectedIndex()==1)
           {
               int aid=0;
               try
               {
                   aid=Integer.parseInt(cAgent.getSelectedItem().toString());
                   query="select min(monthly_rent) from rent_property natural join rented where cid is not null and aid="+aid;
                   minPrice(query);
               }catch (Exception e1)
               {
                   JOptionPane.showMessageDialog(null,"Please select agent");
               }

           }

        }
        if(e.getSource()==find[2])
        {
            String query;
            if(cSoldRented.getSelectedIndex()==0)
            {
                int aid=0;
                try
                {
                    aid=Integer.parseInt(cAgent.getSelectedItem().toString());
                    query="select max(selling_price) from sale_property natural join sold where cid is not null and aid="+aid;
                    maxPrice(query);
                }catch (Exception e1)
                {
                    JOptionPane.showMessageDialog(null,"Please select agent");
                }


            }
            if(cSoldRented.getSelectedIndex()==1)
            {
                int aid=0;
                try
                {
                    aid=Integer.parseInt(cAgent.getSelectedItem().toString());
                    query="select max(monthly_rent) from rent_property natural join rented where cid is not null and aid="+aid;
                    maxPrice(query);
                }catch (Exception e1)
                {
                    JOptionPane.showMessageDialog(null,"Please select agent");
                }

            }

        }

        if(e.getSource()==find[3])
        {
            String query;
            if(cSoldRented.getSelectedIndex()==0)
            {
                int aid=0;
                try
                {
                    aid=Integer.parseInt(cAgent.getSelectedItem().toString());
                    query="with details(aid,aname,avg_price,avg_time) as (select aid,name,avg(selling_price), datediff(date_of_sell,date_of_availability) from sold natural join agent natural join selling_dates natural join sale_property group by aid,name,date_of_sell,date_of_availability) select avg_time from details where aid="+aid;
                    averageTime(query);
                }catch (Exception e1)
                {
                    JOptionPane.showMessageDialog(null,"Please select agent");
                }


            }
            if(cSoldRented.getSelectedIndex()==1)
            {
                int aid=0;
                try
                {
                    aid=Integer.parseInt(cAgent.getSelectedItem().toString());
                    query="with details(aid,aname,avg_price,avg_time) as (select aid,name,avg(monthly_rent), datediff(date_of_rent,date_of_availability) from rented natural join agent natural join rent_date natural join rent_property group by aid,name,date_of_rent,date_of_availability) select avg_time from details where aid="+aid;
                    averageTime(query);
                }catch (Exception e1)
                {
                    JOptionPane.showMessageDialog(null,"Please select agent");
                }

            }

        }

        if(e.getSource()==find[4])
        {
            String query;
            boolean isTrue=true;

            if(cYear.getSelectedIndex()==0 && cAgent.getSelectedIndex()!=0 && cYear.getSelectedIndex()==0)
            {
                int aid=0;
                try
                {
                    aid=Integer.parseInt(cAgent.getSelectedItem().toString());
                    query="select name from agent where aid="+aid;
                    agentName(query);
                    isTrue=false;
                }catch (Exception e1)
                {

                }
            }

            if(isTrue)
            {
                if(cSoldRented.getSelectedIndex()==0 && cYear.getSelectedIndex()!=0 && cAgent.getSelectedIndex()!=0)
                {
                    int year=0;

                    try
                    {
                        year=Integer.parseInt(cYear.getSelectedItem().toString());
                        query="with details(aid,aName,price) as (select aid,name,sum(selling_price) from agent natural join sold natural join sale_property where year_of_construction ="+year+" group by aid,name) select aName,price from details where price = (select max(price) from details);";
                        agentName(query);
                    }catch (Exception e1)
                    {
                        JOptionPane.showMessageDialog(null,"Please select agent");
                    }


                }
                if(cSoldRented.getSelectedIndex()==1 && cYear.getSelectedIndex()!=0 && cAgent.getSelectedIndex()!=0)
                {
                    int year=0;

                    try
                    {
                        year=Integer.parseInt(cYear.getSelectedItem().toString());
                        query="with details(aid,aName,price) as (select aid,name,sum(monthly_rent) from agent natural join sold natural join rent_property where year_of_construction ="+year+" group by aid,name) select aName,price from details where price = (select max(price) from details);";
                        agentName(query);
                    }catch (Exception e1)
                    {
                        JOptionPane.showMessageDialog(null,"Please select agent");
                    }

                }
            }



        }


        if (e.getSource()==refresh)
        {
            cSellRent.setEnabled(true);
            cSoldRented.setEnabled(true);
            cStartYear.setSelectedIndex(0);
            cEndYear.setSelectedIndex(0);
            cStartPrice.setSelectedIndex(0);
            cEndPrice.setSelectedIndex(0);
            cZip.setSelectedIndex(0);
            cLocation.setSelectedIndex(0);
            cMinBedrooms.setSelectedIndex(0);
            cYear.setSelectedIndex(0);
            cAgent.setSelectedIndex(0);
            tAveragePrice.setText("");
            tMinPrice.setText("");
            tMaxPrice.setText("");
            tAverageTime.setText("");
        }
    }


    public void getAgent(String query)
    {
        Connection con=null;
        ResultSet rSet;
        Statement stmt;
        try{

            ConnectionProvidr conn=new ConnectionProvidr();
            con=conn.createCon();
            stmt=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rSet=stmt.executeQuery(query);
            if(rSet.next())
            {
                cAgent.removeAllItems();
                cAgent.addItem("Select");

                do{
                    cAgent.addItem(String.valueOf(rSet.getInt(1)));
                }while (rSet.next());
            }else {
                JOptionPane.showMessageDialog(null,"Agent not available");
            }


            if(con!=null)
            {
                con.close();
            }


        }catch (Exception e)
        {
            JOptionPane.showMessageDialog(null,"Network error");
        }
    }
    public void getYear(String query)
    {
        Connection con=null;
        ResultSet rSet;
        Statement stmt;
        try{

            ConnectionProvidr conn=new ConnectionProvidr();
            con=conn.createCon();
            stmt=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rSet=stmt.executeQuery(query);
            if(rSet.next())
            {
                cStartYear.removeAllItems();
                cEndYear.removeAllItems();
                cStartYear.addItem("Select");
                cEndYear.addItem("Select");
                cYear.removeAllItems();
                cYear.addItem("Select");

                do{
                    cStartYear.addItem(String.valueOf(rSet.getInt(1)));
                    cEndYear.addItem(String.valueOf(rSet.getInt(1)));
                    cYear.addItem(String.valueOf(rSet.getInt(1)));
                }while (rSet.next());
                cStartYear.setSelectedIndex(0);
                cEndYear.setSelectedIndex(0);
                cYear.setSelectedIndex(0);
            }else {
                JOptionPane.showMessageDialog(null,"Date not exist");
            }


            if(con!=null)
            {
                con.close();
            }


        }catch (Exception e)
        {
                JOptionPane.showMessageDialog(null,"Network error");
        }
    }


    public void getPrice( String query)
    {
        Connection con=null;
        ResultSet rSet;
        Statement stmt;
        try{

            ConnectionProvidr conn=new ConnectionProvidr();
            con=conn.createCon();
            stmt=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rSet=stmt.executeQuery(query);

            if(rSet.next())
            {
                cStartPrice.removeAllItems();
                cEndPrice.removeAllItems();
                cStartPrice.addItem("Select");
                cEndPrice.addItem("Select");
                do{
                    cStartPrice.addItem(String.valueOf(rSet.getInt(1)));
                    cEndPrice.addItem(String.valueOf(rSet.getInt(1)));
                }while (rSet.next());
            }else {
                JOptionPane.showMessageDialog(null,"Price not exist");
            }


            if(con!=null)
            {
                con.close();
            }


        }catch (Exception e)
        {
            JOptionPane.showMessageDialog(null,"Network error");
        }
    }



    public void getZIP(String query)
    {
        Connection con=null;
        ResultSet rSet;
        Statement stmt;
        try{

            ConnectionProvidr conn=new ConnectionProvidr();
            con=conn.createCon();
            stmt=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rSet=stmt.executeQuery(query);

            if(rSet.next())
            {
                cZip.removeAllItems();
                cZip.addItem("Select");
                cZip.setSelectedIndex(0);
                do{
                    cZip.addItem(rSet.getString(1));

                }while (rSet.next());

            }else {
                cZip.removeAllItems();
                cZip.addItem("Select");
                cZip.setSelectedIndex(0);
                JOptionPane.showMessageDialog(null,"ZIP not exist");
            }



            if(con!=null)
            {
                con.close();
            }


        }catch (Exception e)
        {
            JOptionPane.showMessageDialog(null,"Network error");
        }
    }

    public void getLocation(String query)
    {
        Connection con=null;
        ResultSet rSet;
        Statement stmt;
        try{

            ConnectionProvidr conn=new ConnectionProvidr();
            con=conn.createCon();
            stmt=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rSet=stmt.executeQuery(query);

            if(rSet.next())
            {
                cLocation.removeAllItems();
                cLocation.addItem("Select");
                cLocation.setSelectedIndex(0);

                do{
                    cLocation.addItem(rSet.getString(1));

                }while (rSet.next());



            }else {
                cLocation.removeAllItems();
                cLocation.addItem("Select");
                cLocation.setSelectedIndex(0);
                JOptionPane.showMessageDialog(null,"Location not exist");
            }



            if(con!=null)
            {
                con.close();
            }


        }catch (Exception e)
        {
            JOptionPane.showMessageDialog(null,"Network error");
        }
    }

    public void getBedrooms(String query)
    {
        Connection con=null;
        ResultSet rSet;
        Statement stmt;
        try{

            ConnectionProvidr conn=new ConnectionProvidr();
            con=conn.createCon();
            stmt=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rSet=stmt.executeQuery(query);

            if(rSet.next())
            {
                cMinBedrooms.removeAllItems();
                cMinBedrooms.addItem("Select");
                cMinBedrooms.setSelectedIndex(0);

                do{
                    cMinBedrooms.addItem(String.valueOf(rSet.getInt(1)));
                }while (rSet.next());

            }else {
                cMinBedrooms.removeAllItems();
                cMinBedrooms.addItem("Select");
                cMinBedrooms.setSelectedIndex(0);

                JOptionPane.showMessageDialog(null,"Bedroom not available");
            }


            if(con!=null)
            {
                con.close();
            }


        }catch (Exception e)
        {
            JOptionPane.showMessageDialog(null,"Network error");
            System.out.println(e);
        }
    }

    public void showData(String query)
    {
        Connection con=null;
        ResultSet rSet;
        Statement stmt;
        try{
            ConnectionProvidr conn=new ConnectionProvidr();
            con=conn.createCon();
            stmt=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rSet=stmt.executeQuery(query);
            rsetMetaData=rSet.getMetaData();
            columnCount=rsetMetaData.getColumnCount();
            rSet.last();
            rowCount=rSet.getRow();
            tableAttribute=new String[columnCount];
            tableData=new String[rowCount][columnCount];
            int i;
            rSet.beforeFirst();
            for(i=0;i<columnCount;i++)
            {
                tableAttribute[i]=rsetMetaData.getColumnName(i+1);
            }
            rSet.beforeFirst();
            int j;

            if(rSet.next())
            {
                i=0;
                do{
                    for(j=0;j<columnCount;j++)
                    {
                        try{
                            tableData[i][j]=rSet.getString(j+1);
                        }catch (Exception e1)
                        {
                            try {
                                tableData[i][j]=String.valueOf(rSet.getInt(j+1));

                            }catch (Exception e2)
                            {

                            }
                        }
                    }
                    i++;


                }while (rSet.next());

                model=new DefaultTableModel(tableData,tableAttribute);
                dataTable=new JTable(model);
                scrollPane=new JScrollPane(dataTable);
                scrollPane.setPreferredSize(new DimensionUIResource(table.getWidth(),table.getHeight()-170));
                try{
                    table.remove(0);
                }catch (Exception e1)
                {

                }
                table.add(scrollPane);
                con.close();
            }else {
                JOptionPane.showMessageDialog(null,"Empty table");
            }

        }catch (Exception e)
        {
            JOptionPane.showMessageDialog(null,"Network error");
            System.out.println(e);
        }
    }

    public void averagePrice(String query)
    {
        Connection con=null;
        ResultSet rSet;
        Statement stmt;
        try{
            ConnectionProvidr conn=new ConnectionProvidr();
            con=conn.createCon();
            stmt=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rSet=stmt.executeQuery(query);

            if(rSet.next())
            {
                tAveragePrice.setText(String.valueOf(rSet.getFloat(1)));
                con.close();
            }else {
                tAveragePrice.setText("0");
            }

        }catch (Exception e)
        {
            JOptionPane.showMessageDialog(null,"Network error");
            System.out.println(e);
        }

    }

    public void minPrice(String query)
    {
        Connection con=null;
        ResultSet rSet;
        Statement stmt;
        try{
            ConnectionProvidr conn=new ConnectionProvidr();
            con=conn.createCon();
            stmt=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rSet=stmt.executeQuery(query);

            if(rSet.next())
            {
                tMinPrice.setText(String.valueOf(rSet.getInt(1)));
                con.close();
            }else {
                tMinPrice.setText("0");
            }

        }catch (Exception e)
        {
            JOptionPane.showMessageDialog(null,"Network error");
            System.out.println(e);
        }

    }

    public void maxPrice(String query)
    {
        Connection con=null;
        ResultSet rSet;
        Statement stmt;
        try{
            ConnectionProvidr conn=new ConnectionProvidr();
            con=conn.createCon();
            stmt=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rSet=stmt.executeQuery(query);

            if(rSet.next())
            {
                tMaxPrice.setText(String.valueOf(rSet.getInt(1)));
                con.close();
            }else {
                tMaxPrice.setText("0");
            }

        }catch (Exception e)
        {
            JOptionPane.showMessageDialog(null,"Network error");
            System.out.println(e);
        }

    }

    public void averageTime(String query)
    {
        Connection con=null;
        ResultSet rSet;
        Statement stmt;
        try{
            ConnectionProvidr conn=new ConnectionProvidr();
            con=conn.createCon();
            stmt=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rSet=stmt.executeQuery(query);

            if(rSet.next())
            {
                tAverageTime.setText(String.valueOf(rSet.getFloat(1)));
                con.close();
            }else {
                tAverageTime.setText("0");
            }

        }catch (Exception e)
        {
            JOptionPane.showMessageDialog(null,"Network error");
            System.out.println(e);
        }

    }

    public void agentName(String query)
    {
        Connection con=null;
        ResultSet rSet;
        Statement stmt;
        try{
            ConnectionProvidr conn=new ConnectionProvidr();
            con=conn.createCon();
            stmt=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rSet=stmt.executeQuery(query);

            if(rSet.next())
            {
                tAgentName.setText(rSet.getString(1));
                con.close();
            }else {
                tAgentName.setText("");
                JOptionPane.showMessageDialog(null,"Agent not found");
            }

        }catch (Exception e)
        {
            JOptionPane.showMessageDialog(null,"Network error");
            System.out.println(e);
        }

    }

}

class AgentSaleReport extends JFrame implements ActionListener {
    JLabel agentId;
    JTextField tAgentId;

    JPanel tablePanel1;
    JPanel tablePanel2;
    JPanel buttonPanel;
    JButton close;
    JButton search;
    String attribute[];
    String tableData[][];
    Connection con=null;


    AgentSaleReport()
    {
        setTitle("Agent's sold Report");
        setBounds(300,100,500,600);
        setVisible(true);
        setLocationRelativeTo(null);
        Container container=getContentPane();
        container.setLayout(new BorderLayout());
        tablePanel1=new JPanel(new GridLayout(1,2,0,30));
        tablePanel1.setBackground(Color.GREEN);
        tablePanel1.setPreferredSize(new DimensionUIResource(500,100));
        agentId=new JLabel("Enter agent Id");
        tablePanel1.add(agentId);
        tAgentId=new JTextField();
        tablePanel1.setBorder(BorderFactory.createEmptyBorder(30,30,30,30));
        tablePanel1.add(tAgentId);
        tablePanel2=new JPanel();
        tablePanel2.setBackground(Color.pink);
        tablePanel2.setPreferredSize(new DimensionUIResource(500,450));
        buttonPanel=new JPanel();
        buttonPanel.setPreferredSize(new DimensionUIResource(500,50));
        buttonPanel.setBackground(Color.CYAN);
        close=new JButton("Close");
        close.addActionListener(this);
        search= new JButton("Search");
        search.addActionListener(this);
        buttonPanel.add(close);
        buttonPanel.add(search);
        container.add(buttonPanel,BorderLayout.SOUTH);
        container.add(tablePanel1,BorderLayout.NORTH);
        container.add(tablePanel2,BorderLayout.CENTER);





    }

    public  void executeQuery(int agentId)
    {
        int i,j;

        Statement stmt;
        ResultSet rset;
        String q;

        try {
            ConnectionProvidr conn = new ConnectionProvidr();
            con=null;
            stmt=null;
            try {

                con = conn.createCon();
                stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            }catch (Exception e)
            {
                JOptionPane.showMessageDialog(null,"Network error","Error",JOptionPane.ERROR_MESSAGE);
            }
            q=" select *from sale_property natural join sold natural join selling_dates where aid="+agentId;
            // System.out.println("connected");


            rset = stmt.executeQuery(q);
            ResultSetMetaData rsmd = rset.getMetaData();
            int collen = rsmd.getColumnCount();
            rset.last();
            int rowlen = rset.getRow();
            tableData = new String[rowlen][collen];
            attribute = new String[collen];

            rset.beforeFirst();
            for (i = 0; i < collen; i++) {
                attribute[i] = rsmd.getColumnName((i + 1));
            }
            i = 0;
            rset.beforeFirst();
            if(rset.next())
            {
                do {
                    for (j = 0; j < collen; j++) {
                        tableData[i][j] = rset.getString((j + 1));
                    }
                    i++;
                    System.out.println();
                }while (rset.next());

                DefaultTableModel model=new DefaultTableModel(tableData,attribute);
                JTable table=new JTable(model);
                //table.getColumnModel().getColumn(0).setPreferredWidth(20);
                JScrollPane sp=new JScrollPane(table);
                //buttonPanel.setBorder(BorderFactory.createEmptyBorder(30,0,10,0));
                //sp.setPreferredSize(new DimensionUIResource(750,600));
                try
                {
                    tablePanel2.remove(0);
                }catch (Exception e)
                {

                }
                tablePanel2.add(sp);
                validate();
            }else {
                JOptionPane.showMessageDialog(null,"Report not available for this agent");
            }





        } catch (Exception e1) {
            JOptionPane.showMessageDialog(null,"Query execute error");
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == close)
        {
            this.dispose();
            try
            {
                if(con!=null)
                {
                    con.close();
                }
            }catch (Exception e1)
            {
                JOptionPane.showMessageDialog(null,"Unable to close connection","Error",JOptionPane.ERROR_MESSAGE);
            }
        }

        if(e.getSource() == search)
        {
            int agnetId=Integer.parseInt(tAgentId.getText());
            executeQuery(agnetId);
        }

    }
}

class AgentRentReport extends AgentSaleReport
{
    AgentRentReport()
    {
        setTitle("Agent's Rented Report");

    }


    public  void executeQuery(int agentId)
    {
        int i,j;

        Statement stmt;
        ResultSet rset;
        String q;

        try {
            ConnectionProvidr conn = new ConnectionProvidr();
            con=null;
            stmt=null;
            try {

                con = conn.createCon();
                stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            }catch (Exception e)
            {
                JOptionPane.showMessageDialog(null,"Network error","Error",JOptionPane.ERROR_MESSAGE);
            }
            q=" select *from rent_property natural join rented natural join rent_dates where cid is not null and aid="+agentId;
            // System.out.println("connected");


            rset = stmt.executeQuery(q);
            ResultSetMetaData rsmd = rset.getMetaData();
            int collen = rsmd.getColumnCount();
            rset.last();
            int rowlen = rset.getRow();
            tableData = new String[rowlen][collen];
            //tab=null;
            attribute = new String[collen];

            rset.beforeFirst();
            for (i = 0; i < collen; i++) {
                attribute[i] = rsmd.getColumnName((i + 1));
                // System.out.print(col[i]);
            }
            i = 0;
            rset.beforeFirst();
           if(rset.next())
           {
               do {
                   for (j = 0; j < collen; j++) {
                       tableData[i][j] = rset.getString((j + 1));

                   }
                   i++;
                   System.out.println();
               }while (rset.next());


               DefaultTableModel model=new DefaultTableModel(tableData,attribute);
               JTable table=new JTable(model);
               //table.getColumnModel().getColumn(0).setPreferredWidth(20);
               JScrollPane sp=new JScrollPane(table);
               sp.setPreferredSize(new DimensionUIResource(400,500));
               try
               {
                   tablePanel2.remove(0);
               }catch (Exception e)
               {

               }
               tablePanel2.add(sp);
               validate();
           }else
           {
               JOptionPane.showMessageDialog(null,"Report not available for this agent");
           }


        } catch (Exception e1) {
            JOptionPane.showMessageDialog(null,"Query execute error");
        }

    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == close)
        {
            this.dispose();
            try
            {
                if(con!=null)
                {
                    con.close();
                }
            }catch (Exception e1)
            {
                JOptionPane.showMessageDialog(null,"Unable to close connection","Error",JOptionPane.ERROR_MESSAGE);
            }
        }

        if(e.getSource() == search)
        {
            int agentId=Integer.parseInt(tAgentId.getText());
            executeQuery(agentId);
        }

    }
}
class  Show extends JFrame implements ActionListener {
    JPanel tablePanel;
    JPanel buttonPanel;
    JButton close;
    Show()
    {
        setTitle("Add to DataBase");
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setExtendedState(JFrame.MAXIMIZED_BOTH);
        setBounds(300,100,500,600);
        setVisible(true);
        setLocationRelativeTo(null);
        Container container=getContentPane();
        container.setLayout(new BorderLayout());
        tablePanel=new JPanel();
        tablePanel.setBackground(new Color(204, 237, 95));
        buttonPanel=new JPanel();
        buttonPanel.setPreferredSize(new DimensionUIResource(500,100));
        buttonPanel.setBackground(new Color(204, 237, 95));
        close=new JButton("Close");
        close.addActionListener(this);
        buttonPanel.add(close);
        container.add(buttonPanel,BorderLayout.SOUTH);
        container.add(tablePanel,BorderLayout.CENTER);



    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==close)
        {
            this.dispose();
        }
    }
}

class SalesReport extends Show
{
    Connection con=null;
    String tableData[][];
    String attribute[];
    SalesReport()
    {
        setTitle("Sales Report");
        setBounds(300,100,800,600);

        int i,j;

        Statement stmt;
        ResultSet rset;
        String q;

        try {
            ConnectionProvidr conn = new ConnectionProvidr();
            con=null;
            stmt=null;
            try {

                con = conn.createCon();
                stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            }catch (Exception e)
            {
                JOptionPane.showMessageDialog(null,"Network error","Error",JOptionPane.ERROR_MESSAGE);
            }
            q="select *from sale_property where cid is not null";

            rset = stmt.executeQuery(q);
            ResultSetMetaData rsmd = rset.getMetaData();
            int collen = rsmd.getColumnCount();
            rset.last();
            int rowlen = rset.getRow();
            tableData = new String[rowlen][collen];
            //tab=null;
            attribute = new String[collen];

            rset.beforeFirst();
            for (i = 0; i < collen; i++) {
                attribute[i] = rsmd.getColumnName((i + 1));

            }
            i = 0;
            while (rset.next()) {
                for (j = 0; j < collen; j++) {
                    tableData[i][j] = rset.getString((j + 1));

                }
                i++;
                System.out.println();
            }


        } catch (Exception e1) {
            JOptionPane.showMessageDialog(null,"Query execute error","Error",JOptionPane.ERROR_MESSAGE);
        }


        // retrieving end
        DefaultTableModel model=new DefaultTableModel(tableData,attribute);
        JTable table=new JTable(model);
        table.getColumnModel().getColumn(0).setPreferredWidth(20);
        JScrollPane sp=new JScrollPane(table);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30,0,10,0));
        sp.setPreferredSize(new DimensionUIResource(750,600));
        tablePanel.add(sp);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==close)
        {
            this.dispose();
            try{
                if(con!=null)
                {
                    con.close();
                }
            }catch (Exception e2)
            {
                JOptionPane.showMessageDialog(null,"Unable to close connection","Error",JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}




class Rented extends Show
{
    Connection con=null;
    String tableData[][];
    String attribute[];
    Rented()
    {
        setTitle("Rented Properties");
        // retrieving data from database
        setBounds(300,100,800,600);

        int i,j;

        Statement stmt;
        ResultSet rset;
        String q;

        try {
            ConnectionProvidr conn = new ConnectionProvidr();
            con=null;
            stmt=null;
            try {

                con = conn.createCon();
                stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            }catch (Exception e)
            {
                JOptionPane.showMessageDialog(null,"Network error","Error",JOptionPane.ERROR_MESSAGE);
            }
            q="select *from rent_property where cid is not null;";


            rset = stmt.executeQuery(q);
            ResultSetMetaData rsmd = rset.getMetaData();
            int collen = rsmd.getColumnCount();
            rset.last();
            int rowlen = rset.getRow();
            tableData = new String[rowlen][collen];
            attribute = new String[collen];



            rset.beforeFirst();
            if(rset.next())
            {
                for (i = 0; i < collen; i++) {
                    attribute[i] = rsmd.getColumnName((i + 1));

                }
                i=0;
                do {


                    for (j = 0; j < collen; j++) {
                        tableData[i][j] = rset.getString((j + 1));

                    }
                    i++;
                }while (rset.next());

                DefaultTableModel model=new DefaultTableModel(tableData,attribute);
                JTable table=new JTable(model);
                table.getColumnModel().getColumn(0).setPreferredWidth(20);
                JScrollPane sp=new JScrollPane(table);
                buttonPanel.setBorder(BorderFactory.createEmptyBorder(30,0,10,0));
                sp.setPreferredSize(new DimensionUIResource(750,600));
                tablePanel.add(sp);
            }else
            {
                JOptionPane.showMessageDialog(null,"No properties to rent!");
            }


        } catch (Exception e1) {
            JOptionPane.showMessageDialog(null,"Query execute error","Error",JOptionPane.ERROR_MESSAGE);
        }



    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==close)
        {
            this.dispose();
            try{
                if(con!=null)
                {
                    con.close();
                }
            }catch (Exception e2)
            {
                JOptionPane.showMessageDialog(null,"Unable to close connection","Error",JOptionPane.ERROR_MESSAGE);
            }
        }
    }


}


class Sold extends Show
{
    Connection con=null;
    String tableData[][];
    String attribute[];
    Sold()
    {
        setTitle("Sold Properties");
        // retrieving data from database
        setBounds(300,100,800,600);

        int i,j;

        Statement stmt;
        ResultSet rset;
        String q;

        try {
            ConnectionProvidr conn = new ConnectionProvidr();
            con=null;
            stmt=null;
            try {

                con = conn.createCon();
                stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            }catch (Exception e)
            {
                JOptionPane.showMessageDialog(null,"Network error","Error",JOptionPane.ERROR_MESSAGE);
            }
            q="select *from sale_property where cid is not null";


            rset = stmt.executeQuery(q);
            ResultSetMetaData rsmd = rset.getMetaData();
            int collen = rsmd.getColumnCount();
            rset.last();
            int rowlen = rset.getRow();
            tableData = new String[rowlen][collen];
            attribute = new String[collen];



            rset.beforeFirst();
            if(rset.next())
            {
                for (i = 0; i < collen; i++) {
                    attribute[i] = rsmd.getColumnName((i + 1));

                }
                i=0;
                do {


                    for (j = 0; j < collen; j++) {
                        tableData[i][j] = rset.getString((j + 1));

                    }
                    i++;
                }while (rset.next());

                DefaultTableModel model=new DefaultTableModel(tableData,attribute);
                JTable table=new JTable(model);
                table.getColumnModel().getColumn(0).setPreferredWidth(20);
                JScrollPane sp=new JScrollPane(table);
                buttonPanel.setBorder(BorderFactory.createEmptyBorder(30,0,10,0));
                sp.setPreferredSize(new DimensionUIResource(750,600));
                tablePanel.add(sp);
            }else
            {
                JOptionPane.showMessageDialog(null,"No properties to sell!");
            }


        } catch (Exception e1) {
            JOptionPane.showMessageDialog(null,"Query execute error","Error",JOptionPane.ERROR_MESSAGE);
        }


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==close)
        {
            this.dispose();
            try{
                if(con!=null)
                {
                    con.close();
                }
            }catch (Exception e2)
            {
                JOptionPane.showMessageDialog(null,"Unable to close connection","Error",JOptionPane.ERROR_MESSAGE);
            }
        }
    }


}




class EditProfile extends Show
{
    EditProfile()
    {
        setTitle("Edit Profile");

    }
}

class ChangePassword extends Show
{
    ChangePassword()
    {
        setTitle("Change Password");

    }
}

class AvailableForSell extends Show
{
    String attribute[];
    String tableData[][];
    Connection con;
    AvailableForSell()
    {

        setTitle("Available Properties");
        setBounds(300,100,800,600);


        int i,j;

        Statement stmt;
        ResultSet rset;
        String q;

        try {
            ConnectionProvidr conn = new ConnectionProvidr();
            con=null;
            stmt=null;
            try {

                con = conn.createCon();
                stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            }catch (Exception e)
            {
                JOptionPane.showMessageDialog(null,"Network error","Error",JOptionPane.ERROR_MESSAGE);
            }
            q="select *from sale_property where cid IS NULL";



            rset = stmt.executeQuery(q);
            ResultSetMetaData rsmd = rset.getMetaData();
            int collen = rsmd.getColumnCount() - 1;
            rset.last();
            int rowlen = rset.getRow();
            tableData = new String[rowlen][collen];
            //tab=null;
            attribute = new String[collen];

            rset.beforeFirst();
            if(rset.next())
            {
                for (i = 0; i < collen; i++) {
                    attribute[i] = rsmd.getColumnName((i + 1));

                }
                i = 0;
                do {

                    for (j = 0; j < collen; j++) {
                        tableData[i][j] = rset.getString((j + 1));

                    }
                    i++;
                }while (rset.next());

                DefaultTableModel model=new DefaultTableModel(tableData,attribute);
                JTable table=new JTable(model);
                table.getColumnModel().getColumn(0).setPreferredWidth(20);
                JScrollPane sp=new JScrollPane(table);
                buttonPanel.setBorder(BorderFactory.createEmptyBorder(30,0,10,0));
                sp.setPreferredSize(new DimensionUIResource(750,600));
                tablePanel.add(sp);
            }else
            {
                JOptionPane.showMessageDialog(null,"No properties to sell!");
            }









        } catch (Exception e1) {
            JOptionPane.showMessageDialog(null,"Query execute error","Error",JOptionPane.ERROR_MESSAGE);
        }


        // retrieving end


    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==close)
        {
            this.dispose();
            try{
                if(con!=null)
                {
                    con.close();
                }
            }catch (Exception e1){
                JOptionPane.showMessageDialog(null,"Unable to close connection","Error",JOptionPane.ERROR_MESSAGE);
            }
        }
    }


}


class AvailableForRent extends Show
{
    String attribute[];
    String tableData[][];
    Connection con;
    AvailableForRent()
    {

        setTitle("Available Properties");
        setBounds(300,100,800,600);


        int i,j;

        Statement stmt;
        ResultSet rset;
        String q;

        try {
            ConnectionProvidr conn = new ConnectionProvidr();
            con=null;
            stmt=null;
            try {

                con = conn.createCon();
                stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            }catch (Exception e)
            {
                JOptionPane.showMessageDialog(null,"Network error","Error",JOptionPane.ERROR_MESSAGE);
            }
            q="select *from rent_property where cid IS NULL";



            rset = stmt.executeQuery(q);
            ResultSetMetaData rsmd = rset.getMetaData();
            int collen = rsmd.getColumnCount() - 1;
            rset.last();
            int rowlen = rset.getRow();
            tableData = new String[rowlen][collen];
            //tab=null;
            attribute = new String[collen];

            rset.beforeFirst();
            if(rset.next())
            {
                for (i = 0; i < collen; i++) {
                    attribute[i] = rsmd.getColumnName((i + 1));

                }
                i = 0;
                do {

                    for (j = 0; j < collen; j++) {
                        tableData[i][j] = rset.getString((j + 1));

                    }
                    i++;
                }while (rset.next());

                DefaultTableModel model=new DefaultTableModel(tableData,attribute);
                JTable table=new JTable(model);
                table.getColumnModel().getColumn(0).setPreferredWidth(20);
                JScrollPane sp=new JScrollPane(table);
                buttonPanel.setBorder(BorderFactory.createEmptyBorder(30,0,10,0));
                sp.setPreferredSize(new DimensionUIResource(750,600));
                tablePanel.add(sp);
            }else
            {
                JOptionPane.showMessageDialog(null,"No properties to sell!");
            }

        } catch (Exception e1) {
            JOptionPane.showMessageDialog(null,"Query execute error","Error",JOptionPane.ERROR_MESSAGE);
        }


        // retrieving end


    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==close)
        {
            this.dispose();
            try{
                if(con!=null)
                {
                    con.close();
                }
            }catch (Exception e1){
                JOptionPane.showMessageDialog(null,"Unable to close connection","Error",JOptionPane.ERROR_MESSAGE);
            }
        }
    }


}




class  CityList extends  Show
{
    String attribute[];
    String tableData[][];
    Connection con;
    CityList()
    {
        setTitle("City list");
        int i,j;

        Statement stmt;
        ResultSet rset;
        String q;

        try {
            ConnectionProvidr conn = new ConnectionProvidr();
            con=null;
            stmt=null;
            try {

                con = conn.createCon();
                stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            }catch (Exception e)
            {
                JOptionPane.showMessageDialog(null,"Network error","Error",JOptionPane.ERROR_MESSAGE);
            }
            q="select distinct city from address";
            // System.out.println("connected");


            rset = stmt.executeQuery(q);
            ResultSetMetaData rsmd = rset.getMetaData();
            int collen = rsmd.getColumnCount();
            rset.last();
            int rowlen = rset.getRow();
            tableData = new String[rowlen][collen];
            //tab=null;
            attribute = new String[collen];

            rset.beforeFirst();
            for (i = 0; i < collen; i++) {
                attribute[i] = rsmd.getColumnName((i + 1));
                // System.out.print(col[i]);
            }
            i = 0;
            while (rset.next()) {
                for (j = 0; j < collen; j++) {
                    tableData[i][j] = rset.getString((j + 1));

                }
                i++;
                System.out.println();
            }


        } catch (Exception e1) {
            JOptionPane.showMessageDialog(null,"Query execute error","Error",JOptionPane.ERROR_MESSAGE);
        }

        // retrieving end
        // connection close
        DefaultTableModel model=new DefaultTableModel(tableData,attribute);
        JTable table=new JTable(model);
        //table.getColumnModel().getColumn(0).setPreferredWidth(20);
        tablePanel.add(new JScrollPane(table));
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==close)
        {
            this.dispose();
            try{
                if(con!=null)
                {
                    con.close();
                }
            }catch (Exception e1){
                JOptionPane.showMessageDialog(null,"Unable to close connection","Error",JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}


class AgentsList extends Show
{
    String tableData[][];
    String attribute[];
    Connection con;
    AgentsList()
    {
        setTitle("Agents list");
// retrieving data from database
        int i,j;

        Statement stmt;
        ResultSet rset;
        String q;

        try {
            ConnectionProvidr conn = new ConnectionProvidr();
            con=null;
            stmt=null;
            try {

                con = conn.createCon();
                stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            }catch (Exception e)
            {
                JOptionPane.showMessageDialog(null,"Network error");
            }
            q="select * from agent";


            rset = stmt.executeQuery(q);
            ResultSetMetaData rsmd = rset.getMetaData();
            int collen = rsmd.getColumnCount();
            rset.last();
            int rowlen = rset.getRow();
            tableData = new String[rowlen][collen];
            //tab=null;
            attribute = new String[collen];

            rset.beforeFirst();
            for (i = 0; i < collen; i++) {
                attribute[i] = rsmd.getColumnName((i + 1));

            }
            i = 0;
            while (rset.next()) {
                for (j = 0; j < collen; j++) {
                    tableData[i][j] = rset.getString((j + 1));

                }
                i++;
                System.out.println();
            }


        } catch (Exception e1) {
            JOptionPane.showMessageDialog(null,"Query execute error","Error",JOptionPane.ERROR_MESSAGE);
        }


        DefaultTableModel model=new DefaultTableModel(tableData,attribute);
        JTable table=new JTable(model);
        //table.getColumnModel().getColumn(0).setPreferredWidth(20);
        tablePanel.add(new JScrollPane(table));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==close)
        {
            this.dispose();
            try{
                if(con!=null)
                {
                    con.close();
                }
            }catch (Exception e1){
                JOptionPane.showMessageDialog(null,"Unable to close connection","Error",JOptionPane.ERROR_MESSAGE);
            }
        }
    }


}

class UsersList extends Show
{
    String attribute[];
    String tableData[][];
    Connection con=null;
    UsersList()
    {
        setTitle("Users list");

        int i,j;

        Statement stmt;
        ResultSet rset;
        String q;

        try {
            ConnectionProvidr conn = new ConnectionProvidr();
            con=null;
            stmt=null;
            try {

                con = conn.createCon();
                stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            }catch (Exception e)
            {
                JOptionPane.showMessageDialog(null,"Network error");
            }
            q="select * from customer";


            rset = stmt.executeQuery(q);
            ResultSetMetaData rsmd = rset.getMetaData();
            int collen = rsmd.getColumnCount();
            rset.last();
            int rowlen = rset.getRow();
            tableData = new String[rowlen][collen];
            //tab=null;
            attribute = new String[collen];

            rset.beforeFirst();
            for (i = 0; i < collen; i++) {
                attribute[i] = rsmd.getColumnName((i + 1));

            }
            i = 0;
            while (rset.next()) {
                for (j = 0; j < collen; j++) {
                    tableData[i][j] = rset.getString((j + 1));

                }
                i++;
                System.out.println();
            }


        } catch (Exception e1) {
            JOptionPane.showMessageDialog(null,"Query execute error","Error",JOptionPane.ERROR_MESSAGE);
        }

        DefaultTableModel model=new DefaultTableModel(tableData,attribute);
        JTable table=new JTable(model);
        //table.getColumnModel().getColumn(0).setPreferredWidth(20);
        tablePanel.add(new JScrollPane(table));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==close)
        {
            this.dispose();
            try{
                if(con!=null)
                {
                    con.close();
                }
            }catch (Exception e1){
                JOptionPane.showMessageDialog(null,"Unable to close connection","Error",JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

class AppointmentsList extends Show
{
    String attribute[];
    String tableData[][];
    Connection con=null;
    AppointmentsList()
    {
        setTitle("Appointments list");
        int i,j;

        Statement stmt;
        ResultSet rset;
        String q;

        try {
            ConnectionProvidr conn = new ConnectionProvidr();
            con=null;
            stmt=null;
            try {

                con = conn.createCon();
                stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            }catch (Exception e)
            {
                JOptionPane.showMessageDialog(null,"Network error");
            }
            q="select *from student";


            rset = stmt.executeQuery(q);
            ResultSetMetaData rsmd = rset.getMetaData();
            int collen = rsmd.getColumnCount();
            rset.last();
            int rowlen = rset.getRow();
            tableData = new String[rowlen][collen];
            //tab=null;
            attribute = new String[collen];

            rset.beforeFirst();
            for (i = 0; i < collen; i++) {
                attribute[i] = rsmd.getColumnName((i + 1));

            }
            i = 0;
            while (rset.next()) {
                for (j = 0; j < collen; j++) {
                    tableData[i][j] = rset.getString((j + 1));

                }
                i++;
                System.out.println();
            }


        } catch (Exception e1) {
            JOptionPane.showMessageDialog(null,"Query execute error","Error",JOptionPane.ERROR_MESSAGE);
        }
        DefaultTableModel model=new DefaultTableModel(tableData,attribute);
        JTable table=new JTable(model);
        //table.getColumnModel().getColumn(0).setPreferredWidth(20);
        tablePanel.add(new JScrollPane(table));
    }
}

class PropertyTypeList extends Show
{
    PropertyTypeList()
    {
        setTitle("Property list");
        int rowSize=2;
        int columnSize=2;
        String col[]=new String[columnSize];
        col[0]="serial No";
        col[1]="Property Type";
        String row[][]=new String[rowSize][columnSize];
        row[0][0]="1";
        row[0][1]="Rent";
        row[1][0]="2";
        row[1][1]="Sell";
        DefaultTableModel model=new DefaultTableModel(row,col);
        JTable table=new JTable(model);
        table.getColumnModel().getColumn(0).setPreferredWidth(20);
        tablePanel.add(new JScrollPane(table));

    }
}

class AddProperty extends JFrame implements ActionListener {

    JPanel north;
    JPanel center;
    JPanel buttonPanel;
    JButton add;
    JButton close;
    //JList typeList;
    JComboBox typeCombo;
    String type[];
    JMenuItem rent;
    JMenuItem sell;
    JLabel propertyId;
    JLabel price;
    JLabel area;
    JLabel yearOfConstruction;
    JLabel bedrooms;
    JTextField tPropertyId;
    JTextField tPrice;
    JTextField tArea;
    JTextField tYearOfConstruction;
    JTextField tBedrooms;
    JButton ok;
    AddProperty()
    {
        setTitle("Add to DataBase");
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setExtendedState(JFrame.MAXIMIZED_BOTH);
        setBounds(300,100,500,500);
        setVisible(true);
        setLocationRelativeTo(null);
        Container container=getContentPane();
        container.setLayout(new BorderLayout());

        north=new JPanel(new GridLayout(7,2,10,20));
        north.setBackground(Color.cyan);
        north.setBorder(BorderFactory.createEmptyBorder(10,50,10,50));
        type=new String[2];
        type[0]="Sell";
        type[1]="Rent";
        typeCombo=new JComboBox(type);
        typeCombo.setSelectedIndex(0);
        north.add(typeCombo);


       ok=new JButton("OK");
       ok.addActionListener(this);
        north.add(ok);

        propertyId=new JLabel("Property ID");
        north.add(propertyId);
        tPropertyId=new JTextField();
        north.add(tPropertyId);

        price=new JLabel("Price");
        north.add(price);
        tPrice=new JTextField();
        north.add(tPrice);

        area=new JLabel("Area");
        north.add(area);
        tArea=new JTextField();
        north.add(tArea);

        yearOfConstruction=new JLabel("Year of construction");
        north.add(yearOfConstruction);
        tYearOfConstruction=new JTextField();
        north.add(tYearOfConstruction);

        bedrooms=new JLabel("Bedrooms");
        north.add(bedrooms);
        tBedrooms=new JTextField();
        north.add(tBedrooms);


        center=new JPanel(new FlowLayout());
        center.setBorder(BorderFactory.createEmptyBorder(30,0,50,0));
        center.setBackground(Color.cyan);
        add=new JButton("Add");
        buttonPanel=new JPanel();
        buttonPanel.add(add);
        close=new JButton("Close");

        buttonPanel.add(close);
        center.add(buttonPanel);

        container.add(north,BorderLayout.NORTH);
        container.add(center,BorderLayout.CENTER);

        add.addActionListener(this);
        close.addActionListener(this);

        add.setBackground(Color.ORANGE);
        close.setBackground(Color.ORANGE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==close)
        {
            this.dispose();
        }

        if(e.getSource()==ok)
        {
            if(typeCombo.getSelectedIndex()==0)
            {
                price.setText("Price(BHK)");
            }

            if(typeCombo.getSelectedIndex()==1)
            {
                price.setText("Price(per month)");
            }

            add.setBackground(Color.ORANGE);
        }

        if(e.getSource()==add)
        {
           try
           {
               int propertyID=0;
               int area = 0;
               int bedrooms = 0;
               int year_of_construction = 0;
               int price=0;
               boolean isAllTrue=true;
               try{
                   propertyID= Integer.parseInt(tPropertyId.getText());
                   try {
                        price=Integer.parseInt(tPrice.getText());

                       try {
                           area = Integer.parseInt(tArea.getText());

                           try {
                               year_of_construction = Integer.parseInt(tYearOfConstruction.getText());

                               if(tYearOfConstruction.getText().length()!=4 && isAllTrue)
                               {
                                   JOptionPane.showMessageDialog(null,"Invalid date(Year)!");
                                   isAllTrue=false;
                                   return;
                               }

                               try {
                                   bedrooms = Integer.parseInt(tBedrooms.getText());
                               } catch (Exception e3) {
                                   JOptionPane.showMessageDialog(null, "Invalid bedroom number!");
                                   isAllTrue = false;
                                   System.out.println(e3);

                               }
                           } catch (Exception e3) {
                               JOptionPane.showMessageDialog(null, "Invalid year of construction!");
                               isAllTrue = false;
                               System.out.println(e3);

                           }
                       } catch (Exception e3) {
                           JOptionPane.showMessageDialog(null, "Invalid area!");
                           isAllTrue = false;
                           System.out.println(e3);

                       }

                   }catch (Exception e3)
                   {
                       JOptionPane.showMessageDialog(null,"Invalid price","Error",JOptionPane.ERROR_MESSAGE);
                       isAllTrue=false;
                   }
               }
               catch (Exception e2)
               {
                   JOptionPane.showMessageDialog(null,"Invalid property ID!");
                   isAllTrue=false;
                   System.out.println(e2);

               }



               if(isAllTrue)
               {
                   if(typeCombo.getSelectedIndex()==0)
                   {
                       boolean isTrue=false;
                       isTrue=addProperty(0,propertyID,price,area,bedrooms,year_of_construction);
                       if(isTrue)
                       {
                           add.setText("Added");
                           add.setBackground(Color.GREEN);
                       }
                   }

                   if(typeCombo.getSelectedIndex()==1)
                   {
                       boolean isTrue=false;
                       isTrue=addProperty(1,propertyID,price,area,bedrooms,year_of_construction);
                       if(isTrue)
                       {
                           add.setText("Added");
                           add.setBackground(Color.GREEN);
                       }
                   }
               }


           }
           catch (Exception e1)
           {
               System.out.println(e);
           }
        }
    }

    public boolean addProperty(int q,int pid,int price,int area,int bedroom,int yearOfConstruction)
    {
        Connection con=null;
        int rSet=-1;
        Statement stmt;
        try{
            ConnectionProvidr conn=new ConnectionProvidr();
            con=conn.createCon();
            System.out.println("qib");
            String query1 = "Insert into sale_property values(" + pid + "," + price + " ,"+area+ " ,"+bedroom + " ,"+yearOfConstruction+",NULL)";
            String query2 =  "Insert into rent_property values(" + pid + "," + price + " ,"+area+ " ,"+bedroom + " ,"+yearOfConstruction+",NULL)";
            if(q == 0)
            {
                stmt= con.createStatement();
                rSet = stmt.executeUpdate(query1);

                if(rSet>0)
                {
                    JOptionPane.showMessageDialog(null,"Property added successfully");
                    if(con!=null){
                        try
                        {
                            con.close();
                        }catch (Exception e2)
                        {
                            System.out.println(e2);
                        }
                    }
                    return true;
                }else if(rSet<0)
                {
                    JOptionPane.showMessageDialog(null,"Can not add property","Error",JOptionPane.ERROR_MESSAGE);
                    if(con!=null){
                        try
                        {
                            con.close();
                        }catch (Exception e2)
                        {
                            System.out.println(e2);
                        }
                    }
                    return false;
                }else {
                    JOptionPane.showMessageDialog(null,"Can not add property","Error",JOptionPane.ERROR_MESSAGE);
                    if(con!=null){
                        try
                        {
                            con.close();
                        }catch (Exception e2)
                        {
                            System.out.println(e2);
                        }
                    }
                    return false;
                }

            }

            if(q == 1)
            {
                stmt= con.createStatement();
                rSet = stmt.executeUpdate(query2);

                if(rSet>0)
                {
                    JOptionPane.showMessageDialog(null,"Property added successfully");
                    if(con!=null){
                        try
                        {
                            con.close();
                        }catch (Exception e2)
                        {
                            System.out.println(e2);
                        }
                    }
                    return true;
                }else if(rSet<0)
                {
                    JOptionPane.showMessageDialog(null,"Can not add property","Error",JOptionPane.ERROR_MESSAGE);
                    if(con!=null){
                        try
                        {
                            con.close();
                        }catch (Exception e2)
                        {
                            System.out.println(e2);
                        }
                    }
                    return false;
                }else {
                    JOptionPane.showMessageDialog(null,"Can not add property","Error",JOptionPane.ERROR_MESSAGE);
                    if(con!=null){
                        try
                        {
                            con.close();
                        }catch (Exception e2)
                        {
                            System.out.println(e2);
                        }
                    }
                    return false;
                }

            }else {
                return false;
            }



        }
        catch(java.sql.SQLIntegrityConstraintViolationException i)
        {
            JOptionPane.showMessageDialog(null,"Property id already exist!");
            return false;
        }
        catch (Exception e)
        {


            JOptionPane.showMessageDialog(null,"Network Error","Error",JOptionPane.ERROR_MESSAGE);
            System.out.println(e);
            if(con!=null){
                try
                {
                    con.close();
                }catch (Exception e2)
                {
                    System.out.println(e2);
                }
            }
            return false;
        }


    }

}

class AdminProperty extends JFrame implements ActionListener
{
    JPanel backGround;
    JPanel table;

    JPanel updateTable;
    JPanel updatePanel;
    JPanel buttonPanel;

    JComboBox combo;
    String attribute[];
    String tableData[][];
    Connection con;
    JButton ok;
    DefaultTableModel tableModel;
    JTable jTable;
    JScrollPane scrollPane;
    JButton close;

    AdminProperty()
    {

        try
        {
            setTitle("Table updates");
            setBounds(250,80,1200,700);
            //setLocationRelativeTo(null);
            Container container=getContentPane();
            // container.setLayout(null);
            container.setPreferredSize(new DimensionUIResource(1500,700));


            backGround=new JPanel();
            container.add(backGround);
            backGround.setLayout(new BoxLayout(backGround,BoxLayout.X_AXIS));


            table=new JPanel();
            table.setPreferredSize(new DimensionUIResource(700,600));
            table.setBackground(new Color(125, 209, 117));
            backGround.add(table);

            updateTable=new JPanel();
            updateTable.setBackground(new Color(125, 209, 117));
            updateTable.setLayout(new BoxLayout(updateTable,BoxLayout.Y_AXIS));
            updateTable.setPreferredSize(new DimensionUIResource(500,700));
            backGround.add(updateTable);

            updatePanel=new JPanel(new GridLayout(10,2,10,20));
            updatePanel.setPreferredSize(new DimensionUIResource(500,500));
            updatePanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10,30,10,20));
            updateTable.add(updatePanel);

            Font font=new Font("Arial",Font.BOLD,15);

            String type[]=new String[2];
            type[0]="Sell";
            type[1]="Rent";
            combo=new JComboBox(type);
            combo.setSelectedIndex(0);
            updatePanel.add(combo);

            ok=new JButton("Ok");
            ok.addActionListener(this);
            updatePanel.add(ok);


            int n=6;
            int i,j;
            JLabel jl[]=new JLabel[n];
            for(i=0;i<n;i++)
            {
                jl[i]=new JLabel();
                updatePanel.add(jl[i]);
            }

            buttonPanel=new JPanel();
            updatePanel.setBackground(new Color(125, 209, 117));
            buttonPanel.setBackground(new Color(125, 209, 117));
            //buttonPanel.setBackground(Color.darkGray);
            buttonPanel.setPreferredSize(new DimensionUIResource(500,200));
            updateTable.add(buttonPanel);

            close=new JButton("Close");


            // table.add(scrollPane);


            setVisible(true);
            validate();
            revalidate();

        }catch (Exception e)
        {
            System.out.println(e);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource()==close)
        {
            dispose();
            try{
                if(con!=null)
                {
                    con.close();
                }
            }catch (Exception e1)
            {
                JOptionPane.showMessageDialog(null,"Unable to close connection","Error",JOptionPane.ERROR_MESSAGE);
            }
        }

        if(e.getSource()==ok)
        {
            if(combo.getSelectedIndex()==0)
            {
                setTitle("Property available to sell");
                String q="select *from sale_property where cid is null";
                showSellOrRentProperty(q);

            }
            if(combo.getSelectedIndex()==1)
            {
                setTitle("Property available to rent");
                String q="select *from rent_property where cid is null";
                showSellOrRentProperty(q);
            }
        }

    }

    public  void  showSellOrRentProperty(String q)
    {
        int i,j;
        Statement stmt;
        ResultSet rset;
        try {
            ConnectionProvidr conn = new ConnectionProvidr();
            con=null;
            stmt=null;
            try {

                con = conn.createCon();
                stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            }catch (Exception e)
            {
                JOptionPane.showMessageDialog(null,"Network error");
            }

            rset = stmt.executeQuery(q);
            ResultSetMetaData rsmd = rset.getMetaData();
            int collen = rsmd.getColumnCount() -1;
            rset.last();
            int rowlen = rset.getRow();
            tableData = new String[rowlen][collen];
            //tab=null;
            attribute = new String[collen];

            rset.beforeFirst();
            if(rset.next())
            {
                for (i = 0; i < collen; i++) {
                    attribute[i] = rsmd.getColumnName((i + 1));

                }
                i = 0;
                do {
                    for (j = 0; j < collen; j++) {
                        tableData[i][j] = rset.getString((j + 1));

                    }
                    i++;

                }while (rset.next());

                tableModel=new DefaultTableModel(tableData,attribute);
                jTable=new JTable(tableModel);
                //jTable.setPreferredSize(new DimensionUIResource(700,500));
                jTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                //jTable. getColumnModel(). getColumn(4). setPreferredWidth(150);
                scrollPane=new JScrollPane(jTable);
                scrollPane.setPreferredSize(new DimensionUIResource(600,650));
                try{
                    table.remove(0);
                }catch (Exception e1)
                {

                }
                table.add(scrollPane);

            }else {
                JOptionPane.showMessageDialog(null,"Property not available");
            }


        } catch (Exception e1) {
            JOptionPane.showMessageDialog(null,"Query execute error2","Error",JOptionPane.ERROR_MESSAGE);
            System.out.println(e1);

        }
    }

    public  boolean updateTable(String q)
    {
        int i,j;
        Statement stmt;
        int rset;
        try {
            ConnectionProvidr conn = new ConnectionProvidr();
            con=null;
            stmt=null;
            try {

                con = conn.createCon();
                stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            }catch (Exception e)
            {
                JOptionPane.showMessageDialog(null,"Network error");
                return false;
            }

            rset = stmt.executeUpdate(q);

            if(rset<0)
            {
                JOptionPane.showMessageDialog(null,"Invalid credentials!","Error",JOptionPane.ERROR_MESSAGE);
                return false;
            }else if(rset==0) {
                JOptionPane.showMessageDialog(null,"Invalid  Customer");
                System.out.println(rset);
                return false;
            }else {
                return true;
            }


        } catch (Exception e1) {
            JOptionPane.showMessageDialog(null,"Query execute error1","Error",JOptionPane.ERROR_MESSAGE);
            System.out.println(e1);
            return false;
        }

    }

    public  boolean check(String q)
    {
        int i,j;
        Statement stmt;
        ResultSet rset;
        try {
            ConnectionProvidr conn = new ConnectionProvidr();
            con=null;
            stmt=null;
            try {

                con = conn.createCon();
                stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            }catch (Exception e)
            {
                JOptionPane.showMessageDialog(null,"Network error");
                if(con!=null)
                {
                    con.close();
                }
                return false;
            }

            rset = stmt.executeQuery(q);
            if(rset.next())
            {
                return true;
            }else {
                return false;
            }




        } catch (Exception e1) {
            JOptionPane.showMessageDialog(null,"Query execute error1","Error",JOptionPane.ERROR_MESSAGE);
            System.out.println(e1);
            return false;
        }

    }
}


class SoldOrRentProperty extends JFrame implements ActionListener {
    JPanel backGround;
    JPanel table;

    JPanel updateTable;
    JPanel updatePanel;
    JPanel buttonPanel;
    static JButton update;
    //JButton rent;
    static JButton close;
    static JButton search;

    static JLabel pid,cid;

    static JTextField tPid,tCid;
    JComboBox combo;
    String attribute[];
    String tableData[][];
    Connection con;
    JButton ok;
    DefaultTableModel tableModel;
    JTable jTable;
    JScrollPane scrollPane;

    SoldOrRentProperty()
    {

        try
        {
            setTitle("Table updates");
            setBounds(250,80,1200,700);
            //setLocationRelativeTo(null);
            Container container=getContentPane();
            // container.setLayout(null);
            container.setPreferredSize(new DimensionUIResource(1500,700));


            backGround=new JPanel();
            container.add(backGround);
            backGround.setLayout(new BoxLayout(backGround,BoxLayout.X_AXIS));


            table=new JPanel();
            table.setPreferredSize(new DimensionUIResource(700,600));
            table.setBackground(new Color(125, 209, 117));
            backGround.add(table);

            updateTable=new JPanel();
            updateTable.setBackground(new Color(125, 209, 117));
            updateTable.setLayout(new BoxLayout(updateTable,BoxLayout.Y_AXIS));
            updateTable.setPreferredSize(new DimensionUIResource(500,700));
            backGround.add(updateTable);

            updatePanel=new JPanel(new GridLayout(10,2,10,20));
            updatePanel.setPreferredSize(new DimensionUIResource(500,500));
            updatePanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10,30,10,20));
            updateTable.add(updatePanel);

            Font font=new Font("Arial",Font.BOLD,15);

            String type[]=new String[2];
            type[0]="Sell";
            type[1]="Rent";
            combo=new JComboBox(type);
            combo.setSelectedIndex(0);
            updatePanel.add(combo);

           ok=new JButton("Ok");
           ok.addActionListener(this);
            updatePanel.add(ok);

            pid=new JLabel("Property Id");
            pid.setFont(font);
            updatePanel.add(pid);
            tPid=new JTextField();
            updatePanel.add(tPid);


            cid=new JLabel("Customer ID");
            cid.setFont(font);
            updatePanel.add(cid);
            tCid=new JTextField();
            updatePanel.add(tCid);

            int n=6;
            int i,j;
            JLabel jl[]=new JLabel[n];
            for(i=0;i<n;i++)
            {
                jl[i]=new JLabel();
                updatePanel.add(jl[i]);
            }

            buttonPanel=new JPanel();
            updatePanel.setBackground(new Color(125, 209, 117));
            buttonPanel.setBackground(new Color(125, 209, 117));
            //buttonPanel.setBackground(Color.darkGray);
            buttonPanel.setPreferredSize(new DimensionUIResource(500,200));
            updateTable.add(buttonPanel);



            update=new JButton("Update");
            buttonPanel.add(update);

            search=new JButton("Search");
            buttonPanel.add(search);

            close=new JButton("Close");
            buttonPanel.add(close);

            //close.setMargin(new Insets(20,0,0,0));
            close.addActionListener(this);
           // rent.addActionListener(this);
            update.addActionListener(this);
            search.addActionListener(this);

            // table.add(scrollPane);


            setVisible(true);
            validate();
            revalidate();

        }catch (Exception e)
        {
            System.out.println(e);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource()==close)
        {
            dispose();
            try{
                if(con!=null)
                {
                    con.close();
                }
            }catch (Exception e1)
            {
                JOptionPane.showMessageDialog(null,"Unable to close connection","Error",JOptionPane.ERROR_MESSAGE);
            }
        }
        if(e.getSource()==update)
        {
            int pid=0;
            int cid=0;
            try {
                pid=Integer.parseInt(tPid.getText());
                cid=Integer.parseInt(tCid.getText());

            }catch (Exception e2)
            {
                JOptionPane.showMessageDialog(null,"Invalid id");
            }
            if(combo.getSelectedIndex()==0)
            {
                String q="select * from customer where interest = 'buy' and cid=" + cid;
                boolean isTrue=false;
                isTrue=check(q);
                if (isTrue)
                {
                    boolean isUpdate=false;

                    q= "update sale_property set cid = " + cid + " where pid = "+ pid;
                    isUpdate=updateTable(q);

                    int aid=1000;
                    q="insert into sold values("+aid+" ,"+pid+")";
                    isUpdate=addIntoTable(q);

                    if(isUpdate)
                    {
                        update.setBackground(Color.GREEN);
                        update.setText("Updated");
                    }

                JOptionPane.showMessageDialog(null,"Property details updated!");

                }
                else {
                    JOptionPane.showMessageDialog(null,"Invalid customer");
                }


            }
            if(combo.getSelectedIndex()==1)
            {
                String q="select * from customer where interest = 'rent' and cid=" + cid;
                boolean isTrue=false;
                isTrue=check(q);
                if (isTrue)
                {
                    boolean isUpdate=false;

                    q= "update rent_property set cid = " + cid + " where pid = "+ pid;
                    isUpdate=updateTable(q);
                    if(isUpdate)
                    {
                        update.setBackground(Color.GREEN);
                        update.setText("Updated");
                    }

                    JOptionPane.showMessageDialog(null,"Property details updated!");

                }
                else {
                    JOptionPane.showMessageDialog(null,"Invalid customer");
                }

            }

        }
        if(e.getSource()==search)
        {
            int id=0;
            try {
                id=Integer.parseInt(tPid.getText());

                if(combo.getSelectedIndex()==0)
                {

                    String q="select *from sale_property where cid IS NULL and pid = " + id;
                    showSellOrRentProperty(q);


                }
                if(combo.getSelectedIndex()==1)
                {

                    String q="select *from rent_property where cid is NULL and pid = " + id;
                    showSellOrRentProperty(q);
                }

            }catch (Exception e2)
            {
                JOptionPane.showMessageDialog(null,"Invalid id");
            }


        }

        if(e.getSource()==ok)
        {
            if(combo.getSelectedIndex()==0)
            {
                setTitle("Property available to sell");
                String q="select *from sale_property where cid IS NULL";
                showSellOrRentProperty(q);

            }
            if(combo.getSelectedIndex()==1)
            {
                setTitle("Property available to rent");
                String q="select *from rent_property where cid is NULL";
                showSellOrRentProperty(q);
            }
        }

    }

    public  void  showSellOrRentProperty(String q)
    {
        int i,j;
        Statement stmt;
        ResultSet rset;
        try {
            ConnectionProvidr conn = new ConnectionProvidr();
            con=null;
            stmt=null;
            try {

                con = conn.createCon();
                stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            }catch (Exception e)
            {
                JOptionPane.showMessageDialog(null,"Network error");
            }

            rset = stmt.executeQuery(q);
            ResultSetMetaData rsmd = rset.getMetaData();
            int collen = rsmd.getColumnCount() -1;
            rset.last();
            int rowlen = rset.getRow();
            tableData = new String[rowlen][collen];
            //tab=null;
            attribute = new String[collen];

            rset.beforeFirst();
            if(rset.next())
            {
                for (i = 0; i < collen; i++) {
                    attribute[i] = rsmd.getColumnName((i + 1));

                }
                i = 0;
                do {
                    for (j = 0; j < collen; j++) {
                        tableData[i][j] = rset.getString((j + 1));

                    }
                    i++;

                }while (rset.next());

                 tableModel=new DefaultTableModel(tableData,attribute);
                 jTable=new JTable(tableModel);
                //jTable.setPreferredSize(new DimensionUIResource(700,500));
                jTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                //jTable. getColumnModel(). getColumn(4). setPreferredWidth(150);
                scrollPane=new JScrollPane(jTable);
                scrollPane.setPreferredSize(new DimensionUIResource(600,650));
                try{
                    table.remove(0);
                }catch (Exception e1)
                {

                }
                table.add(scrollPane);

            }else {
                JOptionPane.showMessageDialog(null,"Property not available");
            }


        } catch (Exception e1) {
            JOptionPane.showMessageDialog(null,"Query execute error2","Error",JOptionPane.ERROR_MESSAGE);
            System.out.println(e1);

        }
    }

    public  boolean updateTable(String q)
    {
            int i,j;
            Statement stmt;
            int rset;
            try {
                ConnectionProvidr conn = new ConnectionProvidr();
                con=null;
                stmt=null;
                try {

                    con = conn.createCon();
                    stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                }catch (Exception e)
                {
                    JOptionPane.showMessageDialog(null,"Network error");
                    return false;
                }

                rset = stmt.executeUpdate(q);

                if(rset<0)
                {
                    JOptionPane.showMessageDialog(null,"Invalid credentials!","Error",JOptionPane.ERROR_MESSAGE);
                    return false;
                }else if(rset==0) {
                    JOptionPane.showMessageDialog(null,"Invalid  Customer");
                    System.out.println(rset);
                    return false;
                }else {
                    return true;
                }


            } catch (Exception e1) {
                JOptionPane.showMessageDialog(null,"Query execute error1","Error",JOptionPane.ERROR_MESSAGE);
                System.out.println(e1);
                return false;
            }

    }

    public  boolean check(String q)
    {
        int i,j;
        Statement stmt;
        ResultSet rset;
        try {
            ConnectionProvidr conn = new ConnectionProvidr();
            con=null;
            stmt=null;
            try {

                con = conn.createCon();
                stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            }catch (Exception e)
            {
                JOptionPane.showMessageDialog(null,"Network error");
                if(con!=null)
                {
                    con.close();
                }
                return false;
            }

            rset = stmt.executeQuery(q);
            if(rset.next())
            {
                return true;
            }else {
                return false;
            }




        } catch (Exception e1) {
            JOptionPane.showMessageDialog(null,"Query execute error1","Error",JOptionPane.ERROR_MESSAGE);
            System.out.println(e1);
            return false;
        }

    }

    public boolean addIntoTable(String query)
    {
        Connection con=null;
        Statement stmt;
        int rSet;

        try
        {
            ConnectionProvidr conn=new ConnectionProvidr();
            con=conn.createCon();
            stmt=con.createStatement();
            rSet=stmt.executeUpdate(query);
            con.close();
            return true;


        }catch (Exception e)
        {
            //JOptionPane.showMessageDialog(null,"Network error");
            return false;
        }
    }
}





class Profiles extends JFrame implements ActionListener {
    JPanel backGround;
    JPanel profilePicture;
    JPanel details;
    JPanel base;
    JLabel profilePic;
    JLabel name;
    JLabel mobile;
    JLabel dob;
    JLabel age;
    JLabel address;
    JLabel getName;
    JLabel getMobile;
    JLabel getDob;
    JLabel getAge;
    JLabel getAddress;
    JButton close;
    Profiles()
    {
       try
       {
           setTitle("Profiles");
           setBounds(550,100,400,600);
           //setLocationRelativeTo(null);
           Container container=getContentPane();
          // container.setLayout(null);
           container.setPreferredSize(new DimensionUIResource(450,600));

           backGround=new JPanel();
           container.add(backGround);
           backGround.setLayout(new BoxLayout(backGround,BoxLayout.Y_AXIS));

           profilePicture=new JPanel();
           profilePicture.setPreferredSize(new DimensionUIResource(500,100));
           backGround.add(profilePicture);

           details=new JPanel(new GridLayout(10,2,50,10));
           details.setPreferredSize(new DimensionUIResource(500,500));
           details.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 70, 0, 10));
           backGround.add(details);

           profilePic=new JLabel("Profile Pic ");
           profilePicture.add(profilePic);

           name=new JLabel("Name ");
           details.add(name);
           getName=new JLabel("Name");
           details.add(getName);

           dob=new JLabel("D.O.B ");
           details.add(dob);
           getDob=new JLabel("D.O.B");
           details.add(getDob);

           mobile=new JLabel("Mobile ");
           details.add(mobile);
           getMobile=new JLabel("Mobile");
           details.add(getMobile);



           age=new JLabel("Age ");
           details.add(age);
           getAge=new JLabel("Age");
           details.add(getAge);


           int n=10;
           int i,j;
           JLabel jl[]=new JLabel[n];
           for(i=0;i<n;i++)
           {
               jl[i]=new JLabel();
               details.add(jl[i]);
           }

           base=new JPanel();
           base.setPreferredSize(new DimensionUIResource(500,100));
           backGround.add(base);
           close=new JButton("Close");
           base.setBackground(new Color(63, 85, 92));
           base.add(close);
           //close.setMargin(new Insets(20,0,0,0));
           close.addActionListener(this);

           profilePicture.setBackground(new Color(98, 105, 245));
           details.setBackground(new Color(109, 252, 147));
           //name=new JLabel()
           setVisible(true);
           validate();
           revalidate();

       }catch (Exception e)
       {
           System.out.println("error occur");
       }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==close)
        {
            dispose();
        }
    }
}

class AdminProfile extends Profiles
{
    AdminProfile()
    {
        setTitle("My Profile");
        getName.setText("Admin name");
        getMobile.setText("xxxxxxxxxx");
        getDob.setText("xx/xx/xxxx");
        getAge.setText("NA");


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==close)
        {
            dispose();
        }
    }
}

class AgentProfile extends Profiles
{
    AgentProfile()
    {
        setTitle("My Profile");
        getName.setText("Agent name");
        getMobile.setText("xxxxxxxxxx");
        getDob.setText("xx/xx/xxxx");
        getAge.setText("NA");


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==close)
        {
            dispose();
        }
    }
}

class BuyerProfile extends Profiles
{
    BuyerProfile()
    {
        setTitle("My Profile");
        getName.setText("Buyer name");
        getMobile.setText("xxxxxxxxxx");
        getDob.setText("NA");
        getAge.setText("NA");
        getAddress.setText("buyer address");

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==close)
        {
            dispose();
        }
    }
}


class OfficeProfile extends Profiles
{
    OfficeProfile()
    {
        setTitle("My Profile");
        getName.setText("seller name");
        getMobile.setText("123456789");
        getDob.setText("NA");
        getAge.setText("NA");


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==close)
        {
            dispose();
        }
    }
}



class UserHomePage extends JFrame implements ActionListener, MenuListener {

    JButton b1;
    JButton b2;
    JButton b3;
    JPanel pLeft;
    JPanel pCenter;

    JMenu jmLogin;
    JButton logOut;
    Container container;
    JMenuBar jmb;

    UserHomePage()
    {
        setTitle("Login As Agent");
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
        setLocationRelativeTo(null);
        container=getContentPane();
        container.setLayout(new BorderLayout());
        jmb=new JMenuBar();

        pLeft=new JPanel();
        pLeft.setLayout(new BoxLayout(pLeft, BoxLayout.Y_AXIS));
        pLeft.setBackground(Color.cyan);
        //pLeft.setSize(500,400);
        pLeft.setPreferredSize(new Dimension(160,500));
        //pLeft.setAlignmentY(0);

        JPanel pRight=new JPanel();
       // pLeft.setSize(50,400);

        JPanel pBase=new JPanel();
        //pLeft.setSize(50,40);

        pCenter=new JPanel();
        //pLeft.setSize(500,400);

        b1=new JButton("Profiles");
        //b1.setSize(50,15);
        pLeft.add(b1);

        b2=new JButton("My Properties");
        //b2.setSize(50,15);
        pLeft.add(b2);

//        b3=new JButton("Enquiry");
//        //b3.setSize(50,15);
//        pLeft.add(b3);

        Font font=new Font("Arial",Font.ITALIC,70);

        //JButton b5=new JButton("hello");
        jmb.setBackground(Color.GREEN);
        Font font2=new Font("Arial",Font.BOLD,20);
        JMenu jmHome=new JMenu("      Home");
        jmHome.setFont(font2);
        jmb.add(jmHome);


        JMenu jmAbout=new JMenu("About");
        jmAbout.addMenuListener(this);
        jmAbout.setFont(font2);
        jmb.add(jmAbout);

        JMenu jmContactUs=new JMenu("Contact Us");
        jmContactUs.setFont(font2);
        jmb.add(jmContactUs);

        jmb.add(Box.createHorizontalGlue());

        JTextField tf = new JTextField();
        tf.setFont(font2);
        tf.setSize(50, 15);
        tf.setPreferredSize(new Dimension(100,15));
        Action action = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String rm = tf.getText();


            }
        };
        tf.addActionListener(action);

        tf.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (tf.getText().equals("Search me")) {
                    tf.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (tf.getText().equals("")) {
                    tf.setText("Search me");
                }

            }
        });


        jmb.add(tf);
//        jmLogin=new JMenu("Agent Name");
//        jmLogin.setFont(font2);
//        jmb.add(jmLogin);

        logOut=new JButton("Log Out");
       logOut.setActionCommand("logOut");
       logOut.setFont(font2);
       logOut.setBackground(Color.GREEN);
       jmb.add(logOut);
       logOut.addActionListener(this);



        container.add(jmb,BorderLayout.NORTH);
        container.add(pLeft,BorderLayout.WEST);
        container.add(pRight,BorderLayout.EAST);
        container.add(pBase,BorderLayout.SOUTH);
        //container.add(pCenter,BorderLayout.CENTER);
        validate();

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String command=e.getActionCommand();
        if(command.equals("logOut"))
        {
            System.out.println("logedout successful");
            dispose();
        }
    }

    @Override
    public void menuSelected(MenuEvent e) {

    }

    @Override
    public void menuDeselected(MenuEvent e) {

    }

    @Override
    public void menuCanceled(MenuEvent e) {

    }
}

class LoginAdmin extends UserHomePage
{
    JButton b4;
    JButton b5;
    JButton b6;
    JButton b7;
    JButton b8;

    JButton b9;
    JButton b10;
    JButton b11;
    JButton b12;
    JButton b;
    JButton b13;
    LoginAdmin()
    {

        JLabel lImage=new JLabel();
        // lImage.setBounds(100,50,500,400);
        lImage.setSize((getWidth()-pLeft.getWidth()),(getHeight()-jmb.getHeight()));
        try
        {
            BufferedImage image= ImageIO.read(new File("E:\\E Drive\\Coding Programme\\My work\\Project\\admin.jpg"));
            ImageIcon imageIcon=new ImageIcon(image);
            Image getImage=imageIcon.getImage();
            Image scaleImage=getImage.getScaledInstance(lImage.getWidth(),lImage.getHeight(),Image.SCALE_SMOOTH);
            ImageIcon scaleIcon=new ImageIcon(scaleImage);
            lImage.setIcon(scaleIcon);

        }catch (Exception e)
        {
            JOptionPane.showMessageDialog(null,"image not found");
        }

        container.add(lImage,BorderLayout.CENTER);


        setTitle("Login as Admin");
        //jmLogin.setText("Admin Name");

//        b=new JButton();
//        b.setText("My Profile");
//        b.setMaximumSize(new DimensionUIResource(pLeft.getWidth(),b1.getHeight()));
//        //System.out.println(pLeft.getWidth());
//        b.addActionListener(this);
//        pLeft.add(b);

        b1.setText("Dashboard");
        b1.setMaximumSize(new DimensionUIResource(pLeft.getWidth(),b1.getHeight()));
        //System.out.println(pLeft.getWidth());
        b1.addActionListener(this);
        pLeft.add(b1);


        b2.setText("Property type");
        b2.setMaximumSize(new DimensionUIResource(pLeft.getWidth(),b1.getHeight()));
        b2.addActionListener(this);
        pLeft.add(b2);

        b13=new JButton();
        b13.setText("Add Property");
        b13.setMaximumSize(new DimensionUIResource(pLeft.getWidth(),b1.getHeight()));
        b13.addActionListener(this);
        pLeft.add(b13);

//        b3.setText("country");
//        b3.setMaximumSize(new DimensionUIResource(pLeft.getWidth(),b1.getHeight()));
//        b3.addActionListener(this);
//        pLeft.add(b3);

//        b4=new JButton();
//        b4.setText("State");
//        b4.setMaximumSize(new DimensionUIResource(pLeft.getWidth(),b1.getHeight()));
//        b4.addActionListener(this);
//        pLeft.add(b4);

        b5=new JButton();
        b5.setText("City");
        b5.setMaximumSize(new DimensionUIResource(pLeft.getWidth(),b1.getHeight()));
        b5.addActionListener(this);
        pLeft.add(b5);

//        b6=new JButton();
//        b6.setText("Owners");
//        b6.setMaximumSize(new DimensionUIResource(pLeft.getWidth(),b1.getHeight()));
//        b6.addActionListener(this);
//        pLeft.add(b6);

        b7=new JButton();
        b7.setText("Agents");
        b7.setMaximumSize(new DimensionUIResource(pLeft.getWidth(),b1.getHeight()));
        b7.addActionListener(this);
        pLeft.add(b7);

        b8=new JButton();
        b8.setText("Customers");
        b8.setMaximumSize(new DimensionUIResource(pLeft.getWidth(),b1.getHeight()));
        b8.addActionListener(this);
        pLeft.add(b8);

       b9=new JButton();
       b9.setText("Reviews");
       b9.setMaximumSize(new DimensionUIResource(pLeft.getWidth(),b1.getHeight()));
       b9.addActionListener(this);
       pLeft.add(b9);

       b10=new JButton();
       b10.setText("Pages");
       b10.setMaximumSize(new DimensionUIResource(pLeft.getWidth(),b1.getHeight()));
       b10.addActionListener(this);
       pLeft.add(b10);

        b11=new JButton();
        b11.setText("Property");
        b11.setMaximumSize(new DimensionUIResource(pLeft.getWidth(),b1.getHeight()));
        b11.addActionListener(this);
        pLeft.add(b11);

        b12=new JButton();
        b12.setText("Notifications");
        b12.setMaximumSize(new DimensionUIResource(pLeft.getWidth(),b1.getHeight()));
        b12.addActionListener(this);
        pLeft.add(b12);

       validate();
       revalidate();


    }

    @Override
    public void actionPerformed(ActionEvent e) {

        //String command=e.getActionCommand();
//        if(e.getSource()==b)
//        {
//            //System.out.println("profile shown");
//            //ShowDataBase sdb=new ShowDataBase();
//            AdminProfile ap=new AdminProfile();
//
//        }

        if(e.getSource()==logOut)
        {
            dispose();
        }

        if(e.getSource()==b1)
        {
            JOptionPane.showMessageDialog(null,"No Dashboard found");
        }

        if(e.getSource()==b2)
        {
            PropertyTypeList list=new PropertyTypeList();
        }

//        if(e.getSource()==b3)
//        {
//            CountryList list=new CountryList();
//            System.out.println("list");
//        }
//        if(e.getSource()==b4)
//        {
//            StateList list=new StateList();
//        }
        if(e.getSource()==b5)
        {
           CityList list=new CityList();
        }
//        if(e.getSource()==b6)
//        {
//            OwnersList list=new OwnersList();
//        }
        if(e.getSource()==b7)
        {
            AgentsList list=new AgentsList();
        }
        if(e.getSource()==b8)
        {
            UsersList list=new UsersList();
        }

        if(e.getSource()==b11)
        {
            AdminProperty ap = new AdminProperty();
        }

        if(e.getSource()==b9)
        {
            JOptionPane.showMessageDialog(null,"No Reviews found");
        }
        if(e.getSource()==b10)
        {
            JOptionPane.showMessageDialog(null,"No pages found");
        }
        if(e.getSource()==b12)
        {
            JOptionPane.showMessageDialog(null,"No notifications found");
        }
        if(e.getSource()==b13)
        {
            AddProperty adb=new AddProperty();
        }

        //validate();
    }

}


// class for agent login,contains agent details
class LoginAgent extends UserHomePage
{
    JButton b4;
    JButton b3;
    JButton b5;
    JButton b7;
    JButton b8;

    JButton b9;
    JButton b10;
    JButton b11;



    LoginAgent()
    {

        b3=new JButton();

        JLabel lImage=new JLabel();
       // lImage.setBounds(100,50,500,400);
        lImage.setSize((getWidth()-pLeft.getWidth()),(getHeight()-jmb.getHeight()));
        try
        {
            BufferedImage image= ImageIO.read(new File("E:\\E Drive\\Coding Programme\\My work\\Project\\img5.jpg"));
            ImageIcon imageIcon=new ImageIcon(image);
            Image getImage=imageIcon.getImage();
            Image scaleImage=getImage.getScaledInstance(lImage.getWidth(),lImage.getHeight(),Image.SCALE_SMOOTH);
            ImageIcon scaleIcon=new ImageIcon(scaleImage);
            lImage.setIcon(scaleIcon);

        }catch (Exception e)
        {
            JOptionPane.showMessageDialog(null,"image not found");
        }

        container.add(lImage,BorderLayout.CENTER);
        setTitle("Agent Login");
        Font font=new Font("Arial",Font.BOLD,20);
        jmLogin=new JMenu(new Login().getUserName());
        jmLogin.setFont(font);
        jmb.add(jmLogin);

        b1.setText("My Profile");
        b1.setMaximumSize(new DimensionUIResource(pLeft.getWidth(),b1.getHeight()));
        //System.out.println(pLeft.getWidth());
        b1.addActionListener(this);
        pLeft.add(b1);

        b2.setText("Edit Profile");
        b2.setMaximumSize(new DimensionUIResource(pLeft.getWidth(),b1.getHeight()));
        b2.addActionListener(this);
        pLeft.add(b2);


        b3.setText("change Password");
        b3.setMaximumSize(new DimensionUIResource(pLeft.getWidth(),b1.getHeight()));
        //b3.setPreferredSize(new DimensionUIResource(pLeft.getWidth(), b3.getHeight()));
        b3.addActionListener(this);
        pLeft.add(b3);

        b4=new JButton();
        b4.setText("To Sale");
        b4.setMaximumSize(new DimensionUIResource(pLeft.getWidth(),b1.getHeight()));
        b4.addActionListener(this);
        pLeft.add(b4);

        b5=new JButton();
        b5.setText("To Rent");
        b5.setMaximumSize(new DimensionUIResource(pLeft.getWidth(),b1.getHeight()));
        b5.addActionListener(this);
        pLeft.add(b5);


        b9=new JButton();
        b9.setText("Sell/Rent Property");
        b9.setMaximumSize(new DimensionUIResource(pLeft.getWidth(),b1.getHeight()));
        b9.addActionListener(this);
        pLeft.add(b9);

        b10=new JButton();
        b10.setText("Search Property");
        b10.setMaximumSize(new DimensionUIResource(pLeft.getWidth(),b1.getHeight()));
        b10.addActionListener(this);
        pLeft.add(b10);

        b7=new JButton();
        b7.setText("Sold");
        b7.setMaximumSize(new DimensionUIResource(pLeft.getWidth(),b1.getHeight()));
        b7.addActionListener(this);
        pLeft.add(b7);

        b8=new JButton();
        b8.setText("Rented");
        b8.setMaximumSize(new DimensionUIResource(pLeft.getWidth(),b1.getHeight()));
        b8.addActionListener(this);
        pLeft.add(b8);


        validate();


    }



    @Override
    public void actionPerformed(ActionEvent e) {



        //String command=e.getActionCommand();
        if(e.getSource()==b1)
        {
            AgentProfile ap=new AgentProfile();
        }

        if(e.getSource()==logOut)
        {
            dispose();
        }

        if(e.getSource()==b2)
        {
            EditProfile ep=new EditProfile();
        }

        if(e.getSource()==b3)
        {
            ChangePassword cp=new ChangePassword();
        }

        if(e.getSource()==b4)
        {
            AvailableForSell as=new AvailableForSell();
        }

        if(e.getSource()==b5)
        {
            AvailableForRent ar=new AvailableForRent();
        }

        if(e.getSource()==b7)
        {
            Sold s=new Sold();
        }

        if(e.getSource()==b8)
        {
            Rented r=new Rented();
        }

        if(e.getSource()==b9)
        {
            SoldOrRentProperty sor=new SoldOrRentProperty();
        }
        if(e.getSource()==b10)
        {
            Find fd= new Find();
        }










    }


}

class LoginOffice extends UserHomePage
{
    JButton b4;
    JButton b5;
    JButton b6;
    JButton b7;
    JButton b8;

    JButton b9;
    JButton b10;
    JButton b3;
    LoginOffice()
    {
        b3=new JButton();

        JLabel lImage=new JLabel();
        // lImage.setBounds(100,50,500,400);
        lImage.setSize((getWidth()-pLeft.getWidth()),(getHeight()-jmb.getHeight()));
        try
        {
            BufferedImage image= ImageIO.read(new File("E:\\E Drive\\Coding Programme\\My work\\Project\\agent'sOffice.jpg"));
            ImageIcon imageIcon=new ImageIcon(image);
            Image getImage=imageIcon.getImage();
            Image scaleImage=getImage.getScaledInstance(lImage.getWidth(),lImage.getHeight(),Image.SCALE_SMOOTH);
            ImageIcon scaleIcon=new ImageIcon(scaleImage);
            lImage.setIcon(scaleIcon);

        }catch (Exception e)
        {
            JOptionPane.showMessageDialog(null,"image not found");
        }

        container.add(lImage,BorderLayout.CENTER);


        //jmLogin.setText("Office Name");
        setTitle("Office Login");
        b1.setText("Profile");
        b1.setMaximumSize(new DimensionUIResource(pLeft.getWidth(),b1.getHeight()));
        b1.addActionListener(this);
        pLeft.add(b1);

        b2.setText("Edit Profile");
        b2.setMaximumSize(new DimensionUIResource(pLeft.getWidth(),b2.getHeight()));
        b2.addActionListener(this);
        pLeft.add(b2);

        b3.setText("change Password");
        b3.setMaximumSize(new DimensionUIResource(pLeft.getWidth(),b1.getHeight()));
        b3.addActionListener(this);
        pLeft.add(b3);

        b4=new JButton();
        b4.setText("Sales Report");
        b4.setMaximumSize(new DimensionUIResource(pLeft.getWidth(),b1.getHeight()));
        b4.addActionListener(this);
        pLeft.add(b4);

        b5=new JButton();
        b5.setText("Property on rent");
        b5.setMaximumSize(new DimensionUIResource(pLeft.getWidth(),b1.getHeight()));
        b5.addActionListener(this);
        pLeft.add(b5);

//        b6=new JButton();
//        b6.setText("Add Property");
//        b6.setMaximumSize(new DimensionUIResource(pLeft.getWidth(),b1.getHeight()));
//        pLeft.add(b6);

        b7=new JButton();
        b7.setText("Received Enquiries");
        b7.setMaximumSize(new DimensionUIResource(pLeft.getWidth(),b1.getHeight()));
        b7.addActionListener(this);
        pLeft.add(b7);

        b8=new JButton();
        b8.setText("Answer/ed Enquiries");
        b8.setMaximumSize(new DimensionUIResource(pLeft.getWidth(),b1.getHeight()));
        b8.addActionListener(this);
        pLeft.add(b8);

        b9 = new JButton();
        b9.setText("Agent Sale Report");
        b9.setMaximumSize(new DimensionUIResource(pLeft.getWidth(),b1.getHeight()));
        b9.addActionListener(this);
        pLeft.add(b9);

        b10 = new JButton();
        b10.setText("Agent Rent Report");
        b10.setMaximumSize(new DimensionUIResource(pLeft.getWidth(),b1.getHeight()));
        b10.addActionListener(this);
        pLeft.add(b10);
        validate();


    }

    @Override
    public void actionPerformed(ActionEvent e) {

        //String command=e.getActionCommand();
        if(e.getSource()==b1)
        {
            System.out.println("profile shown");
            //ShowDataBase sdb=new ShowDataBase();
            OfficeProfile sp=new OfficeProfile();

        }

        if(e.getSource()==logOut)
        {
            dispose();
        }

        if(e.getSource()==b2)
        {
            EditProfile ep=new EditProfile();

        }

        if(e.getSource()==b3)
        {
            ChangePassword cp=new ChangePassword();

        }

        if(e.getSource()==b4)
        {
            SalesReport sr=new SalesReport();

        }

        if(e.getSource()==b5)
        {
            Rented r=new Rented();

        }

        if(e.getSource()==b6)
        {

        }

        if(e.getSource()==b7)
        {
            JOptionPane.showMessageDialog(null,"No Enquiry received");

        }

        if(e.getSource()==b8)
        {
            JOptionPane.showMessageDialog(null,"Empty");

        }

        if(e.getSource()==b9)
        {
            //JOptionPane.showMessageDialog(null,"Empty");
            AgentSaleReport ae1 = new AgentSaleReport();

        }

        if(e.getSource()==b10)
        {
            AgentRentReport ae2 = new AgentRentReport();
        }
    }


}

class Login extends JFrame implements ActionListener {
    //JFrame jf=new JFrame();
    JButton login;
     int UserId;
     String Upassword;
    String mobileNumber;
    JTextField userId;
    JPasswordField password;
    char[] Password;
    boolean logedIn=false;
    String userType;
    static Connection con;
    String query;
    Login()
    {

    }
    Login(String userType)
    {

        setTitle(userType);

        this.userType=userType;
        //jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //jf.setExtendedState(JFrame.MAXIMIZED_BOTH);
        setBounds(200,0,400,300);
        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);
        Container container=getContentPane();
        container.setLayout(new BorderLayout());

        JPanel p1=new JPanel();
        Font font=new Font("Arial",Font.BOLD,15);
        p1.setLayout(new GridLayout(3,2,10,10));
        p1.setBorder(javax.swing.BorderFactory.createEmptyBorder(30,50,0,50));
        p1.setBounds(0,0,500,500);
        p1.setBackground(Color.YELLOW);
        // p1.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        JPanel p2=new JPanel(new FlowLayout(FlowLayout.CENTER,0,50));
        login=new JButton("Login");
        login.setFont(font);
        login.setActionCommand("login");
        p2.setBackground(Color.YELLOW);
        p2.add(login);

        JLabel user=new JLabel("User ID ");
        user.setFont(font);
        p1.add(user);
        userId=new JTextField("");
        p1.add(userId);

        JLabel pass=new JLabel("Password  ");
        pass.setFont(font);
        p1.add(pass);
        password=new JPasswordField("");
        p1.add(password);

        container.add(p1);


        //container.add(p1,BorderLayout.NORTH);
        container.add(p2,BorderLayout.SOUTH);
        container.validate();

        login.addActionListener(this);
        con=null;



        UserId=-1;
        Upassword="";


    }


    @Override
    public void actionPerformed(ActionEvent e) {
       if (e.getSource()==login)
       {
           boolean isLogin=false;
           if(this.userType.equals("Admin Login"))
           {
               int id=0;
               try {
                    id=Integer.parseInt(userId.getText());
                   query="select *from admin_login where admin_id="+id;
                   isLogin=getUserIdPassword(query);
                   if(isLogin)
                   {
                       if(userId.getText().equals(String.valueOf(UserId)))
                       {
                           Password=password.getPassword();
                           if(isPasswordCorrect(Password))
                           {
                               LoginAdmin logedInAdmin=new LoginAdmin();
                               userId.setText("");
                               password.setText("");
                               this.dispose();
                           }else
                           {
                               JOptionPane.showMessageDialog(null,"Invalid password!","Error...",JOptionPane.ERROR_MESSAGE);
                           }
                       }
                   }else
                   {
                       JOptionPane.showMessageDialog(null,"Admin id not available! please sign up...","Error...",JOptionPane.ERROR_MESSAGE);
                   }
               }catch (Exception e1)
               {
                   JOptionPane.showMessageDialog(null,"Invalid user id!","Error...",JOptionPane.ERROR_MESSAGE);

               }





           }

           if(this.userType.equals("Agent Login"))
           {
               int id=0;
               try {
                   id=Integer.parseInt(userId.getText());
                   query="select *from agent_login where aid="+id;
                   isLogin=getUserIdPassword(query);
                   if(isLogin)
                   {
                       if(userId.getText().equals(String.valueOf(UserId)))
                       {
                           Password=password.getPassword();
                           if(isPasswordCorrect(Password))
                           {
                               LoginAgent la=new LoginAgent();
                               userId.setText("");
                               password.setText("");
                               this.dispose();
                           }else
                           {
                               JOptionPane.showMessageDialog(null,"Invalid password!","Error...",JOptionPane.ERROR_MESSAGE);
                           }
                       }else
                       {
                           //JOptionPane.showMessageDialog(null,"Invalid user id!","Error",JOptionPane.ERROR_MESSAGE);
                       }
                   }else {
                       JOptionPane.showMessageDialog(null,"Agent id not available !, please sign up...");
                   }
               }catch (Exception e1)
               {

                   JOptionPane.showMessageDialog(null,"Invalid user id!","Error...",JOptionPane.ERROR_MESSAGE);

               }



           }

           if(this.userType.equals("Office Login"))
           {
               int id=0;
               try {
                   id=Integer.parseInt(userId.getText());
                   query="select *from agent_login where aid="+id;
                   isLogin=getUserIdPassword(query);
                   if(isLogin)
                   {
                       if(userId.getText().equals(String.valueOf(UserId)))
                       {
                           Password=password.getPassword();
                           if(isPasswordCorrect(Password))
                           {
                               LoginOffice logenInSeller=new LoginOffice();
                               userId.setText("");
                               password.setText("");
                               this.dispose();
                           }else
                           {
                               JOptionPane.showMessageDialog(null,"Invalid password!","Error...",JOptionPane.ERROR_MESSAGE);
                           }
                       }else
                       {
                           //JOptionPane.showMessageDialog(null,"Invalid user id!","Error",JOptionPane.ERROR_MESSAGE);
                       }
                   }else {
                       JOptionPane.showMessageDialog(null,"Office/Agent id not available !, please sign up...");
                   }

               }catch (Exception e1)
               {

                   JOptionPane.showMessageDialog(null,"Invalid office/agent id!","Error...",JOptionPane.ERROR_MESSAGE);

               }


           }
       }

    }


    private  boolean isPasswordCorrect(char[] input) {
        boolean isCorrect = true;
        char[] correctPassword =Upassword.toCharArray();

        if (input.length != correctPassword.length) {
            isCorrect = false;
        } else {
            isCorrect = Arrays.equals (input, correctPassword);
        }

        //Zero out the password.
        Arrays.fill(correctPassword,'x');

        return isCorrect;
    }

    ResultSet rSet;

    public  boolean getUserIdPassword(String q)
    {
        try
        {
            ConnectionProvidr conn=new ConnectionProvidr();
            con=conn.createCon();
            Statement stmt=con.createStatement();
            rSet=stmt.executeQuery(q);

            if(rSet.next())
            {

                UserId=rSet.getInt(1);
                Upassword=rSet.getString(2);
                if(con!=null)
                {
                    con.close();
                }
                return true;
            }else {
                //JOptionPane.showMessageDialog(null,"Invalid user ID!","Error",JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null,"Network error","Error",JOptionPane.ERROR_MESSAGE);
            System.out.println(e);
            return false;
        }
    }

    public String getUserName()
    {

        ResultSet rset;
        Statement stmt;
        String userName="";
        Connection con=null;
        try{
            ConnectionProvidr conn=new ConnectionProvidr();
            con=conn.createCon();
            stmt=con.createStatement();
            rset=stmt.executeQuery("select name from agent where aid="+this.UserId);
            System.out.println(UserId);
            if(rset.next())
            {

                userName=rset.getString(2);
                con.close();
                return userName;

            }else {
                return null;
            }

        }catch (Exception e)
        {
            JOptionPane.showMessageDialog(null,"Network error");
            return null;
        }
    }
}


class SignUp extends JFrame implements ActionListener {
     //JFrame jf=new JFrame();
     JButton submit;
     String signUpType;
     JTextField userName;
    static JPasswordField password;
    JTextField Name;
    JTextField middleName;
    JTextField lastName;
    JTextField mobileNo;
    JTextField emailId;
    JButton close;
     SignUp()
     {

     }
    SignUp(String signUptype)
    {
        //setUndecorated ( true );
        //setDefaultLookAndFeelDecorated(true);
        UIManager.put("JFrame.activeTitleBackground", Color.red);


        this.signUpType=signUptype;
        setTitle(signUptype);
        //jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //jf.setExtendedState(JFrame.MAXIMIZED_BOTH);
        setBounds(200,0,500,400);
        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);
        Container container=getContentPane();
        container.setLayout(new BorderLayout());
        JPanel p1=new JPanel();
        p1.setLayout(new GridLayout(7,2,10,10));
        p1.setBounds(0,0,500,500);
        p1.setBorder(javax.swing.BorderFactory.createEmptyBorder(30,70,0,50));
        Font font=new Font("Arial",Font.BOLD,15);
        p1.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        JPanel p2=new JPanel(new FlowLayout(FlowLayout.CENTER,0,20));
        submit=new JButton("Submit");
        submit.setFont(font);
        submit.setActionCommand("submit");
        close=new JButton("Close");
        close.setFont(font);
        close.addActionListener(this);
        p1.setBackground(new Color(214, 125, 250));
        p2.setBackground(new Color(214, 125, 250));
        p2.add(submit);
        p2.add(close);

        JLabel user=new JLabel("User ID ");
        user.setFont(font);
        p1.add(user);
        userName=new JTextField("");
        p1.add(userName);

        JLabel pass=new JLabel("Password  ");
        pass.setFont(font);
        p1.add(pass);
        password=new JPasswordField("");
        p1.add(password);

        JLabel firstN=new JLabel("Name");
        firstN.setFont(font);
        p1.add(firstN);
        Name=new JTextField("");
        p1.add(Name);


        JLabel mobile=new JLabel("Mobile No");
        mobile.setFont(font);
        p1.add(mobile);
        mobileNo=new JTextField("");
        p1.add(mobileNo);

        JLabel email=new JLabel("Email Id");
        email.setFont(font);
        p1.add(email);
        emailId=new JTextField("");

        p1.add(emailId);



        container.add(p1);


        //container.add(p1,BorderLayout.NORTH);
        container.add(p2,BorderLayout.SOUTH);
        container.validate();
       // Font font=new Font("Arial",Font.ITALIC,70);
        submit.addActionListener(this);

    }



     @Override
     public void actionPerformed(ActionEvent e) {
         String command=e.getActionCommand();

         if(e.getSource()==close)
         {
             this.dispose();
         }
         if(command.equals("submit")) {
             if (userName.getText().equals("")) {
                 JOptionPane.showMessageDialog(this, "Please enter user id", "Error", JOptionPane.ERROR_MESSAGE);
             } else if (isPasswordCorrect(password.getPassword())) {

                 JOptionPane.showMessageDialog(this, "Please enter password", "Error", JOptionPane.ERROR_MESSAGE);
             } else if (Name.getText().equals("")) {
                 JOptionPane.showMessageDialog(this, "Please enter name", "Error", JOptionPane.ERROR_MESSAGE);
             } else if (mobileNo.getText().equals("")) {

                 JOptionPane.showMessageDialog(this, "Please enter mobile number", "Error", JOptionPane.ERROR_MESSAGE);

             } else if (!(isNumber(mobileNo.getText()) == 10)) {

                 JOptionPane.showMessageDialog(this, "Invalid mobile number", "Error", JOptionPane.ERROR_MESSAGE);
             } else {
                 if (signUpType.equals("Admin SignUp")) {
                     SignUpAdmin sa=new SignUpAdmin();
                     boolean isSignUp=false;
                     isSignUp=sa.setSignUp(userName.getText(),password.getPassword());
                     if(isSignUp)
                     {
                         submit.setBackground(Color.GREEN);
                         setBackground(Color.CYAN);
                         submit.setText("Submitted");
                     }
                 }
                 if (signUpType.equals("Agent SignUp")) {
                     SignUpAgent sa=new SignUpAgent();
                     boolean isSignUp=false;
                     isSignUp=sa.setSignUp(userName.getText(),password.getPassword());
                     if(isSignUp)
                     {
                         isSignUp=sa.setAgentDetails(userName.getText(),Name.getText(),mobileNo.getText(),emailId.getText());
                     }


                     if(isSignUp)
                     {
                         submit.setBackground(Color.GREEN);
                         setBackground(Color.CYAN);
                         submit.setText("Submitted");
                     }
                 }

                 if (signUpType.equals("Office SignUp")) {

                     SignUpOffice so = new SignUpOffice();
                     boolean isSignUp=false;
                     isSignUp=so.setSignUp(userName.getText(),password.getPassword());
                     if(isSignUp)
                     {
                         submit.setBackground(Color.GREEN);
                         setBackground(Color.CYAN);
                         submit.setText("Submitted");
                     }
                 }

                 //System.out.println("else");

             }
         }


     }

     static String cPassword="";

    private static boolean isPasswordCorrect(char[] input) {
        boolean isCorrect = true;
        char[] correctPassword =cPassword.toCharArray();

        if (input.length != correctPassword.length) {
            isCorrect = false;
        } else {
            isCorrect = Arrays.equals (input, correctPassword);
        }

        //Zero out the password.
        Arrays.fill(correctPassword,'x');

        return isCorrect;
    }

    private int isNumber(String input)
    {
        char Input[]=input.toCharArray();
        int i=0;
        boolean isNo=true;
        int numberCount=0;
        for(i=0;i<input.length();i++)
        {
            if(Input[i]>=48 && Input[i]<=57)
            {

            }else {
                isNo=false;
                break;
            }

        }

        if(isNo)
        {
            return numberCount=input.length();
        }else {
            return 0;
        }
    }
 }

class SignUpAdmin extends SignUp{

    int adminId;
    char[] password;
    Connection con;
    String query;

    String sPassword;

    SignUpAdmin()
    {

    }

    public boolean setSignUp(String user,char[] pass)
    {


            try{
                this.adminId=Integer.parseInt(user);

                try
                {
                    this.password=pass;
                    sPassword=new String(password);
                    con=null;
                    ConnectionProvidr conn=new ConnectionProvidr();
                    con=conn.createCon();
                    query="insert into admin_login values(?,?)";
                    PreparedStatement pstmt=con.prepareStatement(query);
                    pstmt.setInt(1,adminId);
                    pstmt.setString(2,sPassword);
                    pstmt.executeUpdate();
                    return true;


                }catch (Exception e)
                {
                    JOptionPane.showMessageDialog(null,"Network error","Error",JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            catch (Exception e1)
            {
                JOptionPane.showMessageDialog(null,"Please enter integer id","Error",JOptionPane.ERROR_MESSAGE);
                return false;
            }



    }





}
class SignUpAgent extends SignUp {


    int agentId;
    char[] password;
    String name;
    String mobileNo;
    String emailId;

    String sPassword;
    Connection con;
    String query;
    SignUpAgent()
    {

    }

    public boolean setSignUp(String user,char[] pass)
    {

        try {
            this.agentId=Integer.parseInt(user);
            String q="select aid from agent_login where aid=?";
            boolean isTrue;
            isTrue=checkAid(q,this.agentId);

            if(isTrue) {
                JOptionPane.showMessageDialog(null, "Agent id already exist");
                return false;
            }else {
                try {
                    this.password = pass;
                    sPassword = new String(password);
                    con = null;
                    ConnectionProvidr conn = new ConnectionProvidr();
                    con = conn.createCon();
                    query = "insert into agent_login values(?,?)";
                    PreparedStatement pstmt = con.prepareStatement(query);
                    pstmt.setInt(1, agentId);
                    pstmt.setString(2, sPassword);
                    pstmt.executeUpdate();
                    con.close();


                    return true;


                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Network error", "Error", JOptionPane.ERROR_MESSAGE);
                    System.out.println(e);
                    return false;
                }
            }
        }catch (Exception e1)
        {
            JOptionPane.showMessageDialog(null,"Invalid agent id","Error",JOptionPane.ERROR_MESSAGE);
            return  false;
        }


    }

    public boolean setAgentDetails(String user,String name,String mobile,String email)
    {
        try {
            this.agentId=Integer.parseInt(user);

            try {
                    this.name = name;
                    this.mobileNo = mobile;
                    this.emailId = email;
                    con = null;
                    ConnectionProvidr conn = new ConnectionProvidr();
                    con = conn.createCon();
                    query = "insert into agent values(?,?,?,?)";
                    PreparedStatement pstmt = con.prepareStatement(query);
                    pstmt.setInt(1, agentId);
                    pstmt.setString(2, this.name);
                    pstmt.setString(3, this.mobileNo);
                    pstmt.setString(4, this.emailId);
                    pstmt.executeUpdate();
                    con.close();

                    return true;


                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Network error", "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

        }catch (Exception e1)
        {
            JOptionPane.showMessageDialog(null,"Invalid agent id","Error",JOptionPane.ERROR_MESSAGE);
            return  false;
        }


    }

    public boolean checkAid(String q,int id)
    {
        Connection con=null;
        PreparedStatement pStmt;
        ResultSet rSet;
        try
        {

            ConnectionProvidr conn=new ConnectionProvidr();
            con=conn.createCon();
            pStmt=con.prepareStatement(q);
            pStmt.setInt(1,id);
            rSet=pStmt.executeQuery();
            if(rSet.next())
            {
                if(con!=null)
                {
                   con.close();
                }
                return true;
            }else {
                return false;
            }

        }catch (Exception e)
        {
            System.out.println(e);
            return false;
        }


    }



}



class SignUpOffice extends SignUp  {
    char[] password;
    int officeId;
    String sPassword;
    Connection con;
    String query;

    SignUpOffice()
    {

    }

    public boolean setSignUp(String user,char[] pass)
    {
        try{
            this.officeId=Integer.parseInt(user);

            try
            {
                this.password=pass;
                sPassword=new String(password);
                con=null;
                ConnectionProvidr conn=new ConnectionProvidr();
                con=conn.createCon();
                query="insert into office_login values(?,?)";
                PreparedStatement pstmt=con.prepareStatement(query);
                pstmt.setInt(1,officeId);
                pstmt.setString(2,sPassword);
                pstmt.executeUpdate();
                return true;


            }catch (Exception e)
            {
                JOptionPane.showMessageDialog(null,"Network error","Error",JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        catch (Exception e1)
        {
            JOptionPane.showMessageDialog(null,"Please enter integer id","Error",JOptionPane.ERROR_MESSAGE);
            return false;
        }

    }



}

class HomePage implements  ActionListener {
    JFrame jf=new JFrame();
    JMenuItem loginadmin;
    JMenu userLogin;
    JMenuItem loginAgent;
    JMenuItem loginOffice;

    //JMenuItem signUpAdmin;
    JMenu userSignUp;
    JMenuItem signUpAgent;
    //JMenuItem signUpOffice;
    JPanel homePage;
    JLabel l1=new JLabel();

    HomePage()
    {
        try{
            UIManager.put("JFrame.activeTitleBackground", Color.red);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        l1.setText("Login here");
        l1.setBounds(400,500,80,40);
        jf.setTitle("Home");

        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setExtendedState(JFrame.MAXIMIZED_BOTH);
        jf.setVisible(true);
        jf.setLocationRelativeTo(null);
        Container container=jf.getContentPane();
        homePage=new JPanel();
        JLabel lImage=new JLabel();
        lImage.setSize(jf.getWidth(),jf.getHeight());
        try
        {
            BufferedImage image= ImageIO.read(new File("E:\\E Drive\\Coding Programme\\My work\\Project\\finalimg.jpg"));
            ImageIcon imageIcon=new ImageIcon(image);
            Image getImage=imageIcon.getImage();
            Image scaleImage=getImage.getScaledInstance(lImage.getWidth(),lImage.getHeight(),Image.SCALE_SMOOTH);
            ImageIcon scaleIcon=new ImageIcon(scaleImage);
            lImage.setIcon(scaleIcon);

        }catch (Exception e)
        {
            JOptionPane.showMessageDialog(null,"image not found");
        }
        homePage.add(lImage);
        container.setLayout(new BorderLayout());
        JMenuBar jmb=new JMenuBar();
        jmb.setBounds(650,520,210,40);

        Font font=new Font("Arial",Font.BOLD,20);

        jmb.setBackground(new Color(165, 184, 178));


        JMenu jmLogin=new JMenu("Login As");
        jmLogin.setFont(font);


        jmb.add(jmLogin);

        loginadmin=new JMenuItem("Admin");
        loginadmin.setActionCommand("loginAdmin");
        jmLogin.add(loginadmin);

        userLogin=new JMenu("user");

        loginAgent=new JMenuItem("Agent");
        loginAgent.setActionCommand("loginAgent");
        userLogin.add(loginAgent);


        loginOffice=new JMenuItem("Agent's Office");
        loginOffice.setActionCommand("LoginOffice");
        userLogin.add(loginOffice);


        jmLogin.add(userLogin);

        JMenu jmSignup=new JMenu("SignUp As        ");
        jmSignup.setFont(font);
        jmb.add(jmSignup);

//        signUpAdmin=new JMenuItem("Admin");
//        signUpAdmin.setActionCommand("signUpAdmin");
//        jmSignup.add(signUpAdmin);

        //userSignUp=new JMenu("user");

        signUpAgent=new JMenuItem("Agent");
        signUpAgent.setActionCommand("SignUpAgent");
        jmSignup.add(signUpAgent);

//
//        signUpOffice=new JMenuItem("Agent's Office");
//        signUpOffice.setActionCommand("SignUpOffice");
//        jmSignup.add(signUpOffice);

       // jmSignup.add(userSignUp);

        lImage.add(jmb);
        container.add(homePage,BorderLayout.CENTER);
        jf.validate();

        //signUpAdmin.addActionListener(this);
        signUpAgent.addActionListener(this);
        //signUpOffice.addActionListener(this);


        loginadmin.addActionListener(this);
        loginAgent.addActionListener(this);
        loginOffice.addActionListener(this);

    }


    @Override
    public void actionPerformed(ActionEvent e) {

        String command=e.getActionCommand();

        if(command.equals("SignUpAgent"))
        {
            SignUp signup=new SignUp("Agent SignUp");

        }

        if(command.equals("SignUpOffice"))
        {
            SignUp signup=new SignUp("Office SignUp");

        }



        if(command.equals("loginAdmin"))
        {
            Login login=new Login("Admin Login");

        }

        if(command.equals("loginAgent"))
        {
            Login login=new Login("Agent Login");

        }

        if(command.equals("LoginOffice"))
        {
            Login login=new Login("Office Login");

        }

    }
}

public class Main {
    public static void main(String[] args) {

        HomePage hp=new HomePage();
    }
}