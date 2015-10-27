package de.moso.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URL;

/**
 * Created by Sandro on 26.10.15 for shop.
 */
public class HtmlAnalyser {
    public void analyze() {
        try {
            // Webseite parsen, 5 Sekunden Timeout
            Document document = Jsoup.parse(new URL("http://mvnrepository.com/artifact/com.ning/async-http-client"), 5000);
            //http://start.vag.de/dm/api/abfahrten.json/vgn/361/?callback=&timedelay=0&product=Ubahn,Bus,Tram
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}

/*
{
  "Metadata": {
    "Version": "Puls-API-v1.0",
    "Timestamp": "2015-10-27T18:40:54+01:00"
  },
  "Haltestellenname": "Harmoniestr. (Nürnberg)",
  "VAGKennung": "HARMON",
  "VGNKennung": 361,
  "Abfahrten": [
    {
      "Linienname": "36",
      "Haltepunkt": "HARMON:1",
      "Richtung": "Richtung1",
      "Richtungstext": "Doku-Zentrum",
      "AbfahrtszeitSoll": "2015-10-27T18:36:00+01:00",
      "AbfahrtszeitIst": "2015-10-27T18:42:44+01:00",
      "Produkt": "Bus",
      "Longitude": 11.09117028,
      "Latitude": 49.45526889,
      "Fahrtnummer": 11372,
      "Fahrtartnummer": 1,
      "Prognose": true
    },
    {
      "Linienname": "36",
      "Haltepunkt": "HARMON:1",
      "Richtung": "Richtung1",
      "Richtungstext": "Doku-Zentrum",
      "AbfahrtszeitSoll": "2015-10-27T18:46:00+01:00",
      "AbfahrtszeitIst": "2015-10-27T18:46:49+01:00",
      "Produkt": "Bus",
      "Longitude": 11.09117028,
      "Latitude": 49.45526889,
      "Fahrtnummer": 11257,
      "Fahrtartnummer": 1,
      "Prognose": true
    },
    {
      "Linienname": "36",
      "Haltepunkt": "HARMON:2",
      "Richtung": "Richtung2",
      "Richtungstext": "Plärrer",
      "AbfahrtszeitSoll": "2015-10-27T18:47:00+01:00",
      "AbfahrtszeitIst": "2015-10-27T18:51:42+01:00",
      "Produkt": "Bus",
      "Longitude": 11.09135056,
      "Latitude": 49.45551667,
      "Fahrtnummer": 10838,
      "Fahrtartnummer": 1,
      "Prognose": true
    },
    {
      "Linienname": "36",
      "Haltepunkt": "HARMON:1",
      "Richtung": "Richtung1",
      "Richtungstext": "Doku-Zentrum",
      "AbfahrtszeitSoll": "2015-10-27T18:56:00+01:00",
      "AbfahrtszeitIst": "2015-10-27T18:56:13+01:00",
      "Produkt": "Bus",
      "Longitude": 11.09117028,
      "Latitude": 49.45526889,
      "Fahrtnummer": 10889,
      "Fahrtartnummer": 1,
      "Prognose": true
    },
    {
      "Linienname": "36",
      "Haltepunkt": "HARMON:2",
      "Richtung": "Richtung2",
      "Richtungstext": "Plärrer",
      "AbfahrtszeitSoll": "2015-10-27T18:57:00+01:00",
      "AbfahrtszeitIst": "2015-10-27T18:57:00+01:00",
      "Produkt": "Bus",
      "Longitude": 11.09135056,
      "Latitude": 49.45551667,
      "Fahrtnummer": 11221,
      "Fahrtartnummer": 1,
      "Prognose": true
    },
    {
      "Linienname": "36",
      "Haltepunkt": "HARMON:1",
      "Richtung": "Richtung1",
      "Richtungstext": "Doku-Zentrum",
      "AbfahrtszeitSoll": "2015-10-27T19:06:00+01:00",
      "AbfahrtszeitIst": "2015-10-27T19:06:00+01:00",
      "Produkt": "Bus",
      "Longitude": 11.09117028,
      "Latitude": 49.45526889,
      "Fahrtnummer": 10936,
      "Fahrtartnummer": 1,
      "Prognose": true
    },
    {
      "Linienname": "36",
      "Haltepunkt": "HARMON:2",
      "Richtung": "Richtung2",
      "Richtungstext": "Plärrer",
      "AbfahrtszeitSoll": "2015-10-27T19:07:00+01:00",
      "AbfahrtszeitIst": "2015-10-27T19:07:00+01:00",
      "Produkt": "Bus",
      "Longitude": 11.09135056,
      "Latitude": 49.45551667,
      "Fahrtnummer": 11373,
      "Fahrtartnummer": 1,
      "Prognose": true
    },
    {
      "Linienname": "36",
      "Haltepunkt": "HARMON:2",
      "Richtung": "Richtung2",
      "Richtungstext": "Plärrer",
      "AbfahrtszeitSoll": "2015-10-27T19:17:00+01:00",
      "AbfahrtszeitIst": "2015-10-27T19:17:00+01:00",
      "Produkt": "Bus",
      "Longitude": 11.09135056,
      "Latitude": 49.45551667,
      "Fahrtnummer": 11258,
      "Fahrtartnummer": 1,
      "Prognose": true
    }
  ],
  "Sonderinformationen": [
    "Großveranstaltung Frankenstadion bis ca. 23:00 Uhr: Linie 55 fährt nicht zw. Max-Grundig-Platz und Meistersingerhalle. ",
    "Verkehrsunfall Rathenauplatz beendet: Auf der Linie 8 vereinzelt Verzögerungen/Fahrtausfälle bis ca. 18:50 Uhr möglich. "
  ]
}
 */