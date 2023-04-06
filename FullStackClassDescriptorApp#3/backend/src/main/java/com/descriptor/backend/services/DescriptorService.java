package com.descriptor.backend.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DescriptorService {

    public static String GetDescriptionFromClass(String definition){

        //Validate input first
        if(definition==null || definition.isEmpty()) return "Contenido vacio.";

        //Replace bad chars
        definition = definition.replaceAll("\\s", " ");

        //Variables definitions
        String result="";

        String headers = String.format("%-25s %-25s %-25s %-25s", "NAME", "VARI", "SCOPE", "SIGNATURE");

        String body = "";

        String[] classDescription = null;

        String[] genericDescription = null;

        int countBody = 0;

        String content="";

        List<String> lines = new ArrayList<String>();

        List<String> listRegex = Arrays.asList("(public|private|protected) .*[a-z] \\w+ = ", "(public|private|protected) (void|String|int|Boolean) \\w+([(][^)]*[)])");

        try{

            //Regex for class
            Pattern patternClass = Pattern.compile("(public|private|protected) class ([A-Z])\\w+");

            Matcher matcherClass = patternClass.matcher(definition);

            if(matcherClass.find()){

                classDescription = matcherClass.group().split(" ");

            }

            //Regex for contents body
            for(int i =0; i < listRegex.size(); i++){

                Pattern patternGeneric = Pattern.compile(listRegex.get(i));

                Matcher matcherGeneric = patternGeneric.matcher(definition);

                genericDescription = new String[matcherGeneric.groupCount()];

                while (matcherGeneric.find()){

                    genericDescription[countBody] = matcherGeneric.group();

                    countBody++;

                }

                //Iterate over each method and attribute

                for(String generic : genericDescription) {

                    //Only if generic is diferent of null
                    if(generic != null){

                        String[] methodSplited = generic.split(" ");

                        String name = "";
                        String vari = "";
                        String scope = "";
                        String rType = "";
                        String params = "";

                        switch (i) {

                            //First iterations
                            case 0:
                                String tempAtrb = generic.substring(generic.indexOf("{") + 1);

                                String[] attributes = tempAtrb.split(";");

                                for (String atr : attributes) {

                                    //Replace all invalid characters, from request..
                                    atr = atr.replaceAll(".*(  )", "");

                                    atr = atr.trim();

                                    String[] tempValue = atr.split(" ");

                                    name = tempValue[2];

                                    vari = "A";

                                    scope = tempValue[0];

                                    rType = tempValue[1];

                                    //Format the body, with some spaces
                                    body += String.format("%-25s %-25s %-25s %-25s", name, vari, scope, " TYPE: " + rType + "\n");
                                }
                                break;

                                //Second Iterations
                            case 1:
                                //Set value from generic content, for be used in bodyString
                                name = methodSplited[2].replace(methodSplited[2].substring(methodSplited[2].indexOf("(")), "");

                                vari = "M";

                                scope = methodSplited[0];

                                rType = methodSplited[1];

                                params = methodSplited[2].substring(methodSplited[2].indexOf("("));

                                //Format the params
                                if (!params.equals("()")) {

                                    params = params.replace("(", "");

                                    params += " " + methodSplited[3].replace(")", "");
                                } else {
                                    params = "";
                                }

                                //Format like a table
                                body += String.format("%-25s %-25s %-25s %-25s", name, vari, scope, " RTYPE: " + rType + ", PARAMS: " + params + "\n");

                                break;
                        }
                }
                }

                //Set it to 0, for use again.
                countBody=0;
            }

            //Join Headers + Body
            result = "Class Name: " + classDescription[2] + "\n" + "Scope: " + classDescription[0] + "\n" +
                    "Contructor: " + classDescription[2] + "\n" + headers + "\n" + body;

            return result;

        }
        catch (Exception e){

            e.printStackTrace();

        }

        return "";
    }
}
