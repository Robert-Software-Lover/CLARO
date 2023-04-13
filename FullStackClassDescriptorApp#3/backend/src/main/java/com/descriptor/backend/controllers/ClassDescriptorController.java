package com.descriptor.backend.controllers;

import com.descriptor.backend.services.DescriptorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:4200" })
@RequestMapping("/api/descriptor")
public class ClassDescriptorController {

    private DescriptorService generalDescriptorService;

    public ClassDescriptorController(DescriptorService descriptorService){

        this.generalDescriptorService = descriptorService;

    }

    @PostMapping("/info")
    public String SaveFile(@RequestBody String definition) throws Exception{

        return generalDescriptorService.GetDescriptionFromClass(definition);
    }

}

