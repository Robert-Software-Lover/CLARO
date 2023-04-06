package Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DescriptorService {

    public String GetDescriptionFromClass(String pathFile){

        //Variables definition
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

            //Check if file exist
            if(new File(pathFile).exists()){

                //Open file
                BufferedReader br = new BufferedReader(new FileReader(pathFile));

                String line = br.readLine();

                //Iterate over each line
                while (line != null)
                {
                    lines.add(line);
                    line = br.readLine();
                }

                //Fill content variable
                for(String ln : lines){

                    content+=ln;
                }

                //Regex for class
                Pattern patternClass = Pattern.compile("(public|private|protected) class ([A-Z])\\w+");

                Matcher matcherClass = patternClass.matcher(content);

                //if class regex match
                if(matcherClass.find()){

                    classDescription = matcherClass.group().split(" ");

                }

                //Iterate over each regex inside of listRegex
                for(int i =0; i < listRegex.size(); i++){

                    //Generic pattern, for reduce lines of code
                    Pattern patternGeneric = Pattern.compile(listRegex.get(i));

                    Matcher matcherGeneric = patternGeneric.matcher(content);

                    genericDescription = new String[matcherGeneric.groupCount()];

                    //Fill array with values group
                    while (matcherGeneric.find()){

                        genericDescription[countBody] = matcherGeneric.group();

                        countBody++;

                    }

                    //Iterate over each method and attribute
                    for(String generic : genericDescription) {

                        if(generic != null){

                            //Split each result
                            String[] methodSplited = generic.split(" ");

                            String name = "";
                            String vari = "";
                            String scope = "";
                            String rType = "";
                            String params = "";

                            //First iteration
                            switch (i) {

                                case 0:

                                    //Sub string, for work overs attribues
                                    String tempAtrb = generic.substring(generic.indexOf("{") + 1);

                                    //Split by ; for get all attributes
                                    String[] attributes = tempAtrb.split(";");

                                    for (String atr : attributes) {

                                        //Remove spaces
                                        atr = atr.trim();

                                        String[] tempValue = atr.split(" ");

                                        name = tempValue[2];

                                        vari = "A";

                                        scope = tempValue[0];

                                        rType = tempValue[1];

                                        //Set body, like a table
                                        body += String.format("%-25s %-25s %-25s %-25s", name, vari, scope, " TYPE: " + rType + "\n");
                                    }
                                    break;

                                    //case 1 -- > Second iterations
                                case 1:
                                    name = methodSplited[2].replace(methodSplited[2].substring(methodSplited[2].indexOf("(")), "");

                                    vari = "M";

                                    scope = methodSplited[0];

                                    rType = methodSplited[1];

                                    params = methodSplited[2].substring(methodSplited[2].indexOf("("));

                                    //Format params
                                    if (!params.equals("()")) {

                                        params = params.replace("(", "");

                                        params += " " + methodSplited[3].replace(")", "");
                                    } else {
                                        params = "";
                                    }

                                    //Make a beatiful body with String.format
                                    body += String.format("%-25s %-25s %-25s %-25s", name, vari, scope, " RTYPE: " + rType + ", PARAMS: " + params + "\n");

                                    break;
                            }

                    }
                    }

                    // = 0, for use it, again.
                    countBody=0;
                }

                //Join Headers + Body
                result = "Class Name: " + classDescription[2] + "\n" + "Scope: " + classDescription[0] + "\n" +
                        "Contructor: " + classDescription[2] + "\n" + headers + "\n" + body;

                return result;

            }
            else{
                return "File not found.";
            }
        }
        catch (Exception e){

            e.printStackTrace();

        }

        return "";
    }
}
