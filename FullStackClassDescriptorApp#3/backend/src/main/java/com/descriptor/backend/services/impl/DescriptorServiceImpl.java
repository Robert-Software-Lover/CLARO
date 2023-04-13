package com.descriptor.backend.services.impl;

import com.descriptor.backend.services.DescriptorService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class DescriptorServiceImpl  implements DescriptorService {

    public  String GetDescriptionFromClass(String definition){


        try {
            //Validate input first
            if(definition==null || definition.isEmpty()) return "Contenido vacio.";

            //Replace bad chars
            definition = definition.replaceAll("\\s", " ");

            // Extract class name and scope using regex
            Pattern classPattern = Pattern.compile("(public|private|protected) class ([A-Z]\\w+)");
            Matcher classMatcher = classPattern.matcher(definition);
            if (!classMatcher.find()) {
                return "Class not found.";
            }
            String className = classMatcher.group(2);
            String classScope = classMatcher.group(1);

            // Extract attribute and method declarations using regex and functional programming
            List<String> attributes = new ArrayList<>();
            List<String> methods = new ArrayList<>();

            List<Pattern> patterns = Arrays.asList(
                    Pattern.compile("(public|private|protected) .*[a-z] \\w+ = "),
                    Pattern.compile("(public|private|protected) (void|String|int|Boolean) \\w+([(][^)]*[)])")
            );

            String finalDefinition = definition;
            patterns.forEach(pattern -> {
                Matcher matcher = pattern.matcher(finalDefinition);
                while (matcher.find()) {
                    String declaration = matcher.group();
                    if (pattern.pattern().contains("= ")) {
                        attributes.add(declaration);
                    } else {
                        methods.add(declaration);
                    }
                }
            });

            // Format attribute and method information into a table using functional programming
            String header = String.format("%-25s %-25s %-25s %-25s", "NAME", "VARI", "SCOPE", "SIGNATURE");
            String attributeTable = attributes.stream()
                    .map(declaration -> {
                        String[] parts = declaration.split(" ");
                        String name = parts[2];
                        String scope = parts[0];
                        String type = parts[1];
                        return String.format("%-25s %-25s %-25s %-25s", name, "A", scope, " TYPE: " + type);
                    })
                    .collect(Collectors.joining("\n"));

            String methodTable = methods.stream()
                    .map(declaration -> {
                        String[] parts = declaration.split(" ");
                        String name = parts[2].substring(0, parts[2].indexOf("("));
                        String scope = parts[0];
                        String returnType = "";
                        String params = "";

                        if (parts.length > 1) {
                            returnType = parts[1];
                        }

                        if (parts.length > 2) {
                            params = parts[2].substring(parts[2].indexOf("(") + 1);
                            if (parts.length > 3) {
                                params += " " + parts[3].replaceAll("\\)", "");
                            }

                            if(params.equals(")")) params="";
                        }

                        return String.format("%-25s %-25s %-25s %-25s", name, "M", scope, " RTYPE: " + returnType + ", PARAMS: " + params);

                    })
                    .collect(Collectors.joining("\n"));

            // Combine all information into a single string and return it
            return String.format("Class Name: %s\nScope: %s\nConstructor: %s\n%s\n%s\n%s",
                    className, classScope, className, header, attributeTable, methodTable);

        } catch (Exception e) {
            e.printStackTrace();

            return "";
        }

    }
}
