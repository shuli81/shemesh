package com.shuli.apps.shemesh;

import android.util.Log;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

//takes html received from HttpPost and parses it
class Parser {
    private static final String TAG = "Parser";
    private int resultsFound;
    private Person[] people;

    Parser(Document doc) {
        this.resultsFound = parseResultsFound(doc.select("b").text());
        int resultsToDisplay = (resultsFound > 40) ? 40 : resultsFound;
        this.people = parsePerson(doc, resultsToDisplay);
    }

    private static int parseResultsFound(String entries) {//figures out how many results were found
        int numOfResultsFound;
        if (Character.isDigit(entries.charAt(0))) {//check if first character is a digit, if not then numOfResultsFound=0
            numOfResultsFound = Integer.parseInt(entries.substring(0, entries.indexOf(" ")));
        } else {
            numOfResultsFound = 0;
        }
        Log.i(TAG, numOfResultsFound + " results found");
        return numOfResultsFound;
    }

    private static String flipName(String personName) {//Takes name as "Cohen, Chaim" and returns "Chaim Cohen"
        String[] nameParts = personName.split(",");
        if (nameParts.length != 2) {
            Log.e(TAG, "too many name parts");
        }
        return nameParts[1].trim() + " " + nameParts[0].trim();
    }

    private Person[] parsePerson(Document doc, int resultsToDisplay) {//runs through the results html and creates a Person from each result
        Elements people = doc.select("table").select("form");
        Person[] results = new Person[resultsToDisplay];
        int resultsIndex = 0;
        for (int i = 3; i < people.size(); i++) {//i=3 because the the first 3 elements are not actual results
            String html = people.get(i).html();
            int numOfPhoneNums = StringUtils.countMatches(html, "tel:");//determines how many phone# a person has
            String[] numbersText = new String[numOfPhoneNums];
            String personName, personAddress1 = "", personAddress2 = ""; // address fields initialized here because they will not be needed if theres no address
            String[] info = html.split("<br>");
            personName = info[0];
            int numbersBeginIndex = 1;
            if (!info[1].trim().startsWith("<a")) {
                personAddress1 = info[1].trim();
                numbersBeginIndex++;
            }
            if (info.length >= 3) {
                if (!info[2].trim().startsWith("<a")) {
                    personAddress2 = info[2].trim();
                    numbersBeginIndex++;
                }
            }
            if (personName.contains(",")) {//because sometimes its only a family name
                personName = flipName(personName);
            }
            if (personName.contains("&amp;")) {//replaces "&amp" with "&"
                personName = personName.replace("&amp;", "&");
            }

            for (int j = 0; numbersBeginIndex < info.length; j++) {
                numbersText[j] = parsePhoneNum(info[numbersBeginIndex]);
                numbersBeginIndex++;
            }
            results[resultsIndex] = new Person(personName, personAddress1, personAddress2, numbersText);
            resultsIndex++;
        }
        return results;
    }

    private String parsePhoneNum(String html) {
        return Jsoup.parse(html).select("a").text().trim();
    }

    int getResultsFound() {
        return resultsFound;
    }

    Person[] getPeople() {
        return people;
    }
}
