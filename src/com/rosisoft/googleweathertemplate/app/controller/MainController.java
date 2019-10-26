package com.rosisoft.googleweathertemplate.app.controller;




import com.rosisoft.openweathermap.classes.Unit;
import com.rosisoft.openweathermap.entities.WeatherForecastDaily;
import com.rosisoft.openweathermap.exceptions.WeatherDataServiceException;
import com.rosisoft.openweathermap.interfaces.IWeatherDataService;
import com.rosisoft.openweathermap.services.DataServiceFactory;
import com.rosisoft.openweathermap.services.DataServiceFactory.service;
import java.net.URL;

import java.text.SimpleDateFormat;
import java.time.LocalDate;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class MainController implements Initializable{
   IWeatherDataService dataService ;
    @FXML
    private Label text_City,text_Date,text_Time,
                  text_Temperature,text_WeatherDescription,
                  text_fahrenheit,text_celsius,
                  text_Precipitation,text_Wind,text_Humidity,
            
            text_theFirstDay,txt_tempMax_theFirstDay,txt_tempMin_theFirstDay,
            text_theSecondDay,txt_tempMax_theSecondDay,txt_tempMin_theSecondDay,
            text_theThirdDay,txt_tempMax_theThirdDay,txt_tempMin_theThirdDay,
            text_theFourthDay,txt_tempMax_theFourthDay,txt_tempMin_theFourthDay,
            text_theFifthDay,txt_tempMax_theFifthDay,txt_tempMin_theFifthDay,
            text_theSixthDay,txt_tempMax_theSixthDay,txt_tempMin_theSixthDay,
            text_theSeventhDay,txt_tempMax_theSeventhDay,txt_tempMin_theSeventhDay,
            text_toDay,txt_tempMax_toDay,txt_tempMin_toDay;                       
        

    @FXML
    private ImageView img_Weather,
            img_theFirstDay,img_theSecondDay,img_theThirdDay,img_theFourthDay,
            img_theFifthDay,img_theSixthDay,img_theSeventhDay,img_toDay;
            

   @FXML
    void onMousePressedCelsius(MouseEvent event) {
         initImgButtonDisable(true);
         dataService.setUnit(Unit.Metric);
                Platform.runLater(new Runnable() {
         @Override
         public void run() {
             initialize(null, null);
         }
     });
            
    }

    @FXML
    void onMousePressedClose(MouseEvent event) {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    void onMousePressedFahrenheit(MouseEvent event) {
         initImgButtonDisable(false);
         dataService.setUnit(Unit.Imperial);
         Platform.runLater(new Runnable() {
         @Override
         public void run() {
             initialize(null, null);
         }
     });
          
          
         
    }

    @Override
    public void initialize(URL url, ResourceBundle resources) {
     
       
     
        Label tab_days[]={text_toDay,text_theFirstDay,text_theSecondDay,text_theThirdDay,text_theFourthDay,
                         text_theFifthDay,text_theSixthDay,text_theSeventhDay};
        
       Label tab_tempsMax[]={txt_tempMax_toDay,txt_tempMax_theFirstDay,txt_tempMax_theSecondDay,txt_tempMax_theThirdDay,txt_tempMax_theFourthDay,
                              txt_tempMax_theFifthDay,txt_tempMax_theSixthDay,txt_tempMax_theSeventhDay};
     
       Label tab_tempsMin[]={txt_tempMin_toDay,txt_tempMin_theFirstDay,txt_tempMin_theSecondDay,txt_tempMin_theThirdDay,txt_tempMin_theFourthDay,
                              txt_tempMin_theFifthDay,txt_tempMin_theSixthDay,txt_tempMin_theSeventhDay};
     
       ImageView tab_imgs[]={img_toDay,img_theFirstDay,img_theSecondDay,img_theThirdDay,img_theFourthDay,
                         img_theFifthDay,img_theSixthDay,img_theSeventhDay};        

       Locale locale = new Locale("EN");
       SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMM",locale);
       SimpleDateFormat simpleNameWeekFormat = new SimpleDateFormat("E",locale); 
      
      // DATE
       Date today = new Date(); 
       text_Date.setText(simpleDateFormat.format(today));

      //TIME
      // timeFormatter 12H "hh:mm a" or timeFormatter 24H "HH:mm"
      DateTimeFormatter  timeformatter= DateTimeFormatter.ofPattern("hh:mm a");
        
       new Thread() {
       public void run() {

                       while (true) {
                        try {
                            Thread.sleep(1000);
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    text_Time.setText(LocalTime.now().format(timeformatter));
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
            }
        }.start();
  
       try {
           
           String apiKey="***";
           this.dataService = DataServiceFactory.getWeatherService(service.OPEN_WEATHER_MAP,apiKey);
           WeatherForecastDaily data = dataService.GET_16_DAY_WEATHER_FORECAST_DATA_BY_CITY_NAME("Constantine");
          
            text_City.setText(data.getCity().getName()+" ,"+data.getCity().getCountry());
            text_Precipitation.setText(data.getList()[0].getPressure()+" hPa");
            text_Humidity.setText(data.getList()[0].getHumidity()+" %");
 
            if (dataService.getUnit().equals(Unit.Metric))
                text_Wind.setText(data.getList()[0].getSpeed() + "m/s") ;
              else 
                text_Wind.setText(data.getList()[0].getSpeed() + "m/h");
            
            text_Temperature.setText(temperatureFormatter(data.getList()[0].getTemp().getDay()));
            text_WeatherDescription.setText(data.getList()[0].getWeather()[0].getDescription());
           
            URL iconsUrl = getClass().getResource("/com/rosisoft/googleweathertemplate/app/resources/images/");
            // URL iconsUrl = getClass().getResource("http://openweathermap.org/img/wn/");
             img_Weather.setImage(new Image(String.valueOf(iconsUrl+data.getList()[0].getWeather()[0].getIcon()+".png")));
 
             for (int i = 0; i < tab_days.length; i++) {
                tab_days[i].setText(simpleNameWeekFormat.format(convertToDateViaInstant(convertToLocalDate(today).plusDays(i))));
                       tab_imgs[i].setImage(new Image(String.valueOf(iconsUrl+data.getList()[i].getWeather()[0].getIcon()+".png")));
                        tab_tempsMax[i].setText(temperatureFormatter(data.getList()[i].getTemp().getMax()));
                       tab_tempsMin[i].setText(temperatureFormatter(data.getList()[i].getTemp().getMin()));
            }
        
        } catch (WeatherDataServiceException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
   
    private String temperatureFormatter(String str){
        return  (int)Double.parseDouble(str)+"°";
    }


    private void initImgButtonDisable(boolean b) {
        text_celsius.setDisable(b);
        text_fahrenheit.setDisable(!b);
    }
    
    private Date convertToDateViaInstant(LocalDate dateToConvert) {
    return java.util.Date.from(dateToConvert.atStartOfDay().atZone(ZoneId.systemDefault())
      .toInstant());
}
    
    private LocalDate convertToLocalDate(Date dateToConvert) {
    return LocalDate.ofInstant(
      dateToConvert.toInstant(), ZoneId.systemDefault());
}
}
