package guru.springframework.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import guru.springframework.domain.Alarm;
import guru.springframework.swing.dto.AlarmDTO;
import guru.springframework.swing.util.ConverterUtil;

@Controller
public class IndexController {
	@Autowired
    private SimpMessagingTemplate template;
	
    @RequestMapping("/")
    String index(){
        return "index";
    }

    
}
