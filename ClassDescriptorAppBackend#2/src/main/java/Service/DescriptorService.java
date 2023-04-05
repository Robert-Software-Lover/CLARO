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

            if(new File(pathFile).exists()){

                BufferedReader br = new BufferedReader(new FileReader(pathFile));

                String line = br.readLine();

                while (line != null)
                {
                    lines.add(line);
                    line = br.readLine();
                }
                for(String ln : lines){

                    content+=ln;
                }

                //Regex for class
                Pattern patternClass = Pattern.compile("(public|private|protected) class ([A-Z])\\w+");

                Matcher matcherClass = patternClass.matcher(content);

                if(matcherClass.find()){

                    classDescription = matcherClass.group().split(" ");

                }

                for(int i =0; i < listRegex.size(); i++){


                    Pattern patternGeneric = Pattern.compile(listRegex.get(i));

                    Matcher matcherGeneric = patternGeneric.matcher(content);

                    genericDescription = new String[matcherGeneric.groupCount()];

                    while (matcherGeneric.find()){

                        genericDescription[countBody] = matcherGeneric.group();

                        countBody++;

                    }

                    //Iterate over each method and attribute

                    for(String method : genericDescription){

                        String [] methodSplited = method.split(" ");

                        String name = "";
                        String vari = "";
                        String scope = "";
                        String rType = "";
                        String params = "";

                        switch (i){

                            case 0:
                                String tempAtrb = method.substring(method.indexOf("{") + 1);

                                String[] attributes = tempAtrb.split(";");

                                for(String atr : attributes){

                                    atr = atr.trim();

                                    String[] tempValue = atr.split(" ");

                                    name = tempValue[2];

                                    vari = "A";

                                    scope = tempValue[0];

                                    rType = tempValue[1];

                                    body+=String.format("%-25s %-25s %-25s %-25s", name, vari, scope, " TYPE: " + rType + "\n");
                                }
                                break;

                            case 1:
                                name = methodSplited[2].replace(methodSplited[2].substring(methodSplited[2].indexOf("(")), "");

                                vari = "M";

                                scope = methodSplited[0];

                                rType = methodSplited[1];

                                params = methodSplited[2].substring(methodSplited[2].indexOf("("));

                                if(!params.equals("()")){

                                    params = params.replace("(","");

                                    params+= " " + methodSplited[3].replace(")", "");
                                }
                                else{
                                    params="";
                                }

                                body+=String.format("%-25s %-25s %-25s %-25s", name, vari, scope, " RTYPE: " + rType + ", PARAMS: " + params + "\n");

                                break;
                        }

                    }

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
