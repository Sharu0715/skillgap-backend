
package com.sharayu.skillgap.controller;

import com.sharayu.skillgap.service.BrevoEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test-email")
@RequiredArgsConstructor
public class EmailTestController {

    private final BrevoEmailService brevoEmailService;

    @PostMapping
    public String sendTestEmail(@RequestParam String to) {

        brevoEmailService.sendEmail(
                to,
                "SkillGap Email Test",
                "Hello! Your SkillGap email integration is working."
        );

        return "Test email request sent successfully.";
    }
}
