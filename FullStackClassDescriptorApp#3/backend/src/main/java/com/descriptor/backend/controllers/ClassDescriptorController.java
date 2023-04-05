package com.descriptor.backend.controllers;

import com.descriptor.backend.services.DescriptorService;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:4200" })
@RequestMapping("/api/descriptor")
public class ClassDescriptorController {

    @PostMapping("/info")
    public String SaveFile(@RequestBody String definition) throws Exception{

        return DescriptorService.GetDescriptionFromClass(definition);
    }

}

