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
public class GreetingController {

	@Autowired
    private SimpMessagingTemplate template;

	@RequestMapping(value = "/aaa")
    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public AlarmDTO greeting(AlarmDTO alarmDTO) throws Exception {
        
    	Thread.sleep(1000); // simulated delay
        return alarmDTO;
    }

    public void fireGreeting(Alarm message) {
    	AlarmDTO alarmDTO = ConverterUtil.convertAlarm(message);
        System.out.println("Fire");
        this.template.convertAndSend("/topic/greetings", alarmDTO);
    }
}
